package ir.shirazservice.expert.webservice.workmanmessages;

import com.squareup.moshi.Json;

public class WorkmanInput {

    @Json(name = "workmanId")
    private Integer workmanId;
    @Json(name = "provinceId")
    private Integer provinceId;
    @Json(name = "cityId")
    private Integer cityId;

    public Integer getWorkmanId() {
        return workmanId;
    }

    public void setWorkmanId(Integer workmanId) {
        this.workmanId = workmanId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
