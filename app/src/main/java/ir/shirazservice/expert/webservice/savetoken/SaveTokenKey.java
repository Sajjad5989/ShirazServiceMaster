package ir.shirazservice.expert.webservice.savetoken;

import com.squareup.moshi.Json;

public class SaveTokenKey {

    @Json(name = "personId")
    private int personId;
    @Json(name = "deviceTokenKey")
    private String deviceTokenKey;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getDeviceTokenKey() {
        return deviceTokenKey;
    }

    public void setDeviceTokenKey(String deviceTokenKey) {
        this.deviceTokenKey = deviceTokenKey;
    }
}
