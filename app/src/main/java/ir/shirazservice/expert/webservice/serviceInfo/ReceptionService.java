package ir.shirazservice.expert.webservice.serviceInfo;

import java.io.Serializable;

public class ReceptionService implements Serializable {
    private int serviceId;

    public ReceptionService() {
    }

    public ReceptionService(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
