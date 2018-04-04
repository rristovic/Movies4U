package com.runit.moviesmvvmmockup.ui.movie_details;

import android.annotation.TargetApi;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.databinding.ActivityMovieDetailsBinding;
import com.runit.moviesmvvmmockup.utils.UIUtil;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String KEY_ID = "movie_id";
    private static final String KEY_NAME = "movie_name";
    private static final String KEY_THUMBNAIL = "movie_thumnail_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        themeNavAndStatusBar(this);
        ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        MovieDetailsViewModel viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        binding.setMovieDetailsViewModel(viewModel);
        viewModel.getMovie(getIntent().getLongExtra(KEY_ID, -1), error -> UIUtil.showToast(MovieDetailsActivity.this, error.getMessage()))
                .observe(this, movieModel -> {
                    if (movieModel != null) {
                        binding.setMovie(movieModel);
                    }
                });
        Picasso.get().load(getIntent().getStringExtra(KEY_THUMBNAIL)).into(binding.ivMovieThumbnail);
        binding.tvMovieTitle.setText(getIntent().getStringExtra(KEY_NAME));
    }

    public static void startActivity(Context context, long movieId, String movieName, String thumbnailUrl) {
        Intent i = new Intent(context, MovieDetailsActivity.class);
        i.putExtra(KEY_ID, movieId);
        i.putExtra(KEY_NAME, movieName);
        i.putExtra(KEY_THUMBNAIL, thumbnailUrl);
        context.startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void themeNavAndStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;

        Window w = activity.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        w.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
    }

    private int fixToolbarHeight() {
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop =
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        return 0;
    }
}

