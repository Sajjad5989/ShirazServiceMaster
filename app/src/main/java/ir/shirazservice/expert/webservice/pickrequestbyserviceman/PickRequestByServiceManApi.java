package ir.shirazservice.expert.webservice.pickrequestbyserviceman;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PickRequestByServiceManApi {



    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/pickRequestByServiceMan")
    Call<RequestByServiceMan> execute(@Header(BuildConfig.userId) int userId,
                                  @Header(BuildConfig.accessToken) String accessToken,
                                  @Body RequestByServiceManReq requestByWorkManReq);


    interface pickRequestByServiceManCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, RequestByServiceMan response );

        void onFailure ( String cause );
    }




}
