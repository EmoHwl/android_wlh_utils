package com.tool.phoneutils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

/**
 * Created by wlhuang on 16-5-16.
 */
public class BaseSPUtils {
    protected Context context;
    private static SharedPreferences sharedPreferences;

    protected BaseSPUtils(Context context,String config) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
    }

    protected BaseSPUtils(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    protected SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor(){
        return sharedPreferences.edit();
    }

    public void saveInt(String key, int i){
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key,i);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public void saveString(String key, String value){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key,value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public void saveBoolean(String key,boolean value){
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(key,value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public int getInt(String key){
        return  getInt(key,0);
    }

    public int getInt(String key, int defValue){
        return getSharedPreferences().getInt(key,defValue);
    }

    public String getString(String key){
        return  getString(key,"");
    }

    public String getString(String key, String defValue){
        return getSharedPreferences().getString(key,defValue);
    }
}
