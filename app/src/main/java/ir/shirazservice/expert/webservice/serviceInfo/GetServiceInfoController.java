package ir.shirazservice.expert.webservice.serviceInfo;

import android.support.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetServiceInfoController {

    private final GetServiceInfoApi.getServiceInfoCallBack getServiceInfoCallBack;

    public GetServiceInfoController(GetServiceInfoApi.getServiceInfoCallBack getServiceInfoCallBack) {
        this.getServiceInfoCallBack = getServiceInfoCallBack;
    }


    public void start(int userId, String accessToken, ReceptionService receptionService) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetServiceInfoApi getServiceManInfoApi = retrofit.create(GetServiceInfoApi.class);
        Call<ServiceInfo> serviceInfoCall = getServiceManInfoApi.execute(userId, accessToken, receptionService);
        serviceInfoCall.enqueue(new Callback<ServiceInfo>() {
            @Override
            public void onResponse(@NonNull Call<ServiceInfo> call, @NonNull Response<ServiceInfo> response) {
                if (response.isSuccessful())
                    getServiceInfoCallBack.onResponse(true, null, response.body());
                else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getServiceInfoCallBack.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServiceInfo> call, @NonNull Throwable t) {

            }
        });


    }

}
