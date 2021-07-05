package com.jalen.android.scaffold.app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;

import com.jalen.android.scaffold.R;
import com.jalen.android.scaffold.lifecycle.BaseModel;
import com.jalen.android.scaffold.widget.loading.DefaultLoading;
import com.jalen.android.scaffold.widget.loading.Loading;

public class ScaffoldActivity extends CommonActivity implements View.OnClickListener {
    public static int ACTION_UNABLE = 0;
    public static int ACTION_TITLE = 1;
    public static int ACTION_NAVIGATION = 2;
    public static int ACTION_NAVIGATION_CENTER = 3;

    protected Toolbar toolbar;
    private Loading loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new DefaultLoading(getSupportFragmentManager());
        initToolbar(toolbar = findViewById(R.id.toolLayout));
    }

    private void initToolbar(Toolbar toolbar) {
        int type = enableToolbar();
        if (type > ACTION_UNABLE) {
            setSupportActionBar(toolbar);
            if (type == ACTION_TITLE || type == ACTION_NAVIGATION_CENTER) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                ((TextView) toolbar.findViewById(R.id.tvTitle)).setText(getTitleValue());
            } else {
                getSupportActionBar().setTitle(getTitleValue());
                toolbar.findViewById(R.id.tvTitle).setVisibility(View.GONE);
            }
            int background = getTitleBackground();
            if (background != -1) {
                toolbar.setBackgroundResource(background);
            }
            if (type == ACTION_NAVIGATION || type == ACTION_NAVIGATION_CENTER) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(this);
                Drawable drawable = getNavigationIcon();
                if (drawable != null) {
                    toolbar.setNavigationIcon(drawable);
                }
            }
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    protected int enableToolbar() {
        return ACTION_NAVIGATION_CENTER;
    }

    protected Drawable getNavigationIcon() {
        return null;
    }

    protected String getTitleValue() {
        return null;
    }

    protected int getTitleBackground() {
        return -1;
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    protected View getContentView(ViewGroup parentView) {
        View contentView = getLayoutInflater().inflate(R.layout.view_base, parentView, false);
        int resId = getLayout();
        if (resId != View.NO_ID) {
            View view = getLayoutInflater().inflate(resId, (ViewGroup) contentView, false);
            if (getContentBelowParent()) {
                ((RelativeLayout.LayoutParams) view.getLayoutParams())
                        .addRule(RelativeLayout.BELOW, R.id.toolLayout);
            }
            initView(view);
            if (ViewCompat.getFitsSystemWindows(view)) {
                view.setFitsSystemWindows(false);
                contentView.setFitsSystemWindows(true);
            }
            ((ViewGroup) contentView).addView(view);
        }
        return contentView;
    }

    protected boolean getContentBelowParent() {
        return true;
    }

    @Override
    protected void onDestroy() {
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
