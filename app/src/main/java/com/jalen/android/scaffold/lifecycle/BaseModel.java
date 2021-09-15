package com.jalen.android.scaffold.lifecycle;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class BaseModel extends ViewModel {
    private MutableLiveData<Event> status;
    private ObservableBoolean refresh;
    private MutableLiveData<Boolean> loading;
    private int refreshTimes;
    private List<LifeObject> lifeObjects;

    public BaseModel() {
        status = new MutableLiveData<>();
        refresh = new ObservableBoolean(false);
        loading = new MutableLiveData<>();
        refreshTimes = 0;
        lifeObjects = new ArrayList<>();
    }

    public ObservableBoolean getRefresh() {
        return refresh;
    }

    public MutableLiveData<Event> getStatus() {
        return status;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public BaseModel init() {
        return this;
    }

    public void refresh() {
        if (refreshTimes == 0) {
            refresh.set(true);
        }
    }

    public void openRefreshAnimation(boolean canRefresh) {
        if (canRefresh) {
            if (0 == refreshTimes++) {
                if (!refresh.get()) {
                    loading.setValue(true);
                }
            }
        }
    }

    public void closeRefreshAnimation() {
        if (0 == --refreshTimes) {
            if (refresh.get()) {
                refresh.set(false);
            } else {
                loading.setValue(false);
            }
        }
    }

    public void addLifeObject(LifeObject object) {
        if (lifeObjects != null) {
            lifeObjects.add(object);
        } else {
            object.destroy();
        }
    }

    public void finish() {
        doAction("finish");
    }

    public void doAction(String type, Object... params) {
        status.postValue(new Event(type, params));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (lifeObjects != null) {
            for (LifeObject object : lifeObjects) {
                object.destroy();
            }
        }
        lifeObjects = null;
    }
}
