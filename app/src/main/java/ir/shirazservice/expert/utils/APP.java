package ir.shirazservice.expert.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import ir.shirazservice.expert.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class APP extends MultiDexApplication {


    @SuppressLint("StaticFieldLeak")
    public static Context context;
    @SuppressLint("StaticFieldLeak")
    public static Activity currentActivity;
    //    public static final String CURRENCY_SYMBOL = Objects.requireNonNull(currentActivity).getString(R.string.text_currency_symbol);
    public static Typeface defaultFont;

    public static void customToast(String paramString) {
        Display display = currentActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        LinearLayout localLinearLayout = new LinearLayout(context);
        localLinearLayout.setBackground(ContextCompat.getDrawable(APP.currentActivity, R.drawable.bg_toast_custom_primary));
        localLinearLayout.setGravity(1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        localLinearLayout.setLayoutParams(layoutParams);

        TextView localTextView = new TextView(context);
        localTextView.setText(paramString);
        localTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        //localTextView.setTextColor(Color.parseColor("#FFFFFFFF"));
        //localTextView.setTextColor(Color.parseColor(String.valueOf(R.color.colorWhite)));
        localTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
        localTextView.setTextSize(14.0F);
        localTextView.setPadding(16, 1, 16, 1);
        localTextView.setTypeface(defaultFont);
        localLinearLayout.addView(localTextView);
        Toast localToast = new Toast(context);
        localToast.setDuration(Toast.LENGTH_SHORT);
        localToast.setView(localLinearLayout);
        localToast.show();
    }

    public static void killApp() {
        currentActivity.finishAffinity();
        ActivityCompat.finishAffinity(currentActivity);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void setPersianUi() {
        Locale localeNew = new Locale("fa");
        Locale.setDefault(localeNew);

        Resources res = context.getResources();
        Configuration newConfig = new Configuration(res.getConfiguration());
        newConfig.locale = localeNew;
        newConfig.setLayoutDirection(localeNew);
        res.updateConfiguration(newConfig, res.getDisplayMetrics());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newConfig.setLocale(localeNew);
            context.createConfigurationContext(newConfig);
        }

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.locale = localeNew;
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLayoutDirection(localeNew);
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
//
//        String languageToLoad = "fa";
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
//        new Geocoder(currentActivity, new Locale(languageToLoad));
    }

    private void setFonts() {
        FontManager.setDefaultFont(this, "DEFAULT", "fonts/iran_sans.ttf");
        FontManager.setDefaultFont(this, "MONOSPACE", "fonts/iran_sans.ttf");
        FontManager.setDefaultFont(this, "SERIF", "fonts/iran_sans.ttf");
        FontManager.setDefaultFont(this, "SANS_SERIF", "fonts/iran_sans.ttf");

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iran_sans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LocaleUtils.setLocale(new Locale("fa"));
        LocaleUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());

        context = getApplicationContext();

        defaultFont = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans.ttf");

        setFonts();
        setPersianUi();
    }

    @Override
    protected void attachBaseContext(Context base) {
        //https://github.com/dandar3/android-support-multidex/blob/master/README.md
        // https://developer.android.com/studio/build/multidex
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
