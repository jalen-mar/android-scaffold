package com.jalen.android.scaffold.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.jalen.android.scaffold.lifecycle.BaseModel;
import com.jalen.android.scaffold.widget.loading.DefaultLoading;
import com.jalen.android.scaffold.widget.loading.Loading;

public class ScaffoldFragment extends CommonFragment {
    private Loading loading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new DefaultLoading(getChildFragmentManager());
    }

    @Override
    public void onDestroy() {
        loading.dismiss();
        super.onDestroy();
    }

    @Override
    public <M extends BaseModel> M wrap(M vm) {
        M result = super.wrap(vm);
        result.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean it) {
                if (it) {
                    loading.show();
                } else {
                    loading.dismiss();
                }
            }
        });
        return result;
    }

    protected void openLoadingAnimation() {
        loading.show();
    }

    protected void closeLoadingAnimation() {
        loading.dismiss();
    }

    public void setLoading(Loading loading) {
        this.loading = loading;
    }
}
