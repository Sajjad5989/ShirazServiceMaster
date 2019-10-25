package ir.shirazservice.expert.webservice.getgiftchargeformula;

import java.util.List;

import androidx.annotation.NonNull;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetGiftChargeFormulaController {

    private final GetGiftChargeFormulaApi.getGiftChargeFormulaCallback getGiftChargeFormulaCallback;

    public GetGiftChargeFormulaController(
            GetGiftChargeFormulaApi.getGiftChargeFormulaCallback getGiftChargeFormulaCallback) {
        this.getGiftChargeFormulaCallback = getGiftChargeFormulaCallback;
    }

    public void start(int userId, String accessToken,GiftChargeInput giftChargeInput) {
        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetGiftChargeFormulaApi getGiftChargeFormulaApi = retrofit.create(GetGiftChargeFormulaApi.class);
        Call<List<GiftChargeFormula>> listCall = getGiftChargeFormulaApi.execute(userId, accessToken,giftChargeInput);
        listCall.enqueue(new Callback<List<GiftChargeFormula>>() {
            @Override
            public void onResponse(@NonNull Call<List<GiftChargeFormula>> call, @NonNull Response<List<GiftChargeFormula>> response) {
                if (response.isSuccessful())
                    getGiftChargeFormulaCallback.onResponse(true, null, response.body());
                else {

                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getGiftChargeFormulaCallback.onResponse(false, err, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GiftChargeFormula>> call, @NonNull Throwable t) {

            }
        });
    }
}
