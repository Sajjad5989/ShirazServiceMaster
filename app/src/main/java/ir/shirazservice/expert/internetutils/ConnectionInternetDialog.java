package ir.shirazservice.expert.internetutils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.R;

public class ConnectionInternetDialog extends Dialog {


    private final InternetConnectionListener internetConnectionListener;

    public ConnectionInternetDialog(@NonNull Context context, InternetConnectionListener internetConnectionListener) {

        super(context);
        this.internetConnectionListener = internetConnectionListener;
    }

    @OnClick(R.id.tv_retry)
    void tryToConnectToNet() {
        internetConnectionListener.OnRetry();
    }

    @OnClick(R.id.tv_exit)
    void exitApp() {
        internetConnectionListener.onFinish();
    }

    @OnClick(R.id.tv_internet)
    void openNetSetting() {
        internetConnectionListener.onInternet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_connection_internet);

        ButterKnife.bind(this);

    }
}
