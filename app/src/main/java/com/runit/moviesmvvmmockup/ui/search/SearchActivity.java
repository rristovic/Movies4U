package com.runit.moviesmvvmmockup.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.databinding.ActivitySearchBinding;
import com.runit.moviesmvvmmockup.ui.movie_details.MovieDetailsActivity;
import com.runit.moviesmvvmmockup.ui.movie_list.MovieListAdapter;
import com.runit.moviesmvvmmockup.utils.UIUtil;

public class SearchActivity extends AppCompatActivity {
    private SearchView mSvSearch;

    private static final String KEY_SEARCH_QUERY = "search_query";
    private SearchViewModel mViewModel;
    private MovieListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // init VM
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        binding.setSearchVM(mViewModel);
        mViewModel.getSearchResults().observe(this, moviesResult -> {
            if (moviesResult.isSuccess()) {
                if (moviesResult.get().size() == 0)
                    mAdapter.onLoadMoreComplete();
                mAdapter.addData(moviesResult.get());
            } else {
                UIUtil.showShortToast(SearchActivity.this, moviesResult.error().getMessage());
                mAdapter.onLoadMoreComplete();
            }
        });
        // init recycleView & adapter
        mAdapter = new MovieListAdapter(binding.rvMovies, new LinearLayoutManager(this), (parent, view, position, id) -> {
            MovieModel movieModel = mAdapter.getItem(position);
            MovieDetailsActivity.startActivity(SearchActivity.this, movieModel.getId(), movieModel.getTitle(), movieModel.getThumbnailUrl());
        });
        binding.rvMovies.setAdapter(mAdapter);
        // init search
        mSvSearch = findViewById(R.id.sv_search);
        mSvSearch.post(() -> {
            // Hide initial view focus
            mSvSearch.clearFocus();
            if (mSvSearch != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(mSvSearch.getWindowToken(), 0);
            }
        });
        mSvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Reset adapter data
                mAdapter.clearData();
                // set load more listener
                mAdapter.setOnLoadMoreListener(mViewModel::getNextPage);
                // Send new search query
                mViewModel.search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // set initial search
        mSvSearch.post(() -> mSvSearch.setQuery(getIntent().getStringExtra(KEY_SEARCH_QUERY), true));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startActivity(Context context, String searchQuery) {
        Intent i = new Intent(context, SearchActivity.class);
        i.putExtra(KEY_SEARCH_QUERY, searchQuery);
        context.startActivity(i);
    }
}
