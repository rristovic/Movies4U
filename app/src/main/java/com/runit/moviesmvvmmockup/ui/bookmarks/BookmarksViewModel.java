package com.runit.moviesmvvmmockup.ui.bookmarks;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.data.MoviesRepository;
import com.runit.moviesmvvmmockup.data.RepositoryProvider;
import com.runit.moviesmvvmmockup.data.exception.ErrorBundle;
import com.runit.moviesmvvmmockup.data.local.UserCredentials;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.Result;

import java.util.List;

/**
 * Created by Radovan Ristovic on 4/12/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class BookmarksViewModel extends AndroidViewModel {
    // View visibility status for progress bar
    public ObservableInt progressStatus = new ObservableInt();
    private MoviesRepository mRepository;

    public BookmarksViewModel(@NonNull Application application) {
        super(application);
        mRepository = RepositoryProvider.getMoviesRepository(application);
        progressStatus.set(View.VISIBLE);
    }

    /**
     * Retrieves all bookmarked movies for current user. Sets {@link #progressStatus} variable.
     * @return {@link LiveData} observable emitting list of movies, or error result if user is not logged in.
     */
    public LiveData<Result<List<MovieModel>>> getBookmarkedMovies() {
        UserCredentials credentials = UserCredentials.getInstance(getApplication());
        if (credentials.getUserAccount() != null) {
            MediatorLiveData<Result<List<MovieModel>>> result = new MediatorLiveData<>();
            LiveData<Result<List<MovieModel>>> source = mRepository.getBookmarkedMovies(credentials.getUserAccount().getId());
            result.addSource(source, listResult -> {
                result.setValue(listResult);
                progressStatus.set(View.INVISIBLE);
            });
            return result;
        } else {
            MutableLiveData<Result<List<MovieModel>>> data =  new MutableLiveData<>();
            data.setValue(new Result<>(ErrorBundle.error(getApplication().getString(R.string.login_to_use_feature_msg))));
            progressStatus.set(View.INVISIBLE);
            return data;
        }
    }
}
