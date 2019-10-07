package ir.shirazservice.expert.webservice.getservicemaninfo;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetServiceManInfoApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getServiceManInfo")
    Call<ServiceMan> execute(@Header(BuildConfig.userId) int userId,
                             @Header(BuildConfig.accessToken) String accessToken,
                             @Body ServiceManSavedInfo serviceManFirstInfo);

    interface GetServiceManInfoCallback {
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, ServiceMan response );

        void onFailure ( String cause );
    }
}
