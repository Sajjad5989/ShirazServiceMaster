package ir.shirazservice.expert.webservice.workmanfinancial;

import androidx.annotation.NonNull;

import java.util.List;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetWorkmanFinancialTransactionsController {

    private final GetWorkmanFinancialTransactionsApi.getWorkmanFinancialTransactionsCallback getWorkmanFinancialTransactionsCallback;

    public GetWorkmanFinancialTransactionsController(GetWorkmanFinancialTransactionsApi.getWorkmanFinancialTransactionsCallback getWorkmanFinancialTransactionsCallback) {
        this.getWorkmanFinancialTransactionsCallback = getWorkmanFinancialTransactionsCallback;
    }

    public void start(int userId, String accessToken,
                      WorkmanFinancialTransactionReq workmanFinancialTransactionReq) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetWorkmanFinancialTransactionsApi getWorkmanFinancialTransactionsApi = retrofit.create(GetWorkmanFinancialTransactionsApi.class);
        Call<List<WorkmanFinancialTransaction>> workmanFinancialTransactionCall = getWorkmanFinancialTransactionsApi.
                execute(userId, accessToken, workmanFinancialTransactionReq);

        workmanFinancialTransactionCall.enqueue(new Callback<List<WorkmanFinancialTransaction>>() {
            @Override
            public void onResponse(@NonNull Call<List<WorkmanFinancialTransaction>> call, @NonNull Response<List<WorkmanFinancialTransaction>> response) {
                if (response.isSuccessful())
                    getWorkmanFinancialTransactionsCallback.onResponse(true, null, response.body());
                else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getWorkmanFinancialTransactionsCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WorkmanFinancialTransaction>> call, @NonNull Throwable t) {

            }
        });
    }

}
