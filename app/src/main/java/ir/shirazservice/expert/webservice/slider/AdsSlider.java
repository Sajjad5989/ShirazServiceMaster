package ir.shirazservice.expert.webservice.slider;

import java.io.Serializable;

public class AdsSlider implements Serializable {

    private String title;
    private String url;
    private String desc;
    private String picAddress;

    public AdsSlider() {
    }


    public AdsSlider(String title, String url, String desc, String picAddress) {
        this.title = title;
        this.url = url;
        this.desc = desc;
        this.picAddress = picAddress;
    }

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
