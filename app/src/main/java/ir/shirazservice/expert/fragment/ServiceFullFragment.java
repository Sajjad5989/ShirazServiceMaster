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
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.requestdetails.GetRequestDetailsApi;
import ir.shirazservice.expert.webservice.requestdetails.GetRequestDetailsController;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetails;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetailsReq;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static ir.shirazservice.expert.utils.APP.context;

public class ServiceFullFragment extends Fragment  {


    private static int requestId;
    @BindView(R.id.tv_phone_number)
    protected AppCompatTextView tvPhoneNumber;
    @BindView(R.id.tv_mobile)
    protected AppCompatTextView tvMobile;
    @BindView(R.id.img_request_Detail)
    protected AppCompatImageView imgRequestDetail;
    @BindView(R.id.tv_request_detail_service_title)
    AppCompatTextView tvRequestDetailServiceTitle;
    @BindView(R.id.tv_request_detail_tracking_code)
    AppCompatTextView tvRequestDetailTrackingCode;
    @BindView(R.id.tv_request_status)
    AppCompatTextView tvRequestStatus;
    @BindView(R.id.tv_full_address)
    AppCompatTextView tvPositionAddress;
    @BindView(R.id.tv_request_detail_desc_value)
    AppCompatTextView tvRequestDetailDescValue;
    @BindView(R.id.tv_request_detail_calculated_price)
    AppCompatTextView tvRequestDetailCalculatedPrice;
    @BindView(R.id.tv_request_detail_date_register)
    AppCompatTextView tvRequestDetailDateRegister;
    @BindView(R.id.tv_request_detail_date_value)
    AppCompatTextView tvRequestDetailDateValue;
    @BindView(R.id.tv_request_detail_time_value)
    AppCompatTextView tvRequestDetailTimeValue;
    @BindView(R.id.const_waiting_main_fragment)
    ConstraintLayout constWaiting;
    @BindView(R.id.const_not_found_info)
    ConstraintLayout constNotFound;

    private static RequestDetails requestDetails;
//    private final GetRequestDetailsApi.getRequestDetailsCallback getRequestDetailsCallback = new GetRequestDetailsApi.getRequestDetailsCallback() {
//        @Override
//        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, RequestDetails response) {
//            if (successful) {
//                requestDetails = response;
//                fillViews();
//            } else {
//                requestDetails = null;
//                fillViews();
//            }
//
//        }
//
//        @Override
//        public void onFailure(String cause) {
//            requestDetails = null;
//            fillViews();
//        }
//    };

    public static ServiceFullFragment newInstance(RequestDetails requestDetail) {
        requestDetails = requestDetail;
        return new ServiceFullFragment();
    }

    @OnClick(R.id.img_call_by_phone)
    void callPhone() {
        String phone = tvPhoneNumber.getText().toString();

        Intent callIntent = new Intent ( Intent.ACTION_DIAL );
        callIntent.setData ( Uri.parse ( "tel:" + phone.trim ( ) ) );
        callIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity ( callIntent );
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_service_full, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showHideWaitingProgress(false);
        loadRequestDetail();

    }

//    private void loadRequestDetail() {
//        if (!isOnline()) {
//            openInternetCheckingDialog();
//        }
//        if (requestId != 0) {
//            ServiceMan serviceMan = GeneralPreferences.getInstance(getActivity()).getServiceManInfo();
//            RequestDetailsReq requestDetailsReq = new RequestDetailsReq();
//            requestDetailsReq.setRequestId(requestId);
//            requestDetailsReq.setServicemanId(serviceMan.getServicemanId());
//
//            GetRequestDetailsController getRequestDetailsController =
//                    new GetRequestDetailsController(getActivity(), getRequestDetailsCallback);
//            getRequestDetailsController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(), requestDetailsReq);
//        } else
//            getActivity().finish();
//    }

    private void loadRequestDetail() {
        if (requestDetails != null) {

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

            tvPhoneNumber.setText(requestDetails.getPhone());
            tvMobile.setText(requestDetails.getMobile());
            tvRequestDetailServiceTitle.setText(requestDetails.getServiceTitle());
            tvRequestDetailTrackingCode.setText(requestDetails.getTrackingCode());

            //ratingBar.setRating(requestDetails.g);
            tvRequestStatus.setText(requestDetails.getStateTitle());
            tvPositionAddress.setText(requestDetails.getAreaTitle());
            tvRequestDetailDescValue.setText(requestDetails.getDesc());
            tvRequestDetailCalculatedPrice.setText(new UsefulFunction().attachCamma(String.valueOf(requestDetails.getCalculatedPrice())));
            tvRequestDetailDateRegister.setText(requestDetails.getDateToPersian());
            if (requestDetails.getTime() == BuildConfig.immediateCode) {
                tvRequestDetailDateValue.setText(R.string.text_imidiately);
                tvRequestDetailTimeValue.setText(R.string.text_imidiately);
            } else {
                tvRequestDetailDateValue.setText(requestDetails.getDateFromPersian());
                tvRequestDetailTimeValue.setText(requestDetails.getTimeDesc());
            }
            showHideWaitingProgress(true);
        } else
            showNotFoundInfoLayout();

    }

//    @Override
//    public boolean isOnline() {
//        return OnlineCheck.getInstance(getActivity()).isOnline();
//    }

//    private void openInternetCheckingDialog() {
//        ConnectionInternetDialog dialog = new ConnectionInternetDialog(getActivity(), new InternetConnectionListener() {
//            @Override
//            public void onInternet() {
//                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//            }
//
//            @Override
//            public void onFinish() {
//                APP.killApp();
//            }
//
//            @Override
//            public void OnRetry() {
//                fillViews();
//            }
//        });
//
//        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
//        dialog.show();
//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        width = (int) ((width) * 0.8);
//        dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//    }

    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        showHideWaitingProgress(true);
        constNotFound.setVisibility((requestDetails == null) ? View.VISIBLE : View.GONE);
    }

}
