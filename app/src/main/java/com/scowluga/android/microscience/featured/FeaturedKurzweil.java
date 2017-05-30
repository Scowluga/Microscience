package com.scowluga.android.microscience.featured;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.scowluga.android.microscience.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeaturedKurzweil extends Fragment {


    public FeaturedKurzweil() {
        // Required empty public constructor
    }

    public static FeaturedKurzweil newInstance() {

        Bundle args = new Bundle();

        FeaturedKurzweil fragment = new FeaturedKurzweil();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment4_featured_kurzweil, container, false);


        Button launch = (Button)v.findViewById(R.id.kurzweil_launch);
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://microscience.on.ca/kurzweil-3000-web-firefly/"));
                v.getContext().startActivity(intent);
            }
        });
        return v;
    }

}
