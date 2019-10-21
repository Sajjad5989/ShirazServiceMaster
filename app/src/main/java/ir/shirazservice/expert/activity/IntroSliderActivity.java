package ir.shirazservice.expert.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.Objects;

import ir.shirazservice.expert.R;
import ir.shirazservice.expert.adapter.IntroSliderAdapter;
import ir.shirazservice.expert.preferences.GeneralPreferences;

public class IntroSliderActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private LinearLayout dotsLayout;

    private AppCompatButton startButton;
    private AppCompatTextView btnNextSlide;
    private AppCompatTextView banPreviousSlide;

    private AppCompatImageView imageGoToLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro_slider);

        changeStatusBarColor();
        findViews();
        setViewPager();
        ViewPagerChangeListener();
        onClickSetting();

    }


    private void onClickSetting() {
        btnNextSlide.setOnClickListener(view -> goNext());
        banPreviousSlide.setOnClickListener(view -> goPrev());
        startButton.setOnClickListener(view -> startApp());
        imageGoToLastPage.setOnClickListener(view -> goToLastPage());
    }

    private void goToLastPage() {
        int lastPage = Objects.requireNonNull(viewPager.getAdapter()).getCount() - 1;
        viewPager.setCurrentItem(lastPage);
    }

    private void goPrev() {
        int currentPage = viewPager.getCurrentItem();
        int lastPage = Objects.requireNonNull(viewPager.getAdapter()).getCount() - 1;
        setButtonsVisible(lastPage, currentPage);
        if (currentPage <= lastPage) {
            viewPager.setCurrentItem(currentPage - 1);
        }
    }

    private void goNext() {
        int currentPage = viewPager.getCurrentItem();
        int lastPage = Objects.requireNonNull(viewPager.getAdapter()).getCount() - 1;
        setButtonsVisible(lastPage, currentPage);
        if (currentPage == lastPage) {
            startApp();
        } else {
            viewPager.setCurrentItem(currentPage + 1);
        }
    }

    private void startApp() {
        GeneralPreferences.getInstance(this).putBoolean(getString(R.string.text_preference_slider_is_show), true);
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void setButtonsVisible(int lastPage, int currentPage) {

        if (currentPage == lastPage) {
            imageGoToLastPage.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.VISIBLE);
            banPreviousSlide.setVisibility(View.VISIBLE);
            btnNextSlide.setVisibility(View.INVISIBLE);
        } else if (currentPage == 0) {
            imageGoToLastPage.setVisibility(View.VISIBLE);
            banPreviousSlide.setVisibility(View.INVISIBLE);
            btnNextSlide.setVisibility(View.VISIBLE);
        } else {
            imageGoToLastPage.setVisibility(View.VISIBLE);
            btnNextSlide.setVisibility(View.VISIBLE);
            banPreviousSlide.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.INVISIBLE);
        }

    }

    private void ViewPagerChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showDots(position);
                setButtonsVisible(Objects.requireNonNull(viewPager.getAdapter()).getCount() - 1, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setViewPager() {
        IntroSliderAdapter pagerAdapter = new IntroSliderAdapter(IntroSliderActivity.this);
        viewPager.setAdapter(pagerAdapter);
        showDots(viewPager.getCurrentItem());
    }

    private void findViews() {

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layout_dots);
        btnNextSlide = findViewById(R.id.btn_next);
        banPreviousSlide = findViewById(R.id.btn_previous);
        imageGoToLastPage = findViewById(R.id.image_last_page);
        startButton = findViewById(R.id.btn_start);

        startButton.setVisibility(View.INVISIBLE);
        banPreviousSlide.setVisibility(View.INVISIBLE);


        setButtonsVisible(1, 0);
    }

    private void showDots(int pageNumber) {
        AppCompatTextView[] dots = new AppCompatTextView[Objects.requireNonNull(viewPager.getAdapter()).getCount()];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new AppCompatTextView(this);

            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

            dots[i].setTextColor(ContextCompat.getColor(this,
                    (i == pageNumber ? R.color.color_active_dit : R.color.color_in_active_dot)));

            dotsLayout.addView(dots[i]);

        }
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


}
