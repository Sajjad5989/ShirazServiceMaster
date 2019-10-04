package ir.shirazservice.expert.webservice.news;

import com.squareup.moshi.Json;

public class WorkmanNews {

    @Json(name = "title")
    private String title;
    @Json(name = "url")
    private String url;
    @Json(name = "desc")
    private String desc;
    @Json(name = "picAddress")
    private String picAddress;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }


}
