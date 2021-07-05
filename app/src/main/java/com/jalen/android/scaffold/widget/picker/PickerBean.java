package com.jalen.android.scaffold.widget.picker;

public class PickerBean {
    private String id;
    private String name;
    private Object tag;

    public PickerBean(String id, String name) {
        this(id, name, null);
    }

    public PickerBean(String id, String name, Object tag) {
        this.id = id;
        this.name = name;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Object getTag() {
        return tag;
    }
}
