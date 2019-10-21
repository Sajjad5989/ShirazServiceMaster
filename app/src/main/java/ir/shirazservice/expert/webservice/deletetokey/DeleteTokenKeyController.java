package ir.shirazservice.expert.webservice.deletetokey;

import android.content.Context;
import androidx.annotation.NonNull;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeleteTokenKeyController {

    private final Context context;
    private final DeleteTokenKeyApi.deleteTokenKeyCallback deleteTokenKeyCallback;

    public DeleteTokenKeyController(Context context, DeleteTokenKeyApi.deleteTokenKeyCallback deleteTokenKeyCallback) {
        this.context = context;
        this.deleteTokenKeyCallback = deleteTokenKeyCallback;
    }

    public void start() {
        PersonReception personReception = new PersonReception();
        personReception.setPersonId(2);

        int userId = GeneralPreferences.getInstance(context).getInt(BuildConfig.userId);
        String token = GeneralPreferences.getInstance(context).getString(BuildConfig.accessToken);

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        DeleteTokenKeyApi deleteTokenKeyApi = retrofit.create(DeleteTokenKeyApi.class);
        Call<SuccessResponse> requestListCall = deleteTokenKeyApi.execute(userId, token, personReception);
        requestListCall.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call,@NonNull  Response<SuccessResponse> response) {
                if (response.isSuccessful())
                    deleteTokenKeyCallback.onResponse(true, null, response.body());
                else {
                    //Todo
                    //بایستی خطا رو به تابع مربوطه پاس بدم
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    deleteTokenKeyCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call,@NonNull  Throwable t) {

            }
        });
    }

}
