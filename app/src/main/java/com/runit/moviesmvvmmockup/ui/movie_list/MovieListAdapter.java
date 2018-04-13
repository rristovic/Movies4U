package com.runit.moviesmvvmmockup.ui.movie_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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

    private final AdapterView.OnItemClickListener mListener;

    public MovieListAdapter(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, AdapterView.OnItemClickListener listener) {
        super(recyclerView, layoutManager);
        this.mListener = listener;
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

    class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MovieItemBinding binding;

        MovieItemViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        void bind(MovieModel movieModel) {
            this.binding.setMovie(movieModel);
            this.binding.setStarRating((movieModel.getVoteAverage() != null && movieModel.getVoteAverage() != 0) ? movieModel.getVoteAverage() / 2 : 0);
            // Must be run to populate view with data immediately, so RecycleView can measure properly
            this.binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(null, v, getAdapterPosition(), getItemId());
        }
    }
}
