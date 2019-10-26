package ir.shirazservice.expert.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.R;

public class IncreaseAccountChargeDialog extends Dialog {


    @BindView(R.id.tv_confirm)
    protected AppCompatTextView tvConfirm;

    private final ChooseRequestListener cancelRequestListener;
    private final String confirmMessage;

    public IncreaseAccountChargeDialog(@NonNull Context context, String confirmMessage, ChooseRequestListener chooseRequestListener) {

        super(context);

        this.cancelRequestListener = chooseRequestListener;
        this.confirmMessage = confirmMessage;

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
        setContentView(R.layout.dialog_increase_account_charge);

        ButterKnife.bind(this);
        tvConfirm.setText(Html.fromHtml(confirmMessage));

    }

}
