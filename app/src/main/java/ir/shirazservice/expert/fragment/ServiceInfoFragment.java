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
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.serviceInfo.GetServiceInfoApi;
import ir.shirazservice.expert.webservice.serviceInfo.GetServiceInfoController;
import ir.shirazservice.expert.webservice.serviceInfo.ReceptionService;
import ir.shirazservice.expert.webservice.serviceInfo.ServiceInfo;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static ir.shirazservice.expert.utils.APP.context;

public class ServiceInfoFragment extends Fragment implements IInternetController {


    private static int myServiceId;
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
    private ServiceInfo serviceInfo;
    private final GetServiceInfoApi.getServiceInfoCallBack getServiceInfoCallBack = new
            GetServiceInfoApi.getServiceInfoCallBack() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, ServiceInfo response) {
                    if (successful) {
                        serviceInfo = response;
                    } else
                        serviceInfo = null;

                    setServiceInfo();
                }

                @Override
                public void onFailure(String cause) {
                    serviceInfo = null;
                    setServiceInfo();
                }
            };

    public static ServiceInfoFragment newInstance(int serviceId) {
        myServiceId = serviceId;
        return new ServiceInfoFragment();
    }

    private void setServiceInfo() {
        if (serviceInfo != null) {
            tvRequestDetailServiceTitle.setText(serviceInfo.getServiceTitle());

            tvExtraTitle1.setText(serviceInfo.getExtraTitle1());
            tvExtraField1.setText(serviceInfo.getExtraField1());

            tvExtraTitle2.setText(serviceInfo.getExtraTitle2());
            tvExtraField2.setText(serviceInfo.getExtraField2());

            tvServiceDescriptionTitle.setText(serviceInfo.getServiceTitle());
            tvServiceDescription.setText(serviceInfo.getBody());

            String picAddress = serviceInfo.getPicAddress();
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
        } else
            getActivity().finish();
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

    private void loadServiceDetail() {

        if (!isOnline())
            openInternetCheckingDialog();

        ServiceMan serviceMan = GeneralPreferences.getInstance(getActivity()).getServiceManInfo();

        GetServiceInfoController getServiceInfoController = new GetServiceInfoController(getServiceInfoCallBack);


        ReceptionService receptionService = new ReceptionService();
        receptionService.setServiceId(myServiceId);
        getServiceInfoController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(), receptionService
        );
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
                loadServiceDetail();
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

//    private void showNotFoundInfoLayout() {
//
//        showHideWaitingProgress(true);
//        constNotFound.setVisibility((requestId == 0) ? View.VISIBLE : View.GONE);
//    }

}
