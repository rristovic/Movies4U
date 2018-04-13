package com.runit.moviesmvvmmockup.ui.bookmarks;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.databinding.BookmarkItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radovan Ristovic on 4/13/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarkViewHolder> {

    private List<MovieModel> data = new ArrayList<>(20);
    private AdapterView.OnItemClickListener mListener;

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BookmarkItemBinding binding = BookmarkItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new BookmarkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        holder.bind(this.data.get(position));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    /**
     * Add new collection to this adapter data list if it's not null and not empty.
     *
     * @param data {@link List} to be appended to current data.
     */
    public void addData(List<MovieModel> data) {
        if (data != null && !data.isEmpty()) {
            this.data.addAll(data);
            super.notifyItemRangeInserted(this.data.size() - data.size(), data.size());
        }
    }

    /**
     * Retrieves item from this adapter data list for provided position.
     *
     * @param position list position of desired item.
     * @return {@link MovieModel} from the list if exits on provided position, null otherwise.
     */
    public @Nullable MovieModel getItem(int position) {
        try {
            return data.get(position);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Sets item click listener for adapter's items.
     *
     * @param onItemClickListener {@link android.widget.AdapterView.OnItemClickListener} callback object.
     */
    public void setItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    class BookmarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final BookmarkItemBinding binding;

        BookmarkViewHolder(BookmarkItemBinding bookmarkItemBinding) {
            super(bookmarkItemBinding.getRoot());
            this.binding = bookmarkItemBinding;
            this.binding.getRoot().setOnClickListener(this);
        }

        void bind(MovieModel movieModel) {
            this.binding.setMovie(movieModel);
            // Must be run to populate view with data immediately, so RecycleView can measure properly
            this.binding.executePendingBindings();
            Picasso.get().load(movieModel.getThumbnailUrl()).fit().centerCrop().into(this.binding.ivMovieThumbnail);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null)
                mListener.onItemClick(null, v, getAdapterPosition(), BookmarksAdapter.this.getItemId(getAdapterPosition()));
        }
    }
}
