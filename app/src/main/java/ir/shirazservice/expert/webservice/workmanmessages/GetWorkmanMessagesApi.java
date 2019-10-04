package ir.shirazservice.expert.webservice.workmanmessages;

import java.util.List;

import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetWorkmanMessagesApi {


    @Headers({ShirazServiceApi.CONTENT_TYPE, ShirazServiceApi.DEVICE_TYPE})
    @POST("workman/getWorkmanMessages")
    Call<List<WorkmanMessages>> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                        @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken);


    interface getWorkmanMessagesCallback {
        void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<WorkmanMessages> response);

        void onFailure(String cause);
    }
}
