package com.jalen.android.scaffold.widget.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jalen.android.scaffold.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BinderAdapter<T, V extends ViewHolder> extends RecyclerView.Adapter<V> {
    public final List<T> list;
    private int resId;
    public final LayoutInflater inflater;

    public BinderAdapter(LayoutInflater inflater) {
        this(inflater, new ArrayList<T>());
    }

    public BinderAdapter(LayoutInflater inflater, List<T> list) {
        this.inflater = inflater;
        this.list = list;
    }

    public BinderAdapter(LayoutInflater inflater, int resId) {
        this(inflater);
        this.resId = resId;
    }

    public int getEmptyView() {
        return R.layout.item_empty;
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int resId) {
        View view = inflater.inflate(resId, parent, false);
        ViewHolder holder;
        if (resId == getEmptyView()) {
            holder = BinderHolder.newInstance(view);
        } else {
            try {
                holder = BinderHolder.newInstance(DataBindingUtil.bind(view));
            } catch (Exception e) {
                holder = BinderHolder.newInstance(view);
            }
        }
        return (V) holder;
    }

    @Override
    public final void onBindViewHolder(@NonNull V holder, int position) {
        if (list.size() > position) {
            convert(holder, position);
        }
    }

    public abstract void convert(@NonNull V holder, int position);

    @Override
    public final int getItemViewType(int position) {
        if (list.size() > position) {
            return resId;
        } else {
            return getEmptyView();
        }
    }

    @Override
    public int getItemCount() {
        int count = list.size();
        if (count == 0) {
            count += 1;
        }
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position == -1 ? 5 : super.getItemId(position);
    }

    public void addItem(Object obj) {
        list.add((T) obj);
        notifyItemInserted(list.size());
    }

    public void addAll(List list) {
        int index = this.list.size();
        this.list.addAll(list);
        if (index == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(index, list.size());
        }
    }

    public void delete(Object obj) {
        int index = list.indexOf(obj);
        if (index != -1) {
            list.remove(obj);
            notifyItemRemoved(index);
        }
    }

    public void clear() {
        int count = list.size();
        if (count != 0) {
            list.clear();
            notifyItemRangeRemoved(0, count);
        }
    }
}
