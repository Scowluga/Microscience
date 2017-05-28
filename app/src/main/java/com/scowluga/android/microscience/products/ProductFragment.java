package com.scowluga.android.microscience.products;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scowluga.android.microscience.MainActivity;
import com.scowluga.android.microscience.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {
    public ProductFragment() {
        // Required empty public constructor
    }
    public static ProductFragment newInstance() {
        Bundle args = new Bundle();
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static final String FIRST_URL = "https://microscience.on.ca/products/product-category/";
    public static RecyclerView rv;
    public static ProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment5_product, container, false);

        if (container == null) {
            return null;
        }

        List<Category> categories = Category.getCategories();
        rv = (RecyclerView)v.findViewById(R.id.product_rv);
        adapter = new ProductAdapter(categories, getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onResume() {
        MainActivity.toolbar.setTitle("Products");
        super.onResume();
    }
}
