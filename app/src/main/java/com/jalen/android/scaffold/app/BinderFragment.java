package com.jalen.android.scaffold.app;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class BinderFragment<T extends ViewDataBinding> extends ScaffoldFragment {
    protected T binder;

    @Override
    protected void initView(View contentView) {
        binder = DataBindingUtil.bind(contentView);
    }

    @Override
    public void onDestroy() {
        if (binder != null) {
            binder.unbind();
        }
        super.onDestroy();
    }

    protected T getBinder() {
        return binder;
    }
}
