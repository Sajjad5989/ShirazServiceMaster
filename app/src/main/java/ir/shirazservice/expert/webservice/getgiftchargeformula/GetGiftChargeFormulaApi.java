package ir.shirazservice.expert.webservice.getgiftchargeformula;

import java.util.List;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.GeneralIdsInput;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetGiftChargeFormulaApi {

    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getGiftChargeFormula")
    Call<List<GiftChargeFormula>> execute(@Header(BuildConfig.userId) int userId,
                                          @Header(BuildConfig.accessToken) String accessToken,
                                          @Body GeneralIdsInput generalIdsInput);


    interface getGiftChargeFormulaCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, List<GiftChargeFormula> response );

        void onFailure ( String cause );
    }



}
