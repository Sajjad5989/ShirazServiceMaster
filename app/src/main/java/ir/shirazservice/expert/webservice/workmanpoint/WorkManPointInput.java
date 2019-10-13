package ir.shirazservice.expert.webservice.workmanpoint;

import com.squareup.moshi.Json;

public class WorkManPointInput {

    @Json(name = "id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
