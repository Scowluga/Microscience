package com.scowluga.android.microscience.news;

/**
 * Created by scowluga on 5/24/2017.
 */

public class Post {

    String title;
    String content;
    String excerpt;

    public Post(String t, String c) {
        this.title = t;
        this.content = c;
    }

    public Post(String t, String c, String ex) {
        this.title = t;
        this.content = c;
        this.excerpt = ex;
    }
}
