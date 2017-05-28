package com.scowluga.android.microscience.products;

import com.scowluga.android.microscience.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by scowluga on 5/26/2017.
 */

public class Category {

    public String title;
    public String link;
    public int icon;
    boolean isHeader;
    boolean isFooter;

    public Category (String t, String l, int i) {
        this.title = t;
        this.link = l;
        this.icon = i;
    }

    public Category (boolean head, boolean foot) {
        this.isHeader = head;
        this.isFooter = foot;
    }

    public static List<Category> getCategories() {
        return new ArrayList<>(Arrays.asList(
                new Category(true, false),
                new Category("Autism and AAC", "autism-aac", R.drawable.icon_puzzle),
                new Category("Blind Access", "blind-access", R.drawable.icon_blind_access),
                new Category("Learning Differences", "learning-differences", R.drawable.icon_library_book),
                new Category("Low Vision", "low-vision", R.drawable.icon_low_vision),
                new Category("Produits Francophones", "produits-francophones", R.drawable.icon_language),
                new Category("Voice Recognition", "voice-recognition", R.drawable.icon_voice),
                new Category(false, true)
        ));
    }
}
