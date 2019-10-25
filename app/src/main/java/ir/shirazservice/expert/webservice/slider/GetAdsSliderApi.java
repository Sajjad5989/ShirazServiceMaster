package ir.shirazservice.expert.webservice.slider;

import java.util.List;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.GeneralIdsInput;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetAdsSliderApi {


    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getAdsSliders")
    Call<List<AdsSlider>> execute(@Header(BuildConfig.userId) int userId,
                                  @Header(BuildConfig.accessToken) String accessToken,
                                  @Body GeneralIdsInput generalIdsInput);


    interface getAdsSliderCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, List<AdsSlider> response );

        void onFailure ( String cause );
    }



}
