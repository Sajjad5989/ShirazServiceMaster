package ir.shirazservice.expert.webservice.workmanmessages;

import com.squareup.moshi.Json;

public class WorkmanMessages {
    @Json(name = "title")
    private String title;
    @Json(name = "desc")
    private String desc;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
