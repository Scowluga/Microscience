package com.scowluga.android.microscience.training;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scowluga.android.microscience.MainActivity;
import com.scowluga.android.microscience.R;

import java.util.List;

/**
 * Created by robertlu on 2017-05-20.
 */
public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {

    List<Point> pointList;
    Context context;

    public TrainingAdapter (List<Point> points, Context c) {
        this.pointList = points;
        this.context = c;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView iconImage;
        public TextView titleText;
        public TextView infoText;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            iconImage = (ImageView) itemView.findViewById(R.id.training_icon);
            titleText = (TextView) itemView.findViewById(R.id.training_title);
            infoText = (TextView) itemView.findViewById(R.id.training_info);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View rowView;
        switch (viewType)
        {
            case VIEW_TYPES.Normal:
                rowView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment4_training_row, parent, false);
                break;
            case VIEW_TYPES.Header:
                rowView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment4_training_row_header, parent, false);
                break;
            case VIEW_TYPES.Footer:
                rowView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment4_training_row_footer, parent, false);
                Button website = (Button)rowView.findViewById(R.id.training_website);
                website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.websiteLaunch(context);
                    }
                });

                Button contact = (Button)rowView.findViewById(R.id.training_contact);
                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.contactLaunch(context);
                    }
                });
                break;
            default:
                rowView=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment4_training_row, parent, false);
                break;
        }
        return new ViewHolder (rowView);
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
}
