package ir.shirazservice.expert.dialog.socialnetworks;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import ir.shirazservice.expert.R;


public class SocialNetworkDialog extends Dialog {
    private final SocialNetworkOnClickListener listener;
    private LinearLayout layoutSocialInstagram;
    private LinearLayout layoutSocialAparat;
    private LinearLayout layoutSocialTelegram;

    private AppCompatImageView imageSocialInstagram;
    private AppCompatImageView imageSocialAparat;
    private AppCompatImageView imageSocialTelegram;

    private AppCompatTextView textSocialInstagram;
    private AppCompatTextView textSocialApparat;
    private AppCompatTextView textSocialTelegram;

    private AppCompatTextView buttonCloseDialog;


    public SocialNetworkDialog ( @NonNull Context context, SocialNetworkOnClickListener listener ) {
        super( context );
        this.listener = listener;
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView( R.layout.dialog_social );
        findViews( );
        onClickSetting( );
    }

    private void findViews ( ) {

        layoutSocialInstagram = findViewById( R.id.layout_social_instagram );
        layoutSocialAparat = findViewById( R.id.layout_social_apparat );
        layoutSocialTelegram = findViewById( R.id.layout_social_telegram );

        imageSocialInstagram = findViewById( R.id.image_social_instagram );
        imageSocialAparat = findViewById( R.id.image_social_apparat );
        imageSocialTelegram = findViewById( R.id.image_social_telegram );

        textSocialInstagram = findViewById( R.id.text_social_instagram );
        textSocialApparat = findViewById( R.id.text_social_apparat );
        textSocialTelegram = findViewById( R.id.text_social_telegram );

        buttonCloseDialog = findViewById( R.id.button_close_dialog );
    }


    private void onClickSetting ( ) {

        layoutSocialInstagram.setOnClickListener( view -> onInstagram( ) );
        layoutSocialAparat.setOnClickListener( view -> onApparat( ) );
        layoutSocialTelegram.setOnClickListener( view -> onTelegram( ) );

        imageSocialInstagram.setOnClickListener( view -> onInstagram( ) );
        imageSocialAparat.setOnClickListener( view -> onApparat( ) );
        imageSocialTelegram.setOnClickListener( view -> onTelegram( ) );

        textSocialInstagram.setOnClickListener( view -> onInstagram( ) );
        textSocialApparat.setOnClickListener( view -> onApparat( ) );
        textSocialTelegram.setOnClickListener( view -> onTelegram( ) );

        buttonCloseDialog.setOnClickListener( view -> dismiss () );

    }


    private void onInstagram ( ) {
        listener.onInstagram( );
        dismiss( );
    }

    private void onApparat ( ) {
        listener.onApparat( );
        dismiss( );
    }

    private void onTelegram ( ) {
        listener.onTelegram( );
        dismiss( );
    }


}
