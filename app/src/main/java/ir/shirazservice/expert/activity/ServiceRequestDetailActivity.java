package ir.shirazservice.expert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.fragment.OfflineChargeFragment;
import ir.shirazservice.expert.fragment.RequestFullFragment;
import ir.shirazservice.expert.fragment.RequestLimitFragment;
import ir.shirazservice.expert.fragment.ServiceFullFragment;
import ir.shirazservice.expert.fragment.ServiceInfoFragment;
import ir.shirazservice.expert.fragment.ServiceLimitFragment;
import ir.shirazservice.expert.interfaces.IDefault;
import ir.shirazservice.expert.interfaces.IRtl;
import ir.shirazservice.expert.utils.APP;

public class ServiceRequestDetailActivity extends AppCompatActivity implements IRtl, IDefault {


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private int chooseCorrectFragment;
    private int reqId;
    private String toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_request_detail);
        ButterKnife.bind(this);

        OnActivityDefaultSetting();

        getIds();
        openCorrectFragment();

        prepareToolbar();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getIds() {

        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        if (bundle != null) {
            int serviceStatus = bundle.getInt(getString(R.string.text_bundle_service_status));
            reqId = bundle.getInt(getString(R.string.text_bundle_req_id));

            switch (serviceStatus) {
                case 1:
                    chooseCorrectFragment = APP.REQUEST_LIMIT;
                    break;
                case 2:
                case 3:
                case 5:
                case 7:
                    chooseCorrectFragment = APP.SERVICE_LIMIT;
                    break;
                case 4:
                    chooseCorrectFragment = APP.REQUEST_FULL;
                    break;
                case 6:
                    chooseCorrectFragment = APP.SERVICE_FULL;
                    break;
                case 100:
                    chooseCorrectFragment = APP.SERVICE_INFO;
                    break;
                case 101:
                    chooseCorrectFragment = APP.OFFLINE_CHARGE;
                    break;
            }
        }
    }

    private void prepareToolbar() {

        toolbar.setTitle(toolbarTitle);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
    }


    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = ServiceRequestDetailActivity.this;
        APP.setPersianUi();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void OnActivityDefaultSetting() {
        OnPageRight();
    }

    @Override
    public void OnPageRight() {
        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    private void openCorrectFragment() {
        switch (chooseCorrectFragment) {
            case APP.REQUEST_LIMIT:
                toolbarTitle = getString(R.string.text_request_detail_title);
                openRequestLimit();
                break;
            case APP.REQUEST_FULL:
                toolbarTitle = getString(R.string.text_request_detail_title);
                openRequestFull();
                break;
            case APP.SERVICE_LIMIT:
                toolbarTitle = getString(R.string.text_service_detail_title);
                openServicelimit();
                break;
            case APP.SERVICE_FULL:
                toolbarTitle = getString(R.string.text_service_detail_title);
                openServiceFull();
                break;
            case APP.SERVICE_INFO:
                toolbarTitle = getString(R.string.text_service_detail_title);
                openServiceInfo();
                break;
            case APP.OFFLINE_CHARGE:
                toolbarTitle = getString(R.string.text_payment_detail);
                OpenOfflineCharge();
                break;
        }
    }

    private void OpenOfflineCharge() {
        OfflineChargeFragment offlineChargeFragment = OfflineChargeFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, offlineChargeFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRequestLimit() {
        RequestLimitFragment requestLimitFragment = RequestLimitFragment.newInstance(null);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, requestLimitFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRequestFull() {
        RequestFullFragment requestFullFragment = RequestFullFragment.newInstance(reqId);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, requestFullFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openServicelimit() {
        ServiceLimitFragment serviceLimitFragment = ServiceLimitFragment.newInstance(reqId);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceLimitFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openServiceFull() {
        ServiceFullFragment serviceFullFragment = ServiceFullFragment.newInstance(reqId);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceFullFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openServiceInfo() {

        ServiceInfoFragment serviceFullFragment = ServiceInfoFragment.newInstance(reqId);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, serviceFullFragment)
                .addToBackStack(null)
                .commit();
    }


}
