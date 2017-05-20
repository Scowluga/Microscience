package com.scowluga.android.microscience.training;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scowluga.android.microscience.MainActivity;
import com.scowluga.android.microscience.R;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment4_training, container, false);

        if (container == null) {
            return null;
        }
        initialize();

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.training_recycler);
        TrainingAdapter trainer = new TrainingAdapter(points, getContext());
        recyclerView.setAdapter(trainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Point p = points.get(position);
                        if (p.isHeader || p.isFooter) {

                        } else {
                            LinearLayout l = (LinearLayout) view.findViewById(R.id.training_hidden);
                            if (l.getVisibility() == View.GONE) {
                                expand(l);
                            } else if (l.getVisibility() == View.VISIBLE){
                                collapse(l);
                            }
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toast.makeText(getContext(), "Long!", Toast.LENGTH_SHORT).show();
                    }
                })
        );

        return v;
    }

    private void initialize() {
        points = new ArrayList<>(Arrays.asList(
                new Point(true, false),
                new Point(R.drawable.icon_chart, "Product Demos", "Our outside sales team does presentations all over Canada for small and large groups. These demonstrations typically include surveys of assistive technology for one type of disability, or individual product overviews for featured items. These overviews are completely free, and are a great way to orient yourself to the many assistive technology options available."),
                new Point(R.drawable.icon_student, "Training for Students", "We can provide assistive technology training to students of all ages and abilities on all the products we sell. We can train on site, in the home or at school. Our approach to training is student centered, informed by the belief that everyone is special and that their needs are unique. We develop our training approach to reflect that individuality, taking into consideration the input of students, parents and educational staff. The ultimate goal is greater independence through technology, and so our training is aimed at maximizing their abilities and getting the most out assistive technology they use."),
                new Point(R.drawable.nav_training, "Training in the Workforce", "For adults, accessing the workplace with a disability can be a challenge. While Assistive Technology can level the playing field, training can give an employee an extra edge in achieving success in the workplace. At MSC we feel training is essential to perform at your full potential. Please be aware that our trainers will come to your office and work with you to acquire the skills necessary to be an independent and productive member of society."),
                new Point(R.drawable.icon_accessible, "Our Training Staff", "Our training staff can instruct on a wide range of assistive technology devices and software. Our trainers are available throughout Canada and the United States to support almost every type of learner, and nearly any kind of disability. Whether it is a K-12 student, or a student in higher education, we can provide the professional support necessary to make the most of an accommodation, and to succeed in the classroom. For more in-depth training we can provide on-site training for nearly every application or device that we sell."),
                new Point(R.drawable.icon_wrench, "Our Technology Lab", "Our computer lab can accommodate up to 20 people, allowing for hands on instruction on assistive technology. Additionally, we have a smaller presentation room for meetings, and product demonstrations."),
                new Point(R.drawable.icon_computer, "Mobile Lab", "We can also take our training on the road. MSC now has a mobile computer lab that could be set up wherever you need it. We have 20 notebook stations available, and can custom configure the software to whatever the occasion requires."),
                new Point(R.drawable.icon_screenshare, "Distance Training", "We can also train clients in remote regions or at multiple sites using on-line meeting technology. If you need training on access technology, and you are not within driving distance of Mississauga office, please contact us to find out more about how we can accommodate your needs.")
//                new Point(false, true)
        ));
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
