package ir.shirazservice.expert.webservice.getbaseinfo;

import java.util.Objects;

import androidx.annotation.NonNull;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class GetBaseInfoOfAppController {


    private final GetBaseInfoOfAppApi.GetBaseInfoOfAppCallback callback;


    public GetBaseInfoOfAppController(GetBaseInfoOfAppApi.GetBaseInfoOfAppCallback callback) {
        this.callback = callback;
    }

    public void start(int userId, String accessToken) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetBaseInfoOfAppApi getBaseInfoOfAppApi = retrofit.create(GetBaseInfoOfAppApi.class);
        Call<BaseInfoOfApp> authenticationSuccessCall = getBaseInfoOfAppApi.execute(userId, accessToken);
        authenticationSuccessCall.enqueue(new Callback<BaseInfoOfApp>() {
            @Override
            public void onResponse(@NonNull Call<BaseInfoOfApp> call, @NonNull Response<BaseInfoOfApp> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(true, null, response.body());
                } else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    callback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseInfoOfApp> call,@NonNull  Throwable t) {
                callback.onFailure(Objects.requireNonNull(t.getCause()).getMessage());
            }
        });
    }


}
