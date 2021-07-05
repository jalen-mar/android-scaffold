package com.jalen.android.scaffold.widget.list;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SparseArray<View> targetList;
    private SparseArray<View> children;
    private OnClickListener listener;
    private Map<String, Object> data;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        targetList = new SparseArray<>();
        children = new SparseArray<>();
        data = new HashMap<>();
        itemView.setTag(this);
    }

    public void setData(String key, Object obj) {
        data.put(key, obj);
    }

    public <T> T getData(String key) {
        return (T) data.get(key);
    }

    public <T extends View> T getChild(int id) {
        View view = targetList.get(id);
        if(view == null){
            view = itemView.findViewById(id);
            targetList.put(id, view);
        }
        return (T) view;
    }

    public <T extends View> T getChildAt(int index) {
        View view = children.get(index);
        if(view == null){
            ViewGroup viewGroup = ((ViewGroup) itemView);
            if (viewGroup.getChildCount() > index) {
                view = viewGroup.getChildAt(index);
                children.put(index, view);
            }
        }
        return (T) view;
    }

    public boolean contains(int id) {
        return getChild(id) != null;
    }

    //---------------------------------------------------------------------------------------

    public <T> T getTag(int viewId) {
        return (T) getChild(viewId).getTag();
    }

    public <T> T getTag(int viewId, int key) {
        return (T) getChild(viewId).getTag(key);
    }

    public ViewHolder setTag(int viewId, Object tag) {
        getChild(viewId).setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        getChild(viewId).setTag(key, tag);
        return this;
    }

    public ViewHolder setTag(int viewId) {
        getChild(viewId).setTag(this);
        return this;
    }

    public ViewHolder setText(int viewId, CharSequence value) {
        TextView view = getChild(viewId);
        view.setText(value);
        return this;
    }

    public ViewHolder setText(int viewId, int resId) {
        TextView view = getChild(viewId);
        view.setText(resId);
        return this;
    }

    public String getText(int viewId) {
        TextView view = getChild(viewId);
        return view.getText().toString();
    }

    public ViewHolder setTextColor(int viewId, int color) {
        TextView view = getChild(viewId);
        view.setTextColor(color);
        return this;
    }

    public ViewHolder setTextDrawableLeft(int viewId, Drawable drawable) {
        TextView view = getChild(viewId);
        view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        return this;
    }

    public ViewHolder setTextDrawableTop(int viewId, Drawable drawable) {
        TextView view = getChild(viewId);
        view.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        return this;
    }

    public ViewHolder setTextDrawableRight(int viewId, Drawable drawable) {
        TextView view = getChild(viewId);
        view.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        return this;
    }

    public ViewHolder setTextDrawableBottom(int viewId, Drawable drawable) {
        TextView view = getChild(viewId);
        view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        return this;
    }

    public int getProgress(int viewId) {
        ProgressBar view = getChild(viewId);
        return view.getProgress();
    }

    public ProgressBar setProgress(int viewId, int progress) {
        ProgressBar view = getChild(viewId);
        view.setProgress(progress);
        return view;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        setProgress(viewId, progress).setMax(max);
        return this;
    }

    public float getRating(int viewId) {
        RatingBar view = getChild(viewId);
        return view.getRating();
    }

    public RatingBar setRating(int viewId, float rating) {
        RatingBar view = getChild(viewId);
        view.setRating(rating);
        return view;
    }

    public ViewHolder setRating(int viewId, float rating, int max) {
        setRating(viewId, rating).setMax(max);
        return this;
    }

    public ViewHolder setImage(int viewId, int resId) {
        ImageView view = getChild(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImage(int viewId, Drawable drawable) {
        ImageView view = getChild(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImage(int viewId, Bitmap bitmap) {
        ImageView view = getChild(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setBackground(int viewId, int resId) {
        View view = getChild(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public ViewHolder setBackground(int viewId, Drawable drawable) {
        View view = getChild(viewId);
        view.setBackground(drawable);
        return this;
    }

    public ViewHolder show(int viewId) {
        View view = getChild(viewId);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    public ViewHolder hide(int viewId) {
        View view = getChild(viewId);
        view.setVisibility(View.GONE);
        return this;
    }

    public ViewHolder invisible(int viewId) {
        View view = getChild(viewId);
        view.setVisibility(View.INVISIBLE);
        return this;
    }

    public <T extends ViewHolder> ViewHolder setOnClickListener(int viewId, OnClickListener<T> listener) {
        this.listener = listener;
        getChild(viewId).setOnClickListener(this);
        return this;
    }

    public <T extends ViewHolder> ViewHolder setOnClickListener(OnClickListener<T> listener) {
        this.listener = listener;
        itemView.setOnClickListener(this);
        return this;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(this);
        }
    }

    public interface OnClickListener<T extends ViewHolder> {
        void onClick(T holder);
    }
}
