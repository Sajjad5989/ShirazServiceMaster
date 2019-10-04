package ir.shirazservice.expert.webservice.shirazserviceapi;

import java.util.concurrent.TimeUnit;

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
                .baseUrl( ShirazServiceApi.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create( ) )
                .client( new OkHttpClient.Builder( )
                        .connectTimeout( ShirazServiceApi.CONNECTION_TIMEOUT, TimeUnit.SECONDS )
                        .writeTimeout( ShirazServiceApi.WRITE_TIMEOUT, TimeUnit.SECONDS )
                        .readTimeout( ShirazServiceApi.READ_TIMEOUT, TimeUnit.SECONDS )

                        .build( ) )
                .build( );
    }
}
