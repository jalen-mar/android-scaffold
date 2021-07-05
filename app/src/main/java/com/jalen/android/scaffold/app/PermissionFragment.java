package com.jalen.android.scaffold.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionFragment extends LifecycleFragment {
    private static final String PERMISSION_FRAGMENT_TAG = "com.jalen.android.scaffold.app.fragment@permission";

    protected static void injectIfNeededIn(AppCompatActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        if (manager.findFragmentByTag(PERMISSION_FRAGMENT_TAG) == null) {
            manager.beginTransaction().add(new PermissionFragment(), PERMISSION_FRAGMENT_TAG).commit();
            manager.executePendingTransactions();
        }
    }

    @Override
    public Context getContext() {
        Context context = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = super.getContext();
        }
        if(context == null) {
            context = getActivity();
        }
        return context;
    }

    private boolean checkPermission(String permission) {
        return getActivity().checkPermission(permission, Process.myPid(),
                Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    private List<String> checkPermission(String[] permissions) {
        List<String> authorities = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermission(permission)) {
                authorities.add(permission);
            }
        }
        return authorities;
    }

    protected void requestPermission(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> authorities;
            if ((authorities = checkPermission(permissions)).size() == 0) {
                onRequestPermissionsResult(requestCode,  new String[0], new int[0]);
            } else {
                permissions = new String[authorities.size()];
                requestPermissions(authorities.toArray(permissions), requestCode);
            }
        } else {
            onRequestPermissionsResult(requestCode, new String[0], new int[0]);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public final void onRequestPermissionsResult(int requestCode, String[] permissions, int[] result) {
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < result.length; i++) {
            if (result[i] != PackageManager.PERMISSION_GRANTED) {
                boolean reset = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    reset = shouldShowRequestPermissionRationale(permissions[i]);
                }
                if (reset){
                    requestPermission(permissions, requestCode);
                    return;
                } else {
                    permissionList.add(permissions[i]);
                }
            }
        }
        int[] status = new int[permissionList.size()];
        Arrays.fill(status, PackageManager.PERMISSION_DENIED);
        onPermissionsResult(
                requestCode,
                permissionList.toArray(new String[permissionList.size()]),
                status
        );
    }

    private void onPermissionsResult(int requestCode, String[] permissions, int[] result) {
        getActivity().onRequestPermissionsResult(requestCode,  permissions, result);
    }

    static PermissionFragment get(AppCompatActivity activity) {
        return (PermissionFragment) activity.getSupportFragmentManager().findFragmentByTag(PERMISSION_FRAGMENT_TAG);
    }
}
