package com.jalen.android.scaffold.widget.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclableView extends androidx.recyclerview.widget.RecyclerView {
    public RecyclableView(@NonNull Context context) {
        this(context, null);
    }

    public RecyclableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(context));
    }

    @BindingAdapter("adapter")
    public static void setAdapter(RecyclerView view, Adapter<ViewHolder> adapter) {
        view.setAdapter(adapter);
    }
}
