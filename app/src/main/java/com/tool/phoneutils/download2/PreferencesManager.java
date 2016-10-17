package com.tool.phoneutils.download2;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wlhuang on 16-5-16.
 */
public class PreferencesManager {
    protected static final String PREF_NAME = "odm_config";
    public static final String TRANSFER_MODE = "transfer_mode";
    public static final String LANGUAGE_TYPE = "language_type";
    public static final String DONT_PROMPT = "prompt_tips";
    public static final String ACCOUNT_NAME = "google_account_name";
    public static final String FACEBOOK_NAME = "facebook_account_name";

    public static final String CURRENT_FW_VERSION = "current_fw_version";//当前固件版本
    public static final String OLD_FW_NAME = "old_fw_name";//已更新固件版本
    public static final String NEW_FW_NAME = "new_fw_name";//新固件版本
    public static final String FW_UPLOAD_URL = "fw_upload_url";//上传url;
    public static final String LOCAL_FILE_LIST = "local_file_list";
    public static final String SN_CODE = "sn_code";
    public static final String RATING_FILE_NAME = "rating_file_name";
    public static final String RATING_FILE_SIZE = "rating_file_size";
    public static final String OLD_VERSION = "old_version";
    public static final String DEVICE_WIFI_SSID = "device_wifi_ssid";
    public static final String CURRENT_DEVICE_WIFI_SSID = "current_device_wifi_ssid";
    private final SharedPreferences mPref;
    private static PreferencesManager instance;

    public static synchronized void initializeInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
    }

    public PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public SharedPreferences getPref() {
        return mPref;
    }

    public SharedPreferences.Editor getEditor() {
        return mPref.edit();
    }

    public void saveInt(String key, int i) {
        getEditor().putInt(key, i).apply();
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        return getPref().getInt(key, defValue);
    }

    public void saveLong(String key, long l) {
        getEditor().putLong(key, l).apply();
    }

    public Long getLong(String key) {
        return getLong(key, 0l);
    }

    public Long getLong(String key, long defValue) {
        return getPref().getLong(key, defValue);
    }

    public void saveString(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defValue) {
        return getPref().getString(key, defValue);
    }

    public String getNewFwName() {
        return mPref.getString(NEW_FW_NAME, null);
    }

    public void setNewFwName(String fwName) {
        getEditor().putString(NEW_FW_NAME, fwName).apply();
    }

    public String getOldFwName() {
        return mPref.getString(OLD_FW_NAME, null);
    }

    public void setOldFwName(String fwName) {
        getEditor().putString(OLD_FW_NAME, fwName).apply();
    }

    public String getCurFwVersion() {
        return mPref.getString(CURRENT_FW_VERSION, null);
    }

    public void setCurFwVersion(String fwName) {
        getEditor().putString(CURRENT_FW_VERSION, fwName).apply();
    }

    public String getFwUploadUrl() {
        return mPref.getString(FW_UPLOAD_URL, null);
    }

    public void setFwUploadUrl(String fwName) {
        getEditor().putString(FW_UPLOAD_URL, fwName).apply();
    }

    public String getLocalFileListJson() {
        return getPref().getString(LOCAL_FILE_LIST, null);
    }

    public void setLocalFileListJson(String json) {
        getEditor().putString(LOCAL_FILE_LIST, json).apply();
    }

    public void setOldVersion(String version){
        getEditor().putString(OLD_VERSION,version).apply();
    }

    public String getOldVersion(){
        return getPref().getString(OLD_VERSION,"1.01");
    }

    public String getSNCode() {
        return getPref().getString(SN_CODE, "xxx");
    }

    public void setSNCode(String snCode) {
        getEditor().putString(SN_CODE, snCode).apply();
    }

    public String getRatingFileName() {
        return getPref().getString(RATING_FILE_NAME, null);
    }

    public void setRatingFileName(String ratingFileName) {
        getEditor().putString(RATING_FILE_NAME, ratingFileName).apply();
    }

    public long getRatingFileSize() {
        return getPref().getLong(RATING_FILE_SIZE, -1l);
    }

    public void setRatingFileSize(long fileSize) {
        getEditor().putLong(RATING_FILE_SIZE, fileSize).apply();
    }

    public Set<String> getDeviceWifiSSID() {
        return getPref().getStringSet(DEVICE_WIFI_SSID, null);
    }

    public void setDeviceWifiSSID(String deviceWifiSSID) {
        Set<String> stringSet = getDeviceWifiSSID();
        if (stringSet == null){
            stringSet = new HashSet<>();
        }
        if (!stringSet.contains(deviceWifiSSID)){
            stringSet.add(deviceWifiSSID);
            getEditor().putStringSet(DEVICE_WIFI_SSID, stringSet).apply();
        }
    }

    public void setCurDeviceWifiSsid(String curWifi){
        getEditor().putString(CURRENT_DEVICE_WIFI_SSID,curWifi).apply();
    }

    public String getCurDeviceWifiSsid(){
        return getPref().getString(CURRENT_DEVICE_WIFI_SSID,null);
    }

    public void clearKey(String s) {
        getEditor().remove(s).apply();
    }
}
