package ir.shirazservice.expert.webservice.requestdetails;

import java.io.Serializable;

import ir.shirazservice.expert.webservice.requestlist.Request;

public class RequestDetailList  implements Serializable {

    private Request requestLists;

    public RequestDetailList() {
    }

    public RequestDetailList(Request requestLists) {
        this.requestLists = requestLists;
    }

    public Request getRequestLists() {
        return requestLists;
    }

    public void setRequestLists(Request requestLists) {
        this.requestLists = requestLists;
    }
}
