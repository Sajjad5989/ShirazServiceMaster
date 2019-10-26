package ir.shirazservice.expert.webservice.getservicemaninfo;

import androidx.annotation.NonNull;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetServiceManInfoController {

    private final GetServiceManInfoApi.GetServiceManInfoCallback callback;

    public GetServiceManInfoController( GetServiceManInfoApi.GetServiceManInfoCallback callback) {
        this.callback = callback;
    }

    public void start(ServiceManSavedInfo serviceManFirstInfo)
    {

        Retrofit retrofit= AppController.getInstance().getRetrofitObject();
        GetServiceManInfoApi getServiceManInfoApi = retrofit.create(GetServiceManInfoApi.class);
        Call<ServiceMan> authenticationSuccessCall = getServiceManInfoApi.execute(serviceManFirstInfo);
        authenticationSuccessCall.enqueue(new Callback<ServiceMan>() {
            @Override
            public void onResponse(@NonNull Call<ServiceMan> call,@NonNull  Response<ServiceMan> response) {
                if(response.isSuccessful()){
                    callback.onResponse(true, null, response.body());
                }
                else{
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(),response.message());
                    callback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServiceMan> call,@NonNull  Throwable t) {
               // callback.onFailure( t.getCause( ).getMessage( ));
            }
        });
    }

}
