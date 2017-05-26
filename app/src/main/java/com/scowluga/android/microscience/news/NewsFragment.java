package com.scowluga.android.microscience.news;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment3_news, container, false);

        String fetchURL = "https://microscience.on.ca/wp-json/wp/v2/posts?fields=title,content,excerpt";
        DataFetchAsyncTask asyncTask = (DataFetchAsyncTask) new DataFetchAsyncTask(getActivity(), fetchURL, new DataFetchAsyncTask.AsyncResponse(){
            @Override
            public void processFinish(String output){
                postList = parseNews(output);
                RecyclerView rv = (RecyclerView) v.findViewById(R.id.news_recycler);
                NewsAdapter adapter = new NewsAdapter(postList, getContext());
                rv.setAdapter(adapter);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                rv.setLayoutManager(llm);
            }
        }).execute();
        return v;
    }

    private List<Post> parseNews(String output) {
        List<Post> info = new ArrayList<>();

        try{
            JSONArray jsonArray = new JSONArray(output);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject childObj = jsonArray.getJSONObject(i);
                String postTitle = new JSONObject(childObj.getString("title")).getString("rendered");
                String postContent = new JSONObject(childObj.getString("excerpt")).getString("rendered");

                // PostContent formatting
                postContent = postContent.replace("\\/", "/");


                info.add(new Post(postTitle, postContent));
            }

        }catch(Exception e){
            Log.i("App", "Error parsing data" +e.getMessage());
        }

        return info;
    }

    @Override
    public void onResume() {
        MainActivity.toolbar.setTitle("News");
        super.onResume();
    }


}
