package com.scowluga.android.microscience.products;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.scowluga.android.microscience.R;

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
                rowView = inflater.inflate(R.layout.fragment5_product_row, parent, false);
                return new ProductAdapter.ViewHolder(rowView, viewType);
            case ProductAdapter.VIEW_TYPES.Header:
                rowView = inflater.inflate(R.layout.fragment5_product_row_header, parent, false);
                return new ProductAdapter.ViewHolder(rowView, viewType);
            case ProductAdapter.VIEW_TYPES.Footer:
                rowView = inflater.inflate(R.layout.fragment5_product_row_footer, parent, false);
                return new ProductAdapter.ViewHolder(rowView, viewType);
            default:
                rowView = inflater.inflate(R.layout.fragment5_product_row, parent, false);
                return new ProductAdapter.ViewHolder(rowView, viewType);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleText;
        public ImageView iconView;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            switch (type) {
                case ProductAdapter.VIEW_TYPES.Normal:
                    titleText = (TextView) itemView.findViewById(R.id.product_title);
                    iconView = (ImageView) itemView.findViewById(R.id.product_icon);
                    itemView.setOnClickListener(this);
                    break;
                case VIEW_TYPES.Footer:
                    Button b = (Button)itemView.findViewById(R.id.product_search);
                    final EditText et = (EditText)itemView.findViewById(R.id.product_query);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(et.getText())) {
                                et.setError("Empty Search");
                            } else {
                                String url = "https://microscience.on.ca/?s=" + et.getText() + "&post_type=product";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                context.startActivity(intent);
                            }
                        }
                    });
            }
        }

        @Override
        public void onClick(View v) {
            Category category = categoryList.get(ProductFragment.rv.indexOfChild(v));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ProductFragment.FIRST_URL + category.link + "/"));
            context.startActivity(intent);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (!(category.isHeader || category.isFooter)) {
            holder.titleText.setText(category.title);
            holder.iconView.setImageDrawable(context.getResources().getDrawable(category.icon));
        }
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
