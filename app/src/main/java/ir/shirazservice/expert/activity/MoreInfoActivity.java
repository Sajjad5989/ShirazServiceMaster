package ir.shirazservice.expert.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.view.Display;
import android.view.View;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.dialog.ExitAppDialog;
import ir.shirazservice.expert.interfaces.IDefault;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.interfaces.IRtl;
import ir.shirazservice.expert.packedactivity.WorkmanMessageActivity;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.webservice.getbaseinfo.BaseInfoOfApp;

import static ir.shirazservice.expert.utils.APP.context;

public class MoreInfoActivity extends AppCompatActivity implements IRtl, IDefault, IInternetController {



    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.tv_view_profile)
    AppCompatTextView tvViewProfile;
    @BindView(R.id.tv_view_message)
    AppCompatTextView tvViewMessage;
    @BindView(R.id.tv_view_rolls)
    AppCompatTextView tvViewRolls;
    @BindView(R.id.tv_view_about_us)
    AppCompatTextView tvViewAboutUs;
    @BindView(R.id.tv_view_contact_us)
    AppCompatTextView tvViewContactUs;
    @BindView(R.id.tv_view_follow_us)
    AppCompatTextView tvViewFollowUs;
    @BindView(R.id.tv_view_exit)
    AppCompatTextView tvViewExit;

    private String supportTeamPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_more_info);
        ButterKnife.bind(this);

        OnActivityDefaultSetting();

        prepareToolbar();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        loadLinks();
    }

    private void prepareToolbar() {

        toolbar.setTitle("اطلاعات بیشتر");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        APP.currentActivity = MoreInfoActivity.this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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

    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(MoreInfoActivity.this).isOnline();
    }


    @OnClick({R.id.tv_view_profile, R.id.tv_view_message, R.id.tv_view_rolls, R.id.tv_view_about_us, R.id.tv_view_contact_us, R.id.tv_view_follow_us, R.id.tv_view_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_view_profile:
                openProfile();
                break;
            case R.id.tv_view_message:
                openMessage();
                break;
            case R.id.tv_view_rolls:
                break;
            case R.id.tv_view_about_us:
                break;
            case R.id.tv_view_contact_us:
                CallSupportTeam();
                break;
            case R.id.tv_view_follow_us:
                break;
            case R.id.tv_view_exit:
                ExitApp();
                break;
        }
    }

    private void openProfile() {
        Intent intent = new Intent(MoreInfoActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openMessage() {
        Intent intent = new Intent(MoreInfoActivity.this, WorkmanMessageActivity.class);
        startActivity(intent);
    }



    private void loadLinks() {
        BaseInfoOfApp baseInfoOfApp = GeneralPreferences.getInstance(this).getBaseInfoOfApp();
        if (baseInfoOfApp != null) {
            supportTeamPhone = baseInfoOfApp.getSupportPhone();
            //baseInfoOfApp.get
        }
    }

    private void CallSupportTeam()
    {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + supportTeamPhone.trim()));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }

    private void ExitApp()
    {

        ExitAppDialog chooseRequestDialog = new ExitAppDialog(MoreInfoActivity.this,
                done -> {
               if (done)
                   APP.killApp();
                });

        Objects.requireNonNull(chooseRequestDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        chooseRequestDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        chooseRequestDialog.setCancelable(false);
        chooseRequestDialog.show();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.9);
        chooseRequestDialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

    }
}

