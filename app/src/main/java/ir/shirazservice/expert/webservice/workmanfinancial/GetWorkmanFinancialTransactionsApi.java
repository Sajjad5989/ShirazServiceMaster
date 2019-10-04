package ir.shirazservice.expert.webservice.workmanfinancial;

import java.util.List;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.ShirazServiceApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetWorkmanFinancialTransactionsApi {



    @Headers({BuildConfig.contentType, BuildConfig.deviceType })
    @POST("workman/getWorkmanFinancialTransactions")
    Call<List<WorkmanFinancialTransaction>> execute(@Header(ShirazServiceApi.USER_ID) int userId,
                                                    @Header(ShirazServiceApi.ACCESS_TOKEN) String accessToken,
                                                    @Body WorkmanFinancialTransactionReq workmanFinancialTransactionReq);


    interface getWorkmanFinancialTransactionsCallback{
        void onResponse (boolean successful, ErrorResponseSimple errorResponse, List<WorkmanFinancialTransaction> response );

        void onFailure ( String cause );
    }


}
