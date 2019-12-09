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
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
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
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.ChargeAmount;
import ir.shirazservice.expert.utils.NumberTextWatcherForThousand;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.depositmoney.DepositMoney;
import ir.shirazservice.expert.webservice.depositmoney.DepositMoneyReq;
import ir.shirazservice.expert.webservice.depositmoney.InsrtDepositMoneyApi;
import ir.shirazservice.expert.webservice.depositmoney.InsrtDepositMoneyController;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getbaseinfo.BaseInfoOfApp;
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
    @BindView(R.id.et_charge_amount)
    protected AppCompatEditText etChargeAmount;
    @BindView(R.id.tv_gift_charge)
    protected AppCompatTextView tvGiftCharge;
    @BindView(R.id.image_add_charge)
    protected AppCompatImageView imageIncreaseCharge;
    @BindView(R.id.image_decrease_charge)
    protected AppCompatImageView imageDecreaseCharge;

    @BindView(R.id.recycler_charge)
    protected AnimatedRecyclerView animatedRecyclerView;
    @BindView(R.id.tv_pay_online)
    protected AppCompatTextView tvPayOnline;
    @BindView(R.id.btn_increase_charge)
    protected AppCompatTextView tvIncreaseCharge;

    @BindView(R.id.const_waiting_main_fragment)
    protected ConstraintLayout constWaiting;
    @BindView(R.id.const_not_found_info)
    protected ConstraintLayout constNotFound;
    private ArrayList<ChargeAmount> chargeAmountList = new ArrayList<ChargeAmount>();
    private List<GiftChargeFormula> giftChargeFormulas;
    private int minAccountChargeAmount;
    private int servicemanId;
    private String accessToken;
    private String serviceManFullName;
    private String currentRefId;
    private GeneralIdsInput generalIdsInput;
    private UsefulFunction usefulFunction = new UsefulFunction();
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
        getMinAccountCharge();
        btnClickConfig();

        callFormulaCharge();
//
//        if (getActivity().getIntent().getData() != null) {
//
//            ZarinPal.getPurchase(getActivity()).verificationPayment(getActivity().getIntent().getData(),
//                    (isPaymentSuccess, refID, paymentRequest) -> {
//
//                        currentRefId = refID;
//                        sendPayMoneyToSite();
//
//                    });
//        }

    }


    @Override
    public void onResume() {
        super.onResume();
//
//        if (getActivity().getIntent().getData() != null) {
//
//            ZarinPal.getPurchase(getActivity()).verificationPayment(getActivity().getIntent().getData(),
//                    (isPaymentSuccess, refID, paymentRequest) -> {
//
//                        currentRefId = refID;
//                        sendPayMoneyToSite();
//
//                    });
//        }
    }


    private void btnClickConfig() {
        etChargeAmount.addTextChangedListener(new NumberTextWatcherForThousand(etChargeAmount));

        tvPayOnline.setOnClickListener(view -> confirmRequest());
        tvIncreaseCharge.setOnClickListener(view -> openPayFragment());
        imageIncreaseCharge.setOnClickListener(view -> incDecChargeAmount(1));
        imageDecreaseCharge.setOnClickListener(view -> incDecChargeAmount(2));
    }

    private void incDecChargeAmount(int incDec) {
        String priceStr = usefulFunction.deAttachCamma(
                Objects.requireNonNull(etChargeAmount.getText()).toString());

        int price = "".equals(priceStr) ? 0 : Integer.valueOf(priceStr);

        int newPrice;
        newPrice = incDec == 1 ? price + 10000 : price - 10000;
        if (newPrice < 0)
            newPrice = 0;

        etChargeAmount.setText(String.valueOf(newPrice));
        tvGiftCharge.setText(usefulFunction.attachCamma(String.valueOf(getCorrectPoint(newPrice))));

    }

    private void openPayFragment() {
        Intent intent = new Intent(getActivity(), ServiceRequestDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.text_bundle_req_id), 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getAccountRecommentCharge() {

        int[] price = {100000, 200000, 500000, 1000000};// minAccountChargeAmount;
        int giftPrice;
        ChargeAmount chargeAmount;
        for (int i = 0; i < 4; i++) {
            chargeAmount = new ChargeAmount();
            chargeAmount.setPrice(price[i]);
            giftPrice = getCorrectPoint(price[i]);
            chargeAmount.setNewPrice(giftPrice + price[i]);
            chargeAmountList.add(chargeAmount);
        }

        ChargeAccountAdapter chargeAccountAdapter = new ChargeAccountAdapter(chargeAmountList, (v, position) -> {
            int price1 = chargeAmountList.get(position).getPrice();
//            etChargeAmount.setText(usefulFunction.attachCamma(String.valueOf(price1)));
            etChargeAmount.setText(String.valueOf(price1));
            tvGiftCharge.setText(usefulFunction.attachCamma(String.valueOf(getCorrectPoint(price1))));
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        animatedRecyclerView.setLayoutManager(gridLayoutManager);
        animatedRecyclerView.setAdapter(chargeAccountAdapter);
        animatedRecyclerView.scheduleLayoutAnimation();

        showHideWaitingProgress(true);
        showNotFoundInfoLayout();

    }

    private String makeConfirmMessage() {

        String chargeAmount = usefulFunction.deAttachCamma(Objects.requireNonNull(etChargeAmount.getText()).toString());
        String giftAmount = String.valueOf(getCorrectPoint(Integer.valueOf(chargeAmount)));

        chargeAmount = usefulFunction.attachCamma(chargeAmount);
        giftAmount = "<span style=\"color: #d32f2f\">" + usefulFunction.attachCamma(giftAmount) + "</span> ";
        return String.format(getString(R.string.text_gift_charge), chargeAmount, giftAmount);

    }

    private void confirmRequest() {

        if ("".equals(Objects.requireNonNull(etChargeAmount.getText()).toString())) {
            APP.customToast(getString(R.string.error_money_select));
            return;
        }
//
       Long payMoney = Long.valueOf(usefulFunction.deAttachCamma(Objects.requireNonNull(etChargeAmount.getText()).toString()));
        if (minAccountChargeAmount > payMoney) {
            APP.customToast(getString(R.string.text_min_charge_1) + minAccountChargeAmount
                    + getString(R.string.text_min_charge_2));
            return;
        }

        if (payMoney <= 0) {
            APP.customToast(getString(R.string.error_money_select));
            return;
        }

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
        paymentRequest.setAmount((long)amount/10);
        String payDesc = String.format("%s%s- %s%s%s", getString(R.string.text_zarinpal_part_1), serviceManFullName
                , getString(R.string.text_zarinpal_part_2), String.valueOf(amount),
                getString(R.string.text_currency_symbol));
        paymentRequest.setDescription(payDesc);
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

        PersianCalendar persianCalendar = new PersianCalendar();

        DepositMoneyReq depositMoneyReq = new DepositMoneyReq();
        depositMoneyReq.setServicemanId(servicemanId);
        depositMoneyReq.setCardNo(currentRefId);
        depositMoneyReq.setTrackingCode(currentRefId);
        depositMoneyReq.setFullName(serviceManFullName);
        depositMoneyReq.setType(3);
        depositMoneyReq.setDepositTimeYear(persianCalendar.getPersianYear());
        depositMoneyReq.setDepositTimeMonth(persianCalendar.getPersianMonth());
        depositMoneyReq.setDepositTimeDay(persianCalendar.getPersianDay());
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
        serviceManFullName = serviceMan.getFullName();
        generalIdsInput = new GeneralIdsInput();
        generalIdsInput.setCityId(serviceMan.getCityId());
        generalIdsInput.setProvinceId(serviceMan.getProvinceId());

    }

    private void getMinAccountCharge() {
        BaseInfoOfApp baseInfoOfApp = GeneralPreferences.getInstance(context).getBaseInfoOfApp();
        if (baseInfoOfApp != null)
            minAccountChargeAmount = baseInfoOfApp.getMinChargeCreditAmountForWorkman();
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


    private int getCorrectPoint(int thisMoney) {
        double startPeriod;
        double endPeriod;
        int giftPercent;
        int giftValue = 0;
        for (int i = 0; i < giftChargeFormulas.size(); i++) {
            startPeriod = Double.valueOf(giftChargeFormulas.get(i).getStartPeriod());
            endPeriod = Double.valueOf(giftChargeFormulas.get(i).getEndPeriod());
            giftPercent = giftChargeFormulas.get(i).getGiftPercent();
            if (startPeriod <= thisMoney) {
                if (endPeriod >= thisMoney || endPeriod == 0) {
                    giftValue = thisMoney * giftPercent / 100;
                }
            }
        }

        if (giftValue>0)
        {
            int remain = giftValue%100;
            giftValue = giftValue - remain;
        }

        return giftValue;
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
