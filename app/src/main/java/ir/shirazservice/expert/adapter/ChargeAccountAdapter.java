package ir.shirazservice.expert.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.interfaces.RecyclerViewClickListener;
import ir.shirazservice.expert.utils.ChargeAmount;
import ir.shirazservice.expert.utils.UsefulFunction;

public class ChargeAccountAdapter  extends RecyclerView.Adapter<ChargeAccountAdapter.ViewHolder> {



    private List<ChargeAmount> items;
    private final RecyclerViewClickListener listener;


    public ChargeAccountAdapter(List<ChargeAmount>  items,  RecyclerViewClickListener listener) {

        this.items = items;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ChargeAccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.template_charge, parent, false
        );


        final ChargeAccountAdapter.ViewHolder mViewHolder = new ChargeAccountAdapter.ViewHolder(view);
        view.setOnClickListener(view1 -> listener.onItemClick(view1, mViewHolder.getAdapterPosition()));

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChargeAccountAdapter.ViewHolder holder, int position) {

        UsefulFunction usefulFunction=new UsefulFunction();


        holder.tvChargeAmount.setText(usefulFunction.attachCamma(String.valueOf(items.get(position).getPrice())));
      //  holder.tvNewPrice.setText(usefulFunction.attachCamma(String.valueOf(items.get(position).getNewPrice())));

    }


    @Override
    public int getItemCount() {

        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView tvChargeAmount;
       // private final AppCompatTextView tvNewPrice;

        private ViewHolder(final View itemView) {
            super(itemView);

            tvChargeAmount = itemView.findViewById(R.id.tv_charge_amount);
          //  tvNewPrice = itemView.findViewById(R.id.tv_new_price);


        }
    }



}
