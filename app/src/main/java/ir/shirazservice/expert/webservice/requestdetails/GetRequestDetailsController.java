package ir.shirazservice.expert.webservice.requestdetails;

import android.content.Context;
import androidx.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetRequestDetailsController {

    private final GetRequestDetailsApi.getRequestDetailsCallback getRequestDetailsCallback;

    public GetRequestDetailsController(Context context, GetRequestDetailsApi.getRequestDetailsCallback getRequestDetailsCallback) {

        this.getRequestDetailsCallback = getRequestDetailsCallback;
    }

    public void start(int userId, String accessToken, RequestDetailsReq requestDetailsReception) {


        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetRequestDetailsApi getRequestDetailsApi = retrofit.create(GetRequestDetailsApi.class);
        Call<RequestDetails> requestDetailsCall = getRequestDetailsApi.execute(userId, accessToken, requestDetailsReception);
        requestDetailsCall.enqueue(new Callback<RequestDetails>() {
            @Override
            public void onResponse(@NonNull Call<RequestDetails> call, @NonNull Response<RequestDetails> response) {

                if (response.isSuccessful())
                    getRequestDetailsCallback.onResponse(true, null, response.body());
                else {
                    //Todo
                    //بایستی خطا رو به تابع مربوطه پاس بدم
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getRequestDetailsCallback.onResponse(false, err, null);
                }

            }

            @Override
            public void onFailure(@NonNull Call<RequestDetails> call, @NonNull Throwable t) {

            }
        });
    }

}
