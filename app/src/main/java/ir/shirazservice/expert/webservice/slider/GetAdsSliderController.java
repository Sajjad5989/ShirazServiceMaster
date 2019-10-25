package ir.shirazservice.expert.webservice.slider;

import java.util.List;

import androidx.annotation.NonNull;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import ir.shirazservice.expert.webservice.shirazserviceapi.GeneralIdsInput;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetAdsSliderController {

    private final GetAdsSliderApi.getAdsSliderCallback getAdsSliderCallback;

    public GetAdsSliderController(GetAdsSliderApi.getAdsSliderCallback getAdsSliderCallback) {
        this.getAdsSliderCallback = getAdsSliderCallback;
    }

    public void start(int userId, String accessToken, GeneralIdsInput generalIdsInput) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetAdsSliderApi getAdsSliderApi = retrofit.create(GetAdsSliderApi.class);

        Call<List<AdsSlider>> adsSliderCall = getAdsSliderApi.execute(userId, accessToken,generalIdsInput);

        adsSliderCall.enqueue(new Callback<List<AdsSlider>>() {
            @Override
            public void onResponse(@NonNull Call<List<AdsSlider>> call, @NonNull Response<List<AdsSlider>> response) {
                if (response.isSuccessful()) {
                    getAdsSliderCallback.onResponse(true, null, response.body());
                } else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getAdsSliderCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AdsSlider>> call, @NonNull Throwable t) {

            }
        });
    }


}
