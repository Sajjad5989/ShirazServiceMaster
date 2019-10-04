package ir.shirazservice.expert.webservice.getgiftchargeformula;

import java.util.List;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetGiftChargeFormulaApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getGiftChargeFormula")
    Call<List<GiftChargeFormula>> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                          @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken);


    interface getGiftChargeFormulaCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, List<GiftChargeFormula> response );

        void onFailure ( String cause );
    }



}
