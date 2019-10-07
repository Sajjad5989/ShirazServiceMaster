package ir.shirazservice.expert.webservice.shirazserviceapi;

import java.util.concurrent.TimeUnit;

import ir.shirazservice.expert.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppController {
    private static AppController instance = null;

    private AppController ( ) {
    }

    public static AppController getInstance ( ) {
        if ( instance == null )
            instance = new AppController( );
        return instance;
    }

    public Retrofit getRetrofitObject ( ) {
        return new Retrofit.Builder( )
                .baseUrl( BuildConfig.baseUrl )
                .addConverterFactory( GsonConverterFactory.create( ) )
                .client( new OkHttpClient.Builder( )
                        .connectTimeout( BuildConfig.connectionTimeOut, TimeUnit.SECONDS )
                        .writeTimeout( BuildConfig.writeTimeOut, TimeUnit.SECONDS )
                        .readTimeout( BuildConfig.readTimeOut, TimeUnit.SECONDS )

                        .build( ) )
                .build( );
    }
}
