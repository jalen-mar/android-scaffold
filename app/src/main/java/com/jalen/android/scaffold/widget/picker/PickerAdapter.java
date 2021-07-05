package com.jalen.android.scaffold.widget.picker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jalen.android.scaffold.R;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PickerAdapter extends RecyclerView.Adapter<Holder> implements View.OnClickListener {
    private List<PickerBean> list;
    private List<PickerBean> data;
    private LayoutInflater inflater;
    private View.OnClickListener listener;
    private boolean multiple;
    private Set<PickerBean> beans;
    private Object tag;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setKeyword(String keyword) {
        keyword = keyword == null ? "" : keyword;
        data.clear();
        for (PickerBean bean: list) {
            if (bean.getName().contains(keyword)) {
                data.add(bean);
            }
        }
        notifyDataSetChanged();
    }

    public PickerAdapter(List<PickerBean> list, LayoutInflater inflater, View.OnClickListener listener, boolean multiple) {
        this.list = list;
        this.data = new LinkedList<>(list);
        this.inflater = inflater;
        this.listener = listener;
        this.multiple = multiple;
        this.beans = new HashSet<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        return new Holder(inflater.inflate(R.layout.item_picker, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PickerBean bean = data.get(position);
        holder.view.setText(bean.getName());
        holder.view.setTag(bean);
        holder.view.setSelected(beans.contains(bean));
        holder.view.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        if (multiple) {
            if (beans.contains(v.getTag())) {
                beans.remove(v.getTag());
            } else {
                beans.add((PickerBean) v.getTag());
            }
        } else {
            beans.clear();
            beans.add((PickerBean) v.getTag());
        }
        listener.onClick(v);
    }

    public PickerAdapter refresh() {
        beans.clear();
        return this;
    }
}
