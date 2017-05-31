package com.scowluga.android.microscience.nav1_home;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scowluga.android.microscience.MainActivity;
import com.scowluga.android.microscience.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        
        Bundle args = new Bundle();
        
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ViewPager viewPager;
    ViewPageAdapter adapter;
    int[] images;
    String[] urls;
    public static int currentPage = 0;
    public static int totalPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment1_home, container, false);

        images = new int[] {
                R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,
                R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8,
                R.drawable.img9, R.drawable.img10, R.drawable.img11, R.drawable.img12, R.drawable.img13, R.drawable.img14};

        urls = new String[] {
                "https://microscience.on.ca/products/learning-differences/kurzweil-3000/",
                "https://microscience.on.ca/products/low-vision/optelec-traveller-hd/",
                "https://microscience.on.ca/products/voice-recognition/dragon-professional-individual/",
                "https://microscience.on.ca/products/low-vision/magnilink-s/",
                "https://microscience.on.ca/products/learning-differences/claro-cloud/",
                "https://microscience.on.ca/products/blind-access/orcam-myeye-ocr-face-products-money/",
                "https://microscience.on.ca/enhanced-vision/",
                "https://microscience.on.ca/dt-trainer-activity-trainer/",
                "https://microscience.on.ca/humanware-braillenote-touch/",
                "https://microscience.on.ca/humanware-prodigi-connect/",
                "https://microscience.on.ca/clockwork/",
                "https://microscience.on.ca/buzzclip/",
                "https://microscience.on.ca/eyegaze-edge/",
                "https://microscience.on.ca/kurzweil-3000-web-firefly/"
        };
        totalPage = images.length;

        viewPager = (ViewPager) v.findViewById(R.id.home_pager);
        adapter = new ViewPageAdapter(images, urls, getContext());
        viewPager.setAdapter(adapter);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / density;
        float pagerHeightDp = dpWidth * ((float)290 / (float)770.0);
        int paperHeightPx = Math.round(pagerHeightDp * density + 0.5f);

        viewPager.getLayoutParams().height = paperHeightPx;


        // CIRCLE INDICATOR CODE
        CirclePageIndicator indicator = (CirclePageIndicator) v.findViewById(R.id.home_indicator);
        indicator.setViewPager(viewPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == totalPage) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++);
            }
        };
        Timer swipe = new Timer();
        swipe.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000, 3000);

        return v;
    }

    @Override
    public void onResume() {
        MainActivity.toolbar.setTitle("Home"); 
        super.onResume();
    }
}
