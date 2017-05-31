package com.scowluga.android.microscience.nav6_training;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scowluga.android.microscience.R;

import java.util.List;

/**
 * Created by robertlu on 2017-05-20.
 */
public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {

    List<Point> pointList;
    static Context context;

    public TrainingAdapter (List<Point> points, Context c) {
        this.pointList = points;
        this.context = c;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView iconImage;
        public TextView titleText;
        public TextView infoText;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, boolean isNormal) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            if (isNormal) {
                iconImage = (ImageView) itemView.findViewById(R.id.training_icon);
                titleText = (TextView) itemView.findViewById(R.id.training_title);
                infoText = (TextView) itemView.findViewById(R.id.training_info);

                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            ImageView drop = (ImageView)v.findViewById(R.id.training_drop);
            LinearLayout l = (LinearLayout) v.findViewById(R.id.training_hidden);
            if (l.getVisibility() == View.GONE) {
                expand(l);
                drop.setImageDrawable(context.getResources().getDrawable(R.drawable.drop_up));
            } else if (l.getVisibility() == View.VISIBLE) {
                drop.setImageDrawable(context.getResources().getDrawable(R.drawable.drop_down));
                collapse(l);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View rowView;
        switch (viewType) {
            case VIEW_TYPES.Normal:
                rowView = inflater.inflate(R.layout.fragment6_training_row, parent, false);
                return new ViewHolder (rowView, true);
            case VIEW_TYPES.Header:
                rowView = inflater.inflate(R.layout.fragment6_training_row_header, parent, false);
                return new ViewHolder (rowView, false);
            case VIEW_TYPES.Footer:
                rowView = inflater.inflate(R.layout.fragment6_training_row_footer, parent, false);
                return new ViewHolder (rowView, false);
            default:
                rowView = inflater.inflate(R.layout.fragment6_training_row, parent, false);
                return new ViewHolder (rowView, true);
        }
    }

    @Override
    public void onBindViewHolder(TrainingAdapter.ViewHolder holder, int position) {
        Point p = pointList.get(position);
        if(!(p.isHeader || p.isFooter)) {
            ImageView icon = holder.iconImage;
            icon.setImageDrawable(context.getResources().getDrawable(p.icon));
            TextView title = holder.titleText;
            title.setText(p.title);
            TextView info = holder.infoText;
            info.setText(p.info);
        }
    }

    @Override
    public int getItemCount() {
        return pointList.size();
    }

    private class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(pointList.get(position).isHeader)
            return VIEW_TYPES.Header;
        else if(pointList.get(position).isFooter)
            return VIEW_TYPES.Footer;
        else
            return VIEW_TYPES.Normal;
    }



    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
