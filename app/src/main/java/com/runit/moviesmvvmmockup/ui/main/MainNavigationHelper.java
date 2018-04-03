package com.runit.moviesmvvmmockup.ui.main;

import android.content.res.Resources;
import android.support.v4.view.ViewPager;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.ui.movie_list.MovieListCategory;

/**
 * Created by Radovan Ristovic on 3/30/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

/**
 * Helper class that holds all available categories that will be in main navigation. Holds the reference to the title for each of the category.
 * Provides method for switching pages.
 */
class MainNavigationHelper {

    // Hold the titles for fragments
    final private String[] mTitles;
    // Hold categories for fragments
    final private MovieListCategory[] mCategories;
    private final ViewPager mPager;

    /**
     * Constructor.
     *
     * @param res   {@link Resources} object from which titles will be read.
     * @param pager {@link ViewPager} pager that holds already loaded fragments for categories provided by this helper.
     */
    MainNavigationHelper(Resources res, ViewPager pager) {
        this.mPager = pager;
        this.mTitles = res.getStringArray(R.array.movie_categories);
        // Creating categories for movie titles, num of titles = num of categories
        this.mCategories = new MovieListCategory[]{MovieListCategory.TOP_RATED, MovieListCategory.UPCOMING, MovieListCategory.NOW_PLAYING, MovieListCategory.POPULAR};
    }

    /**
     * Retrieve the title string for fragment for provided page position.
     * @param position page position. Cannot be greater than result from ({@link #getNumberOfCategories()} - 1).
     * @return title string.
     */
    String getTitleForPosition(int position) {
        return mTitles[position];
    }

    /**
     * Retrieve the movie category for provided page position.
     * @param position page position. Cannot be greater than result from ({@link #getNumberOfCategories()} - 1).
     * @return movie category.
     */
    MovieListCategory getCategoryForPosition(int position) {
        return mCategories[position];
    }

    /**
     * Retrieve total number of available movie's categories.
     * @return number of categories.
     */
    int getNumberOfCategories() {
        return mCategories.length;
    }

    /**
     * Switch to correct page for provided category.
     *
     * @param category of movies that need to be displayed.
     */
    void switchToPage(MovieListCategory category) {
        for (int i = mCategories.length - 1; i >= 0; i--) {
            if (mCategories[i].equals(category)) {
                mPager.setCurrentItem(i);
                break;
            }
        }
    }
}
