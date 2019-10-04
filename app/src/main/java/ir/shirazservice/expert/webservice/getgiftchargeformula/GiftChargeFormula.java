package ir.shirazservice.expert.webservice.getgiftchargeformula;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class GiftChargeFormula implements Serializable {


    @Json(name = "id")
    private int id;
    @Json(name = "startPeriod")
    private String startPeriod;
    @Json(name = "endPeriod")
    private String endPeriod;
    @Json(name = "giftPercent")
    private int giftPercent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public int getGiftPercent() {
        return giftPercent;
    }

    public void setGiftPercent(int giftPercent) {
        this.giftPercent = giftPercent;
    }

}
