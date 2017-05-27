package com.scowluga.android.microscience.products;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.scowluga.android.microscience.R;
import com.scowluga.android.microscience.training.TrainingAdapter;

import java.util.List;

/**
 * Created by scowluga on 5/27/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    static List<Category> categoryList;
    static Context context;

    public ProductAdapter(List<Category> categories, Context c) {
        categoryList = categories;
        context = c;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View rowView;
        switch (viewType) {
            case ProductAdapter.VIEW_TYPES.Normal:
                rowView = inflater.inflate(R.layout.fragment4_training_row, parent, false);
                return new ProductAdapter.ViewHolder(rowView, true);
            case ProductAdapter.VIEW_TYPES.Header:
                rowView = inflater.inflate(R.layout.fragment4_training_row_header, parent, false);
                return new ProductAdapter.ViewHolder(rowView, false);
            case ProductAdapter.VIEW_TYPES.Footer:
                rowView = inflater.inflate(R.layout.fragment4_training_row_footer, parent, false);
                return new ProductAdapter.ViewHolder(rowView, false);
            default:
                rowView = inflater.inflate(R.layout.fragment4_training_row, parent, false);
                return new ProductAdapter.ViewHolder(rowView, true);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleText;
        public ImageView iconView;

        public ViewHolder(View itemView, boolean isNormal) {
            super(itemView);
            if (isNormal) {
                titleText = 
            }
        }

        @Override
        public void onClick(View v) {
            Category category = categoryList.get(ProductFragment.rv.indexOfChild(v));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(category.link));
            context.startActivity(intent);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    private class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(categoryList.get(position).isHeader)
            return ProductAdapter.VIEW_TYPES.Header;
        else if(categoryList.get(position).isFooter)
            return ProductAdapter.VIEW_TYPES.Footer;
        else
            return ProductAdapter.VIEW_TYPES.Normal;
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
