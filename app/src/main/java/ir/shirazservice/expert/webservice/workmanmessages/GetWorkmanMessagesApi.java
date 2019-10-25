package ir.shirazservice.expert.webservice.workmanmessages;

import java.util.List;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetWorkmanMessagesApi {


    @Headers({BuildConfig.contentType, BuildConfig.deviceType})
    @POST("workman/getWorkmanMessages")
    Call<List<WorkmanMessages>> execute(@Header(BuildConfig.userId) int userId,
                                        @Header(BuildConfig.accessToken) String accessToken,
                                        @Body WorkmanInput workmanInput);


    interface getWorkmanMessagesCallback {
        void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<WorkmanMessages> response);

        void onFailure(String cause);
    }
}
