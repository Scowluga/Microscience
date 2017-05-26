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
    String excerpt;

    // CONSTRUCTORS 
    public Post(String t, String c) {
        this.title = t;
        this.content = c;
    }
    public Post(String t, String c, String ex) {
        this.title = t;
        this.content = c;
        this.excerpt = ex;
    }
  
  
    // STATIC FUNCTIONALITY
  
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

    public static String grandEncode (List<Post> postList) {
        String ret = "";

        for (int i = 0; i < postList.size() - 1; i ++) {
            ret += encode(postList.get(i)) + GRAND_DELIM;
        }
        ret += encode(postList.get(postList.size() - 1));
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
}
