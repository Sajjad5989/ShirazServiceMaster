package ir.shirazservice.expert.webservice.requestdetails;


import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetRequestDetailsApi {



    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("request/getRequestDetails")
    Call<RequestDetails> execute(@Header(BuildConfig.userId) int userId,
                                 @Header(BuildConfig.accessToken) String accessToken,
                                 @Body RequestDetailsReq requestDetailsReception);


    interface getRequestDetailsCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, RequestDetails response );

        void onFailure ( String cause );
    }

}
