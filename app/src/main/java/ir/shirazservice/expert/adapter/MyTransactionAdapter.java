package ir.shirazservice.expert.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.interfaces.RecyclerViewClickListener;
import ir.shirazservice.expert.utils.APP;
import ir.shirazservice.expert.utils.UsefulFunction;
import ir.shirazservice.expert.webservice.workmanfinancial.WorkmanFinancialTransaction;

public class MyTransactionAdapter extends RecyclerView.Adapter<MyTransactionAdapter.ViewHolder> {

    private final List<WorkmanFinancialTransaction> items;
    private final RecyclerViewClickListener listener;

    private int recordCount ;

    public MyTransactionAdapter(List<WorkmanFinancialTransaction> items, int countRequest, RecyclerViewClickListener listener) {

        this.recordCount = countRequest;
        this.items = items;
//
//        if (countRequest != 0) {
//            int topIndex = items.size() > countRequest ? countRequest : items.size();
//            this.items = items.subList(0, topIndex);
//        } else this.items = items;
        this.listener = listener;

    }

    @NonNull
    @Override
    public MyTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_template_my_transaction, parent, false
        );


        final MyTransactionAdapter.ViewHolder mViewHolder = new MyTransactionAdapter.ViewHolder(view);
        view.setOnClickListener(view1 -> listener.onItemClick(view1, mViewHolder.getAdapterPosition()));

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTransactionAdapter.ViewHolder holder, int position) {


        holder.tvActionTitle.setText(items.get(position).getActionTitle());
        holder.tvTypeTitle.setText(items.get(position).getInsrtDatePersian());

        String payMoney = items.get(position).getPrice();
        if (payMoney.contains("-")) {
            holder.imgPayMoney.setBackgroundResource(R.drawable.ic_get_money);
            holder.tvPayMoney.setTextColor(APP.context.getResources().getColor(R.color.colorRedStep_1));
        } else {
            holder.imgPayMoney.setBackgroundResource(R.drawable.ic_pay_money);
            holder.tvPayMoney.setTextColor(APP.context.getResources().getColor(R.color.colorGreen));
        }
        holder.tvPayMoney.setText(new UsefulFunction().attachCamma(getPositiveNumber(payMoney)));
    }

    private String getPositiveNumber(String value) {
        return value.contains("-") ?
                value.replace("-", "") : value;

    }

    @Override
    public int getItemCount() {

        return recordCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView tvActionTitle;
        private final AppCompatTextView tvTypeTitle;
        private final AppCompatTextView tvPayMoney;
        private final AppCompatImageView imgPayMoney;

        private ViewHolder(final View itemView) {
            super(itemView);

            tvActionTitle = itemView.findViewById(R.id.tv_transaction_actionTitle);
            tvTypeTitle = itemView.findViewById(R.id.tv_transaction_typeTitle);
            tvPayMoney = itemView.findViewById(R.id.tv_transaction_money);
            imgPayMoney = itemView.findViewById(R.id.img_transaction);


        }
    }


}
