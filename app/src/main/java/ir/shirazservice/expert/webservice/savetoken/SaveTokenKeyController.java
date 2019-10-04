package ir.shirazservice.expert.webservice.savetoken;

import android.support.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SaveTokenKeyController {


    private final SaveTokenKeyApi.saveTokenKeyCallback saveTokenKeyCallback;

    public SaveTokenKeyController(SaveTokenKeyApi.saveTokenKeyCallback saveTokenKeyCallback) {
        this.saveTokenKeyCallback = saveTokenKeyCallback;
    }

    public void start(int userId, String accessToken, SaveTokenKey saveTokenKey) {
        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        SaveTokenKeyApi saveTokenKeyApi = retrofit.create(SaveTokenKeyApi.class);
        Call<SaveTokenKeyResponse> saveTokenKeyResponseCall = saveTokenKeyApi.execute(userId, accessToken, saveTokenKey);
        saveTokenKeyResponseCall.enqueue(new Callback<SaveTokenKeyResponse>() {
            @Override
            public void onResponse(@NonNull Call<SaveTokenKeyResponse> call, @NonNull Response<SaveTokenKeyResponse> response) {
                if (response.isSuccessful())
                    saveTokenKeyCallback.onResponse(true, null, response.body());
                else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    saveTokenKeyCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SaveTokenKeyResponse> call, @NonNull Throwable t) {

            }
        });
    }

}
