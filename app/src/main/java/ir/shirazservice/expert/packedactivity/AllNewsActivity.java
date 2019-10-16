package ir.shirazservice.expert.packedactivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import ir.shirazservice.expert.adapter.WorkManAllNewsAdapter;
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
import ir.shirazservice.expert.webservice.news.GetWorkmanNewsApi;
import ir.shirazservice.expert.webservice.news.GetWorkmanNewsController;
import ir.shirazservice.expert.webservice.news.WorkmanNews;

import static ir.shirazservice.expert.utils.APP.context;

public class AllNewsActivity extends AppCompatActivity implements IInternetController, IRtl, IDefault {


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.all_transaction_list)
    protected RecyclerView allTransactionListRecycler;

    @BindView(R.id.const_waiting_main_fragment)
    protected ConstraintLayout constWaiting;
    @BindView(R.id.const_not_found_info)
    protected ConstraintLayout constNotFound;


    private List<WorkmanNews> workmanNews;
    private final GetWorkmanNewsApi.getWorkmanNewsCallback getWorkmanNewsCallback = new GetWorkmanNewsApi.getWorkmanNewsCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<WorkmanNews> response) {
            if (successful) {
                workmanNews = response;

            } else {
                workmanNews = new ArrayList<>();
            }
            fillNewsList();
        }

        @Override
        public void onFailure(String cause) {

        }
    };

    public static AllNewsActivity newInstance() {
        return new AllNewsActivity();
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
        callGetNews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = AllNewsActivity.this;
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
        toolbar.setTitle(R.string.title_all_news);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
    }

    private void callGetNews() {

        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        int servicemanId = serviceMan.getServicemanId();
        String accessToken = serviceMan.getAccessToken();

        GetWorkmanNewsController getWorkmanNewsController = new GetWorkmanNewsController(getWorkmanNewsCallback);
        getWorkmanNewsController.start(servicemanId, accessToken);
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
                callGetNews();
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

    private void fillNewsList() {
        WorkManAllNewsAdapter workManNewsAdapter = new WorkManAllNewsAdapter(this, workmanNews, 0,
                (v, position) -> openUrl(workmanNews.get(position).getUrl()));

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);

        allTransactionListRecycler.setLayoutManager(gridLayoutManager);
        allTransactionListRecycler.setAdapter(workManNewsAdapter);

        showHideWaitingProgress(true);
        showNotFoundInfoLayout();
    }

    private void openUrl(String url) {

        if (url != null && !"".equals(url)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(this).isOnline();
    }

    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        constNotFound.setVisibility(workmanNews == null ? View.VISIBLE : View.GONE);
    }
}
