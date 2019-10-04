package ir.shirazservice.expert.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.R;

public class CancelRequestDialog extends Dialog {

    private final ChooseRequestListener cancelRequestListener;
    public CancelRequestDialog(@NonNull Context context, ChooseRequestListener chooseRequestListener) {

        super(context);
        this.cancelRequestListener = chooseRequestListener;

    }

    @OnClick(R.id.tv_yes)
    void acceptButtonClick() {
        cancelRequestListener.onChooseRequest(true);
        dismiss();
    }

    @OnClick(R.id.tv_no)
    void rejectButtonClick() {
        cancelRequestListener.onChooseRequest(false);
        dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cancle_request);

        ButterKnife.bind(this);
    }


}
