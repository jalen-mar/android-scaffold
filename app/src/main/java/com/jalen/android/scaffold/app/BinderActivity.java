package com.jalen.android.scaffold.app;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BinderActivity<T extends ViewDataBinding> extends ScaffoldActivity {
    protected T binder;

    @Override
    protected void initView(View contentView) {
        binder = DataBindingUtil.bind(contentView);
    }

    @Override
    protected void onDestroy() {
        if (binder != null) {
            binder.unbind();
        }
        super.onDestroy();
    }

    protected T getBinder() {
        return binder;
    }
}
