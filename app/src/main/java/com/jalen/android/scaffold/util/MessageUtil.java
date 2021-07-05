package com.jalen.android.scaffold.util;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MessageUtil {
    private static Map<Object, MutableLiveData<?>> observables;

    static {
        observables = new HashMap<>();
    }

    private static <T> void register(LifecycleOwner owner, String action, T params,
                                     int initVersion, Observer<T> observer) {
        MutableLiveData<T> data = (MutableLiveData<T>) observables.get(action);
        if (data == null) {
            observables.put(action, data = new EventMutableLiveData<>(initVersion));
        }
        if (params != null) {
            data.setValue(params);
        }
        data.observe(owner, observer);
    }

    @MainThread
    public static <T> void register(LifecycleOwner owner, String action, T params, Observer<T> observer) {
        register(owner, action, params, -1, observer);
    }

    @MainThread
    public static <T> void register(LifecycleOwner owner, String action, Observer<T> observer) {
        register(owner, action, null, 0, observer);
    }

    @MainThread
    public static void post(String action, Object params) {
        MutableLiveData data = observables.get(action);
        if (data != null) {
            data.setValue(params);
        }
    }

    @MainThread
    public static void unregister(LifecycleOwner owner, String action) {
        MutableLiveData data = observables.get(action);
        if (data != null) {
            data.removeObservers(owner);
            if (!data.hasObservers()) {
                observables.remove(action);
            }
        }
    }

    private static class ObserverWrapper<T> implements Observer<T> {

        private Observer<T> observer;

        public ObserverWrapper(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (observer != null && !isCallOnObserve()) {
                observer.onChanged(t);
            }
        }

        private boolean isCallOnObserve() {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                for (StackTraceElement element : stackTrace) {
                    if ("androidx.lifecycle.LiveData".equals(element.getClassName()) &&
                            "observeForever".equals(element.getMethodName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    static class EventMutableLiveData<T> extends MutableLiveData<T> {
        private Map<Observer, Observer> observerMap = new HashMap<>();
        private int initVersion;

        EventMutableLiveData(int initVersion) {
            this.initVersion = initVersion;
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, observer);
            try {
                hook(observer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void observeForever(@NonNull Observer<? super T> observer) {
            if (!observerMap.containsKey(observer)) {
                observerMap.put(observer, new ObserverWrapper(observer));
            }
            super.observeForever(observerMap.get(observer));
        }

        @Override
        public void removeObserver(@NonNull Observer<? super T> observer) {
            Observer realObserver;
            if (observerMap.containsKey(observer)) {
                realObserver = observerMap.remove(observer);
            } else {
                realObserver = observer;
            }
            super.removeObserver(realObserver);
        }

        private void hook(@NonNull Observer<? super T> observer) throws Exception {
            //get wrapper's version
            Class<LiveData> classLiveData = LiveData.class;
            Field fieldObservers = classLiveData.getDeclaredField("mObservers");
            fieldObservers.setAccessible(true);
            Object objectObservers = fieldObservers.get(this);
            fieldObservers.setAccessible(false);
            Class<?> classObservers = objectObservers.getClass();
            Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
            methodGet.setAccessible(true);
            Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
            methodGet.setAccessible(false);
            Object objectWrapper = null;
            if (objectWrapperEntry instanceof Map.Entry) {
                objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
            }
            if (objectWrapper == null) {
                throw new NullPointerException("Wrapper can not be null!");
            }
            Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
            Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
            fieldLastVersion.setAccessible(true);
            //get livedata's version
            Field fieldVersion = classLiveData.getDeclaredField("mVersion");
            fieldVersion.setAccessible(true);
            Object objectVersion = fieldVersion.get(this);
            fieldVersion.setAccessible(false);
            //set wrapper's version
            fieldLastVersion.set(objectWrapper, (int) objectVersion + initVersion);
            fieldLastVersion.setAccessible(false);
        }
    }
}
