package ir.shirazservice.expert.webservice.requestinfo;

import android.support.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestInfoController {

    private final RequestInfoApi.GetRequestInfoCallback getRequestInfoCallback;

    public RequestInfoController(RequestInfoApi.GetRequestInfoCallback getRequestInfoCallback) {
        this.getRequestInfoCallback = getRequestInfoCallback;
    }


    public void start(int userId, String accessToken, RequestInfoInput listInputs) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        RequestInfoApi requestInfoApi = retrofit.create(RequestInfoApi.class);
        Call<RequestInfo> requestInfoCall = requestInfoApi.execute(userId, accessToken, listInputs);
        requestInfoCall.enqueue(new Callback<RequestInfo>() {
            @Override
            public void onResponse(@NonNull Call<RequestInfo> call, @NonNull Response<RequestInfo> response) {
                if (response.isSuccessful()) {
                    getRequestInfoCallback.onResponse(true, null, response.body());
                } else {
                    //Todo
                    //بایستی خطا رو به تابع مربوطه پاس بدم
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getRequestInfoCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestInfo> call, @NonNull Throwable t) {

            }
        });
    }

}
