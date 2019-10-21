package ir.shirazservice.expert.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.R;


public class ChooseRequestDialog extends Dialog {

    @BindView(R.id.tv_money)
    protected AppCompatTextView tvMoney;
    private final ChooseRequestListener chooseRequestListener;
    private final String calculatePrice;

    public ChooseRequestDialog(@NonNull Context context, ChooseRequestListener chooseRequestListener, String calculatePrice) {

        super(context);
        this.calculatePrice = calculatePrice;
        this.chooseRequestListener = chooseRequestListener;

    }

    @OnClick(R.id.tv_yes)
    void acceptButtonClick() {
        chooseRequestListener.onChooseRequest(true);
        dismiss();
    }

    @OnClick(R.id.tv_no)
    void rejectButtonClick() {
        chooseRequestListener.onChooseRequest(false);
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_request);

        ButterKnife.bind(this);
        tvMoney.setText(calculatePrice);
    }

}
