package com.jalen.android.scaffold.widget.loading;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.jalen.android.scaffold.R;

public class DefaultLoading extends DialogFragment implements Loading {
    private FragmentManager manager;

    public DefaultLoading(FragmentManager manager) {
        this.manager = manager;
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
        View view = inflater.inflate(R.layout.dialog_default_loading, container, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        window.setLayout(params.width, params.height);
        return view;
    }

    @Override
    @SuppressLint("RestrictedApi")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(this.getActivity(), this.getTheme()) {
            public boolean dispatchKeyEvent(KeyEvent event) {
                return getActivity().dispatchKeyEvent(event);
            }
        };
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void show(@NonNull FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.showNow(manager, tag);
        }
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void show() {
        show(manager, toString());
    }
}
