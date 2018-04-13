package com.runit.moviesmvvmmockup.ui.bookmarks;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.databinding.ActivityBookmarksBinding;
import com.runit.moviesmvvmmockup.ui.movie_details.MovieDetailsActivity;
import com.runit.moviesmvvmmockup.utils.UIUtil;

public class BookmarksActivity extends AppCompatActivity {

    private BookmarksViewModel mViewModel;
    private ActivityBookmarksBinding mBinding;
    private BookmarksAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle(getString(R.string.bookmarked_movies_title));
        super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bookmarks);
        mAdapter = new BookmarksAdapter();
        mAdapter.setItemClickListener((parent, view, position, id) -> {
            MovieModel movieModel = mAdapter.getItem(position);
            if (movieModel != null)
                MovieDetailsActivity.startActivity(BookmarksActivity.this, movieModel.getId(), movieModel.getDisplayTitle());
        });
        mBinding.rvList.setAdapter(mAdapter);
        mBinding.rvList.setLayoutManager(new LinearLayoutManager(this));
        mViewModel = ViewModelProviders.of(this).get(BookmarksViewModel.class);
        mBinding.setBookmarksViewModel(mViewModel);
        mViewModel.getBookmarkedMovies().observe(this, listResult -> {
            if (listResult != null)
                if (listResult.isSuccess()) {
                    mAdapter.addData(listResult.get());
                } else {
                    UIUtil.showShortToast(this, listResult.error().getMessage());
                }
        });
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, BookmarksActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
