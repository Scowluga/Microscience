package com.scowluga.android.microscience.training;

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
}
