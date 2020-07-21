package com.bozobaka.bharatadmin.common;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class EqualSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;
    private int displayMode;
    private boolean topMarginEnabled;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;
    public static final int STAGGERD = 3;

    public EqualSpacingItemDecoration(int spacing) {
        this(spacing, -1, false);
    }

    public EqualSpacingItemDecoration(int spacing, int displayMode, boolean topMarginEnabled) {
        this.spacing = spacing;
        this.displayMode = displayMode;
        this.topMarginEnabled = topMarginEnabled;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        setSpacingForDirection(outRect, layoutManager, position, itemCount);
    }

    private void setSpacingForDirection(Rect outRect,
                                        RecyclerView.LayoutManager layoutManager,
                                        int position,
                                        int itemCount) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager);
        }

        switch (displayMode) {
            case HORIZONTAL:
                outRect.left = spacing;
                outRect.right = position == itemCount - 1 ? spacing : 0;
//                outRect.top = spacing;
//                outRect.bottom = spacing;
                break;
            case VERTICAL:
                outRect.left = spacing;
                outRect.right = spacing;
                outRect.top = spacing;
                outRect.bottom = position == itemCount - 1 ? spacing : 0;
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    int cols = gridLayoutManager.getSpanCount();
                    int rows = itemCount / cols;

//                    outRect.left = spacing;
//                    outRect.right = position % cols == cols - 1 ? spacing : 0;
//                    outRect.top = spacing;
//                    outRect.bottom = position / cols == rows - 1 ? spacing : 0;


                    //set right spacing to all
//                    outRect.right = spacing;
                    //set bottom spacing to all
                    outRect.bottom = spacing ;

                    //we only add top spacing to the first row
                    if (position < cols && topMarginEnabled) {
                        outRect.top = spacing;
                    }
//                    //add left spacing only to the first column
                    if (position % cols == 0) {
                        outRect.left = spacing;
                    }else {
                        outRect.left = spacing / 2;
                    }

                    if (position % cols == 0) {
                        outRect.right = spacing / 2;
                    }else {
                        outRect.right = spacing;
                    }
                }
                break;
            case STAGGERD:
                if (layoutManager instanceof GridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int cols = staggeredGridLayoutManager.getSpanCount();
                    int rows = itemCount / cols;

//                    outRect.left = spacing;
//                    outRect.right = position % cols == cols - 1 ? spacing : 0;
//                    outRect.top = spacing;
//                    outRect.bottom = position / cols == rows - 1 ? spacing : 0;


                    //set right spacing to all
//                    outRect.right = spacing;
                    //set bottom spacing to all
                    outRect.bottom = spacing ;

                    //we only add top spacing to the first row
                    if (position < cols && topMarginEnabled) {
                        outRect.top = spacing;
                    }
//                    //add left spacing only to the first column
                    if (position % cols == 0) {
                        outRect.left = spacing;
                    }else {
                        outRect.left = spacing / 2;
                    }

                    if (position % cols == 0) {
                        outRect.right = spacing / 2;
                    }else {
                        outRect.right = spacing;
                    }
                }
                break;
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}