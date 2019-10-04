package ir.shirazservice.expert.webservice.deletetokey;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DeleteTokenKeyApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("common/deleteTokenKey")
    Call<SuccessResponse> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                  @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                                  @Body PersonReception personReception);


    interface deleteTokenKeyCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, SuccessResponse response );

        void onFailure ( String cause );
    }

}
