package com.scowluga.android.microscience.nav3_news;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scowluga.android.microscience.R;

import java.util.List;

/**
 * Created by scowluga on 5/24/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public static List<Post> postList;
    Context context;

    final NewsOnClickListener newsOnClickListener;

    public NewsAdapter (List<Post> posts, Context c, NewsOnClickListener onClickListener) {
        this.postList = posts;
        this.context = c;
        this.newsOnClickListener = onClickListener;
    }

    public interface NewsOnClickListener { // INTERFACE FOR CLICKING
        void onNewsItemClick(int pos, Post post, ImageView shareImageView, TextView title, TextView date, TextView content);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView titleText;
        public TextView dateText;
        public TextView contentText;
        public ImageView imageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleText = (TextView) itemView.findViewById(R.id.news_regular_title);
            dateText = (TextView) itemView.findViewById(R.id.news_regular_date);
            contentText = (TextView) itemView.findViewById(R.id.news_regular_content);
            imageView = (ImageView) itemView.findViewById(R.id.news_regular_image);
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
    public void onBindViewHolder(final NewsAdapter.ViewHolder holder, final int position) {
        final Post p = postList.get(position);

        holder.titleText.setText(p.title);
        ViewCompat.setTransitionName(holder.titleText, p.id + "title");

        holder.contentText.setText(Html.fromHtml(p.content));
        ViewCompat.setTransitionName(holder.contentText, p.id + "content");

        holder.dateText.setText(p.date);
        ViewCompat.setTransitionName(holder.dateText, p.id + "date");

        if (p.image.equals(Post.NO_IMAGE)) {
            holder.imageView.setVisibility(View.GONE);
        } else {
            holder.imageView.setImageBitmap(StorageManager.loadImageFromStorage(p.image, context));
            ViewCompat.setTransitionName(holder.imageView, p.id + "icon");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newsOnClickListener.onNewsItemClick(holder.getAdapterPosition(), p, holder.imageView, holder.titleText, holder.dateText, holder.contentText);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
