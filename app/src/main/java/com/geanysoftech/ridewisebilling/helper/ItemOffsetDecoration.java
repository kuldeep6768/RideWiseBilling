package com.geanysoftech.ridewisebilling.helper;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int itemOffset;

    public ItemOffsetDecoration(int itemOffset){
        this.itemOffset = itemOffset;
    }

    public ItemOffsetDecoration(Context context, int itemOffsetId){
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(itemOffset, itemOffset, itemOffset, itemOffset);
    }
}
