package ir.shirazservice.expert.webservice.canclerequest;


import androidx.annotation.NonNull;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CancelRequestByWorkmanController {

    private final CancelRequestByWorkmanApi.cancelRequestByWorkmanCallback cancelRequestByWorkmanCallback;

    public CancelRequestByWorkmanController(CancelRequestByWorkmanApi.cancelRequestByWorkmanCallback cancelRequestByWorkmanCallback) {
        this.cancelRequestByWorkmanCallback = cancelRequestByWorkmanCallback;

    }

    public void start(int userId, String accessToken, CancelRequestByWorkmanRequest cancelRequestByWorkmanRequest) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        CancelRequestByWorkmanApi cancelRequestByWorkmanApi = retrofit.create(CancelRequestByWorkmanApi.class);
        Call<PickupResponse> pickupResponseCall = cancelRequestByWorkmanApi.execute(userId, accessToken
                , cancelRequestByWorkmanRequest);
        pickupResponseCall.enqueue(new Callback<PickupResponse>() {
            @Override
            public void onResponse(@NonNull Call<PickupResponse> call, @NonNull Response<PickupResponse> response) {
                if (response.isSuccessful()) {
                    cancelRequestByWorkmanCallback.onResponse(true, null, response.body());
                } else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    cancelRequestByWorkmanCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PickupResponse> call, @NonNull Throwable t) {

            }
        });
    }

}
