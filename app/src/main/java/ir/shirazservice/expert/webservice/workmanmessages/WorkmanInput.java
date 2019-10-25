package ir.shirazservice.expert.webservice.workmanmessages;

public class WorkmanInput {

    private Integer workmanId;
    private Integer provinceId;
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
