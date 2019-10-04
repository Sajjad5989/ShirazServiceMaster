package ir.shirazservice.expert.webservice.finishrequest;

import android.content.Context;
import android.support.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FinishRequestByWorkmanController {

    private final FinishRequestByWorkmanApi.finishRequestByWorkmanCallback finishRequestByWorkmanCallback;

    public FinishRequestByWorkmanController(Context context, FinishRequestByWorkmanApi.finishRequestByWorkmanCallback finishRequestByWorkmanCallback) {
        this.finishRequestByWorkmanCallback = finishRequestByWorkmanCallback;
    }

    public void start(int userId, String accessToken, FinishRequestByWorkmanRequest finishRequestByWorkmanRequest) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        FinishRequestByWorkmanApi finishRequestByWorkmanApi = retrofit.create(FinishRequestByWorkmanApi.class);
        Call<SuccessIdResponse> requestListCall = finishRequestByWorkmanApi.execute(userId, accessToken
                , finishRequestByWorkmanRequest);
        requestListCall.enqueue(new Callback<SuccessIdResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessIdResponse> call, @NonNull Response<SuccessIdResponse> response) {
                if (response.isSuccessful())
                    finishRequestByWorkmanCallback.onResponse(true, null, response.body());
                else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    finishRequestByWorkmanCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessIdResponse> call, @NonNull Throwable t) {

            }
        });
    }

}
