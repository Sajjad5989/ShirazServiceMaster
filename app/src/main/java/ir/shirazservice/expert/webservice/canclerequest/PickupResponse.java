package ir.shirazservice.expert.webservice.canclerequest;


import java.io.Serializable;

public class PickupResponse implements Serializable {
    private int requestId;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
