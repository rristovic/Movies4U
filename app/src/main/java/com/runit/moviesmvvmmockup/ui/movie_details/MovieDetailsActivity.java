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

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.databinding.ActivityMovieDetailsBinding;
import com.runit.moviesmvvmmockup.ui.profile.login.LoginViewModel;
import com.runit.moviesmvvmmockup.utils.UIUtil;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String KEY_ID = "movie_id";
    private static final String KEY_NAME = "movie_name";
    private static final String KEY_THUMBNAIL = "movie_thumnail_url";
    private MovieDetailsViewModel mViewModel;

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
                if (mViewModel != null) {
                    onBookmarkClicked();
                }
                break;
            }
            case R.id.action_rate: {
                if (mViewModel != null) {
                    onRateMovieClicked();
                }
                break;
            }
            default: {
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void onBookmarkClicked() {
        ViewModelProviders.of(this).get(LoginViewModel.class).isUserLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn != null && isLoggedIn) {
                mViewModel.onBookmarkClicked(getApplicationContext());
            } else {
                UIUtil.showLoginDialog(MovieDetailsActivity.this, getString(R.string.login_to_use_feature_msg));
            }
        });
    }

    private void onRateMovieClicked() {
        ViewModelProviders.of(this).get(LoginViewModel.class).isUserLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn != null && isLoggedIn) {
                mViewModel.rateMovie(getApplicationContext(),0);
            } else {
                UIUtil.showLoginDialog(MovieDetailsActivity.this, getString(R.string.login_to_use_feature_msg));
            }
        });
    }


    public static void startActivity(Context context, long movieId, String movieName, String thumbnailUrl) {
        Intent i = new Intent(context, MovieDetailsActivity.class);
        i.putExtra(KEY_ID, movieId);
        i.putExtra(KEY_NAME, movieName);
        i.putExtra(KEY_THUMBNAIL, thumbnailUrl);
        context.startActivity(i);
    }
}

