package ir.shirazservice.expert.webservice.news;

import java.util.List;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetWorkmanNewsApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getWorkmanNews")
    Call<List<WorkmanNews>> execute(@Header(BuildConfig.userId) int userId,
                                  @Header(BuildConfig.accessToken) String accessToken);


    interface getWorkmanNewsCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, List<WorkmanNews> response );

        void onFailure ( String cause );
    }


}
