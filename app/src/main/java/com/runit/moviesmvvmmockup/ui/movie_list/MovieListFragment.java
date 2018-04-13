package com.runit.moviesmvvmmockup.ui.movie_list;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.data.model.MovieListCategory;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.databinding.FragmentMovieListBinding;
import com.runit.moviesmvvmmockup.ui.movie_details.MovieDetailsActivity;
import com.runit.moviesmvvmmockup.utils.UIUtil;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieListFragment extends Fragment {
    // Fragment position in view pager
    private static final String ARG_FRAG_CATEGORY = "fragment_cateogry";
    // Movie list adapter
    private MovieListAdapter mAdapter;
    private MovieListViewModel mViewModel;

    public static MovieListFragment newInstance(MovieListCategory category) {
        MovieListFragment f = new MovieListFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_FRAG_CATEGORY, category);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Setup binding & viewModel
        FragmentMovieListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);
        mViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        binding.setMovieListVM(mViewModel);
        // Setup list
        mAdapter = new MovieListAdapter(binding.rvMovies, new GridLayoutManager(getActivity(), 2), (parent, view, position, id) -> {
            MovieModel movieModel = mAdapter.getItem(position);
            MovieDetailsActivity.startActivity(getActivity(), movieModel.getId(), movieModel.getTitle());
        });
        mAdapter.setOnLoadMoreListener(mViewModel::getNextPage);
        binding.rvMovies.setAdapter(mAdapter);
        mViewModel.getMoviesForCategory((MovieListCategory) getArguments().getSerializable(ARG_FRAG_CATEGORY))
                .observe(this, movieResults -> {
                    if (movieResults != null)
                        if (movieResults.isSuccess()) {
                            if (movieResults.get().size() == 0) {
                                // If loading zero items, it means no more items to load
                                mAdapter.onLoadMoreComplete();
                            }
                            if (mAdapter.getItemCount() > 0) {
                                // get only last page data
                                mAdapter.addData(movieResults.get().subList(movieResults.get().size() - mViewModel.getLastPageItemCount(), movieResults.get().size()));
                            } else {
                                // initial load
                                mAdapter.addData(movieResults.get());
                            }
                        } else {
                            UIUtil.showShortToast(getActivity(), movieResults.error().getMessage());
                            mAdapter.onLoadMoreFailed();
                        }
                });

        return binding.getRoot();
    }
}
