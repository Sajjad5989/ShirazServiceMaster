package ir.shirazservice.expert.webservice.serviceInfo;

import com.squareup.moshi.Json;

public class ServiceInfo {

    @Json(name = "catId")
    private int catId;
    @Json(name = "catTitle")
    private String catTitle;
    @Json(name = "subCatId")
    private int subCatId;
    @Json(name = "subCatTitle")
    private String subCatTitle;
    @Json(name = "serviceId")
    private int serviceId;
    @Json(name = "serviceTitle")
    private String serviceTitle;
    @Json(name = "basePrice")
    private String basePrice;
    @Json(name = "picAddress")
    private String picAddress;
    @Json(name = "body")
    private String body;
    @Json(name = "extraTitle1")
    private String extraTitle1;
    @Json(name = "extraField1")
    private String extraField1;
    @Json(name = "extraTitle2")
    private String extraTitle2;
    @Json(name = "extraField2")
    private String extraField2;
    @Json(name = "tagId")
    private int tagId;
    @Json(name = "tagTitle")
    private String tagTitle;
    @Json(name = "type")
    private int type;
    @Json(name = "insrtTime")
    private int insrtTime;
    @Json(name = "insrtTimePersian")
    private String insrtTimePersian;

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

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtraTitle1() {
        return extraTitle1;
    }

    public void setExtraTitle1(String extraTitle1) {
        this.extraTitle1 = extraTitle1;
    }

    public String getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(String extraField1) {
        this.extraField1 = extraField1;
    }

    public String getExtraTitle2() {
        return extraTitle2;
    }

    public void setExtraTitle2(String extraTitle2) {
        this.extraTitle2 = extraTitle2;
    }

    public String getExtraField2() {
        return extraField2;
    }

    public void setExtraField2(String extraField2) {
        this.extraField2 = extraField2;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

}
