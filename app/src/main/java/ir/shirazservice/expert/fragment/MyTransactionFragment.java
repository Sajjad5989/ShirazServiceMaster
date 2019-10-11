package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.AllTransactionActivity;
import ir.shirazservice.expert.adapter.MyTransactionAdapter;
import ir.shirazservice.expert.dialog.TransactionDetailDialog;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.workmancredit.GetWorkmanCreditApi;
import ir.shirazservice.expert.webservice.workmancredit.GetWorkmanCreditController;
import ir.shirazservice.expert.webservice.workmancredit.WorkmanCredit;
import ir.shirazservice.expert.webservice.workmancredit.WorkmanCreditReq;
import ir.shirazservice.expert.webservice.workmanfinancial.GetWorkmanFinancialTransactionsApi;
import ir.shirazservice.expert.webservice.workmanfinancial.GetWorkmanFinancialTransactionsController;
import ir.shirazservice.expert.webservice.workmanfinancial.TransactionList;
import ir.shirazservice.expert.webservice.workmanfinancial.WorkmanFinancialTransaction;
import ir.shirazservice.expert.webservice.workmanfinancial.WorkmanFinancialTransactionReq;

import static ir.shirazservice.expert.utils.APP.context;


public class MyTransactionFragment extends Fragment implements Serializable, IInternetController {

    private List<WorkmanFinancialTransaction> myWorkmanFinancialTransactionList;
    private final TransactionList transactionList = new TransactionList();
    private RecyclerView myTransactionListRecycler;
    private ConstraintLayout consWaiting;
    private ConstraintLayout constNotFoundInfo;
    private AppCompatTextView tvCurrentAmount;
    private AppCompatTextView tvGetMoney;
    private AppCompatTextView tvPayMoney;
    private WorkmanCredit workmanCredit;
    private final GetWorkmanFinancialTransactionsApi.getWorkmanFinancialTransactionsCallback getWorkmanFinancialTransactionsCallback =
            new GetWorkmanFinancialTransactionsApi.getWorkmanFinancialTransactionsCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<WorkmanFinancialTransaction> response) {
                    if (successful) {
                        transactionList.setWorkmanFinancialTransactions(response);
                        myWorkmanFinancialTransactionList = response;

                    } else {
                        myWorkmanFinancialTransactionList = new ArrayList<>();
                        transactionList.setWorkmanFinancialTransactions(new ArrayList<>());
                    }
                    fillTransactionList();
                }

                @Override
                public void onFailure(String cause) {
                    myWorkmanFinancialTransactionList = new ArrayList<>();
                    transactionList.setWorkmanFinancialTransactions(new ArrayList<>());
                    fillTransactionList();
                }
            };
    private int servicemanId;
    private String accessToken;
    private final GetWorkmanCreditApi.getWorkmanCreditCallback getWorkmanCreditCallback =
            new GetWorkmanCreditApi.getWorkmanCreditCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, WorkmanCredit response) {
                    if (successful) {
                        workmanCredit = response;

                    } else workmanCredit = null;
                    setCredit();
                }

                @Override
                public void onFailure(String cause) {
                    workmanCredit = null;
                    setCredit();
                }

            };

    public static MyTransactionFragment newInstance() {
        return new MyTransactionFragment();
    }

    private void setCredit() {
        if (workmanCredit == null)
            return;
        else
            tvCurrentAmount.setText(new UsefulFunction().attachCamma(workmanCredit.getTempCredit()));


        getMyTransaction();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_transaction, container, false);

        APP.currentActivity = getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        setNeededIds();
        getWorkManCredit();

    }

    private void getMyTransaction() {

        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        WorkmanFinancialTransactionReq workmanFinancialTransactionReq = new WorkmanFinancialTransactionReq();
        workmanFinancialTransactionReq.setInsrtDateShamsi("");
        workmanFinancialTransactionReq.setServicemanId(servicemanId);
        workmanFinancialTransactionReq.setTypeId(0);


        GetWorkmanFinancialTransactionsController getWorkmanFinancialTransactionsController =
                new GetWorkmanFinancialTransactionsController(getWorkmanFinancialTransactionsCallback);

        getWorkmanFinancialTransactionsController.start(servicemanId, accessToken, workmanFinancialTransactionReq);
    }

    private void getWorkManCredit() {

        if (!isOnline()) {
            openInternetCheckingDialog();
        }
        showHideWaitingProgress(false);


        WorkmanCreditReq workmanCreditReq = new WorkmanCreditReq();
        workmanCreditReq.setId(servicemanId);

        GetWorkmanCreditController getWorkmanCreditController =
                new GetWorkmanCreditController(getWorkmanCreditCallback);
        getWorkmanCreditController.start(servicemanId, accessToken, workmanCreditReq);
    }

    private void setNeededIds() {
        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        servicemanId = serviceMan.getServicemanId();
        accessToken = serviceMan.getAccessToken();

    }

    private void openInternetCheckingDialog() {
        ConnectionInternetDialog dialog = new ConnectionInternetDialog(getActivity(), new InternetConnectionListener() {
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
                getMyTransaction();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        dialog.show();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.8);
        dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }


    private void getSumPayments() {
        String price;
        double payMoney = 0;
        double getMoney = 0;
        for (int i = 0; i < myWorkmanFinancialTransactionList.size(); i++) {
            price = myWorkmanFinancialTransactionList.get(i).getPrice();
            if (Double.valueOf(price) > 0)
                payMoney += Double.valueOf(price);
            else
                getMoney += Double.valueOf(price) * -1;
        }

        UsefulFunction usefulFunction = new UsefulFunction();

        String stringPayMoney = String.format(getString(R.string.text_float_point), payMoney);
        String stringGetMoney = String.format(getString(R.string.text_float_point), getMoney);

        tvGetMoney.setText(usefulFunction.attachCamma(stringGetMoney));
        tvPayMoney.setText(usefulFunction.attachCamma(stringPayMoney));
    }

    private void fillTransactionList() {
        if (myWorkmanFinancialTransactionList == null)
            return;

        getSumPayments();
        MyTransactionAdapter myTransactionAdapter;
        myTransactionAdapter = new MyTransactionAdapter(myWorkmanFinancialTransactionList,
                7, (v, position) -> {
            TransactionDetailDialog transactionDetailDialog =
                    new TransactionDetailDialog(getActivity(), transactionList.getWorkmanFinancialTransactions().get(position));

            Objects.requireNonNull(transactionDetailDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            transactionDetailDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_bg));
            transactionDetailDialog.show();
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            width = (int) ((width) * 0.9);
            transactionDetailDialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        myTransactionListRecycler.setLayoutManager(gridLayoutManager);
        myTransactionListRecycler.setAdapter(myTransactionAdapter);


        showHideWaitingProgress(true);
        showNotFoundInfoLayout();
    }

    private void findViews(View view) {

        myTransactionListRecycler = view.findViewById(R.id.my_transaction_list_recycler);
        AppCompatTextView btnShowAllList = view.findViewById(R.id.btn_show_all_lis);
        AppCompatTextView btnIncreaseCharge = view.findViewById(R.id.btn_increase_charge);

        consWaiting = view.findViewById(R.id.const_waiting_main_fragment);
        constNotFoundInfo = view.findViewById(R.id.const_not_found_info);
        tvCurrentAmount = view.findViewById(R.id.tv_current_amount);
        tvGetMoney = view.findViewById(R.id.tv_get_money);
        tvPayMoney = view.findViewById(R.id.tv_pay);

        btnShowAllList.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AllTransactionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.text_financial_transaction_serialize), transactionList);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnIncreaseCharge.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), ChargeFragment.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(getActivity()).isOnline();
    }

    private void showHideWaitingProgress(boolean hide) {
        consWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        constNotFoundInfo.setVisibility((myWorkmanFinancialTransactionList == null && workmanCredit == null) ? View.VISIBLE : View.GONE);
    }


}
