package ir.shirazservice.expert.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import ir.shirazservice.expert.R;
import ir.shirazservice.expert.interfaces.RecyclerViewClickListener;
import ir.shirazservice.expert.webservice.myservice.MyService;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MyServiceAdapter extends RecyclerView.Adapter<MyServiceAdapter.ViewHolder> {


    private final List<MyService> items;


    private final RecyclerViewClickListener listener;

    public MyServiceAdapter( List<MyService> items, RecyclerViewClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_template_my_services, parent, false
        );

        final MyServiceAdapter.ViewHolder mViewHolder = new MyServiceAdapter.ViewHolder(view);
        view.setOnClickListener(view1 -> listener.onItemClick(view1, mViewHolder.getAdapterPosition()));

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyServiceAdapter.ViewHolder holder, int position) {

        holder.tvServiceTitle.setText(items.get(position).getServiceTitle());
        holder.tvServiceTime.setText(items.get(position).getInsrtDatePersian1());
        holder.tvServiceStatus.setText(items.get(position).getStateTitle());
        holder.ratingBar.setRating(
                items.get(position).getState() == 6 ? items.get(position).getRate() : 0);
        String picAddress = items.get(position).getServicePicAddress();
        if (null == picAddress || picAddress.equals("")) {
            holder.imageService.setImageResource(R.drawable.img_no_icon);
        } else {
            Picasso.get()
                    .load(picAddress)  //Url of the image to load.
                    .transform(new CropCircleTransformation())
                    .error(R.drawable.img_no_icon)
                    .placeholder(R.drawable.img_loading)
                    .into(holder.imageService);
        }
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView tvServiceTitle;
        private final AppCompatTextView tvServiceTime;
        private final AppCompatTextView tvServiceStatus;
        private final AppCompatImageView imageService;
        private final RatingBar ratingBar;


        private ViewHolder(final View itemView) {
            super(itemView);

            tvServiceTitle = itemView.findViewById(R.id.tv_my_service_title);
            tvServiceTime = itemView.findViewById(R.id.tv_my_service_time);
            tvServiceStatus = itemView.findViewById(R.id.tv_service_status);
            imageService = itemView.findViewById(R.id.image_my_service);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }


}
