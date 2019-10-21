package ir.shirazservice.expert.webservice.workmanpoint;

import androidx.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WorkmanActivityPointController {


    private final WorkmanActivityPointApi.getWorkmanActivityPointCallback getWorkmanMessagesCallback;

    public WorkmanActivityPointController(WorkmanActivityPointApi.getWorkmanActivityPointCallback getWorkmanMessagesCallback) {
        this.getWorkmanMessagesCallback = getWorkmanMessagesCallback;
    }

    public void start(int userId, String accessToken, WorkManPointInput workManPointInput) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        WorkmanActivityPointApi workmanActivityPointApi = retrofit.create(WorkmanActivityPointApi.class);
        Call<WorkManPoint> pointCall = workmanActivityPointApi.execute(userId, accessToken, workManPointInput);
        pointCall.enqueue(new Callback<WorkManPoint>() {
            @Override
            public void onResponse(@NonNull Call<WorkManPoint> call, @NonNull Response<WorkManPoint> response) {
                if (response.isSuccessful())
                    getWorkmanMessagesCallback.onResponse(true, null, response.body());
                else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getWorkmanMessagesCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkManPoint> call, @NonNull Throwable t) {

            }
        });

    }
}
