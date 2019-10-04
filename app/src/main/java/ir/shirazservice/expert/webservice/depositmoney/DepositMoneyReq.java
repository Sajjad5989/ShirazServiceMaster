package ir.shirazservice.expert.webservice.depositmoney;

public class DepositMoneyReq {
    private int servicemanId;
    private int type;
    private String cardNo;
    private String fullName;
    private String trackingCode;
    private String price;
    private int depositTime;
    private int depositTimeYear;
    private int depositTimeMonth;
    private int depositTimeDay;

    public int getServicemanId() {
        return servicemanId;
    }

    public void setServicemanId(int servicemanId) {
        this.servicemanId = servicemanId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(int depositTime) {
        this.depositTime = depositTime;
    }

    public int getDepositTimeYear() {
        return depositTimeYear;
    }

    public void setDepositTimeYear(int depositTimeYear) {
        this.depositTimeYear = depositTimeYear;
    }

    public int getDepositTimeMonth() {
        return depositTimeMonth;
    }

    public void setDepositTimeMonth(int depositTimeMonth) {
        this.depositTimeMonth = depositTimeMonth;
    }

    public int getDepositTimeDay() {
        return depositTimeDay;
    }

    public void setDepositTimeDay(int depositTimeDay) {
        this.depositTimeDay = depositTimeDay;
    }

}
