package ir.shirazservice.expert.webservice.requestlist;

import java.util.List;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequestListApi {


    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("request/getRequestsList")
    Call<List<Request>> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                                @Body RequestListInputs listInputs);


    interface GetRequestListCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, List<Request> response );

        void onFailure ( String cause );
    }
}
