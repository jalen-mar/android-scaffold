package com.jalen.android.scaffold.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
    private Context application;

    public Storage() {
        this(AppUtil.getApplicationContext());
    }

    public Storage(Context application) {
        this.application = application;
    }

    public SharedPreferences getGlobalStorage() {
        return application.getSharedPreferences("APP_STORAGE", Context.MODE_PRIVATE);
    }

    public String getCurrentUserId() {
        return getGlobalStorage().getString("current_user_id", "");
    }

    public SharedPreferences getUserStorage() {
        return application.getSharedPreferences(getCurrentUserId(), Context.MODE_PRIVATE);
    }

//    public String getCurrentUserToken() {
//        return getUserStorage().getString("current_user_authentication", "");
//    }
//
//    public Storage save(String userId, String token) {
//        getGlobalStorage().edit().putString("current_user_id", userId).apply();
//        getUserStorage().edit().putString("current_user_authentication", token).apply();
//        return this;
//    }
//
//    public String getUserData(String key) {
//        return getUserStorage().getString(key, "");
//    }
//
//    public Storage saveUserData(String key, String value) {
//        getUserStorage().edit().putString(key, value).apply();
//        return this;
//    }
//
    public Storage delete(String key) {
        getGlobalStorage().edit().remove(key).apply();
        return this;
    }

    public String getString(String key) {
        return getGlobalStorage().getString(key, "");
    }

    public Boolean getBoolean(String key) {
        return getGlobalStorage().getBoolean(key, false);
    }

    public Storage save(String key, String value) {
        getGlobalStorage().edit().putString(key, value).apply();
        return this;
    }

    public Storage save(String key, Boolean value) {
        getGlobalStorage().edit().putBoolean(key, value).apply();
        return this;
    }
//
//    public void delete() {
//        getUserStorage().edit().clear().apply();
//        getGlobalStorage().edit().remove("current_user_id").apply();
//    }
}
