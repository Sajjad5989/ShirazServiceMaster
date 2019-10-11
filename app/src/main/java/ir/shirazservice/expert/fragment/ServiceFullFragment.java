package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetails;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ServiceFullFragment extends Fragment {


    private static int requestId;
    private static RequestDetails requestDetails;

    @BindView(R.id.tv_service_info)
    AppCompatTextView tvServiceInfo;
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

    public static ServiceFullFragment newInstance(RequestDetails requestDetail) {
        requestDetails = requestDetail;
        return new ServiceFullFragment();
    }

    @OnClick(R.id.img_call_by_phone)
    void callPhone() {
        String phone = tvPhoneNumber.getText().toString();

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone.trim()));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }

    @OnClick(R.id.img_call_by_mobile)
    void callMobile() {
        String phone = tvMobile.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone.trim()));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
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

        onClickConfig();
        showHideWaitingProgress(false);
        loadRequestDetail();

    }
    private void onClickConfig() {

        tvServiceInfo.setOnClickListener(view -> openServiceInfo());
    }

    private void openServiceInfo() {
        Intent intent = new Intent(getActivity(), ServiceRequestDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.text_bundle_req_id), requestDetails.getServiceId() * -1);
        intent.putExtras(bundle);
        startActivity(intent);
    }


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

    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        showHideWaitingProgress(true);
        constNotFound.setVisibility((requestDetails == null) ? View.VISIBLE : View.GONE);
    }

}
