package com.scowluga.android.microscience.news;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scowluga.android.microscience.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetails extends Fragment {

    public static final String TAG_TITLE = "TAG_TITLE";
    public static final String TAG_CONTENT = "TAG_CONTENT";



    public NewsDetails() {
        // Required empty public constructor
    }

    public static NewsDetails newInstance(String title, String content) {

        Bundle args = new Bundle();
        args.putString(TAG_TITLE, title);
        args.putString(TAG_CONTENT, content);

        NewsDetails fragment = new NewsDetails();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment3_news_details, container, false);

        Bundle args = getArguments();
        String title = args.getString(TAG_TITLE);
        String content = args.getString(TAG_CONTENT);

        TextView titleTv = (TextView) v.findViewById(R.id.news_detail_title);
        TextView contentTv = (TextView) v.findViewById(R.id.news_detail_content);

        titleTv.setText(title);
        contentTv.setText(Html.fromHtml(content));



        return v;
    }

}
