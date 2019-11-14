package ir.shirazservice.expert.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.interfaces.IDefault;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.interfaces.IRtl;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.getservicemaninfobyid.GetServiceManInfoByIdApi;
import ir.shirazservice.expert.webservice.getservicemaninfobyid.GetServiceManInfoByIdController;
import ir.shirazservice.expert.webservice.getservicemaninfobyid.ServiceManInfoById;
import ir.shirazservice.expert.webservice.getservicemaninfobyid.ServiceManInfoDetail;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static ir.shirazservice.expert.utils.APP.context;

public class ProfileActivity extends AppCompatActivity implements IRtl, IDefault, IInternetController {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.image_expert)
    AppCompatImageView imageExpert;
    @BindView(R.id.tv_expert_name)
    AppCompatTextView tvExpertName;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.tv_credit)
    AppCompatTextView tvCredit;
    @BindView(R.id.tv_register)
    AppCompatTextView tvRegister;
    @BindView(R.id.tv_service_count)
    AppCompatTextView tvServiceCount;
    @BindView(R.id.tv_service_medal)
    AppCompatTextView tvServiceMedal;
    @BindView(R.id.tv_birthday)
    AppCompatTextView tvBirthday;
    @BindView(R.id.tv_address)
    AppCompatTextView tvAddress;

    @BindView(R.id.tv_comment)
    AppCompatTextView tvComment;
    @BindView(R.id.tv_document)
    AppCompatTextView tvDocument;

    @BindView(R.id.const_waiting_main_fragment)
    ConstraintLayout constWaiting;

    @BindView(R.id.const_not_found_info)
    ConstraintLayout constNotFound;


    private ServiceManInfoDetail serviceManInfoDetail;
    private final GetServiceManInfoByIdApi.GetServiceManInfoByIdCallback getServiceManInfoByIdCallback =
            new GetServiceManInfoByIdApi.GetServiceManInfoByIdCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, ServiceManInfoDetail response) {
                    if (successful)
                        serviceManInfoDetail = response;
                    else
                        serviceManInfoDetail = null;

                    setProfileInfo();
                }

                @Override
                public void onFailure(String cause) {
                    serviceManInfoDetail = null;
                    setProfileInfo();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        OnActivityDefaultSetting();

        prepareToolbar();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        showHideWaitingProgress(false);
        callProfileInfo();
    }

    private void prepareToolbar() {

        toolbar.setTitle(R.string.text_profile_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
    }


    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = ProfileActivity.this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    private void openInternetCheckingDialog() {
        ConnectionInternetDialog dialog = new ConnectionInternetDialog(this, new InternetConnectionListener() {
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
                callProfileInfo();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        dialog.show();
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.8);
        dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }

    private void callProfileInfo() {
        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        ServiceManInfoById serviceManInfoById = new ServiceManInfoById();
        serviceManInfoById.setWorkmanId(serviceMan.getServicemanId());
        GetServiceManInfoByIdController getServiceManInfoByIdController =
                new GetServiceManInfoByIdController( getServiceManInfoByIdCallback);
        getServiceManInfoByIdController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(), serviceManInfoById);
    }

    private void setProfileInfo() {
        if (serviceManInfoDetail != null) {

            String picAddress = serviceManInfoDetail.getPicAddress();
            if (null == picAddress || picAddress.equals("")) {
                imageExpert.setImageResource(R.drawable.img_no_icon);
            } else {
                Picasso.get()
                        .load(picAddress)  //Url of the image to load.
                        .transform(new CropCircleTransformation())
                        .error(R.drawable.img_no_icon)
                        .placeholder(R.drawable.img_loading)
                        .into(imageExpert);
            }

            tvExpertName.setText(serviceManInfoDetail.getFullName());
            ratingBar.setRating(serviceManInfoDetail.getRating());
            tvCredit.setText(new UsefulFunction().attachCamma(serviceManInfoDetail.getTempCredit()));
            tvRegister.setText(String.valueOf(serviceManInfoDetail.getMembershipTime()));
            tvServiceCount.setText(String.valueOf(serviceManInfoDetail.getFinishedServicesCount()));
            tvServiceMedal.setText(String.valueOf(serviceManInfoDetail.getMedalsCount()));
            tvBirthday.setText(serviceManInfoDetail.getBirthdayTimePersian());
            tvAddress.setText(serviceManInfoDetail.getAddress());


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvComment.setText(Html.fromHtml(serviceManInfoDetail.getRecords(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvComment.setText(Html.fromHtml(serviceManInfoDetail.getRecords()));
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvDocument.setText(Html.fromHtml(serviceManInfoDetail.getDocuments(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvDocument.setText(Html.fromHtml(serviceManInfoDetail.getDocuments()));
            }

//            tvComment.setText(serviceManInfoDetail.getRecords());
//            tvDocument.setText(serviceManInfoDetail.getDocuments());
//

            showHideWaitingProgress(true);
        } else
            showNotFoundInfoLayout();

    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(this).isOnline();
    }

    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        showHideWaitingProgress(true);
        constNotFound.setVisibility((serviceManInfoDetail == null) ? View.VISIBLE : View.GONE);
    }


}
