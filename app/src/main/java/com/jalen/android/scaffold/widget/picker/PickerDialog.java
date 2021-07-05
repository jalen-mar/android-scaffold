package com.jalen.android.scaffold.widget.picker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jalen.android.scaffold.R;
import com.jalen.android.scaffold.app.MainHandler;

import java.util.ArrayList;
import java.util.List;

public class PickerDialog extends DialogFragment implements View.OnClickListener, TextWatcher {
    private List<PickerBean> ids;
    private PickerProvider provider;
    private SenderHandler sender;
    private PickerCallback callback;
    private boolean multiple;
    private boolean customIndex;

    private HorizontalScrollView scrollView;
    private ViewGroup toolbar;
    private RecyclerView recycler;
    private boolean filter;
    private boolean close;

    public PickerDialog() {
        this(false, true);
    }

    public PickerDialog(boolean close) {
        this(false, close);
    }

    public PickerDialog(boolean filter, boolean close) {
        this.filter = filter;
        this.close = close;
        sender = new SenderHandler();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.dialogTheme);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (container == null)
            container = window.findViewById(android.R.id.content);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setGravity(Gravity.BOTTOM);
        View view = inflater.inflate(R.layout.dialog_picker, container, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        window.setLayout(params.width, params.height);
        view.findViewById(R.id.keyword).setVisibility(filter ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.closer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (close) {
                    dismiss();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = view.findViewById(R.id.scroll_layout);
        toolbar = view.findViewById(R.id.picker_toolbar);
        recycler = view.findViewById(R.id.list);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ids = new ArrayList<>();

        ((EditText) view.findViewById(R.id.keyword)).addTextChangedListener(this);

        if (multiple || customIndex) {
            view = view.findViewById(R.id.submit);
            view.setOnClickListener(this);
            view.setVisibility(View.VISIBLE);
        }
        MainHandler.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                provider.loadItem(null, sender);
            }
        }, 25);
    }

    public void setCallback(PickerCallback callback) {
        this.callback = callback;
    }

    public void show(FragmentManager manager, PickerProvider provider) {
        show(manager, false, provider);
    }

    public void show(FragmentManager manager, boolean multiple, PickerProvider provider) {
        show(manager, false, multiple, provider);
    }

    public void show(FragmentManager manager, boolean customIndex, boolean multiple, PickerProvider provider) {
        this.provider = provider;
        this.multiple = multiple;
        this.customIndex = customIndex;
        super.show(manager, toString());
    }

    @Override
    public void onClick(View v) {
        if (R.id.submit == v.getId()) {
            List<PickerBean> data;
            if (ids.size() != 0 && ids.get(ids.size() - 1) == null) {
                data = new ArrayList<>(ids.subList(0, ids.size() - 1));
            } else {
                data = new ArrayList<>(ids);
            }
            callback.selected(data);
            dismiss();
        } else if (v.getId() == R.id.item_picker_value) {
            PickerBean bean = (PickerBean) v.getTag();
            if (!multiple) {
                TextView titleView = (TextView) toolbar.getChildAt(toolbar.getChildCount() - 1);
                Object oldTag = ((PickerAdapter) titleView.getTag()).getTag();
                titleView.setText(bean.getName());
                ((PickerAdapter) titleView.getTag()).setTag(bean);
                titleView.setOnClickListener(this);
                ids.set(ids.indexOf(oldTag), bean);
                if (callback.selected(ids)) {
                    provider.loadItem(bean, sender);
                }
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            } else {
                ids.add(bean);
                recycler.getAdapter().notifyDataSetChanged();
            }
        } else {
            int index = toolbar.indexOfChild(v);
            int start = index + 1;
            toolbar.removeViews(start, toolbar.getChildCount() - start);
            ArrayList<PickerBean> list = new ArrayList<>();
            for (int i = start; i < ids.size(); i++) {
                list.add(ids.get(i));
            }
            ids.removeAll(list);
            recycler.setAdapter((RecyclerView.Adapter) v.getTag());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (recycler.getAdapter() instanceof PickerAdapter) {
            ((PickerAdapter) recycler.getAdapter()).setKeyword(s.toString());
        }
    }

    class SenderHandler implements PickerProvider.Sender {
        @Override
        public void send(List<PickerBean> list) {
            PickerAdapter adapter = new PickerAdapter(list, getLayoutInflater(), PickerDialog.this, multiple);
            send(adapter);
        }

        private void send(PickerAdapter adapter) {
            getLayoutInflater().inflate(R.layout.item_picker_title, toolbar);
            TextView view = (TextView) toolbar.getChildAt(toolbar.getChildCount() - 1);
            view.setText("请选择");
            view.setTag(adapter);
            if (!multiple) {
                ids.add(null);
            }
            recycler.setAdapter(adapter);
        }
    }
}
