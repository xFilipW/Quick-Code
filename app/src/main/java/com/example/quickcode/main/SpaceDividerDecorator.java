package com.example.quickcode.main;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceDividerDecorator extends RecyclerView.ItemDecoration {
    private final int space;

    public SpaceDividerDecorator(Context context, @DimenRes int spaceDimenRes) {
        this.space = context.getResources().getDimensionPixelSize(spaceDimenRes);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position < state.getItemCount()) {
            outRect.bottom = space;
        }
    }
}
