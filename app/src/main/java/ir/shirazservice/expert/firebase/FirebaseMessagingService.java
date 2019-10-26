package ir.shirazservice.expert.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import ir.shirazservice.expert.BuildConfig;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.MainActivity;
import ir.shirazservice.expert.activity.ServiceRequestDetailActivity;
import ir.shirazservice.expert.preferences.GeneralPreferences;
import ir.shirazservice.expert.webservice.generalmodels.ErrorResponseSimple;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;
import ir.shirazservice.expert.webservice.savetoken.SaveTokenKey;
import ir.shirazservice.expert.webservice.savetoken.SaveTokenKeyApi;
import ir.shirazservice.expert.webservice.savetoken.SaveTokenKeyController;
import ir.shirazservice.expert.webservice.savetoken.SaveTokenKeyResponse;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private final SaveTokenKeyApi.saveTokenKeyCallback saveTokenKeyCallback = new SaveTokenKeyApi.saveTokenKeyCallback() {
        @Override
        public void onResponse( boolean successful, ErrorResponseSimple errorResponse, SaveTokenKeyResponse response ){

        }

        @Override
        public void onFailure( String cause ){

        }
    };
    private Map< String, String > data;
    private String mod;
    private String body;
    private String title;
    private String servicemanId;
    private String requestId;
    private String url;

    @Override
    public void onMessageReceived( RemoteMessage remoteMessage ){
        if ( remoteMessage.getData().size() > 0 ) {
            data = remoteMessage.getData();
            pushNotification();
        }
    }

    private void pushNotification( ){
        tryToFillConstantValues();

        Uri sound = Uri.parse( "android.resource://" + getPackageName() + "/raw/" + R.raw.notify );
        MediaPlayer mediaPlayer = MediaPlayer.create( getApplicationContext(), sound );
        mediaPlayer.start();

        Intent intent = getDestinationIntent();//new Intent( this, MainActivity.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0 /* Request code */,
                intent, PendingIntent.FLAG_ONE_SHOT );

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( this, BuildConfig.channelId )
                .setContentIntent( pendingIntent )
                .setStyle( new NotificationCompat.BigTextStyle().bigText( body ) )
                .setDefaults( Notification.DEFAULT_ALL )
                .setPriority( Notification.PRIORITY_HIGH )
                .setWhen( System.currentTimeMillis() )
                .setSmallIcon( R.mipmap.ic_launcher )
                .setTicker( title )
                .setContentTitle( title )
                .setContentText( body )
                .setContentInfo( body )
                .setSound( null );

        NotificationManager notificationManager = ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );


        if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ) {

            Objects.requireNonNull( notificationManager ).deleteNotificationChannel( BuildConfig.channelId );

//            Uri notification = Uri.parse( SoundProcessor.getSoundPath( mod, getPackageName( ) ) );
            NotificationChannel channel = new NotificationChannel( BuildConfig.channelId, BuildConfig.channelName, NotificationManager.IMPORTANCE_HIGH );
            channel.setLockscreenVisibility( Notification.VISIBILITY_SECRET );
            channel.setShowBadge( true );
            channel.enableLights( true );
            channel.setLightColor( getResources().getColor( R.color.colorAccent ) );
            channel.setDescription( getString( R.string.app_name ) );
            channel.setSound( sound, null );


            notificationManager.createNotificationChannel( channel );
        }

        Objects.requireNonNull( notificationManager ).notify( 0 /* ID of notification */, notificationBuilder.build() );


    }

    private void tryToFillConstantValues( ){
        mod = getValue( data, "mod", "New" );
        body = getValue( data, "body", "" );
        title = getValue( data, "title", "" );
        servicemanId = getValue( data, "servicemanId", "0" );
        requestId = getValue( data, "requestId", "0" );
        if ( mod.equals( "Ads" ) ) {
            url = getValue( data, "url", "" );
        } else {
            url = "";
        }
    }

    private String getValue( Map< String, String > data, String tag, String emptyValue ){
        try {
            return data.get( tag );
        } catch ( Exception e ) {
            return emptyValue;
        }
    }

    @Override
    public void onNewToken( @NonNull String token ){
        sendRegistrationToServer( token );
    }

    private void sendRegistrationToServer( String token ){
        ServiceMan serviceMan = GeneralPreferences.getInstance( this ).getServiceManInfo();

        if ( serviceMan.getServicemanId() > 0 ) {
            GeneralPreferences.getInstance( this ).remove( "newFirebaseTokenKey" );

            SaveTokenKey saveTokenKey = new SaveTokenKey();
            saveTokenKey.setPersonId( serviceMan.getServicemanId() );
            saveTokenKey.setDeviceTokenKey( token );

            SaveTokenKeyController saveTokenKeyController = new SaveTokenKeyController( saveTokenKeyCallback );
            saveTokenKeyController.start( serviceMan.getServicemanId(), serviceMan.getAccessToken(), saveTokenKey );

        } else {
            GeneralPreferences.getInstance( this ).putString( "newFirebaseTokenKey", token );
        }
    }

    private Intent getDestinationIntent( ){
        if ( mod.toLowerCase().equals( "New" ) ) {
            Intent intent = new Intent( this, ServiceRequestDetailActivity.class );
            Bundle bundle = new Bundle();
            bundle.putInt( getString( R.string.text_bundle_req_id ), Integer.valueOf( requestId ) );
            intent.putExtras( bundle );
            return intent;
        } else
            return new Intent( this, MainActivity.class );
    }


}
