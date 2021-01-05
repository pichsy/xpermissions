package com.pichs.permissions.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限申请的工具类
 */
public class PermissionHelper {

    private static final String TAG = PermissionHelper.class.getSimpleName();
    private static Object mObject; // activity or fragment
    private static Callback mCallback;

    private static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 对外接口
     *
     * @param activity    Activity
     * @param requestCode requestCode
     * @param callback    onCallback
     * @param permissions permissions
     */
    public static void requestPermissions(final Activity activity, int requestCode, Callback callback, final String... permissions) {
        mCallback = callback;
        mObject = activity;
        requestPermissions(activity, requestCode, permissions);
    }

    /**
     * 对外接口
     *
     * @param fragment    Fragment
     * @param requestCode requestCode
     * @param callback    onCallback
     * @param permissions permissions
     */
    public static void requestPermissions(final Fragment fragment, int requestCode, Callback callback, final String... permissions) {
        mCallback = callback;
        mObject = fragment;
        requestPermissions(fragment, requestCode, permissions);
    }


    private static void requestPermissions(Object object, int requestCode, String... permissions) {
        // 开始检测 权限列表中的权限 分类
        List<String> deniedPermissions = findDeniedPermissions(getActivity(object), permissions);
        doRequestPermissions(object, requestCode, stringListToArray(deniedPermissions));
    }


    private static void doRequestPermissions(Object object, int requestCode, String[] permissions) {
        if (!isOverMarshmallow()) {
            if (mCallback != null) {
                mCallback.onGranted();
            }
            return;
        }

        List<String> deniedPermissions = findDeniedPermissions(getActivity(object), permissions);
        if (deniedPermissions != null && deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(stringListToArray(deniedPermissions), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(stringListToArray(deniedPermissions), requestCode);
            }
        } else {
            if (mCallback != null) {
                mCallback.onGranted();
            }
        }
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, String[] permissions) {
        if (!isOverMarshmallow()) {
            return null;
        }
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permissions) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findNeverAskAgainPermissions(Activity activity, String... permissions) {
        List<String> neverAskAgainPermission = new ArrayList<>();
        if (!isOverMarshmallow()) {
            return neverAskAgainPermission;
        }
        for (String value : permissions) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED
                    && !activity.shouldShowRequestPermissionRationale(value)) {
                // 拒绝&不要需要提示了（即用户勾选了不再询问）
                neverAskAgainPermission.add(value);
            }
        }
        return neverAskAgainPermission;
    }


    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedWithoutNeverAskAgainPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();
        if (!isOverMarshmallow()) {
            return denyPermissions;
        }
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED
                    && activity.shouldShowRequestPermissionRationale(value)) {
                denyPermissions.add(value); // 上次申请被用户拒绝了
            }
        }
        return denyPermissions;
    }


    @TargetApi(value = Build.VERSION_CODES.M)
    public static boolean hasNeverAskAgainPermission(Activity activity, String... permission) {
        if (!isOverMarshmallow()) {
            return false;
        }
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED &&
                    !activity.shouldShowRequestPermissionRationale(value)) {
                return true;
            }
        }
        return false;
    }


    @TargetApi(value = Build.VERSION_CODES.M)
    public static boolean hasDeniedPermission(Activity activity, String... permission) {
        if (!isOverMarshmallow()) {
            return false;
        }
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }


    @TargetApi(value = Build.VERSION_CODES.M)
    public static boolean hasDeniedWithoutNeverAskAgainPermission(Activity activity, String... permission) {
        if (!isOverMarshmallow()) {
            return false;
        }
        for (String value : permission) {
            boolean b = activity.shouldShowRequestPermissionRationale(value);
            Log.d(TAG, "hasDeniedWithoutNeverAskAgainPermission: value: " + value);
            Log.d(TAG, "hasDeniedWithoutNeverAskAgainPermission: bool: " + b);
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED
                    && b) {
                return true;
            }
        }
        return false;
    }

    private static Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        } else {
            throw new IllegalArgumentException(object.getClass().getName() + "is not instanceof Activity or Fragment");
        }
    }


    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0) {
            if (hasNeverAskAgainPermission(getActivity(mObject), permissions)) {
                if (mCallback != null) {
                    mCallback.onNeverAskAgain(stringListToArray(findNeverAskAgainPermissions(getActivity(mObject), permissions)));
                }
            } else {
                if (mCallback != null) {
                    mCallback.onDenied(stringListToArray(findDeniedPermissions(getActivity(mObject), permissions)));
                }
            }
        } else {
            if (mCallback != null) {
                mCallback.onGranted();
            }
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // do nothings，maybe receive system settings activity onCallback
    }

    public static String[] stringListToArray(@Nullable List<String> list) {
        if (list == null) {
            return new String[]{};
        }
        return list.toArray(new String[0]);
    }

    /**
     * 注册所需权限的回调
     */
    public interface Callback {
        // 全部授权
        void onGranted();

        // 拒绝授权
        void onDenied(String... permissions);

        // 勾选 '下次不再询问'
        void onNeverAskAgain(String... permissions);
    }

}
