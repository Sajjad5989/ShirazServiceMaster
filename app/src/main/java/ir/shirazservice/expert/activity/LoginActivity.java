package ir.shirazservice.expert.activity;


import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;

import static ir.shirazservice.expert.utils.APP.context;


public class LoginActivity extends AppCompatActivity implements IInternetController {


    private static final int SERVICE_MAN_INT = 1;
    private static final int BASE_INFO_INT = 2;
    @BindView(R.id.image_view_pass)
    protected AppCompatImageButton imageViewPass;
    @BindView(R.id.et_username)
    protected AppCompatEditText etUserName;
    @BindView(R.id.et_password)
    protected AppCompatEditText etPassword;
    @BindView(R.id.chk_remember)
    protected AppCompatCheckBox chkRemember;
    private final GetBaseInfoOfAppApi.GetBaseInfoOfAppCallback getBaseInfoOfAppCallback =
            new GetBaseInfoOfAppApi.GetBaseInfoOfAppCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, BaseInfoOfApp response) {
                    if (successful) {
                        String appVersion = String.valueOf(new VersionUtils().getAppVersionCode(LoginActivity.this));
                        if (!appVersion.equals(response.getAndroidServiceManAppVerCode())) {
                            updateWarning(response.getAndroidAppDlLink(), response.getAndroidServiceManAppForceUpdate());
                        } else {
                            if (chkRemember.isChecked()) {
                                GeneralPreferences.getInstance(LoginActivity.this).putString(ShirazServiceApi.USER_NAME, Objects.requireNonNull(etUserName.getText()).toString());
                                GeneralPreferences.getInstance(LoginActivity.this).putString(ShirazServiceApi.USER_PASS, Objects.requireNonNull(etPassword.getText()).toString());
                            }
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                        }
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
                        GeneralPreferences.getInstance(LoginActivity.this).putServiceManInfo(response);
                        getBaseInfo();
                    }
                }

                @Override
                public void onFailure(String cause) {

                }
            };
    private int showHide = 1;

    @OnClick(R.id.btn_login)
    void logIn() {
        if (!checkValidity())
            return;
        logInAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = LoginActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        clickButton();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickButton() {
        imageViewPass.setOnClickListener(v -> {

            if (showHide == 0) {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewPass.setImageResource(R.drawable.eye);
                showHide = 1;
            } else if (showHide == 1) {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewPass.setImageResource(R.drawable.eye_off);
                showHide = 0;
            }
        });
    }

    private boolean checkValidity() {
        if (Objects.requireNonNull(etUserName.getText()).toString().equals("")) {
            APP.customToast(getString(R.string.text_error_need_user_name));
            return false;
        }

        String userName = etUserName.getText().toString();

        if (etUserName.length() != 11 || !userName.substring(0, 2).equals("09")) {
            APP.customToast(getString(R.string.text_error_incorrect_user_name));
            return false;
        }

        if (Objects.requireNonNull(etPassword.getText()).toString().equals("")) {
            APP.customToast(getString(R.string.text_error_need_pass));
            return false;
        }


        return true;
    }

    private void logInAction() {
        getServiceMan();
    }

    private void getServiceMan() {
        if (!isOnline()) {
            openInternetCheckingDialog(SERVICE_MAN_INT);
        }

        GetServiceManInfoController getServiceManInfoController =
                new GetServiceManInfoController(LoginActivity.this, getServiceManInfoCallback);

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

        ServiceManSavedInfo serviceManFirstInfo = new ServiceManSavedInfo();
        serviceManFirstInfo.setUsername(Objects.requireNonNull(etUserName.getText()).toString());
        serviceManFirstInfo.setPass(Objects.requireNonNull(etPassword.getText()).toString());

        String appVersion = String.valueOf(new VersionUtils().getAppVersionCode(APP.context));
        serviceManFirstInfo.setApplicationVersion(appVersion);
        serviceManFirstInfo.setDeviceModel(Build.MODEL);
        serviceManFirstInfo.setDeviceName(Build.MANUFACTURER);
        serviceManFirstInfo.setSdkVersion(String.valueOf(Build.VERSION.SDK_INT));

        return serviceManFirstInfo;
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(this).isOnline();
    }

    private void getBaseInfo() {
        if (!isOnline()) {
            openInternetCheckingDialog(BASE_INFO_INT);
        }

        ServiceMan serviceMan = GeneralPreferences.getInstance(LoginActivity.this).getServiceManInfo();

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


    private String getUpdateMessage(int updateIsForce) {
        return updateIsForce == 1 ?
                getString(R.string.text_obligation_update) :
                getString(R.string.text_optional_update);
    }

    private void updateWarning(String updateLink, int updateIsForce) {
        UpdateDialog updateDialog = new UpdateDialog(this, done -> {
            if (done)
                startDownload(updateLink);
            else {
                if (updateIsForce == 1)
                    APP.killApp();
                else
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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


}
