package ir.shirazservice.expert.packedactivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.adapter.MyServiceAdapter;
import ir.shirazservice.expert.interfaces.IDefault;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.interfaces.IRtl;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.myservice.GetMyServicesApi;
import ir.shirazservice.expert.webservice.myservice.GetMyServicesController;
import ir.shirazservice.expert.webservice.myservice.MyService;
import ir.shirazservice.expert.webservice.myservice.ReceptionMyService;

import static ir.shirazservice.expert.utils.APP.context;

public class MyServicesActivity extends AppCompatActivity implements IInternetController, IRtl, IDefault {


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.all_transaction_list)
    protected RecyclerView allTransactionListRecycler;
    @BindView(R.id.const_waiting_main_fragment)
    protected ConstraintLayout constWaiting;
    @BindView(R.id.const_not_found_info)
    protected ConstraintLayout constNotFound;
    private List<MyService> myServices;
    private final GetMyServicesApi.getMyServicesCallback getMyServicesCallback = new GetMyServicesApi.getMyServicesCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<MyService> response) {
            if (successful) {
                myServices = response;
            } else {
                myServices = new ArrayList<>();
            }
            fillMyServiceList();
        }

        @Override
        public void onFailure(String cause) {
            myServices = new ArrayList<>();
            fillMyServiceList();
        }
    };

    public static MyServicesActivity newInstance() {
        return new MyServicesActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_transaction);

        ButterKnife.bind(this);
        prepareToolbar();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        getMyServices();

    }

    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = MyServicesActivity.this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    private void prepareToolbar() {
        toolbar.setTitle(R.string.title_my_services);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
    }


    private void fillMyServiceList() {
        MyServiceAdapter myServiceAdapter = new MyServiceAdapter(this, myServices, (v, position) -> {
            Intent intent = new Intent(this, ServiceRequestDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.text_bundle_req_id), myServices.get(position).getReqId());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

        allTransactionListRecycler.setLayoutManager(gridLayoutManager);
        allTransactionListRecycler.setAdapter(myServiceAdapter);
        showHideWaitingProgress(true);
        showNotFoundInfoLayout();
    }

    private void getMyServices() {
        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        showHideWaitingProgress(false);
        GetMyServicesController getMyServicesController = new GetMyServicesController(getMyServicesCallback);
        getMyServicesController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(), getValue(serviceMan));
    }

    private ReceptionMyService getValue(ServiceMan serviceMan) {
        ReceptionMyService receptionMyService = new ReceptionMyService();
        receptionMyService.setInsrtDateShamsi("");
        receptionMyService.setServicemanId(serviceMan.getServicemanId());
        receptionMyService.setState(0);
        return receptionMyService;
    }


    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(this).isOnline();
    }


    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        constNotFound.setVisibility((myServices == null) ? View.VISIBLE : View.GONE);
    }

    private void openInternetCheckingDialog() {
        ConnectionInternetDialog dialog = new ConnectionInternetDialog(this, new InternetConnectionListener() {
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
                getMyServices();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        dialog.show();
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.8);
        dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }


}
