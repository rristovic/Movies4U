package com.runit.moviesmvvmmockup.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.ui.main.movie_list.MovieListCategory;
import com.runit.moviesmvvmmockup.ui.main.movie_list.MovieListFragment;

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
                        case  R.id.nav_top_rated: {
                            mNavigationHelper.switchToPage(MovieListCategory.TOP_RATED);
                            break;
                        }
                        case R.id.nav_upcoming: {
                            mNavigationHelper.switchToPage(MovieListCategory.UPCOMING);
                            break;
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
        mSectionsPagerAdapter = new MoviesSectionPagerAdapter(getSupportFragmentManager(), mNavigationHelper.getTitles(), mNavigationHelper.getCategories());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MoviesSectionPagerAdapter extends FragmentStatePagerAdapter {
        String[] mTitles;
        MovieListCategory[] mCategories;

        private MoviesSectionPagerAdapter(FragmentManager fm, String[] titles, MovieListCategory[] categories) {
            super(fm);
            this.mTitles = titles;
            this.mCategories = categories;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return MovieListFragment.newInstance(mCategories[position]);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

    /**
     * Helper method for starting this activity.
     *
     * @param context Context used for sending a starting intent.
     */
    public static void startActivity(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }
}
