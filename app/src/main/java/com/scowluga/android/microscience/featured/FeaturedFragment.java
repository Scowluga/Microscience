package com.scowluga.android.microscience.featured;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scowluga.android.microscience.MainActivity;
import com.scowluga.android.microscience.R;

import static com.scowluga.android.microscience.MainActivity.TAGFRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeaturedFragment extends Fragment {
    public FeaturedFragment() {
        // Required empty public constructor
    }
    public static FeaturedFragment newInstance() {
        Bundle args = new Bundle();
        FeaturedFragment fragment = new FeaturedFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment4_featured, container, false);

        tabInitialize(v);
        return v;
    }

    static boolean reset = false;

    private void tabInitialize(View v) {
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.featured_viewPager);
        FeaturedTabAdapter adapter = new FeaturedTabAdapter(((AppCompatActivity)v.getContext()).getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.featured_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {viewPager.setCurrentItem(tab.getPosition());}
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {viewPager.setCurrentItem(tab.getPosition());}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {viewPager.setCurrentItem(tab.getPosition());}
        });
    }

    @Override
    public void onResume() {
        MainActivity.toolbar.setTitle("Featured");

        if (reset) { // Reset Fragment
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    tabInitialize(getView());
                }
            }, 1);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        reset = true;
        super.onPause();
    }
}
