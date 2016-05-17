package com.getbase.floatingactionbutton.sample;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jimin on 2015-10-04.
 */
public class SharedPreferencesMethod {
    public SharedPreferencesMethod() {
    }


    // 값 불러오기
    public String getPreferences(Context context, String table, String field) {
        SharedPreferences pref = context.getSharedPreferences(table, context.MODE_PRIVATE);
        return pref.getString(field, "");

    }

    // 값 저장하기
    public static void savePreferences(Context context, String table, String field, String value) {
        SharedPreferences pref = context.getSharedPreferences(table, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(field, value);
        editor.commit();
    }

    // 값(Key Data) 삭제하기
    public static void removePreferences(Context context, String table, String field) {
        SharedPreferences pref = context.getSharedPreferences(table, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(field);
        editor.commit();
    }

    // 값(ALL Data) 삭제하기
    public static void removeAllPreferences(Context context, String table) {
        SharedPreferences pref = context.getSharedPreferences(table, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    // split으로 나누기
    public static void splitPreferences(Context context) {
        SharedPreferencesMethod spm = new SharedPreferencesMethod();
        MainActivity.spl = spm.getPreferences(context, "DB", "PUSH").split("/");
        MainActivity.selectedapp = MainActivity.spl.length;
    }
}
