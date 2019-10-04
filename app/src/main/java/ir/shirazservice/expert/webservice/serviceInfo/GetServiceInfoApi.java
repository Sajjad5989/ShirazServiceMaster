package ir.shirazservice.expert.webservice.serviceInfo;


import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetServiceInfoApi {


    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("service/getServiceInfo")
    Call<ServiceInfo> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                              @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                              @Body ReceptionService receptionService);

    interface getServiceInfoCallBack{
        void onResponse(boolean successful, ErrorResponseSimple errorResponse,ServiceInfo response);
        void onFailure ( String cause );
    }


}
