package ir.shirazservice.expert.webservice.savetoken;

public class SaveTokenKey {

    private int personId;
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
