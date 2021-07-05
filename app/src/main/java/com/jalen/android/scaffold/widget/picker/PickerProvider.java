package com.jalen.android.scaffold.widget.picker;

import java.util.List;

public interface PickerProvider {
    void loadItem(PickerBean bean, Sender sender);

    interface Sender {
        void send(List<PickerBean> list);
    }
}
