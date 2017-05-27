package com.scowluga.android.microscience.news;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scowluga.android.microscience.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetails extends Fragment {

    public static final String TAG_POST = "TAG_POST";

    public NewsDetails() {
        // Required empty public constructor
    }

    public static NewsDetails newInstance(String post) {

        Bundle args = new Bundle();
        args.putString(TAG_POST, post);

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
        final Post post = Post.decode(args.getString(TAG_POST));

        TextView titleTv = (TextView) v.findViewById(R.id.news_detail_title);
        titleTv.setText(post.title);

        TextView dateTv = (TextView) v.findViewById(R.id.news_detail_date);
        dateTv.setText(post.date);

        TextView contentTv = (TextView) v.findViewById(R.id.news_detail_content);
        contentTv.setText(Html.fromHtml(post.content));

        ImageView imageView = (ImageView) v.findViewById(R.id.news_detail_image);
        if (post.image.equals(Post.NO_IMAGE)) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageBitmap(StorageManager.loadImageFromStorage(post.image, getContext()));
        }

        Button linkBtn = (Button) v.findViewById(R.id.news_detail_link);
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = post.link;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                v.getContext().startActivity(intent);
            }
        });

        return v;
    }

}
