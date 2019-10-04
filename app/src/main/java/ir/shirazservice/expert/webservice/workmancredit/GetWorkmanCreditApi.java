package ir.shirazservice.expert.webservice.workmancredit;


import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetWorkmanCreditApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getWorkmanCredit")
    Call<WorkmanCredit> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                  @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                                  @Body WorkmanCreditReq workmanCreditReq);


    interface getWorkmanCreditCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, WorkmanCredit response );

        void onFailure ( String cause );
    }

}
