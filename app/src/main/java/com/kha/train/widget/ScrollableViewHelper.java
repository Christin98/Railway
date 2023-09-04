package com.kha.train.widget;

import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

public class ScrollableViewHelper {
    public int getScrollableViewScrollPosition(View view, boolean z) {
        int i = 0;
        if (view == null) {
            return 0;
        }
        if (!(view instanceof ScrollView)) {
            if (view instanceof ListView) {
                ListView listView = (ListView) view;
                if (listView.getChildCount() > 0) {
                    if (listView.getAdapter() == null) {
                        return 0;
                    }
                    if (z) {
                        view = listView.getChildAt(0);
                        return (listView.getFirstVisiblePosition() * view.getHeight()) - view.getTop();
                    }
                    view = listView.getChildAt(listView.getChildCount() - 1);
                    return ((((listView.getAdapter().getCount() - listView.getLastVisiblePosition()) - 1) * view.getHeight()) + view.getBottom()) - listView.getBottom();
                }
            }
            if (view instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) view;
                if (recyclerView.getChildCount() > 0) {
                    LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (recyclerView.getAdapter() == null) {
                        return 0;
                    }
                    View childAt;
                    if (z) {
                        childAt = recyclerView.getChildAt(0);
                        return (recyclerView.getChildLayoutPosition(childAt) * layoutManager.getDecoratedMeasuredHeight(childAt)) - layoutManager.getDecoratedTop(childAt);
                    }
                    childAt = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                    i = (((recyclerView.getAdapter().getItemCount() - 1) * layoutManager.getDecoratedMeasuredHeight(childAt)) + layoutManager.getDecoratedBottom(childAt)) - recyclerView.getBottom();
                }
            }
            return i;
        } else if (z) {
            return view.getScrollY();
        } else {
            ScrollView scrollView = (ScrollView) view;
            return scrollView.getChildAt(0).getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
        }
    }
}
