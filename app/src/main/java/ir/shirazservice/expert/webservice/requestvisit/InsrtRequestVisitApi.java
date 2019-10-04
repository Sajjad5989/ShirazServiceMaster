package ir.shirazservice.expert.webservice.requestvisit;


import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InsrtRequestVisitApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("request/insrtRequestVisit")
    Call<RequestVisit> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                  @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                                  @Body RequestVisitReq requestVisitReq);


    interface insrtRequestVisitCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, RequestVisit response );

        void onFailure ( String cause );
    }


}
