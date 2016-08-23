package com.tool.phoneutils.utils;

import android.util.Log;

/**
 * Created by wlhuang on 16-6-22.
 * 日志工具类
 * 统一管理日志
 */
public class L {

    private static final String TAG = L.class.getName();
    private static final int TARGET_LEVEL = 3;
    private static final boolean IS_DEBUG = true;
    private static final boolean IS_BUGLY = false;
    public L() {
    }

    static class LogInfo{
        String TAG;
        String message;

        public String getTAG() {
            return TAG;
        }

        public void setTAG(String TAG) {
            this.TAG = TAG;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private static String[] getCallMethod(){
        Throwable t = new Throwable();
        StackTraceElement[] stackTraceElements = t.getStackTrace();
        int level = TARGET_LEVEL;
        StackTraceElement s = stackTraceElements.length > level ?  stackTraceElements[level]:null;
        String mMethod = s != null ? s.getMethodName():"Couldn't find Method";
        String mFileName = s != null ? s.getFileName():"Couldn't find find";
        String mClass = s != null ? s.getClassName() :"Couldn't find Class";
        String mLine = s != null ?  s.getLineNumber()+"":"Couldn't find Line";
        return new String[]{mMethod,mFileName,mLine};
    }

    private static LogInfo getBaseInfo(String message){
        LogInfo logInfo = new LogInfo();
        String[] callerInfo = getCallMethod();
        logInfo.setMessage(message+"   ("+callerInfo[1]+" "+callerInfo[2] + ")");
        logInfo.setTAG(callerInfo[0]);//Thread.currentThread().getName() + ":
        return  logInfo;
    }

    public static void i(String message){
        LogInfo logInfo = getBaseInfo(message);
        if (IS_BUGLY){
//            BuglyLog.i(logInfo.getTAG(),logInfo.getMessage());
        }else if (IS_DEBUG){
            Log.i(logInfo.getTAG(), logInfo.getMessage());
        }
    }

    public static void d(String message){
        LogInfo logInfo = getBaseInfo(message);
        if (IS_BUGLY){
//            BuglyLog.d(logInfo.getTAG(),logInfo.getMessage());
        }else if (IS_DEBUG){
            Log.d(logInfo.getTAG(), logInfo.getMessage());
        }
    }

    public static void w(String message){
        LogInfo logInfo = getBaseInfo(message);
        if (IS_BUGLY){
//            BuglyLog.w(logInfo.getTAG(),logInfo.getMessage());
        }else if (IS_DEBUG){
            Log.w(logInfo.getTAG(), logInfo.getMessage());
        }
    }

    public static void e(String message){
        LogInfo logInfo = getBaseInfo(message);
        if (IS_BUGLY){
//            BuglyLog.e(logInfo.getTAG(),logInfo.getMessage());
        }else if (IS_DEBUG){
            Log.e(logInfo.getTAG(), logInfo.getMessage());
        }
    }
    public static void v(String message){
        LogInfo logInfo = getBaseInfo(message);
        if (IS_BUGLY){
//            BuglyLog.v(logInfo.getTAG(),logInfo.getMessage());
        }else if (IS_DEBUG){
            Log.v(logInfo.getTAG(), logInfo.getMessage());
        }
    }


}
