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

        String fetchURL = "https://microscience.on.ca/wp-json/wp/v2/posts?fields=title,content";
        DataFetchAsyncTask asyncTask = (DataFetchAsyncTask) new DataFetchAsyncTask(getActivity(), fetchURL, new DataFetchAsyncTask.AsyncResponse(){
            @Override
            public void processFinish(String output){
                Toast.makeText(getContext(), output, Toast.LENGTH_SHORT).show();
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
            JSONObject jsonResponse = new JSONObject(output);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("posts");
            int postCount = Integer.parseInt(jsonResponse.getString("count"));

            int jsonArrLength = jsonMainNode.length();

            for(int i = 0; i < jsonArrLength; i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String postTitle = jsonChildNode.getString("title");
                String postUrl = jsonChildNode.getString("content");

//                tvPostCount.setText("Number of posts:" +postCount);
//                tvPostTitle.setText("Page title:" +postTitle);
//                tvPostUrl.setText("Page PARSE_URL:" +postUrl);
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
