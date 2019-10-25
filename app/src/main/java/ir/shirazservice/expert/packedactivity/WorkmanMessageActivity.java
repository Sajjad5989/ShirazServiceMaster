package ir.shirazservice.expert.packedactivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mlsdev.animatedrv.AnimatedRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.adapter.WorkManMessageAdapter;
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
import ir.shirazservice.expert.webservice.workmanmessages.GetWorkmanMessagesApi;
import ir.shirazservice.expert.webservice.workmanmessages.GetWorkmanMessagesController;
import ir.shirazservice.expert.webservice.workmanmessages.WorkmanInput;
import ir.shirazservice.expert.webservice.workmanmessages.WorkmanMessages;

import static ir.shirazservice.expert.utils.APP.context;

public class WorkmanMessageActivity extends AppCompatActivity implements IInternetController, IRtl, IDefault {


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.all_transaction_list)
    protected AnimatedRecyclerView allTransactionListRecycler;

    @BindView(R.id.const_waiting_main_fragment)
    protected ConstraintLayout constWaiting;
    @BindView(R.id.const_not_found_info)
    protected ConstraintLayout constNotFound;

    private List<WorkmanMessages> workmanMessages;
    private final GetWorkmanMessagesApi.getWorkmanMessagesCallback getWorkmanMessagesCallback =
            new GetWorkmanMessagesApi.getWorkmanMessagesCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<WorkmanMessages> response) {
                    if (successful)
                        workmanMessages = response;
                    else
                        workmanMessages = new ArrayList<>();

                    fillMessageRecyclerView();
                }

                @Override
                public void onFailure(String cause) {
                    workmanMessages = new ArrayList<>();

                    fillMessageRecyclerView();
                }
            };

    public static WorkmanMessageActivity newInstance() {
        return new WorkmanMessageActivity();
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

        showHideWaitingProgress(false);
        loadMessage();
    }


    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = WorkmanMessageActivity.this;
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
        toolbar.setTitle(R.string.title_all_message);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
    }


    private void loadMessage() {

        if (!isOnline())
            openInternetCheckingDialog();

        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();
        WorkmanInput workmanInput= new WorkmanInput();
        workmanInput.setCityId(serviceMan.getCityId());
        workmanInput.setProvinceId(serviceMan.getProvinceId());


        GetWorkmanMessagesController getWorkmanMessagesController = new
                GetWorkmanMessagesController(getWorkmanMessagesCallback);

        getWorkmanMessagesController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(),workmanInput);
    }

    private void fillMessageRecyclerView() {
        if (workmanMessages != null) {
            WorkManMessageAdapter workManMessageAdapter = new WorkManMessageAdapter(workmanMessages);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            allTransactionListRecycler.setLayoutManager(gridLayoutManager);
            allTransactionListRecycler.setAdapter(workManMessageAdapter);
            allTransactionListRecycler.scheduleLayoutAnimation();
        }
        showHideWaitingProgress(true);
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(this).isOnline();
    }


    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
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
                loadMessage();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        dialog.show();
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.8);
        dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }


    private void showNotFoundInfoLayout() {

        showHideWaitingProgress(true);
        constNotFound.setVisibility((workmanMessages == null) ? View.VISIBLE : View.GONE);
    }


}
