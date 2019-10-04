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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.dialog.ChooseRequestDialog;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.pickrequestbyserviceman.PickRequestByServiceManApi;
import ir.shirazservice.expert.webservice.pickrequestbyserviceman.PickRequestByServiceManController;
import ir.shirazservice.expert.webservice.pickrequestbyserviceman.RequestByServiceMan;
import ir.shirazservice.expert.webservice.requestlist.Request;
import ir.shirazservice.expert.webservice.requestvisit.InsrtRequestVisitApi;
import ir.shirazservice.expert.webservice.requestvisit.InsrtRequestVisitController;
import ir.shirazservice.expert.webservice.requestvisit.RequestVisit;
import ir.shirazservice.expert.webservice.requestvisit.RequestVisitReq;
import ir.shirazservice.expert.webservice.workmancredit.GetWorkmanCreditApi;
import ir.shirazservice.expert.webservice.workmancredit.GetWorkmanCreditController;
import ir.shirazservice.expert.webservice.workmancredit.WorkmanCredit;
import ir.shirazservice.expert.webservice.workmancredit.WorkmanCreditReq;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static ir.shirazservice.expert.utils.APP.context;

public class RequestLimitFragment extends Fragment implements Serializable, IInternetController {

    private static Request currentRequest;
    private final PickRequestByServiceManApi.pickRequestByServiceManCallback pickRequestByServiceManCallback =
            new PickRequestByServiceManApi.pickRequestByServiceManCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, RequestByServiceMan response) {
                    if (successful) {
                        getActivity().finish();
                    }
                }

                @Override
                public void onFailure(String cause) {

                }
            };
    private final InsrtRequestVisitApi.insrtRequestVisitCallback insrtRequestVisitCallback =
            new InsrtRequestVisitApi.insrtRequestVisitCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, RequestVisit response) {

                }

                @Override
                public void onFailure(String cause) {

                }
            };
    @BindView(R.id.img_request_Detail)
    protected AppCompatImageView imgRequestDetail;
    @BindView(R.id.tv_request_detail_service_title)
    protected AppCompatTextView tvRequestDetailServiceTitle;
    @BindView(R.id.tv_discount_code)
    protected AppCompatTextView tvDiscountCode;
    @BindView(R.id.tv_discount_alarm_title2)
    protected AppCompatTextView tvDiscountAlarm;
    @BindView(R.id.const_discount)
    protected ConstraintLayout constDiscount;
    @BindView(R.id.tv_request_detail_tracking_code)
    protected AppCompatTextView tvRequestDetailTracking_code;
    @BindView(R.id.tv_request_detail_tracking_caption)
    protected AppCompatTextView tvRequestDetailTrackingCaption;
    @BindView(R.id.tv_request_detail_calculated_price)
    protected AppCompatTextView tvRequestDetailCalculatedPrice;
    @BindView(R.id.tv_request_detail_date_value)
    protected AppCompatTextView tvRequestDetailDateValue;
    @BindView(R.id.tv_request_detail_time_value)
    protected AppCompatTextView tvRequestDetailTimeValue;
    @BindView(R.id.tv_position_address)
    protected AppCompatTextView tvPositionAddress;
    @BindView(R.id.tv_request_detail_desc_value)
    protected AppCompatTextView tvRequestDetailDescValue;
    @BindView(R.id.tv_current_account)
    protected AppCompatTextView tvCurrentAccount;
    @BindView(R.id.tv_subtract_account)
    protected AppCompatTextView tvSubtractAccount;
    @BindView(R.id.btn_accept_request)
    protected AppCompatButton btnAcceptRequest;
    @BindView(R.id.const_waiting_main_fragment)
    protected ConstraintLayout constWaiting;
    @BindView(R.id.const_not_found_info)
    protected ConstraintLayout constNotFound;
    @BindView(R.id.tv_service_info)
    AppCompatTextView tvServiceInfo;
    private int requestId = 0;
    private int requestDetailCalculatedPrice;
    private String accessToken;
    private int servicemanId;
    private final GetWorkmanCreditApi.getWorkmanCreditCallback getWorkmanCreditCallback = new GetWorkmanCreditApi.getWorkmanCreditCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, WorkmanCredit response) {
            if (successful) {
                String currentAmount = response.getTempCredit();
                tvCurrentAccount.setText(new UsefulFunction().attachCamma(currentAmount));
                if (Integer.valueOf(currentAmount) > 0) {
                    int finalAmount = Integer.valueOf(currentAmount) - requestDetailCalculatedPrice;
                    String stringFinalAmount = String.valueOf(finalAmount);
                    tvSubtractAccount.setText(new UsefulFunction().attachCamma(stringFinalAmount));
                }
                visitRequest();
                showHideWaitingProgress(true);

            } else {
                tvSubtractAccount.setText("-");
                tvCurrentAccount.setText("-");
            }
        }

        @Override
        public void onFailure(String cause) {

        }
    };

    public static RequestLimitFragment newInstance(Request request) {
        currentRequest = request;
        return new RequestLimitFragment();
    }

    private void acceptRequest() {

        String payMoney = tvRequestDetailCalculatedPrice.getText().toString();

        ChooseRequestDialog chooseRequestDialog = new ChooseRequestDialog(getActivity(), done -> {
            if (done)
                RequestLimitFragment.this.pickUpService();
        }, payMoney);

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

    private void pickUpService() {

        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        PickRequestByServiceManController pickRequestByServiceManController =
                new PickRequestByServiceManController(getActivity(), pickRequestByServiceManCallback);
        pickRequestByServiceManController.start(requestId);
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
                pickUpService();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_limit, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setNeededIds();
        onClickConfig();
        showHideWaitingProgress(false);
        setRequestDetailBeforeAccept();

    }

    private void onClickConfig() {
        btnAcceptRequest.setOnClickListener(view -> acceptRequest());

        tvServiceInfo.setOnClickListener(view -> openServiceInfo());
    }

    private void openServiceInfo() {
        Intent intent = new Intent(getActivity(), ServiceRequestDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.text_bundle_req_id), currentRequest.getServiceId());
        bundle.putInt(getString(R.string.text_bundle_service_status), APP.SERVICE_INFO_Status);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void setNeededIds() {
        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        servicemanId = serviceMan.getServicemanId();
        accessToken = serviceMan.getAccessToken();

    }

    private void callWorkmanCredit() {

        WorkmanCreditReq workmanCreditReq = new WorkmanCreditReq();
        workmanCreditReq.setId(servicemanId);

        GetWorkmanCreditController getWorkmanCreditController = new GetWorkmanCreditController(getWorkmanCreditCallback);
        getWorkmanCreditController.start(servicemanId, accessToken, workmanCreditReq);

    }

    private void visitRequest() {

        RequestVisitReq requestVisitReq = new RequestVisitReq();
        requestVisitReq.setRequestId(requestId);
        requestVisitReq.setWorkmanId(servicemanId);

        InsrtRequestVisitController insrtRequestVisitController =
                new InsrtRequestVisitController(insrtRequestVisitCallback);
        insrtRequestVisitController.start(servicemanId, accessToken, requestVisitReq);
    }

    private void setRequestDetailBeforeAccept() {

        if (currentRequest != null) {

            String picAddress = currentRequest.getServicePicAddress();
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


            requestId = currentRequest.getRequestId();
            tvRequestDetailServiceTitle.setText(currentRequest.getServiceTitle());
            tvRequestDetailCalculatedPrice.setText(new UsefulFunction().attachCamma(String.valueOf(currentRequest.getCalculatedPrice())));
            requestDetailCalculatedPrice = currentRequest.getCalculatedPrice();

            if (currentRequest.getTime() == APP.IMMEDIATE_CODE) {
                tvRequestDetailDateValue.setText(R.string.text_imidiately);
                tvRequestDetailTimeValue.setText(R.string.text_imidiately);
            } else {
                tvRequestDetailDateValue.setText(currentRequest.getDateFromPersian());
                tvRequestDetailTimeValue.setText(currentRequest.getTimeDesc());
            }

            tvPositionAddress.setText(currentRequest.getAreaTitle());
            tvRequestDetailDescValue.setText(currentRequest.getDesc());

            if ("".equals(currentRequest.getDiscountCode())) {
                constDiscount.setVisibility(View.GONE);

            } else {
                tvDiscountCode.setText(currentRequest.getDiscountCode());
                tvDiscountAlarm.setText(currentRequest.getDiscountDesc());
            }

            tvRequestDetailTracking_code.setVisibility(View.GONE);
            tvRequestDetailTrackingCaption.setVisibility(View.GONE);


            callWorkmanCredit();
        } else
            showNotFoundInfoLayout();
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(getActivity()).isOnline();
    }

    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        showHideWaitingProgress(true);
        constNotFound.setVisibility((currentRequest == null) ? View.VISIBLE : View.GONE);
    }


}
