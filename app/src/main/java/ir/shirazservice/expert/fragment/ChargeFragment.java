package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.dialog.IncreaseAccountChargeDialog;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
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

import static ir.shirazservice.expert.utils.APP.context;
import static ir.shirazservice.expert.utils.APP.currentActivity;

public class ChargeFragment extends Fragment implements  IInternetController {


    private static final int METHOD_TYPE_SAVE_MONEY_TO_SITE = 1;
    private static final int METHOD_TYPE_FORMULA_CHARGE = 2;

   @BindView(R.id.tv_pay_online)
    protected AppCompatTextView tvPayOnline;

    @BindView(R.id.btn_increase_charge)
    protected AppCompatTextView tvIncreaseCharge;

    @BindView(R.id.tv_money)
    protected AppCompatEditText tvMoney;

    @BindView(R.id.tv_first_charge)
    protected AppCompatTextView tvFirstCharge;

    @BindView(R.id.tv_second_charge)
    protected AppCompatTextView tvSecondCharge;

    @BindView(R.id.tv_third_charge)
    protected AppCompatTextView tvThirdCharge;

    @BindView(R.id.tv_fourth_charge)
    protected AppCompatTextView tvFourthCharge;

    @BindView(R.id.tv_fifth_charge)
    protected AppCompatTextView tvFifthCharge;

    @BindView(R.id.tv_sixth_charge)
    protected AppCompatTextView tvSixthCharge;

    @BindView(R.id.tv_first_gift)
    protected AppCompatTextView tvFirstGift;

    @BindView(R.id.tv_second_gift)
    protected AppCompatTextView tvSecondGift;

    @BindView(R.id.tv_third_gift)
    protected AppCompatTextView tvThirdGift;

    @BindView(R.id.tv_fourth_gift)
    protected AppCompatTextView tvFourthGift;

    @BindView(R.id.tv_fifth_gift)
    protected AppCompatTextView tvFifthGift;

    @BindView(R.id.tv_sixth_gift)
    protected AppCompatTextView tvSixthGift;

    @BindView(R.id.const_waiting_main_fragment)
    protected ConstraintLayout constWaiting;
    @BindView(R.id.const_not_found_info)
    protected ConstraintLayout constNotFound;


    private final AppCompatTextView[] chargeTextView = new AppCompatTextView[6];
    private final Long[] chargeAmount = new Long[6];
    private int selectedChargeTextViewInt = -1;
    private List<GiftChargeFormula> giftChargeFormulas;
    private final int[] money = new int[6];
    private final int[] giftMoney = new int[6];

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
    private int servicemanId;
    private String accessToken;
    private String currentRefId;


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

        makeAppCompatTextViewList();
        setNeededIds();
        btnClickConfig();
        fillNumbers();
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


        for (int i = 0; i < 6; i++) {
            int finalI = i;
            chargeTextView[i].setOnClickListener(view -> {
                selectedChargeTextViewInt = finalI;
                showSelectedCharge();
            });
        }
        tvIncreaseCharge.setOnClickListener(view -> openPayFragment());
    }

    private void openPayFragment() {
        Intent intent = new Intent(getActivity(), ServiceRequestDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.text_bundle_req_id), 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void makeAppCompatTextViewList() {
        chargeTextView[0] = tvFirstCharge;
        chargeTextView[1] = tvSecondCharge;
        chargeTextView[2] = tvThirdCharge;
        chargeTextView[3] = tvFourthCharge;
        chargeTextView[4] = tvFifthCharge;
        chargeTextView[5] = tvSixthCharge;


        chargeAmount[0] = 200000L;
        chargeAmount[1] = 300000L;
        chargeAmount[2] = 400000L;
        chargeAmount[3] = 500000L;
        chargeAmount[4] = 600000L;
        chargeAmount[5] = 700000L;


    }

    private void showSelectedCharge() {

        for (int i = 0; i < 6; i++)
            chargeTextView[i].setBackground(getResources().getDrawable(R.drawable.text_red_border_style));

        chargeTextView[selectedChargeTextViewInt].setBackground(getResources().getDrawable(R.drawable.text_green_border_style));

    }

    private String makeConfirmMessage() {

        UsefulFunction usefulFunction = new UsefulFunction();
        String selectedMoney = selectedChargeTextViewInt != -1 ?
                String.valueOf(chargeAmount[selectedChargeTextViewInt]) :
                Objects.requireNonNull(tvMoney.getText()).toString();

        String giftMoney = String.valueOf(getCorrectPoint(Integer.valueOf(selectedMoney)));

        return String.format(getString(R.string.text_gift_charge),
                usefulFunction.attachCamma(selectedMoney), giftMoney);
    }

    private Long getMoneyForPay() {
        if (selectedChargeTextViewInt == -1)
            return Long.valueOf(Objects.requireNonNull(tvMoney.getText()).toString());
        return chargeAmount[selectedChargeTextViewInt];
    }

    private void confirmRequest() {

        if (selectedChargeTextViewInt == -1 && ("".equals(Objects.requireNonNull(tvMoney.getText()).toString())
                || "0".equals(tvMoney.getText().toString()))) {
            APP.customToast(getString(R.string.error_money_select));
            return;
        }
        Long payMoney = getMoneyForPay();
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

    }

    private void callFormulaCharge() {

        if (!isOnline()) {
            openInternetCheckingDialog(METHOD_TYPE_FORMULA_CHARGE);
        }
        showHideWaitingProgress(false);
        GetGiftChargeFormulaController getGiftChargeFormulaController = new GetGiftChargeFormulaController(getGiftChargeFormulaCallback);
        getGiftChargeFormulaController.start(servicemanId, accessToken);
    }

    private void calculateGiftCharge() {
        if (giftChargeFormulas != null) {
            getMoniesGift();
            setGiftViews();
            showHideWaitingProgress(true);
        } else
            showNotFoundInfoLayout();
    }


    private void getMoniesGift() {
        for (int j = 0; j < 6; j++) {
            giftMoney[j] = getCorrectPoint(money[j]);
        }
    }

    private int getCorrectPoint(int thisMoney) {
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
        return 0;
    }

    private void fillNumbers() {

        money[0] = 200000;
        money[1] = 300000;
        money[2] = 400000;
        money[3] = 500000;
        money[4] = 600000;
        money[5] = 700000;
    }

    private void setGiftViews() {
        tvFirstGift.setText(setValues(giftMoney[0]));
        tvSecondGift.setText(setValues(giftMoney[1]));
        tvThirdGift.setText(setValues(giftMoney[2]));
        tvFourthGift.setText(setValues(giftMoney[3]));
        tvFifthGift.setText(setValues(giftMoney[4]));
        tvSixthGift.setText(setValues(giftMoney[5]));
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

    public static ChargeFragment newInstance() {
        return new ChargeFragment();
    }


}
