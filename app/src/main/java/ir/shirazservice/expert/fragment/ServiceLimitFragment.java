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
import android.support.v7.widget.AppCompatTextView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
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
import ir.shirazservice.expert.webservice.requestinfo.RequestInfo;
import ir.shirazservice.expert.webservice.requestinfo.RequestInfoApi;
import ir.shirazservice.expert.webservice.requestinfo.RequestInfoController;
import ir.shirazservice.expert.webservice.requestinfo.RequestInfoInput;

import static ir.shirazservice.expert.utils.APP.context;

public class ServiceLimitFragment extends Fragment implements IInternetController {


    private static int requestId;
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
    private RequestInfo requestInfo;
    private final RequestInfoApi.GetRequestInfoCallback getRequestInfoCallback = new RequestInfoApi.GetRequestInfoCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, RequestInfo response) {
            if (successful) {
                requestInfo = response;
                fillViews();
            } else {
                requestInfo = null;
                fillViews();
            }
        }

        @Override
        public void onFailure(String cause) {
            requestInfo = null;
            fillViews();

        }
    };

    public static ServiceLimitFragment newInstance(int reqId) {
        requestId = reqId;
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

        showHideWaitingProgress(false);
        loadRequestDetail();

    }

    private void loadRequestDetail() {
        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        if (requestId != 0) {

            ServiceMan serviceMan = GeneralPreferences.getInstance(getActivity()).getServiceManInfo();
            RequestInfoInput requestInfoInput = new RequestInfoInput();
            requestInfoInput.setRequestId(requestId);
            requestInfoInput.setServicemanId(serviceMan.getServicemanId());

            RequestInfoController requestInfoController = new RequestInfoController(getRequestInfoCallback);
            requestInfoController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(), requestInfoInput);
        } else
            getActivity().finish();
    }

    private void fillViews() {
        if (requestInfo != null) {
            tvRequestDetailServiceTitle.setText(requestInfo.getServiceTitle());
            tvRequestDetailTrackingCode.setText(requestInfo.getTrackingCode());
            //ratingBar.setRating(response.get);
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
        } else
            showNotFoundInfoLayout();

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
                loadRequestDetail();
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

    private void showNotFoundInfoLayout() {

        showHideWaitingProgress(true);
        constNotFound.setVisibility((requestId == 0) ? View.VISIBLE : View.GONE);
    }


}
