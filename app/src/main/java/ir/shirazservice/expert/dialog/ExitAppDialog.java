package ir.shirazservice.expert.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.R;

public class ExitAppDialog extends Dialog {

    private final ChooseRequestListener chooseRequestListener;


    public ExitAppDialog(@NonNull Context context, ChooseRequestListener chooseRequestListener) {

        super(context);
        this.chooseRequestListener = chooseRequestListener;

    }

    @OnClick(R.id.tv_yes)
    void exitButtonClick() {
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
        setContentView(R.layout.dialog_exit_app);

        ButterKnife.bind(this);
    }

}
