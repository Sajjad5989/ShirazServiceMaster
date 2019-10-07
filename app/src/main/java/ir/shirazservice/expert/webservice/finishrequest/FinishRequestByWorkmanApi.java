package ir.shirazservice.expert.webservice.finishrequest;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FinishRequestByWorkmanApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType})
    @POST("request/finishRequestByWorkman")
    Call<SuccessIdResponse> execute(@Header(BuildConfig.userId) int userId,
                                    @Header(BuildConfig.accessToken) String accessToken,
                                    @Body FinishRequestByWorkmanRequest finishRequestByWorkmanRequest);


    interface finishRequestByWorkmanCallback {
        void onResponse(boolean successful, ErrorResponseSimple errorResponse, SuccessIdResponse response);

        void onFailure(String cause);
    }

}
