package com.jalen.android.scaffold.lifecycle;

import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Event {
    private String action;
    private List<Object> params;

    public Event(String action, Object[] params) {
        this.action = action;
        this.params = Arrays.asList(params);
    }

    public Event(String action, List<Object> params) {
        this.action = action;
        this.params = params;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public void invoke(Object target) {
        if (TextUtils.isEmpty(action)) {
            action = "finish";
            params = null;
        }
        try {
            if (params != null) {
                int len = params.size();
                Class[] classes = new Class[len];
                Object[] array = new Object[len];
                for (int i = 0; i < len; i++) {
                    Object obj = params.get(i);
                    if (obj instanceof List) {
                        classes[i] = List.class;
                    } else if (obj instanceof Map) {
                        classes[i] = Map.class;
                    } else if (obj instanceof Number) {
                        switch (obj.getClass().getSimpleName()) {
                            case "Integer" :
                                classes[i] = int.class;
                                break;
                            case "Float" :
                                classes[i] = float.class;
                                break;
                            case "Character" :
                                classes[i] = char.class;
                                break;
                            case "Long" :
                                classes[i] = long.class;
                                break;
                            case "Double" :
                                classes[i] = double.class;
                                break;
                            case "Byte" :
                                classes[i] = byte.class;
                                break;
                            case "Short" :
                                classes[i] = short.class;
                                break;
                        }
                    } else if (obj instanceof Boolean) {
                        classes[i] = boolean.class;
                    } else if (obj instanceof Character) {
                        classes[i] = char.class;
                    } else if (obj instanceof Set) {
                        classes[i] = Set.class;
                    } else {
                        classes[i] = obj.getClass();
                    }
                }
                Method method = target.getClass().getMethod(action, classes);
                method.invoke(target, params.toArray(array));
            } else {
                Method method = target.getClass().getMethod(action);
                method.invoke(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
