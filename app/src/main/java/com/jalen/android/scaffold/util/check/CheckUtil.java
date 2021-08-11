package com.jalen.android.scaffold.util.check;

import com.jalen.android.scaffold.util.StringUtil;
import com.jalen.android.scaffold.widget.Toast;

public class CheckUtil implements ErrorCallback {
    private ErrorCallback callback;

    private CheckUtil(ErrorCallback callback) {
        this.callback = callback == null ? this : callback;
    }

    @Override
    public void showError(String msg) {
        Toast.show(msg);
    }

    public static CheckUtil getInstance() {
        return getInstance(null);
    }

    public static CheckUtil getInstance(ErrorCallback callback) {
        return new CheckUtil(callback);
    }

    public CheckUtil isNotEmpty(String value, String msg) {
        return check(!StringUtil.isEmpty(value), msg);
    }

    public CheckUtil isEquals(String value1, String value2, String msg) {
        return check(!StringUtil.isEmpty(value1) && value1.equals(value2), msg);
    }

    public CheckUtil isMobile(String value, String msg) {
        return check(StringUtil.isMobile(value), msg);
    }

    public CheckUtil isEmail(String value, String msg) {
        return check(StringUtil.isEmail(value), msg);
    }

    public CheckUtil isPlateNo(String value, String msg) {
        return check(StringUtil.isPlateNo(value), msg);
    }

    public CheckUtil isBankCard(String value, String msg) {
        return check(StringUtil.isBankCard(value), msg);
    }

    public CheckUtil isIP(String value, String msg) {
        return check(StringUtil.isIP(value), msg);
    }

    public CheckUtil isID(String value, String msg) {
        return check(StringUtil.isID(value), msg);
    }

    public CheckUtil between(String value, int min, int max, String msg) {
        return check(StringUtil.between(value, min, max), msg);
    }

    public CheckUtil isCheck(boolean value, String msg) {
        return check(value, msg);
    }

    private CheckUtil check(boolean val, String msg) {
        CheckUtil result = null;
        if (val) {
            result = this;
        } else {
            callback.showError(msg);
        }
        return result;
    }

    public void execute(SuccessCallback task) {
        task.execute();
    }
}
