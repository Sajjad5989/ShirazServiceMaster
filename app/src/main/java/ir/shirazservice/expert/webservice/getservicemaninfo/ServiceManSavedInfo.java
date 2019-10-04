package ir.shirazservice.expert.webservice.getservicemaninfo;

import com.squareup.moshi.Json;

public class ServiceManSavedInfo {

    @Json(name = "username")
    private String username;
    @Json(name = "pass")
    private String pass;
    @Json(name = "applicationVersion")
    private String applicationVersion;
    @Json(name = "sdkVersion")
    private String sdkVersion;
    @Json(name = "deviceName")
    private String deviceName;
    @Json(name = "deviceModel")
    private String deviceModel;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }



}
