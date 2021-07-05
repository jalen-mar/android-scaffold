package com.jalen.android.scaffold.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jalen.android.scaffold.R;

public class RefreshView extends SwipeRefreshLayout {
    private OnRefreshListener listener;

    private float x, y;
    private boolean mIsVpDragger;
    private final int slop;

    public RefreshView(@NonNull Context context) {
        this(context,null);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSize(DEFAULT);
        setProgressViewOffset(false, -getProgressCircleDiameter(), (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()
        ));
        setColorSchemeResources(R.color.actionBackground);
        slop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
        this.listener = listener;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        boolean refresh = isRefreshing();
        super.setRefreshing(refreshing);
        if (refreshing && !refresh) {
            if (listener != null) {
                listener.onRefresh();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                y = ev.getY();
                mIsVpDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if(mIsVpDragger) {
                    return false;
                }
                float Y = ev.getY();
                float X = ev.getX();
                float distanceX = Math.abs(X - x);
                float distanceY = Math.abs(Y - y);
                if(distanceX > slop && distanceX > distanceY) {
                    mIsVpDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsVpDragger = false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @BindingAdapter("refreshing")
    public static void setRefreshing(SwipeRefreshLayout view, boolean refreshing) {
        if (view.isRefreshing() != refreshing) {
            view.setRefreshing(refreshing);
        }
    }

    @InverseBindingAdapter(attribute = "refreshing", event = "refreshingAttrChanged")
    public static boolean isRefreshing(SwipeRefreshLayout view) {
        return view.isRefreshing();
    }

    @BindingAdapter(value = {"onRefreshListener", "refreshingAttrChanged"}, requireAll = false)
    public static void setOnRefreshListener(SwipeRefreshLayout view, final OnRefreshListener listener,
                                            final InverseBindingListener refreshingAttrChanged) {
        OnRefreshListener newValue = new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshingAttrChanged != null) {
                    refreshingAttrChanged.onChange();
                }
                if (listener != null) {
                    listener.onRefresh();
                }
            }
        };
        view.setOnRefreshListener(newValue);
        if (view.isRefreshing()) {
            newValue.onRefresh();
        }
    }
}
