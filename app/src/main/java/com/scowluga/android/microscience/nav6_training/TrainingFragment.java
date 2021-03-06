package com.scowluga.android.microscience.nav6_training;


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
public class TrainingFragment extends Fragment {

    public TrainingFragment() {
        // Required empty public constructor
    }

    public static TrainingFragment newInstance() {

        Bundle args = new Bundle();

        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    List<Point> points;
    public static RecyclerView recyclerView;
    public static TrainingAdapter trainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment6_training, container, false);

        if (container == null) {
            return null;
        }

        points = Point.initialize();

        recyclerView = (RecyclerView)v.findViewById(R.id.training_recycler);
        trainer = new TrainingAdapter(points, getContext());
        recyclerView.setAdapter(trainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onResume() {
        MainActivity.toolbar.setTitle("Training");
        super.onResume();
    }
}
