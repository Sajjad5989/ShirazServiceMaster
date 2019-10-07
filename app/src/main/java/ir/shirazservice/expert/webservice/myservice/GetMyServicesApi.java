package ir.shirazservice.expert.webservice.myservice;

import java.util.List;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetMyServicesApi {


    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getMyServices")
    Call<List<MyService>> execute(@Header(BuildConfig.userId) int userId,
                       @Header(BuildConfig.accessToken) String accessToken,
                       @Body ReceptionMyService receptionMyService);


    interface getMyServicesCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, List<MyService> response );

        void onFailure ( String cause );
    }


}
