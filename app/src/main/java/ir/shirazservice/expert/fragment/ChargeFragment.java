package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.adapter.ChargeAccountAdapter;
import ir.shirazservice.expert.dialog.IncreaseAccountChargeDialog;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.interfaces.RecyclerViewClickListener;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.ChargeAmount;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.depositmoney.DepositMoney;
import ir.shirazservice.expert.webservice.depositmoney.DepositMoneyReq;
import ir.shirazservice.expert.webservice.depositmoney.InsrtDepositMoneyApi;
import ir.shirazservice.expert.webservice.depositmoney.InsrtDepositMoneyController;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getgiftchargeformula.GetGiftChargeFormulaApi;
import ir.shirazservice.expert.webservice.getgiftchargeformula.GetGiftChargeFormulaController;
import ir.shirazservice.expert.webservice.getgiftchargeformula.GiftChargeFormula;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.shirazserviceapi.GeneralIdsInput;

import static ir.shirazservice.expert.utils.APP.context;
import static ir.shirazservice.expert.utils.APP.currentActivity;

public class ChargeFragment extends Fragment implements IInternetController {


    private static final int METHOD_TYPE_SAVE_MONEY_TO_SITE = 1;
    private static final int METHOD_TYPE_FORMULA_CHARGE = 2;
    private ArrayList<ChargeAmount> chargeAmountList=new ArrayList<ChargeAmount>() ;
    private final InsrtDepositMoneyApi.insrtDepositMoneyCallback insrtDepositMoneyCallback =
            new InsrtDepositMoneyApi.insrtDepositMoneyCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, DepositMoney response) {
                    if (successful) {
                        getActivity().finish();
                    } else
                        APP.customToast(errorResponse.getMessage());
                }

                @Override
                public void onFailure(String cause) {

                }
            };

    @BindView( R.id.recycler_charge)
    protected AnimatedRecyclerView animatedRecyclerView;

    @BindView(R.id.tv_pay_online)
    protected AppCompatTextView tvPayOnline;
    @BindView(R.id.btn_increase_charge)
    protected AppCompatTextView tvIncreaseCharge;
//    @BindView(R.id.tv_money)
//    protected AppCompatEditText tvMoney;
    @BindView(R.id.const_waiting_main_fragment)
    protected ConstraintLayout constWaiting;
    @BindView(R.id.const_not_found_info)
    protected ConstraintLayout constNotFound;
    private List<GiftChargeFormula> giftChargeFormulas;
    private final GetGiftChargeFormulaApi.getGiftChargeFormulaCallback getGiftChargeFormulaCallback =
            new GetGiftChargeFormulaApi.getGiftChargeFormulaCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<GiftChargeFormula> response) {
                    if (successful)
                        giftChargeFormulas = response;
                    else
                        giftChargeFormulas = new ArrayList<>();

                    calculateGiftCharge();
                }

                @Override
                public void onFailure(String cause) {
                    giftChargeFormulas = new ArrayList<>();
                    calculateGiftCharge();
                }
            };
    private int servicemanId;
    private String accessToken;
    private String currentRefId;
    private GeneralIdsInput generalIdsInput;

    public static ChargeFragment newInstance() {
        return new ChargeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_charge, container, false);
        ButterKnife.bind(this, view);
        currentActivity = getActivity();
        assert container != null;
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        setNeededIds();
        btnClickConfig();

        callFormulaCharge();

        if (getActivity().getIntent().getData() != null) {

            ZarinPal.getPurchase(getActivity()).verificationPayment(getActivity().getIntent().getData(),
                    (isPaymentSuccess, refID, paymentRequest) -> {

                        currentRefId = refID;
                        sendPayMoneyToSite();

                    });
        }

    }

    private void btnClickConfig() {
        tvPayOnline.setOnClickListener(view -> confirmRequest());
        tvIncreaseCharge.setOnClickListener(view -> openPayFragment());
    }

    private void openPayFragment() {
        Intent intent = new Intent(getActivity(), ServiceRequestDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.text_bundle_req_id), 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getAccountRecommentCharge() {

        Long price = 200000L;
        Long giftPrice;
        ChargeAmount chargeAmount ;
        for (int i = 0; i < 6; i++) {

            chargeAmount = new ChargeAmount();
            chargeAmount.setPrice(price);
            giftPrice = getCorrectPoint(price);
            chargeAmount.setNewPrice(giftPrice+price);
            chargeAmountList.add(chargeAmount);
            price += 100000L;
        }

        ChargeAccountAdapter chargeAccountAdapter = new ChargeAccountAdapter(chargeAmountList, new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        animatedRecyclerView.setLayoutManager(gridLayoutManager);
        animatedRecyclerView.setAdapter(chargeAccountAdapter);
        animatedRecyclerView.scheduleLayoutAnimation();

        showHideWaitingProgress(true);
        showNotFoundInfoLayout();

    }

    private String makeConfirmMessage() {

        UsefulFunction usefulFunction = new UsefulFunction();
//        String selectedMoney = selectedChargeTextViewInt != -1 ?
//                String.valueOf(chargeAmount[selectedChargeTextViewInt]) :
//                Objects.requireNonNull(tvMoney.getText()).toString();

//        String giftMoney = String.valueOf(getCorrectPoint(Integer.valueOf(selectedMoney)));
//
//        return String.format(getString(R.string.text_gift_charge),
//                usefulFunction.attachCamma(selectedMoney), giftMoney);
        return "";
    }

    private void confirmRequest() {
//
//        if (selectedChargeTextViewInt == -1 && ("".equals(Objects.requireNonNull(tvMoney.getText()).toString())
//                || "0".equals(tvMoney.getText().toString()))) {
//            APP.customToast(getString(R.string.error_money_select));
//            return;
//        }
        Long payMoney = 1000L;//getMoneyForPay();
        IncreaseAccountChargeDialog chooseRequestDialog =
                new IncreaseAccountChargeDialog(getActivity(), makeConfirmMessage(), done -> {
                    if (done)
                        onlinePayment(payMoney);
                });

        Objects.requireNonNull(chooseRequestDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        chooseRequestDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        chooseRequestDialog.setCancelable(false);
        chooseRequestDialog.show();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.9);
        chooseRequestDialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

    }

    private void onlinePayment(Long amount) {
        ZarinPal za = ZarinPal.getPurchase(getActivity());
        PaymentRequest paymentRequest = ZarinPal.getPaymentRequest();
        paymentRequest.setMerchantID(BuildConfig.paymentMerchant);
        paymentRequest.setAmount(amount);
        paymentRequest.setDescription(getString(R.string.text_online_payment_description));

        paymentRequest.setCallbackURL(BuildConfig.paymentUrl);

        za.startPayment(paymentRequest, (status, authority, paymentGatewayUri, intent) -> {
                    if (status == 100) {
                        startActivity(intent);
                    } else {
                        APP.customToast(getString(R.string.error_online_payment));
                    }
                }
        );
    }

    private DepositMoneyReq getValues() {

        DepositMoneyReq depositMoneyReq = new DepositMoneyReq();
        depositMoneyReq.setServicemanId(servicemanId);
        depositMoneyReq.setCardNo(currentRefId);
        depositMoneyReq.setTrackingCode(currentRefId);
        depositMoneyReq.setType(3);
        return depositMoneyReq;
    }

    private void sendPayMoneyToSite() {

        if (!isOnline()) {
            openInternetCheckingDialog(METHOD_TYPE_SAVE_MONEY_TO_SITE);
        }

        InsrtDepositMoneyController insrtDepositMoneyController = new
                InsrtDepositMoneyController(insrtDepositMoneyCallback);
        insrtDepositMoneyController.start(servicemanId, accessToken, getValues());
    }

    private void setNeededIds() {
        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        servicemanId = serviceMan.getServicemanId();
        accessToken = serviceMan.getAccessToken();
        generalIdsInput = new GeneralIdsInput();
        generalIdsInput.setCityId(serviceMan.getCityId());
        generalIdsInput.setProvinceId(serviceMan.getProvinceId());
    }

    private void callFormulaCharge() {

        if (!isOnline()) {
            openInternetCheckingDialog(METHOD_TYPE_FORMULA_CHARGE);
        }
        showHideWaitingProgress(false);
        GetGiftChargeFormulaController getGiftChargeFormulaController = new GetGiftChargeFormulaController(getGiftChargeFormulaCallback);
        getGiftChargeFormulaController.start(servicemanId, accessToken, generalIdsInput);
    }

    private void calculateGiftCharge() {
        if (giftChargeFormulas != null) {
            getAccountRecommentCharge();
            showHideWaitingProgress(true);
        } else
            showNotFoundInfoLayout();
    }


    private Long getCorrectPoint(Long thisMoney) {
        double startPeriod;
        double endPeriod;
        int giftPercent;
        for (int i = 0; i < giftChargeFormulas.size(); i++) {
            startPeriod = Double.valueOf(giftChargeFormulas.get(i).getStartPeriod());
            endPeriod = Double.valueOf(giftChargeFormulas.get(i).getEndPeriod());
            giftPercent = giftChargeFormulas.get(i).getGiftPercent();
            if (startPeriod <= thisMoney) {
                if (endPeriod >= thisMoney || endPeriod == 0) {
                    return (thisMoney * giftPercent / 100);
                }
            }
        }
        return 0L;
    }

    private String setValues(int value) {
        String curValue = String.valueOf(value);
        curValue = new UsefulFunction().attachCamma(curValue) + getResources().getString(R.string.text_currency_symbol);
        return curValue;
    }

    private void openInternetCheckingDialog(int typeCallDialog) {
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
                switch (typeCallDialog) {
                    case METHOD_TYPE_SAVE_MONEY_TO_SITE:
                        sendPayMoneyToSite();
                        break;
                    case METHOD_TYPE_FORMULA_CHARGE:
                        callFormulaCharge();
                        break;
                }

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

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(getActivity()).isOnline();
    }

    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {
        showHideWaitingProgress(true);
        constNotFound.setVisibility((giftChargeFormulas == null) ? View.VISIBLE : View.GONE);
    }


}
