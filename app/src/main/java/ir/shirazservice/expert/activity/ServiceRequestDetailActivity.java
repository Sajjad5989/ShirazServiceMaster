package ir.shirazservice.expert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.fragment.OfflineChargeFragment;
import ir.shirazservice.expert.fragment.RequestFullFragment;
import ir.shirazservice.expert.fragment.RequestLimitFragment;
import ir.shirazservice.expert.fragment.ServiceFullFragment;
import ir.shirazservice.expert.fragment.ServiceInfoFragment;
import ir.shirazservice.expert.fragment.ServiceLimitFragment;
import ir.shirazservice.expert.interfaces.IDefault;
import ir.shirazservice.expert.interfaces.IRtl;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.webservice.canclerequest.CancelRequestByWorkmanController;
import ir.shirazservice.expert.webservice.canclerequest.CancelRequestByWorkmanRequest;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.requestdetails.GetRequestDetailsApi;
import ir.shirazservice.expert.webservice.requestdetails.GetRequestDetailsController;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetails;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetailsReq;

public class ServiceRequestDetailActivity extends AppCompatActivity implements IRtl, IDefault {


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private int chooseCorrectFragment;
    private int reqId;
    private String toolbarTitle;
    private RequestDetails requestDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_request_detail);
        ButterKnife.bind(this);

        OnActivityDefaultSetting();

        getIds();
        openCorrectFragment();

        prepareToolbar();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getIds() {

        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        if (bundle != null) {
         //   int serviceStatus = bundle.getInt(getString(R.string.text_bundle_service_status));
            reqId = bundle.getInt(getString(R.string.text_bundle_req_id));

            checkRequestStatus(reqId);
            if (requestDetails == null)
                this.finish();

            int serviceStatus = requestDetails.getState();
            //وضعیت درخواست: 1 => ثبت شده؛ 2 => انتخاب شده؛ 3 => لغو / حذف شده؛ 4 => پذیرش شده؛ 5 => عدم ارائه سرویس؛ 6 => انجام شده؛ 7 => منقضی شده
            //اگر در هر حالتی غیر از "انجام شده" و "پذیرش شده" بودیم، بایستی متد (getRequestInfo) رو فراخوانی کنید
            //getRequestById -



            switch (serviceStatus) {
                case 1:
                    chooseCorrectFragment = BuildConfig.requestLimit;
                    break;
                case 2:
                case 3:
                case 5:
                case 7:
                    chooseCorrectFragment = BuildConfig.serviceLimit;
                    break;
                case 4:
                    chooseCorrectFragment = BuildConfig.requestFull;
                    break;
                case 6:
                    chooseCorrectFragment = BuildConfig.serviceFull;
                    break;
                case 100:
                    chooseCorrectFragment = BuildConfig.serviceInfo;
                    break;
                case 101:
                    chooseCorrectFragment = BuildConfig.offlineCharge;
                    break;
            }
        }
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
        switch (chooseCorrectFragment) {
            case BuildConfig.requestLimit:
                toolbarTitle = getString(R.string.text_request_detail_title);
                openRequestLimit();
                break;
            case BuildConfig.requestFull:
                toolbarTitle = getString(R.string.text_request_detail_title);
                openRequestFull();
                break;
            case BuildConfig.serviceLimit:
                toolbarTitle = getString(R.string.text_service_detail_title);
                openServicelimit();
                break;
            case BuildConfig.serviceFull:
                toolbarTitle = getString(R.string.text_service_detail_title);
                openServiceFull();
                break;
            case BuildConfig.serviceInfo:
                toolbarTitle = getString(R.string.text_service_detail_title);
                openServiceInfo();
                break;
            case BuildConfig.offlineCharge:
                toolbarTitle = getString(R.string.text_payment_detail);
                OpenOfflineCharge();
                break;
        }
    }

    private void OpenOfflineCharge() {
        OfflineChargeFragment offlineChargeFragment = OfflineChargeFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, offlineChargeFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRequestLimit() {
        RequestLimitFragment requestLimitFragment = RequestLimitFragment.newInstance(null);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, requestLimitFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRequestFull() {
        RequestFullFragment requestFullFragment = RequestFullFragment.newInstance(reqId);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, requestFullFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openServicelimit() {
        ServiceLimitFragment serviceLimitFragment = ServiceLimitFragment.newInstance(reqId);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceLimitFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openServiceFull() {
        ServiceFullFragment serviceFullFragment = ServiceFullFragment.newInstance(reqId);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceFullFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openServiceInfo() {

        ServiceInfoFragment serviceFullFragment = ServiceInfoFragment.newInstance(reqId);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceFullFragment)
                .addToBackStack(null)
                .commit();
    }

    private void checkRequestStatus(int requestId)
    {
        ServiceMan serviceMan = GeneralPreferences.getInstance(ServiceRequestDetailActivity.this).getServiceManInfo();

        RequestDetailsReq requestDetailsReception = new RequestDetailsReq();
        requestDetailsReception.setRequestId(requestId);
        requestDetailsReception.setServicemanId(serviceMan.getServicemanId());

        GetRequestDetailsController getRequestDetailsController = new GetRequestDetailsController(this,
                getRequestDetailsCallback);
        getRequestDetailsController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(), requestDetailsReception);
    }

    private final GetRequestDetailsApi.getRequestDetailsCallback getRequestDetailsCallback = new GetRequestDetailsApi.getRequestDetailsCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, RequestDetails response) {
            if (successful) {
                requestDetails = response;
            } else {
                requestDetails = null;
            }
        }

        @Override
        public void onFailure(String cause) {
            requestDetails = null;
        }
    };


}
