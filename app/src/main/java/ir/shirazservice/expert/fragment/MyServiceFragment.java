package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.adapter.MyServiceAdapter;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.myservice.GetMyServicesApi;
import ir.shirazservice.expert.webservice.myservice.GetMyServicesController;
import ir.shirazservice.expert.webservice.myservice.MyService;
import ir.shirazservice.expert.webservice.myservice.ReceptionMyService;

import static ir.shirazservice.expert.utils.APP.context;
import static ir.shirazservice.expert.utils.APP.currentActivity;

public class MyServiceFragment extends Fragment implements IInternetController {


    private List<MyService> myServices;
    private RecyclerView myServicesListRecycler;
    private ConstraintLayout constWaiting;
    private ConstraintLayout constNotFound;
    private final GetMyServicesApi.getMyServicesCallback getMyServicesCallback = new GetMyServicesApi.getMyServicesCallback() {
        @Override
        public void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<MyService> response) {
            if (successful) {
                myServices = response;
            } else {
                myServices = new ArrayList<>();
            }
            fillMyServiceList();
        }

        @Override
        public void onFailure(String cause) {
            myServices = new ArrayList<>();
            fillMyServiceList();
        }
    };

    public static MyServiceFragment newInstance() {
        return new MyServiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_services, container, false);

        currentActivity = getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        getMyServices();
    }

    private void findViews(View view) {
        myServicesListRecycler = view.findViewById(R.id.my_services_list_recycler);
        constWaiting = view.findViewById(R.id.const_waiting_main_fragment);
        constNotFound = view.findViewById(R.id.const_not_found_info);
    }

    private void fillMyServiceList() {
        MyServiceAdapter myServiceAdapter = new MyServiceAdapter(getActivity(), myServices, (v, position) -> {
            Intent intent = new Intent(getActivity(), ServiceRequestDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.text_bundle_req_id), myServices.get(position).getReqId());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        myServicesListRecycler.setLayoutManager(gridLayoutManager);
        myServicesListRecycler.setAdapter(myServiceAdapter);
        showHideWaitingProgress(true);
        showNotFoundInfoLayout();
    }

    private void getMyServices() {
        if (!isOnline()) {
            openInternetCheckingDialog();
        }

        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        showHideWaitingProgress(false);
        GetMyServicesController getMyServicesController = new GetMyServicesController(getMyServicesCallback);
        getMyServicesController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken(), getValue(serviceMan));
    }

    private ReceptionMyService getValue(ServiceMan serviceMan) {
        ReceptionMyService receptionMyService = new ReceptionMyService();
        receptionMyService.setInsrtDateShamsi("");
        receptionMyService.setServicemanId(serviceMan.getServicemanId());
        receptionMyService.setState(0);
        return receptionMyService;
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(getActivity()).isOnline();
    }


    private void showHideWaitingProgress(boolean hide) {
        constWaiting.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void showNotFoundInfoLayout() {

        constNotFound.setVisibility((myServices == null) ? View.VISIBLE : View.GONE);
    }

    private void openInternetCheckingDialog() {
        ConnectionInternetDialog dialog = new ConnectionInternetDialog(getActivity(), new InternetConnectionListener() {
            @Override
            public void onInternet() {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }

            @Override
            public void onFinish() {
                APP.killApp();
            }

            @Override
            public void OnRetry() {
                getMyServices();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        dialog.show();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.8);
        dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }


}
