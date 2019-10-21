package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ProfileActivity;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.adapter.RequestListAdapter;
import ir.shirazservice.expert.adapter.SessionSliderAdapter;
import ir.shirazservice.expert.adapter.WorkManNewsAdapter;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.packedactivity.AllNewsActivity;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.news.GetWorkmanNewsApi;
import ir.shirazservice.expert.webservice.news.GetWorkmanNewsController;
import ir.shirazservice.expert.webservice.news.WorkmanNews;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetailList;
import ir.shirazservice.expert.webservice.requestlist.Request;
import ir.shirazservice.expert.webservice.requestlist.RequestListApi;
import ir.shirazservice.expert.webservice.requestlist.RequestListController;
import ir.shirazservice.expert.webservice.requestlist.RequestListInputs;
import ir.shirazservice.expert.webservice.slider.AdsSlider;
import ir.shirazservice.expert.webservice.slider.GetAdsSliderApi;
import ir.shirazservice.expert.webservice.slider.GetAdsSliderController;
import ir.shirazservice.expert.webservice.workmanpoint.WorkManPoint;
import ir.shirazservice.expert.webservice.workmanpoint.WorkManPointInput;
import ir.shirazservice.expert.webservice.workmanpoint.WorkmanActivityPointApi;
import ir.shirazservice.expert.webservice.workmanpoint.WorkmanActivityPointController;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static ir.shirazservice.expert.utils.APP.context;
import static ir.shirazservice.expert.utils.APP.currentActivity;

public class MainFragment extends Fragment implements IInternetController {

    private static final int METHOD_TYPE_SLIDER = 1;
    private static final int METHOD_TYPE_REQUEST = 2;
    private static final int METHOD_TYPE_NEWS = 3;
    private static final int METHOD_TYPE_WORKMAN_POINT = 4;
    private final RequestDetailList requestDetailList = new RequestDetailList();
    private AppCompatTextView tvExpertName;
    private AppCompatTextView tvExpRate;
    private AppCompatTextView tvExpertDiscount;
    private AppCompatTextView tvRatingBar;
    private AppCompatTextView tvViewAllNews;
    private RatingBar ratingBar;
    private AppCompatTextView tvRetry;
    private AppCompatImageView imageExpert;
    private RecyclerView recyclerRequest;
    private RecyclerView recyclerNews;
    private ViewPager viewPagerSlider;
    private LinearLayout linearDots;
    private ConstraintLayout constraintLayout;
    private ConstraintLayout constNotFoundInfo;
    private List<Request> selectedRequestList;
    private List<WorkmanNews> workmanNews;
    private List<AdsSlider> adsSliders;
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
    private CircularProgressBar circularProgressBar;
    private int servicemanId;
    private String accessToken;
    private final RequestListApi.GetRequestListCallback getRequestListCallback = new RequestListApi.GetRequestListCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<Request> response) {
            if (successful) {
                selectedRequestList = response;
            } else {
                selectedRequestList = new ArrayList<>();
            }
            fillRequestList();
        }

        @Override
        public void onFailure(String cause) {
            selectedRequestList = new ArrayList<>();
            fillRequestList();
        }
    };
    private final GetAdsSliderApi.getAdsSliderCallback getAdsSliderCallback = new GetAdsSliderApi.getAdsSliderCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<AdsSlider> response) {
            if (successful) {
                adsSliders = response;

            } else {
                adsSliders = new ArrayList<>();
            }
            setAndShowSlidersPhoto();
        }

        @Override
        public void onFailure(String cause) {
            adsSliders = new ArrayList<>();
            setAndShowSlidersPhoto();
        }
    };
    private WorkmanActivityPointApi.getWorkmanActivityPointCallback getWorkmanActivityPointCallback =
            new WorkmanActivityPointApi.getWorkmanActivityPointCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, WorkManPoint response) {
                    if (successful) {
                        setWorkmanPoint(response.getActivityPoint());
                    }else setWorkmanPoint(0);
                }

                @Override
                public void onFailure(String cause) {
                    setWorkmanPoint(0);
                }
            };

    private void setWorkmanPoint(int point)
    {
        tvExpRate.setText(String.valueOf(point));
        circularProgressBar.setProgress(point);
        circularProgressBar.setProgressWithAnimation(65f, 1000L); // =1s
        circularProgressBar.setProgressMax(100f);

        callGetSliderAds();
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        currentActivity = getActivity();
        assert container != null;
        context = container.getContext();
        return view;
    }

    private void findViews(View view) {

        tvExpertName = view.findViewById(R.id.tv_expert_name);
        tvExpRate = view.findViewById(R.id.tv_exp_rate);
        tvRetry = view.findViewById(R.id.tv_retry);
        tvExpertDiscount = view.findViewById(R.id.tv_expert_discount);
        tvViewAllNews = view.findViewById(R.id.tv_view_all_news);
        tvRatingBar = view.findViewById(R.id.tv_ratingBar);
        ratingBar = view.findViewById(R.id.ratingBar);
        imageExpert = view.findViewById(R.id.image_expert);
        recyclerRequest = view.findViewById(R.id.recycler_request);
        recyclerNews = view.findViewById(R.id.recycler_news);

        viewPagerSlider = view.findViewById(R.id.view_pager_slider);
        linearDots = view.findViewById(R.id.linear_dots);

        constraintLayout = view.findViewById(R.id.const_waiting_main_fragment);
        constNotFoundInfo = view.findViewById(R.id.const_not_found_info);

        circularProgressBar = view.findViewById(R.id.circularProgressBar);

    }

    private void btnClickConfig() {
        tvRetry.setOnClickListener(view -> getAndSetServiceManInfo());
        tvViewAllNews.setOnClickListener(view -> openAllNewsActivity());
        imageExpert.setOnClickListener(view -> openExpertProfile());
    }

    private void openAllNewsActivity() {
        Intent intent = new Intent(getActivity(), AllNewsActivity.class);
        startActivity(intent);
    }

    private void openExpertProfile() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        loadNeeded();

        btnClickConfig();

    }

    @Override
    public void onResume() {
        super.onResume();
        getAndSetServiceManInfo();
    }

    private void getAndSetServiceManInfo() {
        showHideWaitingProgress(false);
        ServiceMan serviceManInfo = GeneralPreferences.getInstance(getActivity()).getServiceManInfo();
        if (serviceManInfo == null) {
            getActivity().finish();
            return;
        }
//
//        tvExpRate.setText(String.valueOf(serviceManInfo.getTotalPoint()));
//        circularProgressBar.setProgress(serviceManInfo.getTotalPoint());
//        circularProgressBar.setProgressWithAnimation(65f, 1000L); // =1s
//        circularProgressBar.setProgressMax(100f);

        tvExpertName.setText(serviceManInfo.getNameFamily());
        tvExpertDiscount.setText(String.format("%s%%", String.valueOf(serviceManInfo.getDiscountPercent())));

        tvRatingBar.setText(getString(R.string.text_your_rate) + serviceManInfo.getRating());
        ratingBar.setRating((float) serviceManInfo.getRating());

        String picAddress = serviceManInfo.getPicAddress();
        if (null == picAddress || picAddress.equals("")) {
            imageExpert.setImageResource(R.drawable.img_no_icon);
        } else {
            Picasso.get()
                    .load(picAddress)  //Url of the image to load.
                    .transform(new CropCircleTransformation())
                    .error(R.drawable.img_no_icon)
                    .placeholder(R.drawable.img_loading)
                    .into(imageExpert);
        }

        getWorkmanPoint();

    }

    private void openRequestLimitDetail(Request request) {
        Intent intent = new Intent(getActivity(), ServiceRequestDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.text_bundle_req_id), request.getRequestId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void fillRequestList() {
        RequestListAdapter requestListAdapter = new RequestListAdapter(getActivity(), selectedRequestList, (v, position) -> {

            requestDetailList.setRequestLists(selectedRequestList.get(position));
            openRequestLimitDetail(selectedRequestList.get(position));
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerRequest.setLayoutManager(gridLayoutManager);
        recyclerRequest.setAdapter(requestListAdapter);


        callGetNews();
    }

    private void loadNeeded() {
        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        servicemanId = serviceMan.getServicemanId();
        accessToken = serviceMan.getAccessToken();

    }

    private RequestListInputs getListInput() {

        //ToDo  مقادیر زیر را بایستی پر کنم
        RequestListInputs requestListInputs = new RequestListInputs();
        requestListInputs.setServicemanId(servicemanId);
        requestListInputs.setServiceTitle("");
        requestListInputs.setServiceId(0);
        requestListInputs.setServiceSubCatId(0);
        requestListInputs.setAreaId(0);
        requestListInputs.setPriorityId(0);
        requestListInputs.setDateFrom(0);
        requestListInputs.setTime(0);
        requestListInputs.setDesc("");
        return requestListInputs;
    }

    private void callGetNews() {

        if (!isOnline()) {
            openInternetCheckingDialog(METHOD_TYPE_NEWS);
        }

        GetWorkmanNewsController getWorkmanNewsController = new GetWorkmanNewsController(getWorkmanNewsCallback);
        getWorkmanNewsController.start(servicemanId, accessToken);
    }

    private void callGetSliderAds() {
        if (!isOnline()) {
            openInternetCheckingDialog(METHOD_TYPE_SLIDER);
        }

        GetAdsSliderController getAdsSliderController = new GetAdsSliderController(getAdsSliderCallback);
        getAdsSliderController.start(servicemanId, accessToken);
    }

    private void getRequestList() {

        if (!isOnline()) {
            openInternetCheckingDialog(METHOD_TYPE_REQUEST);
        }

        RequestListController requestListController = new RequestListController(getActivity(),
                getRequestListCallback);
        requestListController.start(servicemanId, accessToken, getListInput());

    }

    private void openInternetCheckingDialog(int methodTypeSlider) {
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
                switch (methodTypeSlider) {
                    case METHOD_TYPE_SLIDER:
                        callGetSliderAds();
                        break;
                    case METHOD_TYPE_REQUEST:
                        getRequestList();
                        break;
                    case METHOD_TYPE_NEWS:
                        callGetNews();
                        break;
                    case METHOD_TYPE_WORKMAN_POINT:
                        getWorkmanPoint();
                        break;
                }
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

    private void setAndShowSlidersPhoto() {

        viewPagerChangeListener();
        SessionSliderAdapter sessionSliderAdapter = new SessionSliderAdapter(getActivity(), adsSliders,
                (v, position) -> openUrl(adsSliders.get(position).getUrl()));

        viewPagerSlider.setAdapter(sessionSliderAdapter);
        showDots(viewPagerSlider.getCurrentItem());

        getRequestList();

    }

    private void viewPagerChangeListener() {
        viewPagerSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                showDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void showDots(int pageNumber) {

        if (getActivity() == null)
            return;

        RelativeLayout[] relativeDots = new RelativeLayout[Objects.requireNonNull(viewPagerSlider.getAdapter()).getCount()];

        linearDots.removeAllViews();


        for (int i = 0; i < relativeDots.length; i++) {
            relativeDots[i] = new RelativeLayout(getActivity());

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(20, 6);
            RelativeLayout.LayoutParams currentLayoutParams = new RelativeLayout.LayoutParams(40, 6);
            layoutParams.setMargins(4, 0, 4, 0);
            currentLayoutParams.setMargins(4, 0, 4, 0);

            if (i == pageNumber) {
                relativeDots[i].setLayoutParams(currentLayoutParams);
            } else {
                relativeDots[i].setLayoutParams(layoutParams);
            }

            if (i == pageNumber) {
                relativeDots[i].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
            } else if (i < pageNumber) {
                relativeDots[i].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.info_forms_bg));
            } else {
                relativeDots[i].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorControlNormal));
            }

            linearDots.addView(relativeDots[i]);

        }
    }

    private void openUrl(String url) {

        if (url != null && !"".equals(url)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    private void fillNewsList() {
        WorkManNewsAdapter workManNewsAdapter = new WorkManNewsAdapter(getActivity(), workmanNews, 2,
                (v, position) -> openUrl(workmanNews.get(position).getUrl()));

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);

        recyclerNews.setLayoutManager(gridLayoutManager);
        recyclerNews.setAdapter(workManNewsAdapter);

        showHideWaitingProgress(true);
        showNotFoundInfoLayout();
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(getActivity()).isOnline();
    }

    private void showHideWaitingProgress(boolean hide) {
        constraintLayout.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        constNotFoundInfo.setVisibility((selectedRequestList == null && workmanNews == null && adsSliders == null) ? View.VISIBLE : View.GONE);
    }

    private void getWorkmanPoint() {
        if (!isOnline()) {
            openInternetCheckingDialog(METHOD_TYPE_WORKMAN_POINT);
        }
        WorkManPointInput workManPointInput = new WorkManPointInput();
        workManPointInput.setId(servicemanId);

        WorkmanActivityPointController workmanActivityPointController = new WorkmanActivityPointController(getWorkmanActivityPointCallback);
        workmanActivityPointController.start(servicemanId, accessToken, workManPointInput);
    }

}
