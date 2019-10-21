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

import ir.shirazservice.expert.R;


public class IntroSliderAdapter extends PagerAdapter {

    private final Context context;

    private final String[] slideTitle;

    private final String[] slideDescription;
    private final int[] slideImageIds = new int[]{
            R.drawable.splash_a,
            R.drawable.splash_b,
            R.drawable.splash_c
    };


    public IntroSliderAdapter(Context context) {
        this.context = context;
        slideTitle = context.getResources().getStringArray(R.array.slide_titles);
        slideDescription = context.getResources().getStringArray(R.array.slide_descs);

    }

    @Override
    public int getCount() {
        return slideTitle.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.template_slide, container, false);

//        view.findViewById(R.id.bgLayout).setBackgroundColor(
//                ContextCompat.getColor(context, R.color.colorWhite));

        AppCompatImageView myImg = view.findViewById(R.id.slide_image);
        myImg.setImageResource(slideImageIds[position]);

//        ((AppCompatTextView) view.findViewById(R.id.slide_title)).setTypeface(APP.defaultFont);
//        ((AppCompatTextView) view.findViewById(R.id.slide_desc)).setTypeface(APP.defaultFont);

        ((AppCompatTextView) view.findViewById(R.id.slide_title)).setText(slideTitle[position]);
        ((AppCompatTextView) view.findViewById(R.id.slide_desc)).setText(slideDescription[position]);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            //Do something after 100ms
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