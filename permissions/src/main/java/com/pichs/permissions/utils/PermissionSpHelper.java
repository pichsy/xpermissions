package com.pichs.permissions.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PermissionSpHelper {

    private static SharedPreferences mSp;
    private static final String SP_NAME = "my-permissions-sharedprefs";
    private static PermissionSpHelper sSpUtils;

    /**
     * 单利模式
     */
    private PermissionSpHelper(Context context) {
        mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static PermissionSpHelper getInstance(Context context) {
        if (sSpUtils == null) {
            synchronized (PermissionSpHelper.class) {
                if (sSpUtils == null) {
                    sSpUtils = new PermissionSpHelper(context.getApplicationContext());
                }
            }
        }
        return sSpUtils;
    }

    @SuppressLint("ApplySharedPref")
    public void setBoolean(String key, boolean value) {
        mSp.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, Boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    public boolean isFirstRefuseShowDialog() {
        return getBoolean("isFirstRefuseShowDialog", false);
    }

    public void setFirstRefuseShowDialog(boolean isFirstRefuseShowDialog) {
        setBoolean("isFirstRefuseShowDialog", isFirstRefuseShowDialog);
    }

    /**
     * 是否勾选了禁止后不再提示
     * @return isNeverTips
     */
    public boolean isNeverTips() {
        return getBoolean("isNeverTips", false);
    }

    /**
     * 设置禁止后不再提示
     * @param tipsAble tipsAble
     */
    public void setNeverTips(boolean tipsAble) {
        setBoolean("isNeverTips", tipsAble);
    }

    /**
     * 是否第一次申请权限
     *
     * @return isFirstRequestPermission
     */
    public boolean isFirstRequestPermission() {
        return getBoolean("isFirstRequestPermission", true);
    }

    /**
     * 设置是否是 第一次申请权限
     *
     * @param isFirstRequestPermission isFirstRequestPermission
     */
    public void setFirstRequestPermission(boolean isFirstRequestPermission) {
        setBoolean("isFirstRequestPermission", isFirstRequestPermission);
    }

}
