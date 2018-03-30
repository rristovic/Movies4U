package com.runit.moviesmvvmmockup.ui.main;

import android.content.res.Resources;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.ui.main.movie_list.MovieListCategory;

/**
 * Created by Radovan Ristovic on 3/30/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MainNavigationHelper {

    private Resources mRes;
    // Hold the titles for fragments
    final private String[] mTitles;
    // Hold categories for fragments
    final private MovieListCategory[] mCategories;

    public MainNavigationHelper(Resources res) {
        this.mRes = res;
        this.mTitles = res.getStringArray(R.array.movie_categories);
        // Creating categories for movie titles, num of titles = num of categories
        this.mCategories = new MovieListCategory[]{MovieListCategory.TOP_RATED, MovieListCategory.UPCOMING, MovieListCategory.NOW_PLAYING, MovieListCategory.POPULAR};
    }

    public String[] getTitles() {
        return mTitles;
    }

    public MovieListCategory[] getCategories() {
        return mCategories;
    }
}
