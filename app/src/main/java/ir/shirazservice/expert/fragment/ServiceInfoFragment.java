package ir.shirazservice.expert.fragment;

import android.app.Fragment;
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
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.webservice.serviceInfo.ServiceInfo;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ServiceInfoFragment extends Fragment {


    private static ServiceInfo serviceInfoS;
    @BindView(R.id.img_request_Detail)
    AppCompatImageView imgRequestDetail;
    @BindView(R.id.tv_request_detail_service_title)
    AppCompatTextView tvRequestDetailServiceTitle;
    @BindView(R.id.tv_extraTitle)
    AppCompatTextView tvExtraTitle1;
    @BindView(R.id.tv_extraField)
    AppCompatTextView tvExtraField1;
    @BindView(R.id.tv_extraTitle2)
    AppCompatTextView tvExtraTitle2;
    @BindView(R.id.tv_extraField2)
    AppCompatTextView tvExtraField2;
    @BindView(R.id.tv_service_description_title)
    AppCompatTextView tvServiceDescriptionTitle;
    @BindView(R.id.tv_service_description)
    AppCompatTextView tvServiceDescription;
    @BindView(R.id.const_waiting_main_fragment)
    ConstraintLayout constWaiting;

    public static ServiceInfoFragment newInstance(ServiceInfo serviceInfo) {
        serviceInfoS = serviceInfo;
        return new ServiceInfoFragment();
    }

    private void loadServiceDetail() {

        tvRequestDetailServiceTitle.setText(serviceInfoS.getServiceTitle());

        tvExtraTitle1.setText(serviceInfoS.getExtraTitle1());
        tvExtraField1.setText(serviceInfoS.getExtraField1());

        tvExtraTitle2.setText(serviceInfoS.getExtraTitle2());
        tvExtraField2.setText(serviceInfoS.getExtraField2());

        tvServiceDescriptionTitle.setText(serviceInfoS.getServiceTitle());
        tvServiceDescription.setText(serviceInfoS.getBody());

        String picAddress = serviceInfoS.getPicAddress();
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
        showHideWaitingProgress(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_info, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showHideWaitingProgress(false);
        loadServiceDetail();

    }

    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

}
