package ir.shirazservice.expert.webservice.myservice;

import androidx.annotation.NonNull;

import java.util.List;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetMyServicesController {

    private final GetMyServicesApi.getMyServicesCallback getMyServicesCallback;

    public GetMyServicesController(GetMyServicesApi.getMyServicesCallback getMyServicesCallback) {
        this.getMyServicesCallback = getMyServicesCallback;
    }


    public void start(int userId, String accessToken, ReceptionMyService receptionMyService) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetMyServicesApi getMyServicesApi = retrofit.create(GetMyServicesApi.class);
        Call<List<MyService>> requestListCall = getMyServicesApi.execute(userId, accessToken, receptionMyService);

        requestListCall.enqueue(new Callback<List<MyService>>() {
            @Override
            public void onResponse(@NonNull Call<List<MyService>> call, @NonNull Response<List<MyService>> response) {
                if (response.isSuccessful())
                    getMyServicesCallback.onResponse(true, null, response.body());
                else {

                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getMyServicesCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MyService>> call, @NonNull Throwable t) {

            }
        });
    }
}
