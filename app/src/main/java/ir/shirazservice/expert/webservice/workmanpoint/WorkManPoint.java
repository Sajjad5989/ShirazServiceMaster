package ir.shirazservice.expert.webservice.workmanpoint;

import com.squareup.moshi.Json;

public class WorkManPoint {

    @Json(name = "activityPoint")
    private Integer activityPoint;

    public Integer getActivityPoint() {
        return activityPoint;
    }

    public void setActivityPoint(Integer activityPoint) {
        this.activityPoint = activityPoint;
    }

}
