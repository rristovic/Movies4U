package com.runit.moviesmvvmmockup.ui.main.movie_list;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.databinding.MovieItemBinding;
import com.runit.moviesmvvmmockup.ui.AbsEndlessRecycleViewAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by Radovan Ristovic on 3/30/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieListAdapter extends AbsEndlessRecycleViewAdapter<MovieModel, MovieListAdapter.MovieItemViewHolder> {

    public MovieListAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public MovieItemViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MovieItemBinding binding = MovieItemBinding.inflate(inflater, parent, false);
        return new MovieItemViewHolder(binding);
    }

    @Override
    public void populateViewHolder(MovieItemViewHolder viewHolder, int position) {
        MovieModel item = getItem(position);
        viewHolder.bind(item);
        Picasso.get().load(item.getThumbnailUrl()).into(viewHolder.binding.ivMovieThumbnail);
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder {
        private final MovieItemBinding binding;

        public MovieItemViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MovieModel movieModel) {
            this.binding.setMovie(movieModel);
            // Must be run to populate view with data immediately, so RecycleView can measure properly
            this.binding.executePendingBindings();
        }
    }
}
