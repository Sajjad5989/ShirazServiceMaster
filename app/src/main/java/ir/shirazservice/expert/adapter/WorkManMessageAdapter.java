package ir.shirazservice.expert.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.webservice.workmanmessages.WorkmanMessages;

public class WorkManMessageAdapter extends RecyclerView.Adapter<WorkManMessageAdapter.ViewHolder> {


    private final List<WorkmanMessages> items;

    public WorkManMessageAdapter(List<WorkmanMessages> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public WorkManMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_template_workman_message, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkManMessageAdapter.ViewHolder holder, int position) {
        holder.tvWorkmanMessageTitle.setText(items.get(position).getTitle());
        holder.tvWorkmanMessageDesc.setText(items.get(position).getDesc());
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView tvWorkmanMessageTitle;
        private final AppCompatTextView tvWorkmanMessageDesc;

        private ViewHolder(final View itemView) {
            super(itemView);

            tvWorkmanMessageTitle = itemView.findViewById(R.id.tv_message_title);
            tvWorkmanMessageDesc = itemView.findViewById(R.id.tv_message_desc);


        }
    }

}
