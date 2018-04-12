package com.runit.moviesmvvmmockup.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.runit.moviesmvvmmockup.data.MoviesRepository;
import com.runit.moviesmvvmmockup.data.exception.ErrorBundle;
import com.runit.moviesmvvmmockup.data.local.BookmarkedMovie;
import com.runit.moviesmvvmmockup.data.local.UserCredentials;
import com.runit.moviesmvvmmockup.data.local.dao.MovieDao;
import com.runit.moviesmvvmmockup.data.local.db.DBProvider;
import com.runit.moviesmvvmmockup.data.local.db.MovieDatabase;
import com.runit.moviesmvvmmockup.data.model.MovieAccountState;
import com.runit.moviesmvvmmockup.data.model.MovieListCategory;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.Result;
import com.runit.moviesmvvmmockup.data.model.ServerResponse;
import com.runit.moviesmvvmmockup.data.remote.RetrofitClient;
import com.runit.moviesmvvmmockup.data.remote.TMDBApi;
import com.runit.moviesmvvmmockup.utils.JobExecutor;
import com.runit.moviesmvvmmockup.utils.exception.RepositoryException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * {@link MoviesRepository} implementation which returns data from the {@link TMDBApi} network api.
 */
public class MoviesTMDBRepository implements MoviesRepository {
    private static MoviesTMDBRepository mInstance;
    // DB instance
    private MovieDatabase mDatabase;
    // Application context
    private static Context mContext;

    public static MoviesTMDBRepository getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBProvider.class) {
                if (mInstance == null) {
                    mInstance = new MoviesTMDBRepository(context);
                }
            }
        }
        return mInstance;
    }

    private MoviesTMDBRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = DBProvider.getInstance(context).getDatabaseInstance();
    }

    @Override
    public LiveData<Result<List<MovieModel>>> getBookmarkedMovies(long userId) {
        MediatorLiveData<Result<List<MovieModel>>> result = new MediatorLiveData<>();
        LiveData<List<Long>> source = mDatabase.movieDao().getAllMovieIdsForUser(userId);
        result.addSource(source, bookmarkedMovies -> {
            result.removeSource(source);
            if (bookmarkedMovies != null) {
                // Query all movies by its ID
                LiveData<List<MovieModel>> moviesSource = mDatabase.movieDao().getMovies(bookmarkedMovies);
                result.addSource(moviesSource, movieModels -> {
                    result.removeSource(moviesSource);
                    if (movieModels != null) {
                        result.setValue(new Result<>(movieModels));
                    } else {
                        result.setValue(new Result<>(ErrorBundle.defaultError()));
                    }
                });
            } else {
                result.setValue(new Result<>(ErrorBundle.defaultError()));
            }
        });
        return result;
    }

    @Override
    public LiveData<Result<Boolean>> isMovieBookmarked(long movieId) {
        MediatorLiveData<Result<Boolean>> result = new MediatorLiveData<>();
        UserCredentials credentials = UserCredentials.getInstance(mContext);
        if (credentials.getUserAccount() != null) {
            LiveData<Long> source = mDatabase.movieDao().isMovieBookmarked(movieId, credentials.getUserAccount().getId());
            result.addSource(source, movie -> {
                if (movie != null) {
                    result.setValue(new Result<>(Boolean.TRUE));
                } else {
                    result.setValue(new Result<>(Boolean.FALSE));
                }
            });
        } else {
            result.setValue(new Result<>(Boolean.FALSE));
        }
        return result;
    }

    @Override
    public void bookmark(MovieModel movie) {
        MovieDao movieDao = mDatabase.movieDao();
        UserCredentials credentials = UserCredentials.getInstance(mContext);
        if (credentials.getUserAccount() != null) {
            JobExecutor.execute( () -> {
                MovieDao dao = mDatabase.movieDao();
                if(dao.isMovieBookmarkedAsync(movie.getId(), credentials.getUserAccount().getId()) != null){
                    this.removeFromBookmark(movie);
                } else {
                    movieDao.insert(movie, credentials.getUserAccount().getId());
                }
            });
        }
    }

    @Override
    public void removeFromBookmark(MovieModel movie) {
        UserCredentials credentials = UserCredentials.getInstance(mContext);
        if (credentials.getUserAccount() != null) {
            mDatabase.movieDao().delete(new BookmarkedMovie(movie.getId(), credentials.getUserAccount().getId()));
            List<Long> users = mDatabase.movieDao().getAllUserIdsForMovieIdAsync(movie.getId());
            if (users == null || users.size() == 0) {
                // If no user has this movie bookmarked, delete it from DB.
                mDatabase.movieDao().delete(movie);
            }
        }
    }

    @Override
    public LiveData<Result<MovieModel>> getMovie(long movieId) {
        final MutableLiveData<Result<MovieModel>> movie = new MutableLiveData<>();
        RetrofitClient.getClient().getMovieDetails(movieId).enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movie.setValue(new Result<>(response.body()));
                } else {
                    movie.setValue(new Result<>(ErrorBundle.defaultServerError()));
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                movie.setValue(new Result<>(ErrorBundle.defaultConnectionError()));
            }
        });
        return movie;
    }


    @Override
    public LiveData<Result<List<MovieModel>>> getMovieList(MovieListCategory category, int page) {
        LiveData<Result<List<MovieModel>>> movies;
        switch (category) {
            case POPULAR: {
                movies = fetchPopular(page);
                break;
            }
            case UPCOMING: {
                movies = fetchUpcoming(page);
                break;
            }
            case TOP_RATED: {
                movies = fetchTopRated(page);
                break;
            }
            case NOW_PLAYING: {
                movies = fetchNowPlaying(page);
                break;
            }
            default: {
                throw new RepositoryException(String.format("No implementation found for handling %s category.", category.toString()));
            }
        }

        return movies;
    }

    /**
     * Helper method for downloading "now_playing" movie list.
     *
     * @param page desired page of the list.
     * @return {@link LiveData} observable.
     */
    private LiveData<Result<List<MovieModel>>> fetchNowPlaying(int page) {
        final MutableLiveData<Result<List<MovieModel>>> movies = new MutableLiveData<>();
        RetrofitClient.getClient().getNowPlayingMovies(page).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(movies, response.body().getResults());
                } else {
                    onResponseFailure(movies);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onConnectionFailure(movies);
            }
        });
        return movies;
    }

    /**
     * Helper method for downloading "top_rated" movie list.
     *
     * @param page desired page of the list.
     * @return {@link LiveData} observable.
     */
    private LiveData<Result<List<MovieModel>>> fetchTopRated(int page) {
        final MutableLiveData<Result<List<MovieModel>>> movies = new MutableLiveData<>();
        RetrofitClient.getClient().getTopRated(page).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(movies, response.body().getResults());
                } else {
                    onResponseFailure(movies);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onConnectionFailure(movies);
            }
        });
        return movies;
    }

    /**
     * Helper method for downloading "upcomming" movie list.
     *
     * @param page desired page of the list.
     * @return {@link LiveData} observable.
     */
    private LiveData<Result<List<MovieModel>>> fetchUpcoming(int page) {
        final MutableLiveData<Result<List<MovieModel>>> movies = new MutableLiveData<>();
        RetrofitClient.getClient().getUpcoming(page).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(movies, response.body().getResults());
                } else {
                    onResponseFailure(movies);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onConnectionFailure(movies);
            }
        });
        return movies;
    }

    /**
     * Helper method for downloading "popular" movie list.
     *
     * @param page desired page of the list.
     * @return {@link LiveData} observable.
     */
    private LiveData<Result<List<MovieModel>>> fetchPopular(int page) {
        final MutableLiveData<Result<List<MovieModel>>> movies = new MutableLiveData<>();
        RetrofitClient.getClient().getPopularMovies(page).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(movies, response.body().getResults());
                } else {
                    onResponseFailure(movies);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onConnectionFailure(movies);
            }
        });
        return movies;
    }

    /**
     * Method to call when network response has been successful.
     *
     * @param observable LiveData observable on which to post new data.
     * @param movieList  Newly downloaded list of movies.
     */
    private void onResponseSuccess(MutableLiveData<Result<List<MovieModel>>> observable, @NonNull List<MovieModel> movieList) {
        observable.setValue(new Result<>(movieList));
    }

    /**
     * Method to call when network response has failed.
     *
     * @param observable LiveData observable on which to post an error.
     */
    private void onResponseFailure(MutableLiveData<Result<List<MovieModel>>> observable) {
        observable.setValue(new Result<>(ErrorBundle.defaultConnectionError()));
    }

    /**
     * Method to call when failed to establish connection to the server.
     *
     * @param observable LiveData observable on which to post an error.
     */
    private void onConnectionFailure(MutableLiveData<Result<List<MovieModel>>> observable) {
        observable.setValue(new Result<>(ErrorBundle.defaultConnectionError()));
    }

}
