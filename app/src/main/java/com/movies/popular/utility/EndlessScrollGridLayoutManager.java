package com.movies.popular.utility;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by mukesh on 29/2/16.
 */
public abstract class EndlessScrollGridLayoutManager extends GridLayoutManager {
    public EndlessScrollGridLayoutManager(Context context, int spanCount) {
        super(context,spanCount);

    }

    public EndlessScrollGridLayoutManager(Context context, int spanCount, int orientation,
                                          boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);

    }


    public EndlessScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                          int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        int scrollRange = super.scrollVerticallyBy(dy, recycler, state);
        int overScroll = dy - scrollRange;
        if (overScroll > 0) {
            //bottom reached
            onBottomReached();
        } else if (overScroll < 0) {
            //top reached
            onTopReached();
        }
        if (scrollRange < 0) {
            onScrollUp();
        } else {
            onScrollDown();
        }
        return scrollRange;
    }

    public abstract void onBottomReached();

    public abstract void onTopReached();

    public abstract void onScrollDown();

    public abstract void onScrollUp();
}
