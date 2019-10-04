package ir.shirazservice.expert.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.activity.MainActivity;
import ir.shirazservice.expert.fragment.RequestLimitFragment;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private Map< String, String > data;
    private String mod;
    private String body;
    private String title;
    private String servicemanId;
    private String requestId;
    private String url;


    private final String channelId = "ir.shirazservice.expert.firebase.SHZ_EXPERT_CHANNEL";
    private final String channelName = "SHZ_EXPERT_CHANNEL";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if ( remoteMessage.getData( ).size( ) > 0 ) {
            data = remoteMessage.getData( );
            pushNotification( );
        }
    }

    private void pushNotification() {
        tryToFillConstantValues( );

        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.notify);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),sound );
        mediaPlayer.start();

        Intent intent = getDestinationIntent();//new Intent( this, MainActivity.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0 /* Request code */,
                intent, PendingIntent.FLAG_ONE_SHOT );

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( this, channelId )
                .setContentIntent( pendingIntent )
                .setStyle( new NotificationCompat.BigTextStyle( ).bigText( body ) )
                .setDefaults( Notification.DEFAULT_ALL )
                .setPriority( Notification.PRIORITY_HIGH )
                .setWhen( System.currentTimeMillis( ) )
                .setSmallIcon( R.drawable.ic_charge )
                .setTicker( title )
                .setContentTitle( title )
                .setContentText( body )
                .setContentInfo( body )
                .setSound(null);

        NotificationManager notificationManager = ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );



        if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ) {

            Objects.requireNonNull(notificationManager).deleteNotificationChannel( channelId );

//            Uri notification = Uri.parse( SoundProcessor.getSoundPath( mod, getPackageName( ) ) );
            NotificationChannel channel = new NotificationChannel( channelId, channelName, NotificationManager.IMPORTANCE_HIGH );
            channel.setLockscreenVisibility( Notification.VISIBILITY_SECRET );
            channel.setShowBadge( true );
            channel.enableLights( true );
            channel.setLightColor( getResources( ).getColor( R.color.colorAccent ) );
            channel.setDescription( getString( R.string.app_name ) );
            channel.setSound( sound, null );


            notificationManager.createNotificationChannel( channel );
        }

        Objects.requireNonNull(notificationManager).notify( 0 /* ID of notification */, notificationBuilder.build( ) );



    }

    private void tryToFillConstantValues() {
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

    private String getValue(Map<String, String> data, String tag, String emptyValue) {
        try {
            return data.get( tag );
        } catch ( Exception e ) {
            return emptyValue;
        }
    }

    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

    }

    private Intent getDestinationIntent()
    {
        if (mod.toLowerCase().equals("New"))
            return new Intent(this, RequestLimitFragment.class);
        else
            return new Intent(this,MainActivity.class);
    }
}
