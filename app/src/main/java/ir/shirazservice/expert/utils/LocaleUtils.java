package ir.shirazservice.expert.utils;

import android.app.Application;
import android.content.res.Configuration;
import android.view.ContextThemeWrapper;

import java.util.Locale;

public class LocaleUtils {

    public static final String LAN_PERSIAN = "fa";
    public static final String LAN_ENGLISH = "en";

    private static Locale sLocale;

    public static void setLocale ( Locale locale ) {
        sLocale = locale;
        if ( sLocale != null ) {
            Locale.setDefault( sLocale );
        }
    }

    public static void updateConfig ( ContextThemeWrapper wrapper ) {
        if ( sLocale != null ) {
            Configuration configuration = new Configuration( );
            configuration.setLocale( sLocale );
            wrapper.applyOverrideConfiguration( configuration );
        }
    }

    static void updateConfig ( Application app , Configuration configuration ) {
    }
}