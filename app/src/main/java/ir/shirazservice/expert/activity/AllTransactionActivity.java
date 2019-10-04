package ir.shirazservice.expert.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.Serializable;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.adapter.MyTransactionAdapter;
import ir.shirazservice.expert.dialog.TransactionDetailDialog;
import ir.shirazservice.expert.interfaces.IDefault;
import ir.shirazservice.expert.interfaces.IRtl;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.webservice.workmanfinancial.TransactionList;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AllTransactionActivity extends AppCompatActivity implements Serializable, IRtl, IDefault {


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.all_transaction_list)
    protected RecyclerView allTransactionListRecycler;

    private TransactionList transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_transaction);

        ButterKnife.bind(this);
        prepareToolbar();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        if (bundle != null)
            transactionList = (TransactionList) bundle.getSerializable(getString(R.string.text_financial_transaction_serialize));
        fillTransactionList();
    }


    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = AllTransactionActivity.this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    private void prepareToolbar() {
        toolbar.setTitle(R.string.all_transaction_text);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
    }

    private void fillTransactionList() {
        MyTransactionAdapter myTransactionAdapter;
        myTransactionAdapter = new MyTransactionAdapter(transactionList.getWorkmanFinancialTransactions(), 0, (v, position) -> {

            TransactionDetailDialog transactionDetailDialog =
                    new TransactionDetailDialog(AllTransactionActivity.this, transactionList.getWorkmanFinancialTransactions().get(position));

            Objects.requireNonNull(transactionDetailDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            transactionDetailDialog.getWindow().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.dialog_bg));
            transactionDetailDialog.show();
            Display display = AllTransactionActivity.this.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            width = (int) ((width) * 0.9);
            transactionDetailDialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AllTransactionActivity.this, 1);
        allTransactionListRecycler.setLayoutManager(gridLayoutManager);
        allTransactionListRecycler.setAdapter(myTransactionAdapter);

    }

}
