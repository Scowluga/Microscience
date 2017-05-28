package com.scowluga.android.microscience.news;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scowluga.android.microscience.FirstRun;
import com.scowluga.android.microscience.MainActivity;
import com.scowluga.android.microscience.R;
import com.scowluga.android.microscience.wordpress.DataFetchAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements NewsAdapter.NewsOnClickListener {

    public NewsFragment() {
        // Required empty public constructor
    }
    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ACTUAL FUNCTIONS

    public static List<Post> postList;
    public static boolean isRunning = false;
    public static final String fetchURL = "https://microscience.on.ca/wp-json/wp/v2/posts?fields=title,content,link,id,date,featured_media";

    public static RecyclerView rv;
    public static NewsAdapter adapter;

    public static SwipeRefreshLayout sl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment3_news, container, false);

        // Swipe Refresh Layout
        sl = (SwipeRefreshLayout)v.findViewById(R.id.news_swipe_refresh);
        sl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshStorage(getContext(), getActivity());
            }
        });

        postList = NewsProvider.getPosts(getContext());

        rv = (RecyclerView) v.findViewById(R.id.news_recycler);
        adapter = new NewsAdapter(postList, getContext(), this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    public static void refreshStorage(final Context context, final Activity activity) {
        sl.setRefreshing(true);
        if (FirstRun.wifiOn(context)) {
            // Getting Data
            DataFetchAsyncTask asyncTask = (DataFetchAsyncTask) new DataFetchAsyncTask(postList, activity, isRunning, fetchURL, new DataFetchAsyncTask.AsyncResponse() {
                @Override
                public void processFinish(Integer output) {
                    switch (output) {
                        case DataFetchAsyncTask.KEY_EMPTY:
                            // nothing
                            break;
                        case DataFetchAsyncTask.KEY_NEW:
                            NewsReset(context);
                            break;
                        case DataFetchAsyncTask.KEY_SAME:
                            // do nothing
                            break;
                    }
                    sl.setRefreshing(false);
                }
            }).execute();
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sl.setRefreshing(false);
                }
            }, 1000);
        }
    }

    @Override
    public void onNewsItemClick(int pos, Post post, ImageView shareImageView, TextView title, TextView date, TextView content) { // THE CLICK
        Post p = postList.get(pos);
        Fragment frag = NewsDetails.newInstance(Post.encode(p));
        FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        manager.beginTransaction()
                .addSharedElement(shareImageView, p.id + "icon")
                .addSharedElement(title, p.id + "title")
                .addSharedElement(date, p.id + "date")
                .addSharedElement(content, p.id + "content")
                .addToBackStack(MainActivity.TAGFRAGMENT)
                .replace(R.id.frag_layout, frag, MainActivity.TAGFRAGMENT)
                .commit();
    }

    public static void NewsReset(Context context) {
        List<Post> temp = NewsProvider.getPosts(context);
        if (isRunning) { // RESETTING ADAPTER + notifyDataSetChanged();
            if (adapter == null) {
                // it should never be
            } else {
                adapter.postList = temp;
                adapter.notifyDataSetChanged();
            }
        } else { // not running
            // Do nothing.
        }
    }

    @Override
    public void onResume() {
        MainActivity.toolbar.setTitle("News");
        MainActivity.action_refresh.setVisible(true);
        isRunning = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        MainActivity.action_refresh.setVisible(false);
        isRunning = false;
        super.onPause();
    }
}
