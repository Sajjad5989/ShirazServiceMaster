package ir.shirazservice.expert.webservice.depositmoney;

import androidx.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InsrtDepositMoneyController {

    private final InsrtDepositMoneyApi.insrtDepositMoneyCallback insrtDepositMoneyCallback;

    public InsrtDepositMoneyController(InsrtDepositMoneyApi.insrtDepositMoneyCallback insrtDepositMoneyCallback) {
        this.insrtDepositMoneyCallback = insrtDepositMoneyCallback;
    }


    public void start(int userId, String accessToken, DepositMoneyReq depositMoneyReq) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        InsrtDepositMoneyApi insrtDepositMoneyApi = retrofit.create(InsrtDepositMoneyApi.class);
        Call<DepositMoney> depositMoneyCall = insrtDepositMoneyApi.execute(userId, accessToken, depositMoneyReq);
        depositMoneyCall.enqueue(new Callback<DepositMoney>() {
            @Override
            public void onResponse(@NonNull Call<DepositMoney> call, @NonNull Response<DepositMoney> response) {

                if (response.isSuccessful())
                    insrtDepositMoneyCallback.onResponse(true, null, response.body());
                else {
                    //Todo
                    //بایستی خطا رو به تابع مربوطه پاس بدم
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    insrtDepositMoneyCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DepositMoney> call, @NonNull Throwable t) {

            }
        });
    }


}
