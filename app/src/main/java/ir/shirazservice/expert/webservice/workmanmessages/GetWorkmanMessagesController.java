package ir.shirazservice.expert.webservice.workmanmessages;

import java.util.List;

import androidx.annotation.NonNull;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetWorkmanMessagesController {

    private final GetWorkmanMessagesApi.getWorkmanMessagesCallback getWorkmanMessagesCallback;

    public GetWorkmanMessagesController(GetWorkmanMessagesApi.getWorkmanMessagesCallback getWorkmanMessagesCallback) {
        this.getWorkmanMessagesCallback = getWorkmanMessagesCallback;
    }

    public void start(int userId, String accessToken,WorkmanInput workmanInput) {
        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetWorkmanMessagesApi getWorkmanMessagesApi = retrofit.create(GetWorkmanMessagesApi.class);
        Call<List<WorkmanMessages>> listCall = getWorkmanMessagesApi.execute(userId, accessToken,workmanInput);
        listCall.enqueue(new Callback<List<WorkmanMessages>>() {
            @Override
            public void onResponse(@NonNull Call<List<WorkmanMessages>> call, @NonNull Response<List<WorkmanMessages>> response) {
                if (response.isSuccessful())
                    getWorkmanMessagesCallback.onResponse(true, null, response.body());
                else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getWorkmanMessagesCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WorkmanMessages>> call, @NonNull Throwable t) {

            }
        });
    }
}
