package ir.shirazservice.expert.webservice.workmanfinancial;

import java.io.Serializable;

public class WorkmanFinancialTransaction   implements Serializable {
    public WorkmanFinancialTransaction(int id, int action, String actionTitle, int type, String typeTitle, int requestId, String price, String remainCredit, String desc, String advanceDesc, String insrtFullDateTimePersian, String insrtDatePersian, String insrtDatePersian1, String insrtTimePersian, int insrtTime, String trackingCode) {
        this.id = id;
        this.action = action;
        this.actionTitle = actionTitle;
        this.type = type;
        this.typeTitle = typeTitle;
        this.requestId = requestId;
        this.price = price;
        this.remainCredit = remainCredit;
        this.desc = desc;
        this.advanceDesc = advanceDesc;
        this.insrtFullDateTimePersian = insrtFullDateTimePersian;
        this.insrtDatePersian = insrtDatePersian;
        this.insrtDatePersian1 = insrtDatePersian1;
        this.insrtTimePersian = insrtTimePersian;
        this.insrtTime = insrtTime;
        this.trackingCode = trackingCode;
    }

    public WorkmanFinancialTransaction() {
    }

    private int id;
    private int action;
    private String actionTitle;
    private int type;
    private String typeTitle;
    private int requestId;
    private String price;
    private String remainCredit;
    private String desc;
    private String advanceDesc;
    private String insrtFullDateTimePersian;
    private String insrtDatePersian;
    private String insrtDatePersian1;
    private String insrtTimePersian;
    private int insrtTime;
    private String trackingCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(String actionTitle) {
        this.actionTitle = actionTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemainCredit() {
        return remainCredit;
    }

    public void setRemainCredit(String remainCredit) {
        this.remainCredit = remainCredit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAdvanceDesc() {
        return advanceDesc;
    }

    public void setAdvanceDesc(String advanceDesc) {
        this.advanceDesc = advanceDesc;
    }

    public String getInsrtFullDateTimePersian() {
        return insrtFullDateTimePersian;
    }

    public void setInsrtFullDateTimePersian(String insrtFullDateTimePersian) {
        this.insrtFullDateTimePersian = insrtFullDateTimePersian;
    }

    public String getInsrtDatePersian() {
        return insrtDatePersian;
    }

    public void setInsrtDatePersian(String insrtDatePersian) {
        this.insrtDatePersian = insrtDatePersian;
    }

    public String getInsrtDatePersian1() {
        return insrtDatePersian1;
    }

    public void setInsrtDatePersian1(String insrtDatePersian1) {
        this.insrtDatePersian1 = insrtDatePersian1;
    }

    public String getInsrtTimePersian() {
        return insrtTimePersian;
    }

    public void setInsrtTimePersian(String insrtTimePersian) {
        this.insrtTimePersian = insrtTimePersian;
    }

    public int getInsrtTime() {
        return insrtTime;
    }

    public void setInsrtTime(int insrtTime) {
        this.insrtTime = insrtTime;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public double getPriceByDouble() {
        return
                Double.valueOf(price);
    }
}
