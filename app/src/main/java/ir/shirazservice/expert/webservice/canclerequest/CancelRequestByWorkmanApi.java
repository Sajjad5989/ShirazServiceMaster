package ir.shirazservice.expert.webservice.canclerequest;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CancelRequestByWorkmanApi {



    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("request/cancelRequestByWorkman")
    Call<PickupResponse> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                 @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                                 @Body CancelRequestByWorkmanRequest cancelRequestByWorkmanRequest);


    interface cancelRequestByWorkmanCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, PickupResponse response );
        void onFailure ( String cause );
    }



}
