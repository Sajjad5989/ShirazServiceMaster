package ir.shirazservice.expert.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.dialog.YesNoDialog;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.finishrequest.FinishRequestByWorkmanApi;
import ir.shirazservice.expert.webservice.finishrequest.FinishRequestByWorkmanController;
import ir.shirazservice.expert.webservice.finishrequest.FinishRequestByWorkmanRequest;
import ir.shirazservice.expert.webservice.finishrequest.SuccessIdResponse;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.requestdetails.RequestDetails;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static ir.shirazservice.expert.utils.APP.context;

public class RequestFullFragment extends Fragment implements IInternetController {

    private static final int FINISH_REQUEST = 1;
    private static final int LOAD_REQUEST = 2;
    private static final int LOAD_SERVICE = 3;
    private static RequestDetails requestDetailsFull;
    private static int requestId;

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
    @BindView(R.id.tv_request_detail_date_register)
    AppCompatTextView tvRequestDateRegister;
    @BindView(R.id.tv_service_info)
    AppCompatTextView tvServiceInfo;
    @BindView(R.id.tv_request_detail_time_value)
    AppCompatTextView tvRequestDetailTimeValue;
    @BindView(R.id.tv_request_detail_tracking_code)
    AppCompatTextView tvRequestDetailTrackingCode;
    @BindView(R.id.tv_request_status)
    AppCompatTextView tvRequestStatus;
    @BindView(R.id.btn_finish_service)
    AppCompatButton btnFinishService;
    @BindView(R.id.lyt_map_selector)
    ConstraintLayout lytMapSelector;
    private float mDefaultZoom = 15.0f;
  //  private int requestId;
    private int whichMethod;
    private int servicemanId;
    private String accessToken;

    private MapView mMapView;
    private GoogleMap googleMap;

    private FragmentActivity myContext;

    public static RequestFullFragment newInstance(RequestDetails requestDetails, int reqId) {
        requestDetailsFull = requestDetails;
        requestId = reqId;
        return new RequestFullFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
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


    private void loadRequestDetailInServiceFull() {

        if (requestDetailsFull != null) {

            String picAddress = requestDetailsFull.getServicePicAddress();
            if (null == picAddress || picAddress.equals("")) {
                imgRequestDetail.setImageResource(R.drawable.img_no_icon);
            } else {
                Picasso.get()
                        .load(picAddress)
                        .transform(new CropCircleTransformation())
                        .error(R.drawable.img_no_icon)
                        .placeholder(R.drawable.img_loading)
                        .into(imgRequestDetail);
            }

            tvPhoneNumber.setText(requestDetailsFull.getPhone());
            tvMobile.setText(requestDetailsFull.getMobile());
            tvRequestDetailServiceTitle.setText(requestDetailsFull.getServiceTitle());
            tvRequestDetailTrackingCode.setText(requestDetailsFull.getTrackingCode());

            tvRequestStatus.setText(requestDetailsFull.getStateTitle());

            tvPositionAddress.setText(requestDetailsFull.getAreaTitle());
            tvFullAddress.setText(requestDetailsFull.getAddress());

            tvRequestDetailCalculatedPrice.setText(new UsefulFunction().attachCamma(String.valueOf(requestDetailsFull.getCalculatedPrice())));
            tvRequestDateRegister.setText(requestDetailsFull.getInsrtTimeSimple());
            if (requestDetailsFull.getTime() == BuildConfig.immediateCode) {
                tvRequestDetailDateValue.setText(R.string.text_imidiately);
                tvRequestDetailTimeValue.setText(R.string.text_imidiately);
            } else {
                tvRequestDetailDateValue.setText(requestDetailsFull.getDateFromPersian());
                tvRequestDetailTimeValue.setText(requestDetailsFull.getTimeDesc());
            }

            if ("".equals(requestDetailsFull.getDiscountCode())) {
                constDiscount.setVisibility(View.GONE);
            }


            showHideWaitingProgress(true);
        } else
            showNotFoundInfoLayout();

    }

    private void loadRequestDetailInRequestFull() {

        if (requestDetailsFull != null) {
          //  requestId = requestDetailsFull.getServiceId();
            //***************************************
            String picAddress = requestDetailsFull.getServicePicAddress();
            if (null == picAddress || picAddress.equals("")) {
                imgRequestDetail.setImageResource(R.drawable.img_no_icon);
            } else {
                Picasso.get()
                        .load(picAddress)  //Url of the image to load.
                        .transform(new CropCircleTransformation())
                        .error(R.drawable.img_no_icon)
                        .placeholder(R.drawable.img_loading)
                        .into(imgRequestDetail);
            }

            tvRequestStatus.setText(requestDetailsFull.getStateTitle());
            tvRequestDetailServiceTitle.setText(requestDetailsFull.getServiceTitle());
            tvReceiverName.setText(requestDetailsFull.getReceiverMan());
            tvRequestDetailCalculatedPrice.setText(new UsefulFunction().attachCamma(requestDetailsFull.getCalculatedPrice()));
            tvPositionAddress.setText(requestDetailsFull.getAreaTitle());
            tvFullAddress.setText(requestDetailsFull.getAddress());
            tvDiscountCode.setText(requestDetailsFull.getDiscountCode());
            tvDiscountAlarm.setText(requestDetailsFull.getDiscountDesc());
            tvPhoneNumber.setText(requestDetailsFull.getPhone());
            tvMobile.setText(requestDetailsFull.getMobile());
            tvRequestDateRegister.setText(requestDetailsFull.getInsrtTimeSimple());


            double latitude = ("".equals(requestDetailsFull.getLatitiude())) ? 0 : Double.parseDouble(requestDetailsFull.getLatitiude());
            double longitude = ("".equals(requestDetailsFull.getLongtiude())) ? 0 : Double.parseDouble(requestDetailsFull.getLongtiude());
            if ((latitude != -8000000) && (longitude != -8000000)) {
                mMapView.setVisibility(VISIBLE);
                lytMapSelector.setVisibility(VISIBLE);
                mMapView.getMapAsync(mMap -> {
                    googleMap = mMap;
                    googleMap.setMyLocationEnabled(true);
                    //To add marker
                    LatLng selectedLoc = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions().position(selectedLoc).title(getString(R.string.text_request_location)));
                    // For zooming functionality
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(selectedLoc).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                });
            } else {
                mMapView.setVisibility(GONE);
                lytMapSelector.setVisibility(GONE);
            }

            if (requestDetailsFull.getTime() == BuildConfig.immediateCode) {
                tvRequestDetailDateValue.setText(R.string.text_imidiately);
                tvRequestDetailTimeValue.setText(R.string.text_imidiately);
            } else {
                tvRequestDetailDateValue.setText(requestDetailsFull.getDateFromPersian());
                tvRequestDetailTimeValue.setText(requestDetailsFull.getTimeDesc());
            }
            if ("".equals(requestDetailsFull.getDiscountCode())) {
                constDiscount.setVisibility(View.GONE);
            }

            tvRequestDetailTrackingCode.setText(requestDetailsFull.getTrackingCode());

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
        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showHideWaitingProgress(false);
        setNeededIds();
        onClickConfig();
        setViewsVisibility();
//        if (serviceOrRequest == BuildConfig.openRequest)
//            loadRequestDetailInServiceFull();
//        else
            loadRequestDetailInRequestFull();

    }


    private void setViewsVisibility() {
        btnFinishService.setVisibility(requestDetailsFull.getState()==BuildConfig.requestAccept?VISIBLE:GONE);
    }

    private void onClickConfig() {
        tvServiceInfo.setOnClickListener(view -> openServiceInfo());
        btnFinishService.setOnClickListener(view -> finishServiceConfirmation());

    }

    private void finishServiceConfirmation()
    {
        String msgFinish = getResources().getString(R.string.text_finish_service);

        YesNoDialog yesNoDialog = new YesNoDialog(getActivity(), getString(R.string.text_finish_service_title)
                , msgFinish, done -> {
            if (done) {
                callFinishRequest();
            }
        });


        Objects.requireNonNull(yesNoDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        yesNoDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        yesNoDialog.setCancelable(false);
        yesNoDialog.show();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.9);
        yesNoDialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

    }

    private void setNeededIds() {
        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        servicemanId = serviceMan.getServicemanId();
        accessToken = serviceMan.getAccessToken();

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
        bundle.putInt(getString(R.string.text_bundle_req_id), requestDetailsFull.getServiceId() * -1);
        intent.putExtras(bundle);
        startActivity(intent);
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
                    case LOAD_REQUEST:
                        loadRequestDetailInRequestFull();
                        break;
                    case LOAD_SERVICE:
                        loadRequestDetailInServiceFull();
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
/*
    private void drawMap( ){
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync( this );
    }
    @Override
    public void onMapReady( GoogleMap googleMap ){
        googleMap.clear();

        double latitude = ( "".equals( requestDetailsFull.getLatitiude() ) ) ? 0 : Double.parseDouble( requestDetailsFull.getLatitiude() );
        double longitude = ( "".equals( requestDetailsFull.getLongtiude() ) ) ? 0 : Double.parseDouble( requestDetailsFull.getLongtiude() );

        if ( ( latitude != 0 ) && ( longitude != 0 ) ) {
            googleMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
            LatLng selectedLoc = new LatLng( latitude, longitude );
            googleMap.addMarker( new MarkerOptions().position( selectedLoc ).title( getString( R.string.text_request_location ) ).icon( BitmapDescriptorFactory.fromResource( R.drawable.pin_small ) ) );
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( selectedLoc, 15 ) );
        } else {
            googleMap.clear();
        }
    }*/
}
