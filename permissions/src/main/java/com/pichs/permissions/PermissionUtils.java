package com.pichs.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.pichs.permissions.ui.AbstractPermissionInstructionDialog;
import com.pichs.permissions.ui.PermissionDefaultDialog;
import com.pichs.permissions.utils.PermissionHelper;
import com.pichs.permissions.utils.PermissionSpHelper;

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();
    private static AbstractPermissionInstructionDialog permissionInfoDialog;
    // 一次申请只开头弹一次
    private static boolean hasShowedDialog = false;

    /**
     * 请求带默认弹窗的接口
     *
     * @param activity             Activity
     * @param requestCode          requestCode
     * @param requestPermissions   requestPermissions
     * @param onPermissionCallback onPermissionCallback`
     */
    public static void requestPermissionsWithDefaultDialog(@NonNull final Activity activity, final int requestCode, final String[] requestPermissions, final OnPermissionCallback onPermissionCallback) {
        PermissionDefaultDialog defaultDialog = PermissionDefaultDialog.createDefault(activity, requestPermissions);
        PermissionUtils.setPermissionInfoDialog(defaultDialog);
        PermissionUtils.requestPermissions(activity, requestCode, requestPermissions, onPermissionCallback);

    }

    /**
     * 请求权限标准接口
     *
     * @param activity             Activity
     * @param requestCode          requestCode
     * @param requestPermissions   requestPermissions
     * @param onPermissionCallback onPermissionCallback
     */
    public static void requestPermissions(@NonNull final Activity activity, final int requestCode, final String[] requestPermissions, final OnPermissionCallback onPermissionCallback) {
        if (requestPermissions == null || requestPermissions.length <= 0) {
            if (onPermissionCallback != null) {
                onPermissionCallback.onCallback();
            }
            return;
        }
        // 如果勾选了不再提示，则进入游戏时不会再进行授权了。
        if (PermissionSpHelper.getInstance(activity).isNeverTips()) {
            if (onPermissionCallback != null) {
                onPermissionCallback.onCallback();
            }
            return;
        }
        final PermissionHelper.Callback callback = new PermissionHelper.Callback() {
            @Override
            public void onGranted() {
                if (onPermissionCallback != null) {
                    onPermissionCallback.onCallback();
                }
            }

            @Override
            public void onDenied(String... permissions) {
                if (PermissionSpHelper.getInstance(activity).isFirstRefuseShowDialog()) {
                    showFirstRefusePermissionDialog(activity, requestCode, requestPermissions, false, onPermissionCallback);
                } else {
                    if (onPermissionCallback != null) {
                        onPermissionCallback.onCallback(permissions);
                    }
                }
            }


            @Override
            public void onNeverAskAgain(String... permissions) {
                if (PermissionSpHelper.getInstance(activity).isFirstRefuseShowDialog()) {
                    showFirstRefusePermissionDialog(activity, requestCode, requestPermissions, true, onPermissionCallback);
                } else {
                    if (onPermissionCallback != null) {
                        onPermissionCallback.onCallback(permissions);
                    }
                }
            }
        };

        boolean isNeedRequestPermission;
        if (PermissionSpHelper.getInstance(activity).isFirstRequestPermission()) {
            isNeedRequestPermission = hasDeniedPermission(activity, requestPermissions);
        } else {
            isNeedRequestPermission = hasDeniedWithoutNeverAskAgainPermission(activity, requestPermissions);
        }
        if (!isNeedRequestPermission) {
            // 没有可申请的权限，直接返回
            if (onPermissionCallback != null) {
                onPermissionCallback.onCallback();
            }
            return;
        }
        if (permissionInfoDialog != null && !hasShowedDialog) {
            showPermissionDialog(activity, requestCode, requestPermissions, callback);
        } else {
            PermissionHelper.requestPermissions(activity, requestCode, callback, requestPermissions);
        }
    }

    // 显示弹窗
    private static void showPermissionDialog(final Activity activity, final int requestCode, final String[] requestPermissions, final PermissionHelper.Callback callback) {
        permissionInfoDialog.setCancelable(false);
        permissionInfoDialog.setOnItemClickListener(new AbstractPermissionInstructionDialog.OnItemClickListener() {
            @Override
            public void onCancelClicked(DialogInterface dialog, boolean isNeverPrompt) {
                if (isNeverPrompt) {
                    PermissionSpHelper.getInstance(activity).setNeverTips(true);
                }
                dialog.dismiss();
                // 这里回调 取消默认相当于全部授权后的逻辑，游戏继续进行，所以用此回调
                callback.onGranted();
            }

            @Override
            public void onOkClicked(DialogInterface dialog, boolean isNeverPrompt) {
                dialog.dismiss();
                PermissionSpHelper.getInstance(activity).setFirstRequestPermission(false);
                PermissionHelper.requestPermissions(activity, requestCode, callback, requestPermissions);
            }
        });
        permissionInfoDialog.show();
        hasShowedDialog = true;
    }

    /**
     * 第一次拒绝授权后的提示
     *
     * @param activity                     Activity
     * @param requestPermissions           String[]
     * @param isNeverAskAgain              boolean
     * @param onRegisterPermissionListener OnPermissionCallback
     */
    private static void showFirstRefusePermissionDialog(final Activity activity, final int requestCode, final String[] requestPermissions, final boolean isNeverAskAgain, final OnPermissionCallback onRegisterPermissionListener) {

        final PermissionHelper.Callback callback = new PermissionHelper.Callback() {
            @Override
            public void onGranted() {
                PermissionSpHelper.getInstance(activity).setFirstRefuseShowDialog(false);
                if (onRegisterPermissionListener != null) {
                    onRegisterPermissionListener.onCallback();
                }
            }

            @Override
            public void onDenied(String... permissions) {
                PermissionSpHelper.getInstance(activity).setFirstRefuseShowDialog(false);
                if (onRegisterPermissionListener != null) {
                    onRegisterPermissionListener.onCallback(permissions);
                }
            }

            @Override
            public void onNeverAskAgain(String... permissions) {
                PermissionSpHelper.getInstance(activity).setFirstRefuseShowDialog(false);
                if (onRegisterPermissionListener != null) {
                    onRegisterPermissionListener.onCallback(permissions);
                }
            }
        };

        permissionInfoDialog.setCancelable(false);
        permissionInfoDialog.setOnItemClickListener(new AbstractPermissionInstructionDialog.OnItemClickListener() {
            @Override
            public void onCancelClicked(DialogInterface dialog, boolean isNeverPrompt) {
                dialog.dismiss();
                PermissionSpHelper.getInstance(activity).setFirstRefuseShowDialog(false);
                if (isNeverPrompt) {
                    PermissionSpHelper.getInstance(activity).setNeverTips(true);
                }
                if (onRegisterPermissionListener != null) {
                    onRegisterPermissionListener.onCallback();
                }
            }

            @Override
            public void onOkClicked(DialogInterface dialog, boolean isNeverPrompt) {
                dialog.dismiss();
                if (isNeverAskAgain) {
                    toAppSettingActivity(activity);
                } else {
                    PermissionHelper.requestPermissions(activity, requestCode, callback, requestPermissions);
                }
            }
        });
        permissionInfoDialog.show();
    }

    // 是否有 普通拒绝的权限 （非永久拒绝的权限）
    private static boolean hasDeniedWithoutNeverAskAgainPermission(Activity activity, String... permissions) {
        return PermissionHelper.hasDeniedWithoutNeverAskAgainPermission(activity, permissions);
    }

    // 是否有 拒绝的权限
    private static boolean hasDeniedPermission(Activity activity, String... permissions) {
        return PermissionHelper.hasDeniedPermission(activity, permissions);
    }

    /**
     * 权限处理回调
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 界面返回结果回调
     *
     * @param object      Activity or Fragment
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data「Intent」
     */
    public static void onActivityResult(Object object, int requestCode, int resultCode, Intent data) {
        PermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置权限提示信息dialog
     *
     * @param permissionInstructionDialog {@link AbstractPermissionInstructionDialog}
     */
    public static void setPermissionInfoDialog(AbstractPermissionInstructionDialog permissionInstructionDialog) {
        PermissionUtils.permissionInfoDialog = permissionInstructionDialog;
    }

    /**
     * 可以手动加开关
     *
     * @param enable enable, enable
     */
    public static void setPermissionInfoDialogEnable(boolean enable) {
        PermissionUtils.hasShowedDialog = !enable;
    }

    /**
     * 首次，是否显示两次弹窗
     *
     * @param context     context
     * @param isShowTwice 首次是否弹起两次
     */
    public static void setPermissionInfoDialogShowTwice(Context context, boolean isShowTwice) {
        PermissionSpHelper.getInstance(context).setFirstRefuseShowDialog(isShowTwice);
    }

    /**
     * 检查是否拥有某项权限
     *
     * @param context    Context
     * @param permission permission
     * @return boolean true 有权限，false 无权限
     */
    public static boolean checkPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * 打开app设置界面
     *
     * @param context Context
     */
    public static void toAppSettingActivity(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (9 <= Build.VERSION.SDK_INT) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    /**
     * 注册所需权限的回调
     */
    public interface OnPermissionCallback {
        /**
         * @param deniedPermissions 没有被允许的非必须权限
         */
        void onCallback(@Nullable String... deniedPermissions);
    }

}
