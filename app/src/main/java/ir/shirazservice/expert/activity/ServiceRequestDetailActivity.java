package ir.shirazservice.expert.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.fragment.AllNewsFragment;
import ir.shirazservice.expert.fragment.OfflineChargeFragment;
import ir.shirazservice.expert.fragment.RequestFullFragment;
import ir.shirazservice.expert.fragment.RequestLimitFragment;
import ir.shirazservice.expert.fragment.ServiceFullFragment;
import ir.shirazservice.expert.fragment.ServiceInfoFragment;
import ir.shirazservice.expert.fragment.ServiceLimitFragment;
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
import ir.shirazservice.expert.webservice.requestdetails.GetRequestDetailsApi;
import ir.shirazservice.expert.webservice.requestdetails.GetRequestDetailsController;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetails;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetailsReq;
import ir.shirazservice.expert.webservice.requestinfo.RequestInfo;
import ir.shirazservice.expert.webservice.requestinfo.RequestInfoApi;
import ir.shirazservice.expert.webservice.requestinfo.RequestInfoController;
import ir.shirazservice.expert.webservice.requestinfo.RequestInfoInput;
import ir.shirazservice.expert.webservice.serviceInfo.GetServiceInfoApi;
import ir.shirazservice.expert.webservice.serviceInfo.GetServiceInfoController;
import ir.shirazservice.expert.webservice.serviceInfo.ReceptionService;
import ir.shirazservice.expert.webservice.serviceInfo.ServiceInfo;

import static ir.shirazservice.expert.utils.APP.context;

public class ServiceRequestDetailActivity extends AppCompatActivity implements IRtl, IDefault, IInternetController {


    private static final int METHOD_TYPE_SERVICE_INFO = 1;
    private static final int METHOD_TYPE_REQUEST_INFO = 2;
    private static final int METHOD_TYPE_REQUEST_DETAIL = 3;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    private int chooseCorrectFragment;
    private int reqId;
    private String isNews;
    private String toolbarTitle;
    private RequestDetails requestDetails;
    private final GetRequestDetailsApi.getRequestDetailsCallback getRequestDetailsCallback = new GetRequestDetailsApi.getRequestDetailsCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, RequestDetails response) {
            if (successful) {
                requestDetails = response;
                if (chooseCorrectFragment == BuildConfig.requestAccept)
                    openRequestFull();
                else
                    openServiceFull();

            } else {
                requestDetails = null;
            }
        }

        @Override
        public void onFailure(String cause) {
            requestDetails = null;
        }
    };
    private RequestInfo requestInfo;
    private ServiceInfo serviceInfo;
    private final GetServiceInfoApi.getServiceInfoCallBack getServiceInfoCallBack = new
            GetServiceInfoApi.getServiceInfoCallBack() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, ServiceInfo response) {
                    if (successful) {
                        serviceInfo = response;
                        openServiceInfo();
                    } else
                        serviceInfo = null;
                }

                @Override
                public void onFailure(String cause) {
                    serviceInfo = null;
                }
            };
    private int serviceManId;
    private String accessToken;
    private final RequestInfoApi.GetRequestInfoCallback getRequestInfoCallback = new RequestInfoApi.GetRequestInfoCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, RequestInfo response) {
            if (successful) {
                requestInfo = response;
                chooseCorrectFragment = requestInfo.getState();
                openCorrectFragment();
            } else {
                requestInfo = null;
            }
        }

        @Override
        public void onFailure(String cause) {
            requestInfo = null;
        }
    };

    private void callCorrectFragment() {
        if (reqId == 0) {
            if (isNews.equals("news"))
                chooseCorrectFragment = BuildConfig.newsCode;
            else
                chooseCorrectFragment = BuildConfig.offlineCharge;
            openCorrectFragment();
        } else if (reqId < 0) {
            chooseCorrectFragment = BuildConfig.serviceInfo;
            reqId *= -1;
            openCorrectFragment();
        } else {
            getRequestInfo(reqId);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_request_detail);
        ButterKnife.bind(this);

        OnActivityDefaultSetting();
        getNeededIds();
        getIds();
        callCorrectFragment();


        prepareToolbar();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getNeededIds() {
        ServiceMan serviceMan = GeneralPreferences.getInstance(this).getServiceManInfo();
        serviceManId = serviceMan.getServicemanId();
        accessToken = serviceMan.getAccessToken();
    }

    private void getIds() {

        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        if (bundle != null) {
            reqId = bundle.getInt(getString(R.string.text_bundle_req_id));
            isNews = bundle.getString("news");
        } else this.finish();
    }

    private void prepareToolbar() {
        toolbar.setTitle(toolbarTitle);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = ServiceRequestDetailActivity.this;
        APP.setPersianUi();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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

    private void openCorrectFragment() {

        toolbarTitle = getResources().getString(R.string.text_service_detail);
        prepareToolbar();
        switch (chooseCorrectFragment) {
            case BuildConfig.requestLimit:
                openRequestlimit();
                break;
            case BuildConfig.requestSelect:
                openServicelimit();
                break;
            case BuildConfig.requestFinished:
            case BuildConfig.requestAccept:
                getRequestDetails(reqId);
                break;
            case BuildConfig.serviceInfo:
                toolbarTitle =getString(R.string.text_service_information);
                prepareToolbar();
                getServiceInfo(reqId);
                break;
            case BuildConfig.offlineCharge:
                toolbarTitle = getResources().getString(R.string.text_payment_detail);
                prepareToolbar();
                openOfflineCharge();
                break;
            case BuildConfig.newsCode:
                toolbarTitle = getString(R.string.text_all_news);
                prepareToolbar();
                openAllNews();
                break;
        }
    }

    private void openAllNews() {
        AllNewsFragment allNewsFragment = AllNewsFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, allNewsFragment)
                .addToBackStack(null)
                .commit();

    }

    private void openOfflineCharge() {
        OfflineChargeFragment offlineChargeFragment = OfflineChargeFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, offlineChargeFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRequestFull() {
        RequestFullFragment requestFullFragment = RequestFullFragment.newInstance(requestDetails);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, requestFullFragment)
                .addToBackStack(null)
                .commit();
    }


    private void openRequestlimit() {
        RequestLimitFragment serviceLimitFragment = RequestLimitFragment.newInstance(requestInfo);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceLimitFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openServicelimit() {
        ServiceLimitFragment serviceLimitFragment = ServiceLimitFragment.newInstance(requestInfo);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceLimitFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openServiceFull() {
        ServiceFullFragment serviceFullFragment = ServiceFullFragment.newInstance(requestDetails);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceFullFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openServiceInfo() {

        ServiceInfoFragment serviceFullFragment = ServiceInfoFragment.newInstance(serviceInfo);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceFullFragment)
                .addToBackStack(null)
                .commit();
    }

    private void getRequestDetails(int requestId) {

        if (!isOnline())
            openInternetCheckingDialog(METHOD_TYPE_REQUEST_DETAIL);

        RequestDetailsReq requestDetailsReception = new RequestDetailsReq();
        requestDetailsReception.setRequestId(requestId);
        requestDetailsReception.setServicemanId(serviceManId);

        GetRequestDetailsController getRequestDetailsController = new GetRequestDetailsController(this,
                getRequestDetailsCallback);
        getRequestDetailsController.start(serviceManId, accessToken, requestDetailsReception);
    }

    private void getRequestInfo(int requestId) {

        if (!isOnline())
            openInternetCheckingDialog(METHOD_TYPE_REQUEST_INFO);

        RequestInfoInput requestInfoInput = new RequestInfoInput();
        requestInfoInput.setRequestId(requestId);
        requestInfoInput.setServicemanId(serviceManId);

        RequestInfoController requestInfoController = new RequestInfoController(getRequestInfoCallback);
        requestInfoController.start(serviceManId, accessToken, requestInfoInput);
    }

    private void getServiceInfo(int requestId) {

        if (!isOnline())
            openInternetCheckingDialog(METHOD_TYPE_SERVICE_INFO);

        GetServiceInfoController getServiceInfoController = new GetServiceInfoController(getServiceInfoCallBack);
        ReceptionService receptionService = new ReceptionService();
        receptionService.setServiceId(requestId);
        getServiceInfoController.start(serviceManId, accessToken, receptionService);
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(this).isOnline();
    }

    private void openInternetCheckingDialog(int methodType) {
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
                switch (methodType) {
                    case METHOD_TYPE_SERVICE_INFO:
                        getServiceInfo(reqId);
                        break;
                    case METHOD_TYPE_REQUEST_INFO:
                        getRequestInfo(reqId);
                        break;
                    case METHOD_TYPE_REQUEST_DETAIL:
                        getRequestDetails(reqId);
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


}
