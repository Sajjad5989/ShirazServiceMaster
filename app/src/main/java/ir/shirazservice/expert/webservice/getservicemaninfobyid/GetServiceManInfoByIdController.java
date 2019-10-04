package ir.shirazservice.expert.webservice.getservicemaninfobyid;

import android.support.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetServiceManInfoByIdController {

    private final GetServiceManInfoByIdApi.GetServiceManInfoByIdCallback callback;

    public GetServiceManInfoByIdController(GetServiceManInfoByIdApi.GetServiceManInfoByIdCallback callback) {
        this.callback = callback;
    }

    public void start(int userId, String accessToken, ServiceManInfoById servicemaninfobyid) {
        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetServiceManInfoByIdApi getServiceManInfoByIdApi = retrofit.create(GetServiceManInfoByIdApi.class);
        Call<ServiceManInfoDetail> servicemaninfodetailCall = getServiceManInfoByIdApi.execute(
                userId, accessToken, servicemaninfobyid);

        servicemaninfodetailCall.enqueue(new Callback<ServiceManInfoDetail>() {
            @Override
            public void onResponse(@NonNull Call<ServiceManInfoDetail> call, @NonNull Response<ServiceManInfoDetail> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(true, null, response.body());
                } else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    callback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServiceManInfoDetail> call, @NonNull Throwable t) {
                callback.onFailure(t.getCause().getMessage());
            }
        });
    }
}
