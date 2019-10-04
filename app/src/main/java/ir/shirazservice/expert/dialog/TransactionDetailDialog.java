package ir.shirazservice.expert.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.workmanfinancial.WorkmanFinancialTransaction;

public class TransactionDetailDialog extends Dialog {


    private final WorkmanFinancialTransaction transactionList;
    private final Context context;

    @BindView(R.id.tv_actionTitle)
    protected AppCompatTextView tvActionTitle;

    @BindView(R.id.tv_remain)
    protected AppCompatTextView tvRemain;

    @BindView(R.id.tv_price)
    protected AppCompatTextView tvPrice;

    @BindView(R.id.tv_timePersian)
    protected AppCompatTextView tvTimePersian;

    @BindView(R.id.tv_datePersian)
    protected AppCompatTextView tvDatePersian;

    @BindView(R.id.tv_desc)
    protected AppCompatTextView tvDesc;

    @OnClick(R.id.img_share)
    void shareTransactionDetail()
    {
        sendTextViaWhatsUP("hi");
    }

    private void sendTextViaWhatsUP(String text) {
        PackageManager pm = context.getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            APP.customToast(context.getString(R.string.error_no_whats_up));
        }
    }

    public TransactionDetailDialog(@NonNull Context context, WorkmanFinancialTransaction transactionList) {

        super(context);
        this.context = context;
        this.transactionList = transactionList;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_transaction_detail);
        APP.currentActivity = getOwnerActivity();
        ButterKnife.bind(this);
        setTransactionDetail();

    }

    private void setTransactionDetail()
    {
        tvActionTitle.setText(transactionList.getActionTitle());
        tvPrice.setText(new UsefulFunction().attachCamma(transactionList.getPrice().replace("-","")));
        tvRemain.setText(new UsefulFunction().attachCamma(transactionList.getRemainCredit().replace("-","")));
        tvDatePersian.setText(transactionList.getInsrtDatePersian());
        tvTimePersian.setText(transactionList.getInsrtTimePersian());

        tvDesc.setText(transactionList.getAdvanceDesc());
    }

}
