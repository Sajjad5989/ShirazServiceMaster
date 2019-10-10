package ir.shirazservice.expert.activity;


import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Display;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.fragment.MainFragment;
import ir.shirazservice.expert.fragment.MyServiceFragment;
import ir.shirazservice.expert.fragment.MyTransactionFragment;
import ir.shirazservice.expert.fragment.WorkManMessageFragment;
import ir.shirazservice.expert.interfaces.IDefault;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.interfaces.IRtl;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.savetoken.SaveTokenKey;
import ir.shirazservice.expert.webservice.savetoken.SaveTokenKeyApi;
import ir.shirazservice.expert.webservice.savetoken.SaveTokenKeyController;
import ir.shirazservice.expert.webservice.savetoken.SaveTokenKeyResponse;
import ir.shirazservice.expert.webservice.workmancredit.GetWorkmanCreditApi;
import ir.shirazservice.expert.webservice.workmancredit.GetWorkmanCreditController;
import ir.shirazservice.expert.webservice.workmancredit.WorkmanCredit;
import ir.shirazservice.expert.webservice.workmancredit.WorkmanCreditReq;

import static ir.shirazservice.expert.utils.APP.context;

public class MainActivity extends AppCompatActivity implements IRtl, IDefault, IInternetController {


    private static final int METHOD_TYPE_SAVE_TOKEN = 1;
    private static final int METHOD_TYPE_WORKMAN_CREDIT = 2;
    private final SaveTokenKeyApi.saveTokenKeyCallback saveTokenKeyCallback = new SaveTokenKeyApi.saveTokenKeyCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, SaveTokenKeyResponse response) {

        }

        @Override
        public void onFailure(String cause) {

        }
    };
    @BindView(R.id.text_cr)
    protected AppCompatTextView textCr;
    private WorkmanCredit workmanCredit;
    private final GetWorkmanCreditApi.getWorkmanCreditCallback getWorkmanCreditCallback =
            new GetWorkmanCreditApi.getWorkmanCreditCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, WorkmanCredit response) {
                    if (successful) {
                        workmanCredit = response;

                    } else workmanCredit = null;
                    setCredit();
                }

                @Override
                public void onFailure(String cause) {
                    workmanCredit = null;
                    setCredit();
                }

            };
    private BottomNavigationView bottomNavigation;
    private boolean doubleBackToExitPressedOnce = false;
    private int servicemanId;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        OnActivityDefaultSetting();
        setButtonNavigation();

        bottomNavigation.setSelectedItemId(R.id.navigation_home);
        openMainFragment();

        saveTokenKey();
    }

    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = MainActivity.this;
        neededId();
        getWorkManCredit();
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finishAffinity();
            finish();
            System.exit(0);
            MainActivity.this.finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        APP.customToast(getString(R.string.text_all_tap_back_button));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    public void OnActivityDefaultSetting() {
        OnPageRight();
    }

    @Override
    public void OnPageRight() {
        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(MainActivity.this).isOnline();
    }

    private void setButtonNavigation() {

        bottomNavigation = findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            switch (id) {
                case R.id.navigation_home:
                    openMainFragment();
                    break;
                case R.id.navigation_my_service:
                    openMyServiceFragment();
                    break;
                case R.id.navigation_transaction:
                    openMyTransactionFragment();
                    break;
                case R.id.navigation_message:
                    openMessageFragment();
                    break;
                case R.id.navigation_charge:
                    openChargeActivity();
                    break;
            }
            return true;
        });
    }

    private void openChargeActivity() {
        Intent intent = new Intent(MainActivity.this, ChargeActivity.class);
        startActivity(intent);
    }

    private void openMessageFragment() {
        WorkManMessageFragment workManMessageFragment = WorkManMessageFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, workManMessageFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openMainFragment() {
        MainFragment mainFragment = MainFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openMyServiceFragment() {
        MyServiceFragment myServiceFragment = MyServiceFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, myServiceFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openMyTransactionFragment() {
        MyTransactionFragment myTransactionFragment = MyTransactionFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, myTransactionFragment)
                .addToBackStack(null)
                .commit();
    }

    private void neededId() {
        ServiceMan serviceMan = GeneralPreferences.getInstance(this).getServiceManInfo();
        servicemanId = serviceMan.getServicemanId();
        accessToken = serviceMan.getAccessToken();
    }

    private void saveTokenKey() {

        if (!isOnline()) {
            openInternetCheckingDialog(METHOD_TYPE_SAVE_TOKEN);
        }

        SaveTokenKey saveTokenKey = new SaveTokenKey();
        saveTokenKey.setPersonId(servicemanId);
        saveTokenKey.setDeviceTokenKey(getToken());

        SaveTokenKeyController saveTokenKeyController = new SaveTokenKeyController(saveTokenKeyCallback);
        saveTokenKeyController.start(servicemanId, accessToken, saveTokenKey);

    }

    private String getToken() {
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.text_fire_base_news));
        return FirebaseInstanceId.getInstance().getToken();
    }

    private void openInternetCheckingDialog(int methodType) {
        ConnectionInternetDialog dialog = new ConnectionInternetDialog(MainActivity.this, new InternetConnectionListener() {
            @Override
            public void onInternet() {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }

            @Override
            public void onFinish() {
                APP.killApp();
            }

            @Override
            public void OnRetry() {
                switch (methodType) {
                    case METHOD_TYPE_SAVE_TOKEN:
                        saveTokenKey();
                        break;
                    case METHOD_TYPE_WORKMAN_CREDIT:
                        getWorkManCredit();
                        break;
                }
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        dialog.show();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.8);
        dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }

    private void getWorkManCredit() {

        if (!isOnline()) {
            openInternetCheckingDialog(METHOD_TYPE_WORKMAN_CREDIT);
        }

        WorkmanCreditReq workmanCreditReq = new WorkmanCreditReq();
        workmanCreditReq.setId(servicemanId);

        GetWorkmanCreditController getWorkmanCreditController =
                new GetWorkmanCreditController(getWorkmanCreditCallback);
        getWorkmanCreditController.start(servicemanId, accessToken, workmanCreditReq);
    }

    private void setCredit() {
        textCr.setText(new UsefulFunction().attachCamma(workmanCredit.getTempCredit()));
    }

}
