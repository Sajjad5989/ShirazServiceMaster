package ir.shirazservice.expert.webservice.requestinfo;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequestInfoApi {



    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("request/getRequestInfo")
    Call<RequestInfo> execute(@Header(BuildConfig.userId) int userId,
                              @Header(BuildConfig.accessToken) String accessToken,
                              @Body RequestInfoInput listInputs);


    interface GetRequestInfoCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, RequestInfo response );

        void onFailure ( String cause );
    }}
