package ir.shirazservice.expert.webservice.savetoken;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SaveTokenKeyApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("common/saveTokenKey")
    Call<SaveTokenKeyResponse> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                               @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                               @Body SaveTokenKey saveTokenKey);


    interface saveTokenKeyCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, SaveTokenKeyResponse response );
        void onFailure ( String cause );
    }


}
