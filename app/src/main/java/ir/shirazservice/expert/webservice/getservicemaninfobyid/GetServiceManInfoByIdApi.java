package ir.shirazservice.expert.webservice.getservicemaninfobyid;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetServiceManInfoByIdApi {


    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getServiceManInfoById")
    Call<ServiceManInfoDetail> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                       @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                                       @Body ServiceManInfoById servicemaninfobyid);


    interface GetServiceManInfoByIdCallback {
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, ServiceManInfoDetail response );

        void onFailure ( String cause );
    }


}
