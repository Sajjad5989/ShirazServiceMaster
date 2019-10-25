package ir.shirazservice.expert.webservice.getgiftchargeformula;

import com.squareup.moshi.Json;

public class GiftChargeInput {

    @Json(name = "provinceId")
    private Integer provinceId;
    @Json(name = "cityId")
    private Integer cityId;

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
