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

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.NumberTextWatcherForThousand;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.webservice.depositmoney.DepositMoney;
import ir.shirazservice.expert.webservice.depositmoney.DepositMoneyReq;
import ir.shirazservice.expert.webservice.depositmoney.InsrtDepositMoneyApi;
import ir.shirazservice.expert.webservice.depositmoney.InsrtDepositMoneyController;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;

import static ir.shirazservice.expert.utils.APP.context;

public class OfflineChargeFragment extends Fragment implements Serializable, IInternetController,
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener {

    @BindView(R.id.et_card_number)
    AppCompatEditText etCardNumber;
    @BindView(R.id.rb_cart)
    AppCompatRadioButton rbCart;
    @BindView(R.id.rb_pay)
    AppCompatRadioButton rbPay;
    @BindView(R.id.btn_date)
    AppCompatButton btnDate;
    @BindView(R.id.et_payer)
    AppCompatEditText etPayer;
    @BindView(R.id.et_code_peigiri)
    AppCompatEditText etCodePeigiri;
    @BindView(R.id.et_pay_money)
    AppCompatEditText etPayMoney;
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;
    private int day = 0;
    private int month;
    private int year;
    private int servicemanId;
    private String accessToken;
    private final InsrtDepositMoneyApi.insrtDepositMoneyCallback insrtDepositMoneyCallback =
            new InsrtDepositMoneyApi.insrtDepositMoneyCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, DepositMoney response) {
                    if (successful) {
                        APP.customToast(getActivity().getString(R.string.text_save_pay_money));
                        getActivity().finish();
                    } else
                        APP.customToast(errorResponse.getMessage());
                }

                @Override
                public void onFailure(String cause) {

                }
            };

    public static OfflineChargeFragment newInstance() {
        return new OfflineChargeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_charge, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setNeededIds();
        onClickConfig();
        rbCart.setChecked(true);

    }

    private void onClickConfig() {
        etPayMoney.addTextChangedListener(new NumberTextWatcherForThousand(etPayMoney));
        btnSave.setOnClickListener(view -> saveOfflinePaymentInformation());
        btnDate.setOnClickListener(view -> openDateDialog());
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

                saveOfflinePaymentInformation();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    private void saveOfflinePaymentInformation() {

        if (!checkValidity())
            return;

        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        InsrtDepositMoneyController insrtDepositMoneyController = new
                InsrtDepositMoneyController(insrtDepositMoneyCallback);
        insrtDepositMoneyController.start(servicemanId, accessToken, getValues());
    }

    private DepositMoneyReq getValues() {

        DepositMoneyReq depositMoneyReq = new DepositMoneyReq();
        depositMoneyReq.setServicemanId(servicemanId);
        depositMoneyReq.setCardNo(Objects.requireNonNull(etCardNumber.getText()).toString());
        depositMoneyReq.setTrackingCode(Objects.requireNonNull(etCodePeigiri.getText()).toString());
        depositMoneyReq.setType(rbCart.isChecked() ? 1 : 2);
        depositMoneyReq.setFullName(Objects.requireNonNull(etPayer.getText()).toString());
        depositMoneyReq.setPrice(Objects.requireNonNull(etPayMoney.getText()).toString());

        depositMoneyReq.setDepositTimeDay(day);
        depositMoneyReq.setDepositTimeMonth(month);
        depositMoneyReq.setDepositTimeYear(year);

        return depositMoneyReq;
    }

    private boolean checkValidity() {
        if ("".equals(Objects.requireNonNull(etCardNumber.getText()).toString()) ||
                "".equals(Objects.requireNonNull(etCodePeigiri.getText()).toString()) ||
                "".equals(Objects.requireNonNull(etPayer.getText()).toString()) ||
                "".equals(Objects.requireNonNull(etPayMoney.getText()).toString())) {
            APP.customToast(getString(R.string.text_error_need_value));
            return false;
        }

        if (!rbCart.isChecked() && !rbPay.isChecked()) {
            APP.customToast(getString(R.string.text_error_select_payment_type));
            return false;
        }

        if (day == 0) {
            APP.customToast(getString(R.string.text_error_select_date));
            return false;
        }


        return true;

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        PersianCalendar now = new PersianCalendar();
        now.setPersianDate(year, monthOfYear, dayOfMonth);

        String textDate = now.getPersianWeekDayName()+"، "+now.getPersianDay()+" "+
                now.getPersianMonthName()+" "+now.getPersianYear();
        btnDate.setText( textDate);
        day = dayOfMonth;
        month = monthOfYear + 1;
        this.year = year;
    }

    private void openDateDialog() {
        PersianCalendar now = new PersianCalendar();
        DatePickerDialog dpd;
        dpd = DatePickerDialog.newInstance(
                this,
                now.getPersianYear(),
                now.getPersianMonth(),
                now.getPersianDay()
        );
        dpd.setThemeDark(false);
        dpd.show(getFragmentManager(), "");
    }

}
