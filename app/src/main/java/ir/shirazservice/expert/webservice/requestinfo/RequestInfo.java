package ir.shirazservice.expert.webservice.requestinfo;


import com.squareup.moshi.Json;

public class RequestInfo {

    @Json(name = "requestId")
    private Integer requestId;
    @Json(name = "personId")
    private Integer personId;
    @Json(name = "personName")
    private String personName;
    @Json(name = "serviceId")
    private Integer serviceId;
    @Json(name = "serviceTitle")
    private String serviceTitle;
    @Json(name = "servicePicAddress")
    private String servicePicAddress;
    @Json(name = "catId")
    private Integer catId;
    @Json(name = "catTitle")
    private String catTitle;
    @Json(name = "subCatId")
    private Integer subCatId;
    @Json(name = "subCatTitle")
    private String subCatTitle;
    @Json(name = "areaId")
    private Integer areaId;
    @Json(name = "areaTitle")
    private String areaTitle;
    @Json(name = "desc")
    private String desc;
    @Json(name = "dateFrom")
    private Integer dateFrom;
    @Json(name = "dateFromPersian")
    private String dateFromPersian;
    @Json(name = "dateTo")
    private Integer dateTo;
    @Json(name = "dateToPersian")
    private String dateToPersian;
    @Json(name = "time")
    private Integer time;
    @Json(name = "timeDesc")
    private String timeDesc;
    @Json(name = "insrtTime")
    private Integer insrtTime;
    @Json(name = "insrtTimePersian")
    private String insrtTimePersian;
    @Json(name = "insrtTimeSimple")
    private String insrtTimeSimple;
    @Json(name = "updteTime")
    private Integer updteTime;
    @Json(name = "updteTimePersian")
    private String updteTimePersian;
    @Json(name = "trackingCode")
    private String trackingCode;
    @Json(name = "state")
    private Integer state;
    @Json(name = "stateTitle")
    private String stateTitle;
    @Json(name = "priority")
    private Integer priority;
    @Json(name = "priorityTitle")
    private String priorityTitle;
    @Json(name = "calculatedPrice")
    private String calculatedPrice;
    @Json(name = "finishByWorkman")
    private Integer finishByWorkman;
    @Json(name = "finishTime")
    private Integer finishTime;
    @Json(name = "discountCode")
    private String discountCode;
    @Json(name = "discountDesc")
    private String discountDesc;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getServicePicAddress() {
        return servicePicAddress;
    }

    public void setServicePicAddress(String servicePicAddress) {
        this.servicePicAddress = servicePicAddress;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public Integer getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(Integer subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatTitle() {
        return subCatTitle;
    }

    public void setSubCatTitle(String subCatTitle) {
        this.subCatTitle = subCatTitle;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaTitle() {
        return areaTitle;
    }

    public void setAreaTitle(String areaTitle) {
        this.areaTitle = areaTitle;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Integer dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateFromPersian() {
        return dateFromPersian;
    }

    public void setDateFromPersian(String dateFromPersian) {
        this.dateFromPersian = dateFromPersian;
    }

    public Integer getDateTo() {
        return dateTo;
    }

    public void setDateTo(Integer dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateToPersian() {
        return dateToPersian;
    }

    public void setDateToPersian(String dateToPersian) {
        this.dateToPersian = dateToPersian;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public Integer getInsrtTime() {
        return insrtTime;
    }

    public void setInsrtTime(Integer insrtTime) {
        this.insrtTime = insrtTime;
    }

    public String getInsrtTimePersian() {
        return insrtTimePersian;
    }

    public void setInsrtTimePersian(String insrtTimePersian) {
        this.insrtTimePersian = insrtTimePersian;
    }

    public String getInsrtTimeSimple() {
        return insrtTimeSimple;
    }

    public void setInsrtTimeSimple(String insrtTimeSimple) {
        this.insrtTimeSimple = insrtTimeSimple;
    }

    public Integer getUpdteTime() {
        return updteTime;
    }

    public void setUpdteTime(Integer updteTime) {
        this.updteTime = updteTime;
    }

    public String getUpdteTimePersian() {
        return updteTimePersian;
    }

    public void setUpdteTimePersian(String updteTimePersian) {
        this.updteTimePersian = updteTimePersian;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateTitle() {
        return stateTitle;
    }

    public void setStateTitle(String stateTitle) {
        this.stateTitle = stateTitle;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPriorityTitle() {
        return priorityTitle;
    }

    public void setPriorityTitle(String priorityTitle) {
        this.priorityTitle = priorityTitle;
    }

    public String getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(String calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public Integer getFinishByWorkman() {
        return finishByWorkman;
    }

    public void setFinishByWorkman(Integer finishByWorkman) {
        this.finishByWorkman = finishByWorkman;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDiscountDesc() {
        return discountDesc;
    }

    public void setDiscountDesc(String discountDesc) {
        this.discountDesc = discountDesc;
    }
}
