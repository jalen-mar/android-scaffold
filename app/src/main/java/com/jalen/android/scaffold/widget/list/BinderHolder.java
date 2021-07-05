package com.jalen.android.scaffold.widget.list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

public class BinderHolder <T extends ViewDataBinding> extends ViewHolder {
    public final T binder;

    public BinderHolder(@NonNull T binder) {
        super(binder.getRoot());
        this.binder = binder;
    }

    public static ViewHolder newInstance(Object obj) {
        if (obj instanceof ViewDataBinding) {
            return new BinderHolder((ViewDataBinding) obj);
        } else {
            return new ViewHolder((View) obj);
        }
    }
}
