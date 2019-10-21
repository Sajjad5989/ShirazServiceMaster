package ir.shirazservice.expert.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;

import java.util.Locale;
import java.util.Objects;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.dialog.UpdateDialog;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.VersionUtils;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getbaseinfo.BaseInfoOfApp;
import ir.shirazservice.expert.webservice.getbaseinfo.GetBaseInfoOfAppApi;
import ir.shirazservice.expert.webservice.getbaseinfo.GetBaseInfoOfAppController;
import ir.shirazservice.expert.webservice.getservicemaninfo.GetServiceManInfoApi;
import ir.shirazservice.expert.webservice.getservicemaninfo.GetServiceManInfoController;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceManSavedInfo;

import static ir.shirazservice.expert.utils.APP.context;

public class SplashActivity extends AppCompatActivity implements IInternetController {

    private static final int SERVICE_MAN_INT = 1;
    private static final int BASE_INFO_INT = 2;
    private final GetBaseInfoOfAppApi.GetBaseInfoOfAppCallback getBaseInfoOfAppCallback =
            new GetBaseInfoOfAppApi.GetBaseInfoOfAppCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, BaseInfoOfApp response) {
                    if (successful) {
                        String appVersion = String.valueOf(new VersionUtils().getAppVersionCode(SplashActivity.this));
                        if (!appVersion.equals(response.getAndroidServiceManAppVerCode())) {
                            if (response.getAndroidServiceManAppForceUpdate() == 1) {
                                updateWarning(response.getAndroidAppDlLink(), response.getAndroidServiceManAppForceUpdate());
                            }
                        }
                        GeneralPreferences.getInstance(SplashActivity.this).putBaseInfoOfApp(response);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        SplashActivity.this.finish();
                    }
                }

                @Override
                public void onFailure(String cause) {

                }
            };
    private final GetServiceManInfoApi.GetServiceManInfoCallback getServiceManInfoCallback =
            new GetServiceManInfoApi.GetServiceManInfoCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, ServiceMan response) {

                    if (successful) {
                        GeneralPreferences.getInstance(SplashActivity.this).putServiceManInfo(response);
                        getBaseInfo();
                    }
                }

                @Override
                public void onFailure(String cause) {

                }
            };

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(this).isOnline();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setPersian();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        findViews();
        doActionAfterSplash();
    }

    private void findViews() {
        AppCompatTextView tvVersion = findViewById(R.id.tv_app_version);
        tvVersion.setText("بسته متخصصین، نسخه "+new VersionUtils().getAppVersionName(SplashActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = SplashActivity.this;
        //requestPermission();
    }

    private void setPersian() {
        String languageToLoad = "fa";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        new Geocoder(SplashActivity.this, new Locale("fa"));
    }

    private void doActionAfterSplash() {

        final Handler handler = new Handler();
        handler.postDelayed(this::letsGo, 3000);
    }

    private void letsGo() {

        boolean showedSlider = GeneralPreferences.getInstance(this).getBoolean(getString(R.string.text_preference_slider_is_show));

        if (showedSlider) {
            String userName = GeneralPreferences.getInstance(SplashActivity.this).getString(BuildConfig.userName);
            String userPass = GeneralPreferences.getInstance(SplashActivity.this).getString(BuildConfig.userPass);

            if (userName == null || userPass == null) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            } else {
                getServiceMan();

            }
        } else {
            startActivity(new Intent(SplashActivity.this, IntroSliderActivity.class));
        }
        SplashActivity.this.finish();

    }

    private void getServiceMan() {

        if (!isOnline()) {
            openInternetCheckingDialog(SERVICE_MAN_INT);
        }

        GetServiceManInfoController getServiceManInfoController =
                new GetServiceManInfoController(SplashActivity.this, getServiceManInfoCallback);

        getServiceManInfoController.start(getServiceManIno());
    }

    private void openInternetCheckingDialog(int typeCallDialog) {
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
                switch (typeCallDialog) {
                    case SERVICE_MAN_INT:
                        getServiceMan();
                        break;
                    case BASE_INFO_INT:
                        getBaseInfo();
                        break;
                }
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

    private ServiceManSavedInfo getServiceManIno() {

        String userName = GeneralPreferences.getInstance(SplashActivity.this).getString(BuildConfig.userName);
        String userPass = GeneralPreferences.getInstance(SplashActivity.this).getString(BuildConfig.userPass);

        ServiceManSavedInfo serviceManFirstInfo = new ServiceManSavedInfo();
        serviceManFirstInfo.setUsername(userName);
        serviceManFirstInfo.setPass(userPass);

        String appVersion = String.valueOf(new VersionUtils().getAppVersionCode(APP.context));
        serviceManFirstInfo.setApplicationVersion(appVersion);
        serviceManFirstInfo.setDeviceModel(Build.MODEL);
        serviceManFirstInfo.setDeviceName(Build.MANUFACTURER);
        serviceManFirstInfo.setSdkVersion(String.valueOf(Build.VERSION.SDK_INT));
        return serviceManFirstInfo;
    }

    private void getBaseInfo() {

        if (!isOnline()) {
            openInternetCheckingDialog(BASE_INFO_INT);
        }

        ServiceMan serviceMan = GeneralPreferences.getInstance(SplashActivity.this).getServiceManInfo();

        GetBaseInfoOfAppController getBaseInfoOfAppController =
                new GetBaseInfoOfAppController(getBaseInfoOfAppCallback);
        getBaseInfoOfAppController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken());

    }

    private void startDownload(String downloadLink) {
        DownloadManager mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request mRqRequest = new DownloadManager.Request(
                Uri.parse(downloadLink));
        Objects.requireNonNull(mManager).enqueue(mRqRequest);
    }

    private void updateWarning(String updateLink, int updateIsForce) {
        UpdateDialog updateDialog = new UpdateDialog(this, done -> {
            if (done)
                startDownload(updateLink);
            else {
                if (updateIsForce == 1)
                    APP.killApp();
                else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            }
        }, getUpdateMessage(updateIsForce));

        Objects.requireNonNull(updateDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        updateDialog.setCancelable(false);
        updateDialog.show();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.9);
        updateDialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }

    private String getUpdateMessage(int updateIsForce) {
        return updateIsForce == 1 ?
                getString(R.string.text_obligation_update) :
                getString(R.string.text_optional_update);
    }

}
