package com.pichs.permissions.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    // 获取app的name
    public static String getAppName(Context context) {
        ApplicationInfo appInfo = getApplicationInfo(context);
        return (String) (appInfo != null ? getPackageManager(context).getApplicationLabel(appInfo) : "");
    }

    public static ApplicationInfo getApplicationInfo(Context context) {
        try {
            return getPackageManager(context).getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PackageManager getPackageManager(Context context) {
        return context.getApplicationContext().getPackageManager();
    }

    public static String[] mapToArray(Map<String, Boolean> permissionMap) {
        if (permissionMap == null || permissionMap.isEmpty()) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : permissionMap.entrySet()) {
            // 去重
            if (!list.contains(entry.getKey())) {
                list.add(entry.getKey());
            }
        }
        return PermissionHelper.stringListToArray(list);
    }

}
