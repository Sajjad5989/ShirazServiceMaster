package ir.shirazservice.expert.webservice.pickrequestbyserviceman;

public class RequestByServiceManReq {


    private int requestId;
    private int servicemanId;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getServicemanId() {
        return servicemanId;
    }

    public void setServicemanId(int servicemanId) {
        this.servicemanId = servicemanId;
    }
}
