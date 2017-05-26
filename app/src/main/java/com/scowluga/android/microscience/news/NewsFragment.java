package com.scowluga.android.microscience.news;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scowluga.android.microscience.FirstRun;
import com.scowluga.android.microscience.MainActivity;
import com.scowluga.android.microscience.R;
import com.scowluga.android.microscience.wordpress.DataFetchAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

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
    public static final String fetchURL = "https://microscience.on.ca/wp-json/wp/v2/posts?fields=title,content";

    public static RecyclerView rv;
    public static NewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment3_news, container, false);

        // Swipe Refresh Layout
        SwipeRefreshLayout sl = (SwipeRefreshLayout)v.findViewById(R.id.news_swipe_refresh);
        sl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshStorage(getContext(), getActivity());
            }
        });

        postList = NewsProvider.getPosts(getContext());

        rv = (RecyclerView) v.findViewById(R.id.news_recycler);
        adapter = new NewsAdapter(postList, getContext());
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        return v;
    }

    public static void refreshStorage(final Context context, final Activity activity) {
        if (FirstRun.wifiOn(context)) {
            // Getting Data
            DataFetchAsyncTask asyncTask = (DataFetchAsyncTask) new DataFetchAsyncTask(activity, isRunning, fetchURL, new DataFetchAsyncTask.AsyncResponse(){
                @Override
                public void processFinish(String output){
                    List<Post> temp = parseNews(output);

                    if (isRunning) {
                        if (temp == postList) { // same
                            // do nothing
                        } else { // THERES DIFFERENCE
                            NewsProvider.rewriteContacts(context, temp);
                            postList = temp;

                            if (adapter == null) {
                                adapter = new NewsAdapter(temp, context);
                            } else {
                                adapter.postList = temp;
                                adapter.notifyDataSetChanged();
                            }

                            // re-attach fragment?
//                            AppCompatActivity act = (AppCompatActivity)getActivity();
//                            Fragment frag = act.getSupportFragmentManager().findFragmentByTag(MainActivity.TAGFRAGMENT);
//                            act.getSupportFragmentManager().beginTransaction()
//                                    .detach(frag)
//                                    .attach(frag)
//                                    .commit();
                        }
                    } else { // INITIALIZING
                        if (FirstRun.first) { // FIRST RUN
                            NewsProvider.rewriteContacts(context, temp);
                        } else { // initializing not first run
                            NewsProvider.rewriteContacts(context, temp);
                        }
                    }
                }
            }).execute();
        }
    }

    public static List<Post> parseNews(String output) {
        List<Post> info = new ArrayList<>();

        try{
            JSONArray jsonArray = new JSONArray(output);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject childObj = jsonArray.getJSONObject(i);
                String postTitle = new JSONObject(childObj.getString("title")).getString("rendered");
                String postContent = new JSONObject(childObj.getString("content")).getString("rendered");

                // PostContent formatting
                postContent = postContent.replace("\\/", "/");

                info.add(new Post(postTitle, postContent));
            }
        } catch(Exception e){
            Log.i ("App", "Error parsing data" + e.getMessage());
        }
        return info;
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
