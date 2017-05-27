package com.scowluga.android.microscience.news;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scowluga.android.microscience.MainActivity;
import com.scowluga.android.microscience.R;

import java.util.List;

/**
 * Created by scowluga on 5/24/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public static List<Post> postList;
    Context context;

    public NewsAdapter (List<Post> posts, Context c) {
        this.postList = posts;
        this.context = c;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView titleText;
        public TextView contentText;
        public ImageView imageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleText = (TextView) itemView.findViewById(R.id.news_regular_title);
            contentText = (TextView) itemView.findViewById(R.id.news_regular_content);
            imageView = (ImageView) itemView.findViewById(R.id.news_regular_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Post p = postList.get(NewsFragment.rv.indexOfChild(v));
            Fragment frag = NewsDetails.newInstance(Post.encode(p));
            FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
            manager.beginTransaction()
                    .hide(manager.findFragmentByTag(MainActivity.TAGFRAGMENT))
                    .add(R.id.frag_layout, frag, MainActivity.TAGFRAGMENT)
                    .addToBackStack(MainActivity.TAGFRAGMENT)
                    .commit();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.fragment3_news_row, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        Post p = postList.get(position);
        holder.titleText.setText(p.title);
        String content = p.content;
        holder.contentText.setText(Html.fromHtml(content));
        if (p.image.equals(Post.NO_IMAGE)) {
            holder.imageView.setVisibility(View.GONE);
        } else {
            holder.imageView.setImageBitmap(StorageManager.loadImageFromStorage(p.image, context));
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
