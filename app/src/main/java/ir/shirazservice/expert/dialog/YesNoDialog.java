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

public class YesNoDialog extends Dialog {

    private final ChooseRequestListener chooseRequestListener;
    @BindView(R.id.tv_message)
    protected AppCompatTextView tvMessage;
    @BindView(R.id.tv_title)
    protected AppCompatTextView tvTitle;
    private Context context;
    private String title;
    private String message;

    public YesNoDialog(@NonNull Context context,String title,String message,
                       ChooseRequestListener chooseRequestListener) {

        super(context);
        this.context = context;
        this.chooseRequestListener = chooseRequestListener;
        this.title=title;
        this.message=message;
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
        setContentView(R.layout.dialog_yes_no);

        ButterKnife.bind(this);

        tvTitle.setText(title);
        tvMessage.setText(Html.fromHtml(message));
    }

}
