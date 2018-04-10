package com.runit.moviesmvvmmockup.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.data.model.MovieListCategory;
import com.runit.moviesmvvmmockup.ui.movie_list.MovieListFragment;
import com.runit.moviesmvvmmockup.ui.profile.ProfileActivity;
import com.runit.moviesmvvmmockup.ui.search.SearchActivity;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */
public class MainActivity extends AppCompatActivity {

    private MoviesSectionPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private MainNavigationHelper mNavigationHelper;
    private SearchView mSvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();
                    switch (menuItem.getItemId()) {
                        case R.id.nav_now_playing: {
                            mNavigationHelper.switchToPage(MovieListCategory.NOW_PLAYING);
                            break;
                        }
                        case R.id.nav_popular: {
                            mNavigationHelper.switchToPage(MovieListCategory.POPULAR);
                            break;
                        }
                        case R.id.nav_top_rated: {
                            mNavigationHelper.switchToPage(MovieListCategory.TOP_RATED);
                            break;
                        }
                        case R.id.nav_upcoming: {
                            mNavigationHelper.switchToPage(MovieListCategory.UPCOMING);
                            break;
                        }
                        case R.id.nav_my_profile: {
                            ProfileActivity.startActivity(MainActivity.this);
                        }
                    }
                    return true;
                });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the ViewPager with the TabLayout.
        mViewPager = findViewById(R.id.container);
        mNavigationHelper = new MainNavigationHelper(getResources(), mViewPager);
        mSectionsPagerAdapter = new MoviesSectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Init search
        mSvSearch = findViewById(R.id.sv_search);
        mSvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchActivity.startActivity(MainActivity.this, query);
                // Clear search view and close
                mSvSearch.setQuery(null, false);
                mSvSearch.clearFocus();
                mSvSearch.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
        } else
            super.onBackPressed();
    }

    /**
     * Adapter responsible for instantiating fragments that hold the movie list.
     */
    public class MoviesSectionPagerAdapter extends FragmentStatePagerAdapter {

        private MoviesSectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return MovieListFragment.newInstance(mNavigationHelper.getCategoryForPosition(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mNavigationHelper.getNumberOfCategories();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mNavigationHelper.getTitleForPosition(position);
        }
    }
}
