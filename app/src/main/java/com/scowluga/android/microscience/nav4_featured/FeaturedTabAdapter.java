package com.scowluga.android.microscience.nav4_featured;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by robertlu on 2017-05-27.
 */

public class FeaturedTabAdapter extends FragmentPagerAdapter {

    String fragments[] = {"Kurzweil", "Clockwork"};

    public FeaturedTabAdapter (FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FeaturedKurzweil.newInstance();
            case 1:
                return FeaturedClockwork.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position];
    }
}
