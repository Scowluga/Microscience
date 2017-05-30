package com.scowluga.android.microscience.news;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scowluga.android.microscience.MainActivity;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment3_news_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        final Post post = Post.decode(args.getString(TAG_POST));

        TextView titleTv = (TextView) view.findViewById(R.id.news_detail_title);
        titleTv.setText(post.title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            titleTv.setTransitionName(post.id + "title");
        }

        TextView dateTv = (TextView) view.findViewById(R.id.news_detail_date);
        dateTv.setText(post.date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dateTv.setTransitionName(post.id + "date");
        }

        TextView contentTv = (TextView) view.findViewById(R.id.news_detail_content);
        contentTv.setText(Html.fromHtml(post.content));
        contentTv.setMovementMethod(LinkMovementMethod.getInstance());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            contentTv.setTransitionName(post.id + "content");
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.news_detail_image);
        if (post.image.equals(Post.NO_IMAGE)) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageBitmap(StorageManager.loadImageFromStorage(post.image, getContext()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setTransitionName(post.id + "icon");
            }
            startPostponedEnterTransition();
        }

        Button linkBtn = (Button) view.findViewById(R.id.news_detail_link);
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = post.link;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        ActionBar tb = ((AppCompatActivity) getActivity()).getSupportActionBar();

        // Display as false
        tb.setDisplayHomeAsUpEnabled(false);
        tb.setDisplayShowHomeEnabled(false);

        // RESETTING THE TOOLBAR
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), MainActivity.drawer, MainActivity.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        MainActivity.drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar tb = ((AppCompatActivity) getActivity()).getSupportActionBar();
        tb.setDisplayHomeAsUpEnabled(true);
        tb.setDisplayShowHomeEnabled(true); 
        MainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go back
                ((AppCompatActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }
}
