package ir.shirazservice.expert.adapter;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.interfaces.RecyclerViewClickListener;
import ir.shirazservice.expert.webservice.slider.AdsSlider;

public class SessionSliderAdapter extends PagerAdapter {

    private final Context context;
    private final List<AdsSlider> sessionSliderList;
    private final RecyclerViewClickListener listener;

    public SessionSliderAdapter(Context context, List<AdsSlider> sessionSliders, RecyclerViewClickListener listener) {
        this.context = context;
        this.sessionSliderList = sessionSliders;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return sessionSliderList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.template_session_slider, container, false);
        view.setOnClickListener(v -> listener.onItemClick(v, position));

        String sliderImage = sessionSliderList.get(position).getPicAddress();
        String sliderText = sessionSliderList.get(position).getDesc();

        AppCompatTextView myText = view.findViewById(R.id.session_slider_title);
        AppCompatTextView myOrderBottom = view.findViewById(R.id.session_slider_order_bottom);
        AppCompatTextView myOrderTop = view.findViewById(R.id.session_slider_order_top);

        myOrderBottom.setVisibility(View.GONE);
        myOrderTop.setVisibility(View.GONE);
        myText.setText(sliderText);
        myText.setVisibility("".equals(sliderText) ? View.GONE : View.VISIBLE);

        AppCompatImageView myImg = view.findViewById(R.id.session_slider_image);
        if (sliderImage != null && !"".equals(sliderImage)) {
            Picasso.get().load(sliderImage)
                    .error(R.drawable.image_default_slider)
                    .placeholder(R.drawable.image_default_slider)
                    .into(myImg);
        } else {
            myImg.setImageResource(R.drawable.image_default_slider);
        }

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
        }, 1000);


        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}