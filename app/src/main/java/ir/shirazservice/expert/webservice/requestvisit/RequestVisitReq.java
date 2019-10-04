package ir.shirazservice.expert.webservice.requestvisit;

public class RequestVisitReq {

    private int requestId;
    private int workmanId;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getWorkmanId() {
        return workmanId;
    }

    public void setWorkmanId(int workmanId) {
        this.workmanId = workmanId;
    }
}
