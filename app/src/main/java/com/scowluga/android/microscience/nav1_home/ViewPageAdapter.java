package com.scowluga.android.microscience.nav1_home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scowluga.android.microscience.R;

/**
 * Created by robertlu on 2017-05-20.
 */

public class ViewPageAdapter extends PagerAdapter {

    int[] images;
    String[] urls;
    LayoutInflater inflater;
    Context context;

    public ViewPageAdapter (int[] imgs, String[] urls, Context c) {
        this.images = imgs;
        this.urls = urls;
        this.context = c;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.fragment1_home_image, container, false);
        imageView = (ImageView) itemView.findViewById(R.id.home_image);
        imageView.setImageDrawable(context.getResources().getDrawable(images[position]));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position]));
                context.startActivity(intent);
            }
        });

        ((ViewPager)container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((LinearLayout)object);
    }
}
