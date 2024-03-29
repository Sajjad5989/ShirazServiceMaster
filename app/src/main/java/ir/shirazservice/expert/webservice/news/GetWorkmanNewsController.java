package ir.shirazservice.expert.webservice.news;

import java.util.List;

import androidx.annotation.NonNull;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import ir.shirazservice.expert.webservice.shirazserviceapi.GeneralIdsInput;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetWorkmanNewsController {

    private final GetWorkmanNewsApi.getWorkmanNewsCallback getWorkmanNewsCallback;

    public GetWorkmanNewsController(GetWorkmanNewsApi.getWorkmanNewsCallback getWorkmanNewsCallback)
    {
        this.getWorkmanNewsCallback = getWorkmanNewsCallback;
    }

    public void start(int userId, String  accessToken, GeneralIdsInput generalIdsInput)
    {
        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        GetWorkmanNewsApi getWorkmanNewsApi = retrofit.create(GetWorkmanNewsApi.class);
        Call<List<WorkmanNews>> getWorkmanNewsCall = getWorkmanNewsApi.execute
                (userId,accessToken,generalIdsInput);

        getWorkmanNewsCall.enqueue(new Callback<List<WorkmanNews>>() {
            @Override
            public void onResponse(@NonNull Call<List<WorkmanNews>> call,@NonNull Response<List<WorkmanNews>> response) {
                if (response.isSuccessful())
                {
                    getWorkmanNewsCallback.onResponse(true,null,response.body());
                }
                else
                {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getWorkmanNewsCallback.onResponse(false,err,null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WorkmanNews>> call,@NonNull  Throwable t) {

            }
        });
    }

}
