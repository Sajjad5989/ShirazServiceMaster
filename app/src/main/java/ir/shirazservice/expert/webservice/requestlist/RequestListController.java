package ir.shirazservice.expert.webservice.requestlist;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestListController {

    private final RequestListApi.GetRequestListCallback getRequestListCallback;
private  Context context;

    public RequestListController(Context context, RequestListApi.GetRequestListCallback getRequestListCallback) {
        this.getRequestListCallback = getRequestListCallback;
        this.context = context;

    }


    public void start(int userId, String accessToken, RequestListInputs listInputs) {

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        RequestListApi requestListApi = retrofit.create(RequestListApi.class);

        Call<List<Request>> requestListCall = requestListApi.execute(userId, accessToken
                , listInputs);
        requestListCall.enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(@NonNull Call<List<Request>> call, @NonNull Response<List<Request>> response) {
                if (response.isSuccessful()) {
                    getRequestListCallback.onResponse(true, null, response.body());
                } else {
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    getRequestListCallback.onResponse(false, err, null);
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Request>> call, @NonNull Throwable t) {
                getRequestListCallback.onFailure(context.getString(R.string.text_unsuccessful_operation));

            }
        });

    }

}
