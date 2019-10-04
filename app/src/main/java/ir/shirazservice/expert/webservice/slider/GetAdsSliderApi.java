package ir.shirazservice.expert.webservice.slider;

import java.util.List;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetAdsSliderApi {


    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getAdsSliders")
    Call<List<AdsSlider>> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                 @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken);


    interface getAdsSliderCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, List<AdsSlider> response );

        void onFailure ( String cause );
    }



}
