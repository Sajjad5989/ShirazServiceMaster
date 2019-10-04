package ir.shirazservice.expert.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class OnlineCheck {


    private static OnlineCheck instance = null;

    private final Context context;

    private OnlineCheck(Context context) {
        this.context = context;
    }


    public static OnlineCheck getInstance(Context context)
    {
        if (instance==null)
            instance= new OnlineCheck(context);

        return instance;
    }

    public boolean isOnline()
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }



}
