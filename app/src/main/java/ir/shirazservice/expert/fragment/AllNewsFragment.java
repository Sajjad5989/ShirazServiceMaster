package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.adapter.WorkManNewsAdapter;
import ir.shirazservice.expert.interfaces.IInternetController;
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
import static ir.shirazservice.expert.utils.APP.currentActivity;

public class AllNewsFragment extends Fragment implements IInternetController {

    @BindView(R.id.recycler_news)
    protected RecyclerView recyclerNews;
    @BindView(R.id.const_waiting_main_fragment)
    protected ConstraintLayout constraintLayout;
    @BindView(R.id.const_not_found_info)
    protected ConstraintLayout constNotFoundInfo;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_news, container, false);

        currentActivity = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showHideWaitingProgress(false);
        callGetNews();

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
        ConnectionInternetDialog dialog = new ConnectionInternetDialog(getActivity(), new InternetConnectionListener() {
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
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.8);
        dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }

    private void fillNewsList() {
        WorkManNewsAdapter workManNewsAdapter = new WorkManNewsAdapter(getActivity(), workmanNews, 0,
                (v, position) -> openUrl(workmanNews.get(position).getUrl()));

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false);

        recyclerNews.setLayoutManager(gridLayoutManager);
        recyclerNews.setAdapter(workManNewsAdapter);

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
        return OnlineCheck.getInstance(getActivity()).isOnline();
    }


    private void showHideWaitingProgress(boolean hide) {
        constraintLayout.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        constNotFoundInfo.setVisibility(workmanNews == null ? View.VISIBLE : View.GONE);
    }

    public static AllNewsFragment newInstance() {
        return new AllNewsFragment();
    }
}
