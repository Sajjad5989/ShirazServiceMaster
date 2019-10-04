package ir.shirazservice.expert.webservice.pickrequestbyserviceman;

import android.content.Context;
import android.support.annotation.NonNull;

import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.shirazserviceapi.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PickRequestByServiceManController {

    private final Context context;
    private final PickRequestByServiceManApi.pickRequestByServiceManCallback pickRequestByServiceManCallback;

    public PickRequestByServiceManController(Context context,
                                             PickRequestByServiceManApi.pickRequestByServiceManCallback pickRequestByServiceManCallback
    ) {
        this.context = context;
        this.pickRequestByServiceManCallback = pickRequestByServiceManCallback;
    }

    public void start(int requestId) {

        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        RequestByServiceManReq requestByServiceManReq = new RequestByServiceManReq();
        requestByServiceManReq.setServicemanId(serviceMan.getServicemanId());
        requestByServiceManReq.setRequestId(requestId);

        Retrofit retrofit = AppController.getInstance().getRetrofitObject();
        PickRequestByServiceManApi deleteTokenKeyApi = retrofit.create(PickRequestByServiceManApi.class);
        Call<RequestByServiceMan> requestByServiceManCall = deleteTokenKeyApi.execute(serviceMan.getServicemanId(),
                serviceMan.getAccessToken(), requestByServiceManReq);
        requestByServiceManCall.enqueue(new Callback<RequestByServiceMan>() {
            @Override
            public void onResponse(@NonNull Call<RequestByServiceMan> call, @NonNull Response<RequestByServiceMan> response) {
                if (response.isSuccessful())
                    pickRequestByServiceManCallback.onResponse(true, null, response.body());
                else {
                    //Todo
                    //بایستی خطا رو به تابع مربوطه پاس بدم
                    ErrorResponseSimple err = new ErrorResponseSimple(response.code(), response.message());
                    pickRequestByServiceManCallback.onResponse(false, err, null);
                }

            }

            @Override
            public void onFailure(@NonNull Call<RequestByServiceMan> call,@NonNull  Throwable t) {

            }
        });
    }

}
