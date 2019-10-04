package ir.shirazservice.expert.webservice.requestlist;

import java.io.Serializable;

public class Request implements Serializable {

    private int requestId;
    private int serviceId;
    private String serviceTitle;
    private String servicePicAddress;
    private int catId;
    private String catTitle;
    private int subCatId;
    private String subCatTitle;
    private int areaId;
    private String areaTitle;
    private String desc;
    private int dateFrom;
    private String dateFromPersian;
    private int dateTo;
    private String dateToPersian;
    private int time;
    private String timeDesc;
    private int insrtTime;
    private String insrtTimePersian;
    private String insrtTimeSimple;
    private int updteTime;
    private String updteTimePersian;
    private int state;
    private String stateTitle;
    private int priority;
    private String priorityTitle;
    private int calculatedPrice;
    private int finishByWorkman;
    private int finishTime;
    private String discountCode;
    private String discountDesc;

    public Request() {
    }

    public Request(int requestId, int serviceId, String serviceTitle, String servicePicAddress, int catId, String catTitle, int subCatId, String subCatTitle, int areaId, String areaTitle, String desc, int dateFrom, String dateFromPersian, int dateTo, String dateToPersian, int time, String timeDesc, int insrtTime, String insrtTimePersian, String insrtTimeSimple, int updteTime, String updteTimePersian, int state, String stateTitle, int priority, String priorityTitle, int calculatedPrice, int finishByWorkman, int finishTime, String discountCode, String discountDesc) {
        this.requestId = requestId;
        this.serviceId = serviceId;
        this.serviceTitle = serviceTitle;
        this.servicePicAddress = servicePicAddress;
        this.catId = catId;
        this.catTitle = catTitle;
        this.subCatId = subCatId;
        this.subCatTitle = subCatTitle;
        this.areaId = areaId;
        this.areaTitle = areaTitle;
        this.desc = desc;
        this.dateFrom = dateFrom;
        this.dateFromPersian = dateFromPersian;
        this.dateTo = dateTo;
        this.dateToPersian = dateToPersian;
        this.time = time;
        this.timeDesc = timeDesc;
        this.insrtTime = insrtTime;
        this.insrtTimePersian = insrtTimePersian;
        this.insrtTimeSimple = insrtTimeSimple;
        this.updteTime = updteTime;
        this.updteTimePersian = updteTimePersian;
        this.state = state;
        this.stateTitle = stateTitle;
        this.priority = priority;
        this.priorityTitle = priorityTitle;
        this.calculatedPrice = calculatedPrice;
        this.finishByWorkman = finishByWorkman;
        this.finishTime = finishTime;
        this.discountCode = discountCode;
        this.discountDesc = discountDesc;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
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

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public int getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(int subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatTitle() {
        return subCatTitle;
    }

    public void setSubCatTitle(String subCatTitle) {
        this.subCatTitle = subCatTitle;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
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

    public int getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(int dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateFromPersian() {
        return dateFromPersian;
    }

    public void setDateFromPersian(String dateFromPersian) {
        this.dateFromPersian = dateFromPersian;
    }

    public int getDateTo() {
        return dateTo;
    }

    public void setDateTo(int dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateToPersian() {
        return dateToPersian;
    }

    public void setDateToPersian(String dateToPersian) {
        this.dateToPersian = dateToPersian;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public int getInsrtTime() {
        return insrtTime;
    }

    public void setInsrtTime(int insrtTime) {
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

    public int getUpdteTime() {
        return updteTime;
    }

    public void setUpdteTime(int updteTime) {
        this.updteTime = updteTime;
    }

    public String getUpdteTimePersian() {
        return updteTimePersian;
    }

    public void setUpdteTimePersian(String updteTimePersian) {
        this.updteTimePersian = updteTimePersian;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateTitle() {
        return stateTitle;
    }

    public void setStateTitle(String stateTitle) {
        this.stateTitle = stateTitle;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPriorityTitle() {
        return priorityTitle;
    }

    public void setPriorityTitle(String priorityTitle) {
        this.priorityTitle = priorityTitle;
    }

    public int getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(int calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public int getFinishByWorkman() {
        return finishByWorkman;
    }

    public void setFinishByWorkman(int finishByWorkman) {
        this.finishByWorkman = finishByWorkman;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
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
