package ir.shirazservice.expert.webservice.getservicemaninfobyid;

import java.io.Serializable;

public class ServiceManInfoById implements Serializable {

    private int workmanId;

    public ServiceManInfoById() {
    }


    public ServiceManInfoById(int workmanId) {
        this.workmanId = workmanId;
    }

    public int getWorkmanId() {
        return workmanId;
    }

    public void setWorkmanId(int workmanId) {
        this.workmanId = workmanId;
    }


}
