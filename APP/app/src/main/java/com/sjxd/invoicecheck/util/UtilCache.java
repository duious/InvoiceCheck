package com.sjxd.invoicecheck.util;

import android.content.ContextWrapper;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * 缓存工具
 *
 * @author zhangyl
 * <p>
 * 17-10-26
 */
public class UtilCache {
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public UtilCache(ContextWrapper contextWrapper) {
        preferences = contextWrapper.getSharedPreferences("mercury", MODE_PRIVATE);
        editor = preferences.edit();
    }

    public String getCookie() {
        return preferences.getString("cookie", null);
    }

    public Boolean putCookie(String val) {
        editor.putString("cookie", val);
        return editor.commit();
    }

    public String getValue(String str) {
        return preferences.getString(str, null);
    }


    public Boolean putValue(String str, String val) {
        editor.putString(str, val);
        return editor.commit();
    }


    public boolean delValue(String str) {
        editor.remove(str);
        return editor.commit();
    }
}
