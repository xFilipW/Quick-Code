package com.example.quickcode.main.viewHolders;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDividerDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public ItemDividerDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = space; // Set bottom offset to add space between items
    }
}