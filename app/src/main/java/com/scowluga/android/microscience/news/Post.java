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

    String id;
    String title;
    String content;
    String date;
    String link;
    String image;

    // CONSTRUCTORS

    public Post (String i) {
        this.id = i;
    }

    public Post(String i, String t, String c, String d, String l, String im) {
        this.id = i;
        this.title = t;
        this.content = c;
        this.date = d;
        this.link = l;
        this.image = im;
    }
  
    // STATIC FUNCTIONALITY
  
    public static String encode (Post p) {
        String ret = p.id + SMALL_DELIM
                + p.title + SMALL_DELIM
                + p.content + SMALL_DELIM
                + p.date + SMALL_DELIM
                + p.link + SMALL_DELIM
                + p.image;
        return ret;
    }

    public static Post decode (String s) {
        List<String> characters = new ArrayList<>(Arrays.asList(s.split(SMALL_DELIM)));
        Post p = new Post(characters.get(0), characters.get(1), characters.get(2), characters.get(3), characters.get(4), characters.get(5));
        return p;
    }

    public static String grandEncode (List<Post> postList) {
        String ret = "";

        for (int i = 0; i < postList.size() - 1; i ++) {
            ret += encode(postList.get(i)) + GRAND_DELIM;
        }
        if (postList.size() > 0) {
            ret += encode(postList.get(postList.size() - 1));
        }
        return ret;
    }

    public static List<Post> grandDecode (String file) {
        List<String> posts = new ArrayList<>(Arrays.asList(file.split(GRAND_DELIM)));
        List<Post> postList = new ArrayList<>();
        for (String post : posts) {
            postList.add(decode(post));
        }
        return postList;
    }

    // OTHER

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Post) {
            Post p = (Post)obj;
            return p.id == this.id;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.id + " " + this.title;
    }
}
