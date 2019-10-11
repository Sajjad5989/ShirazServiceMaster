package ir.shirazservice.expert.webservice.myservice;

import com.squareup.moshi.Json;

public class MyService {
    @Json(name = "reqId")
    private Integer reqId;
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
    @Json(name = "insrtTime")
    private Integer insrtTime;
    @Json(name = "insrtDatePersian")
    private String insrtDatePersian;
    @Json(name = "insrtDatePersian1")
    private String insrtDatePersian1;
    @Json(name = "insrtDatePersian2")
    private String insrtDatePersian2;
    @Json(name = "insrtTimeSimple")
    private String insrtTimeSimple;
    @Json(name = "insrtTimePersian")
    private String insrtTimePersian;
    @Json(name = "updteTime")
    private Integer updteTime;
    @Json(name = "updteDatePersian")
    private String updteDatePersian;
    @Json(name = "updteDatePersian1")
    private String updteDatePersian1;
    @Json(name = "updteDatePersian2")
    private String updteDatePersian2;
    @Json(name = "updteTimeSimple")
    private String updteTimeSimple;
    @Json(name = "updteTimePersian")
    private String updteTimePersian;
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
    @Json(name = "rate")
    private Float rate;
    @Json(name = "discountCode")
    private String discountCode;
    @Json(name = "discountDesc")
    private String discountDesc;

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
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

    public Integer getInsrtTime() {
        return insrtTime;
    }

    public void setInsrtTime(Integer insrtTime) {
        this.insrtTime = insrtTime;
    }

    public String getInsrtDatePersian() {
        return insrtDatePersian;
    }

    public void setInsrtDatePersian(String insrtDatePersian) {
        this.insrtDatePersian = insrtDatePersian;
    }

    public String getInsrtDatePersian1() {
        return insrtDatePersian1;
    }

    public void setInsrtDatePersian1(String insrtDatePersian1) {
        this.insrtDatePersian1 = insrtDatePersian1;
    }

    public String getInsrtDatePersian2() {
        return insrtDatePersian2;
    }

    public void setInsrtDatePersian2(String insrtDatePersian2) {
        this.insrtDatePersian2 = insrtDatePersian2;
    }

    public String getInsrtTimeSimple() {
        return insrtTimeSimple;
    }

    public void setInsrtTimeSimple(String insrtTimeSimple) {
        this.insrtTimeSimple = insrtTimeSimple;
    }

    public String getInsrtTimePersian() {
        return insrtTimePersian;
    }

    public void setInsrtTimePersian(String insrtTimePersian) {
        this.insrtTimePersian = insrtTimePersian;
    }

    public Integer getUpdteTime() {
        return updteTime;
    }

    public void setUpdteTime(Integer updteTime) {
        this.updteTime = updteTime;
    }

    public String getUpdteDatePersian() {
        return updteDatePersian;
    }

    public void setUpdteDatePersian(String updteDatePersian) {
        this.updteDatePersian = updteDatePersian;
    }

    public String getUpdteDatePersian1() {
        return updteDatePersian1;
    }

    public void setUpdteDatePersian1(String updteDatePersian1) {
        this.updteDatePersian1 = updteDatePersian1;
    }

    public String getUpdteDatePersian2() {
        return updteDatePersian2;
    }

    public void setUpdteDatePersian2(String updteDatePersian2) {
        this.updteDatePersian2 = updteDatePersian2;
    }

    public String getUpdteTimeSimple() {
        return updteTimeSimple;
    }

    public void setUpdteTimeSimple(String updteTimeSimple) {
        this.updteTimeSimple = updteTimeSimple;
    }

    public String getUpdteTimePersian() {
        return updteTimePersian;
    }

    public void setUpdteTimePersian(String updteTimePersian) {
        this.updteTimePersian = updteTimePersian;
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

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
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
