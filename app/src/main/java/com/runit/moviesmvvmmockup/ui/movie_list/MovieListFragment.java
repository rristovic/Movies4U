package com.runit.moviesmvvmmockup.ui.movie_list;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
        MovieListViewModel viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        binding.setMovieListVM(viewModel);
        // Setup list
        mAdapter = new MovieListAdapter(binding.rvMovies, new GridLayoutManager(getActivity(), 2), (parent, view, position, id) -> {
            MovieModel movieModel = mAdapter.getItem(position);
            MovieDetailsActivity.startActivity(getActivity(), movieModel.getId(), movieModel.getTitle(), movieModel.getThumbnailUrl());
        });
        mAdapter.setOnLoadMoreListener(viewModel::getNextPage);
        binding.rvMovies.setAdapter(mAdapter);
        viewModel.getMoviesForCategory((MovieListCategory) getArguments().getSerializable(ARG_FRAG_CATEGORY), error ->
                UIUtil.showToast(getActivity(), error.getMessage()))
                .observe(this, movieModels -> {
                    if (movieModels != null) {
                        if (movieModels.size() == 0) {
                            // If loading zero items, it means no more items to load
                            mAdapter.onLoadMoreComplete();
                        }
                        mAdapter.addData(movieModels);
                    }
                });

        return binding.getRoot();
    }
}
