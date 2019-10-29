package ir.shirazservice.expert.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.webservice.getbaseinfo.BaseInfoOfApp;
import ir.shirazservice.expert.webservice.getservicemaninfo.ServiceMan;

public class GeneralPreferences {
    private static GeneralPreferences instance = null;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final Context context;

    private GeneralPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("shirazservice_general_pref_file", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static GeneralPreferences getInstance(Context context) {
        if (instance == null)
            instance = new GeneralPreferences(context);
        return instance;
    }

    public void remove(String tag) {
        editor.remove(tag).commit();
    }

    public void removeShared() {
        editor.remove("shirazservice_general_pref_file").commit();
    }

    public void putBoolean(String title, boolean value) {
        remove(title);
        editor.putBoolean(title, value);
        editor.apply();
    }

    public boolean getBoolean(String title) {
        return sharedPreferences.getBoolean(title, false);
    }


    public void putString(String title, String value) {
        remove(title);
        editor.putString(title, value);
        editor.apply();
    }

    public String getString(String title) {
        return sharedPreferences.getString(title, null);
    }

    public void putInt(String title, int value) {
        remove(title);
        editor.putInt(title, value);
        editor.apply();
    }

    public int getInt(String title) {
        return sharedPreferences.getInt(title, 0);
    }


    public void putServiceManInfo(ServiceMan serviceMan) {

        String tag = context.getString(R.string.text_service_man);
        remove(tag);
        editor.putString(tag, new Gson().toJson(serviceMan, ServiceMan.class));
        editor.apply();

    }


    public void putBaseInfoOfApp(BaseInfoOfApp baseInfoOfApp) {

        String tag = context.getString(R.string.text_base_info);
        remove(tag);
        editor.putString(tag, new Gson().toJson(baseInfoOfApp, BaseInfoOfApp.class));
        editor.apply();

    }

    public BaseInfoOfApp getBaseInfoOfApp() {
        String serviceManString = sharedPreferences.getString(context.getString(R.string.text_base_info), null);
        if (serviceManString == null)
            return new BaseInfoOfApp();

        Gson gson = new Gson();
        return gson.fromJson(serviceManString, BaseInfoOfApp.class);

    }


    private void putServiceManString(String serviceMan) {
        editor.remove(serviceMan).commit();
        editor.putString(context.getString(R.string.text_service_man), serviceMan);
        editor.apply();
    }

    public ServiceMan getServiceManInfo() {
        String serviceManString = sharedPreferences.getString(context.getString(R.string.text_service_man), null);
        if (serviceManString == null)
            return new ServiceMan();


        Gson gson = new Gson();
        return gson.fromJson(serviceManString, ServiceMan.class);

    }

}
