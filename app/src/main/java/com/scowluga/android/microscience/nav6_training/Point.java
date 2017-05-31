package com.scowluga.android.microscience.nav6_training;

import com.scowluga.android.microscience.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by robertlu on 2017-05-20.
 */

public class Point {

    int icon;
    String title;
    String info;

    boolean isHeader;
    boolean isFooter;

    public Point(int i, String t, String in) {
        this.icon = i;
        this.title = t;
        this.info = in;
        isHeader = false;
        isFooter = false;
    }

    public Point(boolean one, boolean two) {
        isHeader = one;
        isFooter = two;
    }

    public static List<Point> initialize() {
        return new ArrayList<>(Arrays.asList(
                new Point(true, false),
                new Point(R.drawable.icon_chart, "Product Demos", "Our outside sales team does presentations all over Canada for small and large groups. These demonstrations typically include surveys of assistive technology for one type of disability, or individual product overviews for featured items. These overviews are completely free, and are a great way to orient yourself to the many assistive technology options available."),
                new Point(R.drawable.icon_student, "Training for Students", "We can provide assistive technology training to students of all ages and abilities on all the products we sell. We can train on site, in the home or at school. Our approach to training is student centered, informed by the belief that everyone is special and that their needs are unique. We develop our training approach to reflect that individuality, taking into consideration the input of students, parents and educational staff. The ultimate goal is greater independence through technology, and so our training is aimed at maximizing their abilities and getting the most out assistive technology they use."),
                new Point(R.drawable.nav_training, "Training in the Workforce", "For adults, accessing the workplace with a disability can be a challenge. While Assistive Technology can level the playing field, training can give an employee an extra edge in achieving success in the workplace. At MSC we feel training is essential to perform at your full potential. Please be aware that our trainers will come to your office and work with you to acquire the skills necessary to be an independent and productive member of society."),
                new Point(R.drawable.icon_accessible, "Our Training Staff", "Our training staff can instruct on a wide range of assistive technology devices and software. Our trainers are available throughout Canada and the United States to support almost every type of learner, and nearly any kind of disability. Whether it is a K-12 student, or a student in higher education, we can provide the professional support necessary to make the most of an accommodation, and to succeed in the classroom. For more in-depth training we can provide on-site training for nearly every application or device that we sell."),
                new Point(R.drawable.icon_wrench, "Our Technology Lab", "Our computer lab can accommodate up to 20 people, allowing for hands on instruction on assistive technology. Additionally, we have a smaller presentation room for meetings, and product demonstrations."),
                new Point(R.drawable.icon_computer, "Mobile Lab", "We can also take our training on the road. MSC now has a mobile computer lab that could be set up wherever you need it. We have 20 notebook stations available, and can custom configure the software to whatever the occasion requires."),
                new Point(R.drawable.icon_screenshare, "Distance Training", "We can also train clients in remote regions or at multiple sites using on-line meeting technology. If you need training on access technology, and you are not within driving distance of Mississauga office, please contact us to find out more about how we can accommodate your needs."),
                new Point(false, true)
        ));
    }
}
