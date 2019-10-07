package ir.shirazservice.expert.webservice.deletetokey;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DeleteTokenKeyApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("common/deleteTokenKey")
    Call<SuccessResponse> execute(@Header(BuildConfig.userId) int userId,
                                  @Header(BuildConfig.accessToken) String accessToken,
                                  @Body PersonReception personReception);


    interface deleteTokenKeyCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, SuccessResponse response );

        void onFailure ( String cause );
    }

}
