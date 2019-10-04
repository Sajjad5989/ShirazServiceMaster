package ir.shirazservice.expert.webservice.workmanfinancial;

public class WorkmanFinancialTransactionReq {

    private int servicemanId;
    private int typeId;
    private String insrtDateShamsi;

    public int getServicemanId() {
        return servicemanId;
    }

    public void setServicemanId(int servicemanId) {
        this.servicemanId = servicemanId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getInsrtDateShamsi() {
        return insrtDateShamsi;
    }

    public void setInsrtDateShamsi(String insrtDateShamsi) {
        this.insrtDateShamsi = insrtDateShamsi;
    }

}
