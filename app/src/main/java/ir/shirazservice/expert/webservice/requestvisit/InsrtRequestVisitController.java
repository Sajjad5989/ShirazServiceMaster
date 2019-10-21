package ir.shirazservice.expert.webservice.requestvisit;

import androidx.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InsrtRequestVisitController {

    private final InsrtRequestVisitApi.insrtRequestVisitCallback insrtRequestVisitCallback;

    public InsrtRequestVisitController(InsrtRequestVisitApi.insrtRequestVisitCallback insrtRequestVisitCallback) {
        this.insrtRequestVisitCallback = insrtRequestVisitCallback;
    }

    public void start(int userId, String accessToken, RequestVisitReq requestVisitReq) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        InsrtRequestVisitApi insrtRequestVisitApi = retrofit.create(InsrtRequestVisitApi.class);
        Call<RequestVisit> requestListCall = insrtRequestVisitApi.execute(userId, accessToken, requestVisitReq);
        requestListCall.enqueue(new Callback<RequestVisit>() {
            @Override
            public void onResponse(@NonNull Call<RequestVisit> call, @NonNull Response<RequestVisit> response) {
                if (response.isSuccessful())
                    insrtRequestVisitCallback.onResponse(true, null, response.body());
                else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    insrtRequestVisitCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestVisit> call, @NonNull Throwable t) {

            }
        });
    }

}
