package com.runit.moviesmvvmmockup.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.utils.JobExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * RecycleView adapter which has to ability to add a loader at the end of the view when loading more data is being called and loading is in progress.
 * Can only be used with {@link GridLayoutManager} and {@link LinearLayoutManager}.
 *
 * @param <T>  type of item that this adapter will hold.
 * @param <VH> type of {@link RecyclerView.ViewHolder} that the adapter will populate data with.
 */
public abstract class AbsEndlessRecycleViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    /**
     * Callback that's invoked when list has reached its last visible threshold, so loading more data is required.
     */
    public interface OnLoadMoreListener {
        /**
         * Called when the list has reach its last visible threshold to load more data into the list.
         */
        void onLoadMore();
    }

    private final int VIEW_ITEM = 1;
    private final int VIEW_LOADER = 0;
    private final RecyclerView mRecycleView;
    private LinearLayoutManager mLinearLayoutManager;
    private Context mContext;
    private List<T> mData;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            totalItemCount = mLinearLayoutManager.getItemCount();
            lastVisibleItem = mLinearLayoutManager
                    .findLastVisibleItemPosition();
            if (!loading
                    && totalItemCount <= (lastVisibleItem + visibleThreshold) && dy > 0) {
                // End has been reached
                // Do something
                if (onLoadMoreListener != null) {
                    loading = true;
                    mData.add(null);
                    notifyItemInserted(mData.size() - 1);
                }
                mRecycleView.post(() -> {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                });

            }
        }
    };


    /**
     * Public constructor. It requires {@link RecyclerView} object to be passed so the loader could be inserted at the bottom of the list when required to load more data.
     *
     * @param data         list of items.
     * @param recyclerView recycle view to be manipulated with.
     */
    protected AbsEndlessRecycleViewAdapter(List<T> data, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        mContext = recyclerView.getContext();
        if (data == null) {
            this.mData = new ArrayList<>(0);
        } else {
            this.mData = data;
        }
        mRecycleView = recyclerView;
        mRecycleView.setLayoutManager(layoutManager);
        if (layoutManager instanceof LinearLayoutManager) {
            mLinearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView.addOnScrollListener(mScrollListener);
            if (layoutManager instanceof GridLayoutManager) {
                ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch (getItemViewType(position)) {
                            case VIEW_ITEM:
                                return 1;
                            case VIEW_LOADER:
                                return ((GridLayoutManager) mLinearLayoutManager).getSpanCount();
                            default:
                                return 1;
                        }
                    }
                });
            }
        } else {
            // Try setting linear layout manager
            mLinearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.addOnScrollListener(mScrollListener);

        }
    }

    public AbsEndlessRecycleViewAdapter(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        this(null, recyclerView, layoutManager);
    }

    /**
     * Gets a copy of an item from this adapter's current list of data.
     *
     * @param position of item in the list.
     * @return item from the adapter's list.
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) != null ? VIEW_ITEM : VIEW_LOADER;
    }

    /**
     * Method will be called when adapter requires {@link android.support.v7.widget.RecyclerView.ViewHolder} to be created and inflated with a view.
     *
     * @param parent view holder's view parent.
     * @return newly created view holder.
     */
    public abstract VH createViewHolder(ViewGroup parent);

    /**
     * Method will be called when adapter requires view holder to be populated with data.
     *
     * @param viewHolder holder to be populated.
     * @param position   position of holder in the list.
     */
    public abstract void populateViewHolder(VH viewHolder, int position);

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof ProgressViewHolder)) {
            populateViewHolder((VH) holder, position);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            return createViewHolder(parent);
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.loader, parent, false));

        }
    }

    /**
     * Sets the minimum visible threshold for the list. This number indicates minimum amount of items to have below current scroll position
     * before loading more items and before {@link OnLoadMoreListener#onLoadMore()} will be called.
     *
     * @param threshold must be greater than 1.
     */
    public void setMinimunVisibleThreshold(int threshold) {
        if (threshold > 1)
            this.visibleThreshold = threshold;
    }

    @Override
    public final int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * Set adapter's current list of items. If data if null, the current list of items will be cleared.
     *
     * @param data data to be set.
     */
    public final void setData(@Nullable List<T> data) {
        if (data == null) {
            this.mData.clear();
        } else {
            this.mData = data;
        }
        loading = false;
        notifyDataSetChanged();
    }

    /**
     * Adds list of items to current adapter's list of items. Hides the loader if it is present.
     *
     * @param data list of items to add if it's not null.
     */
    public final void addData(List<T> data) {
        removeLoader();
        if (data != null) {
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * Delete all item currently hold by this adapter.
     */
    public final void clearData() {
        this.mData.clear();
        notifyDataSetChanged();
    }

    /**
     * Helper method for removing loader spinner.
     */
    private void removeLoader() {
        if (this.loading) {
            loading = false;
            if (mData.size() > 0) {
                mData.remove(mData.size() - 1);
                notifyItemRangeRemoved(mData.size(), 1);
            }
        }
    }

    /**
     * Method to call when loading more items has failed. This will make scroll listener inactive for 3 seconds.
     */
    public final void onLoadMoreFailed() {
        JobExecutor.executeOnUI(this::removeLoader, 3000);
    }

    /**
     * Call this method when there are no more pages to load, in other words no more items to load so the loader should stay hidden when list reaches its threshold.
     */
    public final void onLoadMoreComplete() {
        this.mRecycleView.removeOnScrollListener(mScrollListener);
        this.removeLoader();
    }

    /**
     * Returns the current adapter's context.
     *
     * @return adapter's context.
     */
    protected Context getContext() {
        return mContext;
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressViewHolder(View v) {
            super(v);
        }
    }

    /**
     * Gets a copy of all data that this adapter is holding.
     *
     * @return list of current data.
     */
    public List<T> getData() {
        return new ArrayList<>(mData);
    }
}
