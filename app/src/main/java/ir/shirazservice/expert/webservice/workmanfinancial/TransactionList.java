package ir.shirazservice.expert.webservice.workmanfinancial;

import java.io.Serializable;
import java.util.List;

public class TransactionList  implements Serializable {


    private List<WorkmanFinancialTransaction>   workmanFinancialTransactions;

    public TransactionList() {

    }

    public TransactionList(List<WorkmanFinancialTransaction> workmanFinancialTransactions) {
        this.workmanFinancialTransactions = workmanFinancialTransactions;
    }

    public List<WorkmanFinancialTransaction> getWorkmanFinancialTransactions() {
        return workmanFinancialTransactions;
    }

    public void setWorkmanFinancialTransactions(List<WorkmanFinancialTransaction> workmanFinancialTransactions) {
        this.workmanFinancialTransactions = workmanFinancialTransactions;
    }

}
