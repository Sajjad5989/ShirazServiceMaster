package ir.shirazservice.expert.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.interfaces.RecyclerViewClickListener;
import ir.shirazservice.expert.webservice.news.WorkmanNews;

public class WorkManAllNewsAdapter extends RecyclerView.Adapter<WorkManAllNewsAdapter.ViewHolder> {


    private final List<WorkmanNews> items;
    private final RecyclerViewClickListener listener;
    private final Context context;

    public WorkManAllNewsAdapter(Context context, List<WorkmanNews> items, int countNew, RecyclerViewClickListener listener) {

        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkManAllNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_template_all_news, parent, false
        );

        final WorkManAllNewsAdapter.ViewHolder mViewHolder = new WorkManAllNewsAdapter.ViewHolder(view);
        view.setOnClickListener(view1 -> listener.onItemClick(view1, mViewHolder.getAdapterPosition()));

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkManAllNewsAdapter.ViewHolder holder, int position) {

        holder.tvNewTitle.setText(items.get(position).getTitle());
        String sliderImage = items.get(position).getPicAddress();

        if (sliderImage != null && !"".equals(sliderImage)) {
            Picasso.with(context).load(sliderImage)
                    .error(R.drawable.image_default_mag)
                    .placeholder(R.drawable.image_default_mag)
                    .into(holder.imageNews);
        } else {
            holder.imageNews.setImageResource(R.drawable.image_default_mag);
        }

    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView tvNewTitle;
        private final AppCompatTextView tvNewsDesc;
        private final AppCompatImageView imageNews;


        private ViewHolder(final View itemView) {
            super(itemView);

            tvNewTitle = itemView.findViewById(R.id.tv_news_title);
            tvNewsDesc = itemView.findViewById(R.id.tv_news_position);
            imageNews = itemView.findViewById(R.id.image_news);

        }
    }


}
