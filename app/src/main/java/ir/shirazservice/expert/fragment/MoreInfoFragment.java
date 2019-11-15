package ir.shirazservice.expert.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.ProfileActivity;
import ir.shirazservice.expert.dialog.YesNoDialog;
import ir.shirazservice.expert.dialog.socialnetworks.SocialNetworkDialog;
import ir.shirazservice.expert.dialog.socialnetworks.SocialNetworkOnClickListener;
import ir.shirazservice.expert.interfaces.IInternetController;
import ir.shirazservice.expert.packedactivity.WorkmanMessageActivity;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.OnlineCheck;
import ir.shirazservice.expert.webservice.getbaseinfo.BaseInfoOfApp;

import static ir.shirazservice.expert.utils.APP.context;
import static ir.shirazservice.expert.utils.APP.currentActivity;

public class MoreInfoFragment extends Fragment implements IInternetController {


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


    public static MoreInfoFragment newInstance() {
        return new MoreInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_more_info, container, false);
        ButterKnife.bind(this, view);
        currentActivity = getActivity();
        assert container != null;
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public boolean isOnline() {
        return OnlineCheck.getInstance(getActivity()).isOnline();
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
                goToWebUrl(getString(R.string.link_shz_terms));
                break;
            case R.id.tv_view_about_us:
                goToWebUrl(getString(R.string.link_shz_aboutus));
                break;
            case R.id.tv_view_contact_us:
                CallSupportTeam();
                break;
            case R.id.tv_view_consult_us:
                CallConsultTeam();
                break;
            case R.id.tv_view_follow_us:
                openSocialNetWork();
                break;
            case R.id.tv_view_exit:
                ExitApp();
                break;
        }
    }

    private void openProfile() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }

    private void openMessage() {
        Intent intent = new Intent(getActivity(), WorkmanMessageActivity.class);
        startActivity(intent);
    }

    private void openSocialNetWork() {
        SocialNetworkDialog socialNetworkDialog = new SocialNetworkDialog(getActivity(), new SocialNetworkOnClickListener() {
            @Override
            public void onInstagram() {
                goToWebUrl(getString(R.string.link_shz_insta));
            }

            @Override
            public void onApparat() {
                goToWebUrl(getString(R.string.link_shz_aparat));
            }

            @Override
            public void onTelegram() {
                goToWebUrl(getString(R.string.link_shz_telegram));
            }
        });

        Objects.requireNonNull(socialNetworkDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        socialNetworkDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        socialNetworkDialog.setCancelable(false);
        socialNetworkDialog.show();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.9);
        socialNetworkDialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

    }

    private void CallSupportTeam() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + getSupportTeamPhone().trim()));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }

    private void CallConsultTeam() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + getConsultTeamPhone().trim()));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }

    private String getSupportTeamPhone() {
       return getResources().getString(R.string.text_contact_us);
//        BaseInfoOfApp baseInfoOfApp = GeneralPreferences.getInstance(context).getBaseInfoOfApp();
//        return baseInfoOfApp.getSupportPhone();
    }
    private String getConsultTeamPhone() {
       return getResources().getString(R.string.text_consult_us);
//        BaseInfoOfApp baseInfoOfApp = GeneralPreferences.getInstance(context).getBaseInfoOfApp();
//        return baseInfoOfApp.getSupportPhone();
    }



    private void ExitApp() {

        String msgExit = getResources().getString(R.string.text_exit_app) +
                getResources().getString(R.string.text_exit_app_confirmation);
        YesNoDialog yesNoDialog = new YesNoDialog(getActivity(), getString(R.string.text_exit_title)
                , msgExit, done -> {
                    if (done) {
                        GeneralPreferences.getInstance(context).remove(BuildConfig.userName);
                        GeneralPreferences.getInstance(context).remove(BuildConfig.userPass);
                        APP.killApp();
                    }
                });


        Objects.requireNonNull(yesNoDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        yesNoDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bg));
        yesNoDialog.setCancelable(false);
        yesNoDialog.show();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.9);
        yesNoDialog.getWindow().setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT);

    }

    private void goToWebUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}

