package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.adapter.WorkManMessageAdapter;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.internetutils.ConnectionInternetDialog;
import ir.shirazservice.expert.internetutils.InternetConnectionListener;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.workmanmessages.GetWorkmanMessagesApi;
import ir.shirazservice.expert.webservice.workmanmessages.GetWorkmanMessagesController;
import ir.shirazservice.expert.webservice.workmanmessages.WorkmanMessages;

import static ir.shirazservice.expert.utils.APP.context;

public class WorkManMessageFragment extends Fragment implements IInternetController {


    @BindView(R.id.const_waiting_main_fragment)
    ConstraintLayout constWaitingMainFragment;
    @BindView(R.id.const_not_found_info)
    ConstraintLayout constNotFoundInfo;
    @BindView(R.id.recycler_message)
    RecyclerView recyclerMessage;


    private List<WorkmanMessages> workmanMessages;
    private final GetWorkmanMessagesApi.getWorkmanMessagesCallback getWorkmanMessagesCallback =
            new GetWorkmanMessagesApi.getWorkmanMessagesCallback() {
                @Override
                public void onResponse(boolean successful, ErrorResponseSimple errorResponse, List<WorkmanMessages> response) {
                    if (successful)
                        workmanMessages = response;
                    else
                        workmanMessages = new ArrayList<>();

                    fillMessageRecyclerView();
                }

                @Override
                public void onFailure(String cause) {
                    workmanMessages = new ArrayList<>();

                    fillMessageRecyclerView();
                }
            };

    public static WorkManMessageFragment newInstance() {
        return new WorkManMessageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workman_message, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showHideWaitingProgress(false);
        loadMessage();

    }

    private void loadMessage() {

        if (!isOnline())
            openInternetCheckingDialog();

        ServiceMan serviceMan = GeneralPreferences.getInstance(context).getServiceManInfo();

        GetWorkmanMessagesController getWorkmanMessagesController = new
                GetWorkmanMessagesController(getWorkmanMessagesCallback);

        getWorkmanMessagesController.start(serviceMan.getServicemanId(), serviceMan.getAccessToken());
    }

    private void fillMessageRecyclerView() {
        if (workmanMessages != null) {
            WorkManMessageAdapter workManMessageAdapter = new WorkManMessageAdapter(workmanMessages);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
            recyclerMessage.setLayoutManager(gridLayoutManager);
            recyclerMessage.setAdapter(workManMessageAdapter);
        }
        showHideWaitingProgress(true);
    }

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(getActivity()).isOnline();
    }


    private void showHideWaitingProgress(boolean hide) {
        constWaitingMainFragment.setVisibility(hide ? View.GONE : View.VISIBLE);
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
                loadMessage();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        dialog.show();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.8);
        dialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }


    private void showNotFoundInfoLayout() {

        showHideWaitingProgress(true);
        constNotFoundInfo.setVisibility((workmanMessages == null) ? View.VISIBLE : View.GONE);
    }


}
