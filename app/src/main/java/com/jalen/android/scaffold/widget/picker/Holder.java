package com.jalen.android.scaffold.widget.picker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jalen.android.scaffold.R;

public class Holder extends RecyclerView.ViewHolder {
    public TextView view;

    public Holder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.item_picker_value);
    }
}