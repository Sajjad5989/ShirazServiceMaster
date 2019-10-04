package ir.shirazservice.expert.webservice.finishrequest;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FinishRequestByWorkmanApi {

    @Headers({ShirazServiceApi.CONTENT_TYPE, ShirazServiceApi.DEVICE_TYPE})
    @POST("request/finishRequestByWorkman")
    Call<SuccessIdResponse> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                    @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                                    @Body FinishRequestByWorkmanRequest finishRequestByWorkmanRequest);


    interface finishRequestByWorkmanCallback {
        void onResponse(boolean successful, ErrorResponseSimple errorResponse, SuccessIdResponse response);

        void onFailure(String cause);
    }

}
