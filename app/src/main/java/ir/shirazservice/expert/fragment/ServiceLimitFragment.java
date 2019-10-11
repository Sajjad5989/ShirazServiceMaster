package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
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
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.requestinfo.RequestInfo;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static ir.shirazservice.expert.utils.APP.context;

public class ServiceLimitFragment extends Fragment implements IInternetController {


    private static RequestInfo requestInfo;
    private final CancelRequestByWorkmanApi.cancelRequestByWorkmanCallback cancelRequestByWorkmanCallback
            = new CancelRequestByWorkmanApi.cancelRequestByWorkmanCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, PickupResponse response) {
            if (successful)
                getActivity().finish();
            else
                APP.customToast(errorResponse.getMessage());
        }

        @Override
        public void onFailure(String cause) {

        }
    };

    @BindView(R.id.tv_service_info)
    AppCompatTextView tvServiceInfo;
    @BindView(R.id.img_request_Detail)
    protected AppCompatImageView imgRequestDetail;
    @BindView(R.id.img_request_cancel)
    AppCompatImageView imgRequestCancel;
    @BindView(R.id.tv_request_detail_service_title)
    AppCompatTextView tvRequestDetailServiceTitle;
    @BindView(R.id.tv_request_detail_tracking_code)
    AppCompatTextView tvRequestDetailTrackingCode;
    @BindView(R.id.tv_request_status)
    AppCompatTextView tvRequestStatus;
    @BindView(R.id.tv_request_detail_calculated_price)
    AppCompatTextView tvRequestDetailCalculatedPrice;
    @BindView(R.id.tv_position_address)
    AppCompatTextView tvPositionAddress;
    @BindView(R.id.tv_request_detail_desc_value)
    AppCompatTextView tvRequestDetailDescValue;
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

    public static ServiceLimitFragment newInstance(RequestInfo requestInfoInput) {
        requestInfo = requestInfoInput;
        return new ServiceLimitFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_service_limit, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onClickConfig();
        showHideWaitingProgress(false);
        loadRequestDetail();

    }

    private void onClickConfig()
    {
        imgRequestCancel.setOnClickListener(view -> cancelRequest());
        tvServiceInfo.setOnClickListener(view -> openServiceInfo());
    }
    private void openServiceInfo() {
        Intent intent = new Intent(getActivity(), ServiceRequestDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.text_bundle_req_id), requestInfo.getServiceId() * -1);
        intent.putExtras(bundle);
        startActivity(intent);
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

        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        CancelRequestByWorkmanRequest cancelRequestByWorkmanRequest = new CancelRequestByWorkmanRequest();
        cancelRequestByWorkmanRequest.setRequestId(requestInfo.getRequestId());

        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();
        CancelRequestByWorkmanController cancelRequestByWorkmanController
                = new CancelRequestByWorkmanController(cancelRequestByWorkmanCallback);
        cancelRequestByWorkmanController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(),
                cancelRequestByWorkmanRequest);
    }

    private void loadRequestDetail() {
        if (requestInfo != null) {

            String picAddress = requestInfo.getServicePicAddress();
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

            tvRequestDetailServiceTitle.setText(requestInfo.getServiceTitle());
            tvRequestDetailTrackingCode.setText(requestInfo.getTrackingCode());
            //ratingBar.setRating(requestInfo.get);
            tvRequestStatus.setText(requestInfo.getStateTitle());
            tvPositionAddress.setText(requestInfo.getAreaTitle());
            tvRequestDetailDescValue.setText(requestInfo.getDesc());
            tvRequestDetailCalculatedPrice.setText(new UsefulFunction().attachCamma(String.valueOf(requestInfo.getCalculatedPrice())));
            tvRequestDetailDateRegister.setText(requestInfo.getDateToPersian());

            if (requestInfo.getTime() == BuildConfig.immediateCode) {
                tvRequestDetailDateValue.setText(R.string.text_imidiately);
                tvRequestDetailTimeValue.setText(R.string.text_imidiately);
            } else {
                tvRequestDetailDateValue.setText(requestInfo.getDateFromPersian());
                tvRequestDetailTimeValue.setText(requestInfo.getTimeDesc());
            }
            showHideWaitingProgress(true);
        }
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
                cancelRequestByWorkman();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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


}
