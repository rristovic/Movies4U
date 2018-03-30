package com.runit.moviesmvvmmockup.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.runit.moviesmvvmmockup.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Radovan Ristovic on 3/1/2018.
 * Quantox.com
 * radovanr995@gmail.com
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
    private final int VIEW_PROG = 0;
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
                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                // End has been reached
                // Do something
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }
                loading = true;
                mRecycleView.post(() -> {
                    mData.add(null);
                    notifyItemInserted(mData.size() - 1);
                });
            }
        }
    };


    public AbsEndlessRecycleViewAdapter(List<T> data, RecyclerView recyclerView) {
        mContext = recyclerView.getContext();
        if (data == null) {
            this.mData = new ArrayList<>();
        } else {
            this.mData = data;
        }
        mRecycleView = recyclerView;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mLinearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView.addOnScrollListener(mScrollListener);
        } else {
            mLinearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.addOnScrollListener(mScrollListener);
        }
    }

    public AbsEndlessRecycleViewAdapter(RecyclerView recyclerView) {
        this(null, recyclerView);
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public abstract VH createViewHolder(ViewGroup parent);

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

    public void setMinimunVisibleThreshold(int threshold) {
        this.visibleThreshold = threshold;
    }

    @Override
    public final int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public final void setData(@Nullable List<T> data) {
        if (data == null) {
            this.mData.clear();
        } else {
            this.mData = data;
        }
        loading = false;
        notifyDataSetChanged();
    }

    public final void addData(List<T> data) {
        if (this.loading) {
            loading = false;
            mData.remove(mData.size() - 1);
        }
        if (data != null) {
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public final void onLoadMoreComplete() {
        this.mRecycleView.removeOnScrollListener(mScrollListener);
    }

    protected Context getContext() {
        return mContext;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.pb_loader);
        }
    }

    public List<T> getData() {
        return mData;
    }
}
