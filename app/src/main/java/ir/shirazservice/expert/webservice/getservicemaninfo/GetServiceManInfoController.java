package ir.shirazservice.expert.webservice.getservicemaninfo;

import android.content.Context;
import android.support.annotation.NonNull;

import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetServiceManInfoController {

    private final Context context;
    private final GetServiceManInfoApi.GetServiceManInfoCallback callback;

    public GetServiceManInfoController(Context context, GetServiceManInfoApi.GetServiceManInfoCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void start(ServiceManSavedInfo serviceManFirstInfo)
    {
        int userId = GeneralPreferences.getInstance(context).getInt(ShirazServiceApi.USER_ID);
        String token = GeneralPreferences.getInstance(context).getString(ShirazServiceApi.ACCESS_TOKEN);

        Retrofit retrofit= AppController.getInstance().getRetrofitObject();
        GetServiceManInfoApi getServiceManInfoApi = retrofit.create(GetServiceManInfoApi.class);
        Call<ServiceMan> authenticationSuccessCall = getServiceManInfoApi.execute(userId,token,serviceManFirstInfo);
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
                callback.onFailure( t.getCause( ).getMessage( ));
            }
        });
    }

}
