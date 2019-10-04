package ir.shirazservice.expert.webservice.getbaseinfo;


import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetBaseInfoOfAppApi {


    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("common/getBaseInfoOfApp")
    Call<BaseInfoOfApp> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken);


    interface GetBaseInfoOfAppCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, BaseInfoOfApp response );

        void onFailure ( String cause );
    }

}
