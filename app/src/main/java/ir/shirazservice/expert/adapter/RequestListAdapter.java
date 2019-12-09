package ir.shirazservice.expert.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.interfaces.RecyclerViewClickListener;
import ir.shirazservice.expert.webservice.requestlist.Request;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.ViewHolder> {


    private final List<Request> items;
    private final RecyclerViewClickListener listener;
    private final Context context;

    public RequestListAdapter(Context context, List<Request> items, RecyclerViewClickListener listener) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_template_request, parent, false
        );

        final ViewHolder mViewHolder = new ViewHolder(view);
        view.setOnClickListener(view1 -> listener.onItemClick(view1, mViewHolder.getAdapterPosition()));

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvRequestTitle.setText(items.get(position).getServiceTitle());
        holder.tvRequestPosition.setText(String.valueOf(items.get(position).getAreaTitle()));
        Picasso.get()
                .load(items.get(position).getServicePicAddress())
                .into(holder.imageRequest);
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView tvRequestTitle;
        private final AppCompatTextView tvRequestPosition;
        private final AppCompatImageView imageRequest;


        private ViewHolder(final View itemView) {
            super(itemView);

            tvRequestTitle = itemView.findViewById(R.id.tv_request_title);
            tvRequestPosition = itemView.findViewById(R.id.tv_request_area);
            imageRequest = itemView.findViewById(R.id.image_request);

        }
    }
}
