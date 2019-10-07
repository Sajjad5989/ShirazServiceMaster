package ir.shirazservice.expert.webservice.depositmoney;


import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InsrtDepositMoneyApi {



    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/insrtDepositMoney")
    Call<DepositMoney> execute(@Header(BuildConfig.userId) int userId,
                                  @Header(BuildConfig.accessToken) String accessToken,
                                  @Body DepositMoneyReq depositMoneyReq);


    interface insrtDepositMoneyCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, DepositMoney response );

        void onFailure ( String cause );
    }


}
