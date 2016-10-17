package com.tool.phoneutils;

import android.app.Application;
import android.content.Context;

/**
 * Created by wlhuang on 17/10/2016.
 */

public class CustomApplication extends Application{
    public static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();
    }
}
