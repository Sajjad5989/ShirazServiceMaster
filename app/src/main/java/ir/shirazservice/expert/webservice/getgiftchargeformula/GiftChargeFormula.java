package ir.shirazservice.expert.webservice.getgiftchargeformula;

import java.io.Serializable;

public class GiftChargeFormula implements Serializable {


    private int id;
    private String startPeriod;
    private String endPeriod;
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
