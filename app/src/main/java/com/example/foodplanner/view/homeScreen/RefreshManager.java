package com.example.foodplanner.view.homeScreen;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.graphics.Color;

public class RefreshManager {

    public interface OnRefreshAction {
        void onRefresh();
    }

    public static void setup(SwipeRefreshLayout swipeLayout, OnRefreshAction action) {
        swipeLayout.setColorSchemeColors(
                Color.GREEN,
                Color.GREEN,
                Color.RED
        );


        swipeLayout.setOnRefreshListener(() -> {
            if (action != null) {
                action.onRefresh();
            }
        });
    }

    public static void stopRefreshing(SwipeRefreshLayout swipeLayout) {
        if (swipeLayout != null && swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }
}