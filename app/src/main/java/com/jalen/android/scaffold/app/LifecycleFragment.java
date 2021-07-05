package com.jalen.android.scaffold.app;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

public class LifecycleFragment extends Fragment {
    @MainThread
    public static ViewModelProvider createViewModel(Fragment fragment) {
        return createViewModel(fragment, null);
    }

    @MainThread
    public static ViewModelProvider createViewModel(FragmentActivity activity) {
        return createViewModel(activity, null);
    }

    @MainThread
    public static ViewModelProvider createViewModel(Fragment fragment, ViewModelProvider.Factory factory) {
        Application application = checkApplication(checkActivity(fragment));
        if (factory == null) {
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
        return new ViewModelProvider(fragment.getViewModelStore(), factory);
    }

    @MainThread
    public static ViewModelProvider createViewModel(FragmentActivity activity, ViewModelProvider.Factory factory) {
        Application application = checkApplication(activity);
        if (factory == null) {
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
        return new ViewModelProvider(activity.getViewModelStore(), factory);
    }

    private static Activity checkActivity(Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
        }
        return activity;
    }

    private static Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to "
                    + "Application. You can't request ViewModel before onCreate call.");
        }
        return application;
    }
}
