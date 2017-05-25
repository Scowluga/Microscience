package com.scowluga.android.microscience.news;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by scowluga on 5/24/2017.
 */

public class Post {

    public static final String GRAND_DELIM = "XXXXX";
    public static final String SMALL_DELIM = "-----";


    String title;
    String content;

    public Post(String t, String c) {
        this.title = t;
        this.content = c;
    }

    public static String encode (Post p) {
        String ret = "";
        ret += p.title + SMALL_DELIM
                + p.content;
        return ret;
    }

    public static Post decode (String s) {
        List<String> characters = new ArrayList<>(Arrays.asList(s.split(SMALL_DELIM)));
        Post p = new Post(characters.get(0), characters.get(1));
        return p;
    }
}
