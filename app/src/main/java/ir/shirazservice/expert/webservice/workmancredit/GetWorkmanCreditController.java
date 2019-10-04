package ir.shirazservice.expert.webservice.workmancredit;

import android.support.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetWorkmanCreditController {

    private final GetWorkmanCreditApi.getWorkmanCreditCallback getWorkmanCreditCallback;

    public GetWorkmanCreditController(GetWorkmanCreditApi.getWorkmanCreditCallback getWorkmanCreditCallback) {
        this.getWorkmanCreditCallback = getWorkmanCreditCallback;
    }

    public void start(int userId, String accessToken, WorkmanCreditReq workmanCreditReq) {


        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetWorkmanCreditApi getWorkmanCreditApi = retrofit.create(GetWorkmanCreditApi.class);
        Call<WorkmanCredit> workmanCreditCall = getWorkmanCreditApi.execute(userId,
                accessToken
                , workmanCreditReq);
        workmanCreditCall.enqueue(new Callback<WorkmanCredit>() {
            @Override
            public void onResponse(@NonNull Call<WorkmanCredit> call, @NonNull Response<WorkmanCredit> response) {
                if (response.isSuccessful())
                    getWorkmanCreditCallback.onResponse(true, null, response.body());
                else {
                    //Todo
                    //بایستی خطا رو به تابع مربوطه پاس بدم
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getWorkmanCreditCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkmanCredit> call,@NonNull  Throwable t) {

            }
        });
    }

}
