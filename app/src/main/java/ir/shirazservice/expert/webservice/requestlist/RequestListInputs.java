package ir.shirazservice.expert.webservice.requestlist;

public class RequestListInputs {

    private int servicemanId;
    private String serviceTitle;
    private int serviceId;
    private int serviceCatId;
    private int serviceSubCatId;
    private int areaId;
    private int priorityId;
    private int dateFrom;
    private int time;
    private String desc;

    public int getServicemanId() {
        return servicemanId;
    }

    public void setServicemanId(int servicemanId) {
        this.servicemanId = servicemanId;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getServiceCatId() {
        return serviceCatId;
    }

    public void setServiceCatId(int serviceCatId) {
        this.serviceCatId = serviceCatId;
    }

    public int getServiceSubCatId() {
        return serviceSubCatId;
    }

    public void setServiceSubCatId(int serviceSubCatId) {
        this.serviceSubCatId = serviceSubCatId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public int getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(int dateFrom) {
        this.dateFrom = dateFrom;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
