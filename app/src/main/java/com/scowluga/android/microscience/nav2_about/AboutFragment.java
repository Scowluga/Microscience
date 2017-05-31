package com.scowluga.android.microscience.nav2_about;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scowluga.android.microscience.MainActivity;
import com.scowluga.android.microscience.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {

        Bundle args = new Bundle();

        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment2_about, container, false);



        return v;
    }

    @Override
    public void onResume() {
        MainActivity.toolbar.setTitle("About");
        super.onResume();
    }

}
