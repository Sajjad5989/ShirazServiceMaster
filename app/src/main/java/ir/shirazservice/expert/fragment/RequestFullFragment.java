package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.dialog.CancelRequestDialog;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.canclerequest.CancelRequestByWorkmanApi;
import ir.shirazservice.expert.webservice.canclerequest.CancelRequestByWorkmanController;
import ir.shirazservice.expert.webservice.canclerequest.CancelRequestByWorkmanRequest;
import ir.shirazservice.expert.webservice.canclerequest.PickupResponse;
import ir.shirazservice.expert.webservice.finishrequest.FinishRequestByWorkmanApi;
import ir.shirazservice.expert.webservice.finishrequest.FinishRequestByWorkmanController;
import ir.shirazservice.expert.webservice.finishrequest.FinishRequestByWorkmanRequest;
import ir.shirazservice.expert.webservice.finishrequest.SuccessIdResponse;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.requestdetails.GetRequestDetailsApi;
import ir.shirazservice.expert.webservice.requestdetails.GetRequestDetailsController;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetails;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetailsReq;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static ir.shirazservice.expert.utils.APP.context;

public class RequestFullFragment extends Fragment implements IInternetController {


    private static final int FINISH_REQUEST = 1;
    private static final int CANCEL_REQUEST = 2;
    private static final int LOAD_REQUEST = 3;
    private static int requestId;
    private final CancelRequestByWorkmanApi.cancelRequestByWorkmanCallback cancelRequestByWorkmanCallback
            = new CancelRequestByWorkmanApi.cancelRequestByWorkmanCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, PickupResponse response) {

        }

        @Override
        public void onFailure(String cause) {

        }
    };
    private final FinishRequestByWorkmanApi.finishRequestByWorkmanCallback finishRequestByWorkmanCallback =
            new FinishRequestByWorkmanApi.finishRequestByWorkmanCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, SuccessIdResponse response) {
                    if (successful)
                        getActivity().finish();
                    else
                        APP.customToast(errorResponse.getMessage());
                }

                @Override
                public void onFailure(String cause) {

                }
            };
    @BindView(R.id.const_waiting_main_fragment)
    protected ConstraintLayout constWaiting;
    @BindView(R.id.const_not_found_info)
    protected ConstraintLayout constNotFound;
    @BindView(R.id.img_request_Detail)
    AppCompatImageView imgRequestDetail;
    @BindView(R.id.img_request_cancel)
    AppCompatImageView imgRequestCancel;
    @BindView(R.id.tv_request_detail_service_title)
    AppCompatTextView tvRequestDetailServiceTitle;
    @BindView(R.id.tv_receiver_name)
    AppCompatTextView tvReceiverName;
    @BindView(R.id.tv_request_detail_calculated_price)
    AppCompatTextView tvRequestDetailCalculatedPrice;
    @BindView(R.id.tv_position_address)
    AppCompatTextView tvPositionAddress;
    @BindView(R.id.tv_full_address)
    AppCompatTextView tvFullAddress;
    @BindView(R.id.tv_discount_code)
    AppCompatTextView tvDiscountCode;
    @BindView(R.id.tv_discount_alarm_title2)
    AppCompatTextView tvDiscountAlarm;
    @BindView(R.id.const_discount)
    ConstraintLayout constDiscount;
    @BindView(R.id.tv_phone_number)
    AppCompatTextView tvPhoneNumber;
    @BindView(R.id.tv_mobile)
    AppCompatTextView tvMobile;
    @BindView(R.id.tv_request_detail_date_value)
    AppCompatTextView tvRequestDetailDateValue;
    @BindView(R.id.tv_service_info)
    AppCompatTextView tvServiceInfo;
    @BindView(R.id.tv_request_detail_time_value)
    AppCompatTextView tvRequestDetailTimeValue;
    @BindView(R.id.tv_request_detail_tracking_code)
    AppCompatTextView tvRequestDetailTrackingCode;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    private int whichMethod;
    private RequestDetails requestDetails;
    private final GetRequestDetailsApi.getRequestDetailsCallback getRequestDetailsCallback = new GetRequestDetailsApi.getRequestDetailsCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, RequestDetails response) {
            if (successful) {
                requestDetails = response;
                setRequestDetail();
            } else {
                requestDetails = null;
                setRequestDetail();
            }
        }

        @Override
        public void onFailure(String cause) {
            requestDetails = null;
            setRequestDetail();
        }
    };
    private int servicemanId;
    private String accessToken;

    public static RequestFullFragment newInstance(int reqId) {

        requestId = reqId;
        return new RequestFullFragment();
    }

    @OnClick(R.id.img_call_by_phone)
    void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvPhoneNumber.getText().toString()));
        startActivity(intent);
    }

    @OnClick(R.id.img_call_by_mobile)
    void callMobile() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvMobile.getText().toString()));
        startActivity(intent);
    }

    @OnClick(R.id.img_send_sms)
    void sendSms() {
        String number = tvMobile.getText().toString();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
    }

    private void setRequestDetail() {

        if (requestDetails != null) {
            //***************************************
            String picAddress = requestDetails.getServicePicAddress();
            if (null == picAddress || picAddress.equals("")) {
                imgRequestDetail.setImageResource(R.drawable.img_no_icon);
            } else {
                Picasso.with(getActivity())
                        .load(picAddress)  //Url of the image to load.
                        .transform(new CropCircleTransformation())
                        .error(R.drawable.img_no_icon)
                        .placeholder(R.drawable.img_loading)
                        .into(imgRequestDetail);
            }

            tvRequestDetailServiceTitle.setText(requestDetails.getServiceTitle());
            tvReceiverName.setText(requestDetails.getReceiverMan());
            tvRequestDetailCalculatedPrice.setText(new UsefulFunction().attachCamma(requestDetails.getCalculatedPrice()));
            tvPositionAddress.setText(requestDetails.getAreaTitle());
            tvFullAddress.setText(requestDetails.getAddress());
            tvDiscountCode.setText(requestDetails.getDiscountCode());
            tvDiscountAlarm.setText(requestDetails.getDiscountDesc());
            tvPhoneNumber.setText(requestDetails.getPhone());
            tvMobile.setText(requestDetails.getMobile());


            if (requestDetails.getTime() == BuildConfig.immediateCode) {
                tvRequestDetailDateValue.setText(R.string.text_imidiately);
                tvRequestDetailTimeValue.setText(R.string.text_imidiately);
            } else {
                tvRequestDetailDateValue.setText(requestDetails.getDateToPersian());
                tvRequestDetailTimeValue.setText(requestDetails.getTimeDesc());
            }
            if ("".equals(requestDetails.getDiscountCode())) {
                constDiscount.setVisibility(View.GONE);
            }

            tvRequestDetailTrackingCode.setText(requestDetails.getTrackingCode());

            //***************************************
            showHideWaitingProgress(true);
        } else
            showNotFoundInfoLayout();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_full, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showHideWaitingProgress(false);
        setNeededIds();
        loadRequestDetail();
        onClickConfig();

    }

    private void onClickConfig() {
        tvServiceInfo.setOnClickListener(view -> openServiceInfo());
        btnLogin.setOnClickListener(view -> callFinishRequest());
        imgRequestCancel.setOnClickListener(view -> cancelRequest());
    }

    private void setNeededIds() {
        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        servicemanId = serviceMan.getServicemanId();
        accessToken = serviceMan.getAccessToken();

    }

    private void cancelRequest() {

        CancelRequestDialog chooseRequestDialog = new CancelRequestDialog(getActivity(), done -> {
            if (done)
                cancelRequestByWorkman();
        });

        Objects.requireNonNull(chooseRequestDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        chooseRequestDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        chooseRequestDialog.setCancelable(false);
        chooseRequestDialog.show();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.9);
        chooseRequestDialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

    }

    private void cancelRequestByWorkman() {

        whichMethod = CANCEL_REQUEST;
        if (!isOnline()) {
            openInternetCheckingDialog();
        }


        CancelRequestByWorkmanRequest cancelRequestByWorkmanRequest = new CancelRequestByWorkmanRequest();
        cancelRequestByWorkmanRequest.setRequestId(requestId);


        CancelRequestByWorkmanController cancelRequestByWorkmanController
                = new CancelRequestByWorkmanController(cancelRequestByWorkmanCallback);
        cancelRequestByWorkmanController.start(servicemanId, accessToken,
                cancelRequestByWorkmanRequest);
    }

    private void callFinishRequest() {

        whichMethod = FINISH_REQUEST;
        if (!isOnline()) {
            openInternetCheckingDialog();
        }
        FinishRequestByWorkmanRequest finishRequestByWorkmanRequest = new FinishRequestByWorkmanRequest();
        finishRequestByWorkmanRequest.setRequestId(requestId);

        FinishRequestByWorkmanController finishRequestByWorkmanController =
                new FinishRequestByWorkmanController(getActivity(), finishRequestByWorkmanCallback);
        finishRequestByWorkmanController.start(servicemanId, accessToken,
                finishRequestByWorkmanRequest);
    }

    private void openServiceInfo() {
        Intent intent = new Intent(getActivity(), ServiceRequestDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.text_bundle_req_id), requestDetails.getServiceId());

        bundle.putInt(getString(R.string.text_bundle_service_status), BuildConfig.serviceInfoStaus);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void loadRequestDetail() {

        whichMethod = LOAD_REQUEST;
        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        ServiceMan serviceMan = GeneralPreferences.getInstance(getActivity()).getServiceManInfo();

        RequestDetailsReq requestDetailsReception = new RequestDetailsReq();
        requestDetailsReception.setRequestId(requestId);
        requestDetailsReception.setServicemanId(serviceMan.getServicemanId());

        GetRequestDetailsController getRequestDetailsController = new GetRequestDetailsController(getActivity(),
                getRequestDetailsCallback);
        getRequestDetailsController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(), requestDetailsReception);
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(getActivity()).isOnline();
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

                switch (whichMethod) {
                    case FINISH_REQUEST:
                        callFinishRequest();
                        break;
                    case CANCEL_REQUEST:
                        cancelRequest();
                        break;
                    case LOAD_REQUEST:
                        loadRequestDetail();
                        break;
                    default:
                        getActivity().finish();//TODO
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

    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        showHideWaitingProgress(true);
        constNotFound.setVisibility((requestId == 0) ? View.VISIBLE : View.GONE);
    }

}
