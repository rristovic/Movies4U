package com.runit.moviesmvvmmockup.ui.movie_details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.databinding.ActivityMovieDetailsBinding;
import com.runit.moviesmvvmmockup.utils.UIUtil;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String KEY_ID = "movie_id";
    private static final String KEY_NAME = "movie_name";
    private static final String KEY_THUMBNAIL = "movie_thumbnail_url";
    private MovieDetailsViewModel mViewModel;
    private MenuItem mBookmarkMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        // setup toolbar
        super.setSupportActionBar(binding.toolbar);
        super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // setup view model
        mViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        binding.setMovieDetailsViewModel(mViewModel);
        mViewModel.getMovie(getIntent().getLongExtra(KEY_ID, -1))
                .observe(this, movieModel -> {
                    if (movieModel.isSuccess()) {
                        binding.setMovie(movieModel.get());
                    } else {
                        UIUtil.showToast(MovieDetailsActivity.this, movieModel.error().getMessage());
                    }
                });
        mViewModel.onToastMessage().observe(this, msg -> UIUtil.showShortToast(MovieDetailsActivity.this, msg));
        // init UI data
        Picasso picasso = new Picasso.Builder(this).build();
        picasso.load(getIntent().getStringExtra(KEY_THUMBNAIL)).into(binding.ivMovieThumbnail);
        binding.tvMovieTitle.setText(getIntent().getStringExtra(KEY_NAME));
        binding.vgHomepage.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(binding.getMovie().getHomepage()));
            v.getContext().startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        mBookmarkMenuItem = menu.findItem(R.id.action_bookmark);
        if (mViewModel != null) {
            mViewModel.isBookmarked().observe(this, isBookmarked -> {
                if (isBookmarked != null && isBookmarked) {
                    mBookmarkMenuItem.setIcon(getResources().getDrawable(R.drawable.bookmark_filled));
                } else {
                    mBookmarkMenuItem.setIcon(getResources().getDrawable(R.drawable.bookmark));
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                super.onBackPressed();
                break;
            }
            case R.id.action_bookmark: {
                // For simplicity, implementing here instead of in ViewModel;
                if (mViewModel != null && mViewModel.isAccountFeatureAvailable()) {
                    mViewModel.onBookmarkPressed();
                } else {
                    // must login first
                    UIUtil.showLoginDialog(MovieDetailsActivity.this, getString(R.string.login_to_use_feature_msg));
                }
                break;
            }
            case R.id.action_rate: {
                // For simplicity, implementing here instead of in ViewModel;
                if (mViewModel != null && mViewModel.isAccountFeatureAvailable()) {

                } else {
                    // must login first
                    UIUtil.showLoginDialog(MovieDetailsActivity.this, getString(R.string.login_to_use_feature_msg));
                }
                break;
            }
            default: {
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public static void startActivity(Context context, long movieId, String movieName, String thumbnailUrl) {
        Intent i = new Intent(context, MovieDetailsActivity.class);
        i.putExtra(KEY_ID, movieId);
        i.putExtra(KEY_NAME, movieName);
        i.putExtra(KEY_THUMBNAIL, thumbnailUrl);
        context.startActivity(i);
    }
}

