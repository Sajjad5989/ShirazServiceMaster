package ir.shirazservice.expert.webservice.workmanpoint;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WorkmanActivityPointApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType})
    @POST("workman/getWorkmanActivityPoint")
    Call<WorkManPoint>  execute(@Header(BuildConfig.userId) int userId,
                                @Header(BuildConfig.accessToken) String accessToken,
                                @Body WorkManPointInput workManPointInput);

    interface getWorkmanActivityPointCallback{
        void onResponse(boolean successful, ErrorResponseSimple errorResponse, WorkManPoint response);

        void onFailure(String cause);
    }

}
