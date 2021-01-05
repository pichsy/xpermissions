package com.pichs.permissions.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pichs.permissions.ui.AbstractPermissionInstructionDialog;
import com.pichs.permissions.ui.PermissionDefaultDialog;

public class RequestUtils {

    public static void requestReadPhonePermission(final Activity activity, final int requestCode, final PermissionHelper.Callback callback) {
        if (com.pichs.permissions.PermissionUtils.checkPermission(activity, Manifest.permission.READ_PHONE_STATE)) {
            callback.onGranted();
            return;
        }
        new PermissionDefaultDialog(activity)
                .setTitleText(PermissionResource.getInstance(activity).getString("lcm_permission_title_default_text"))
                .setCancelButtonText(PermissionResource.getInstance(activity).getString("lcm_permission_button_default_cancel_text"))
                .setOkButtonText(PermissionResource.getInstance(activity).getString("lcm_permission_button_default_ok_text"))
                .setMessageText(PermissionResource.getInstance(activity).getString("lcm_permission_device_read_phone_state_introduce", Utils.getAppName(activity), Utils.getAppName(activity)))
                .setCheckBoxVisible(false)
                .setOnItemClickListener(new AbstractPermissionInstructionDialog.OnItemClickListener() {
                    @Override
                    public void onCancelClicked(DialogInterface dialog, boolean isNeverPrompt) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onOkClicked(DialogInterface dialog, boolean isNeverPrompt) {
                        dialog.dismiss();
                        PermissionHelper.requestPermissions(activity, requestCode, callback, Manifest.permission.READ_PHONE_STATE);
                    }
                }).show();
    }


    public static void requestReadPhonePermission(final Fragment fragment, final int requestCode, final PermissionHelper.Callback callback) {
        if (com.pichs.permissions.PermissionUtils.checkPermission(fragment.getActivity(), Manifest.permission.READ_PHONE_STATE)) {
            callback.onGranted();
            return;
        }
        new PermissionDefaultDialog(fragment.getActivity())
                .setTitleText(PermissionResource.getInstance(fragment.getActivity()).getString("lcm_permission_title_default_text", Utils.getAppName(fragment.getActivity())))
                .setCancelButtonText(PermissionResource.getInstance(fragment.getActivity()).getString("lcm_permission_button_default_cancel_text"))
                .setOkButtonText(PermissionResource.getInstance(fragment.getActivity()).getString("lcm_permission_button_default_ok_text"))
                .setMessageText(PermissionResource.getInstance(fragment.getActivity()).getString("lcm_permission_device_read_phone_state_introduce", Utils.getAppName(fragment.getActivity()), Utils.getAppName(fragment.getActivity())))
                .setCheckBoxVisible(false)
                .setOnItemClickListener(new AbstractPermissionInstructionDialog.OnItemClickListener() {
                    @Override
                    public void onCancelClicked(DialogInterface dialog, boolean isNeverPrompt) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onOkClicked(DialogInterface dialog, boolean isNeverPrompt) {
                        dialog.dismiss();
                        PermissionHelper.requestPermissions(fragment, requestCode, callback, Manifest.permission.READ_PHONE_STATE);
                    }
                }).show();
    }


    public static void requestStoragePermission(final Activity activity, final int requestCode, final PermissionHelper.Callback callback) {
        if (!PermissionHelper.hasDeniedPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            callback.onGranted();
            return;
        }
        new PermissionDefaultDialog(activity)
                .setTitleText(PermissionResource.getInstance(activity).getString("lcm_permission_title_default_text"))
                .setCancelButtonText(PermissionResource.getInstance(activity).getString("lcm_permission_button_default_cancel_text"))
                .setOkButtonText(PermissionResource.getInstance(activity).getString("lcm_permission_button_default_ok_text"))
                .setMessageText(PermissionResource.getInstance(activity).getString("lcm_permission_picture_read_external_storage_introduce", Utils.getAppName(activity), Utils.getAppName(activity)))
                .setCheckBoxVisible(false)
                .setOnItemClickListener(new AbstractPermissionInstructionDialog.OnItemClickListener() {
                    @Override
                    public void onCancelClicked(DialogInterface dialog, boolean isNeverPrompt) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onOkClicked(DialogInterface dialog, boolean isNeverPrompt) {
                        dialog.dismiss();
                        PermissionHelper.requestPermissions(activity, requestCode, callback, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
    }

    public static void requestStoragePermission(final Fragment fragment, final int requestCode, final PermissionHelper.Callback callback) {
        if (!PermissionHelper.hasDeniedPermission(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            callback.onGranted();
            return;
        }
        new PermissionDefaultDialog(fragment.getActivity())
                .setTitleText(PermissionResource.getInstance(fragment.getActivity()).getString("lcm_permission_title_default_text", Utils.getAppName(fragment.getActivity())))
                .setCancelButtonText(PermissionResource.getInstance(fragment.getActivity()).getString("lcm_permission_button_default_cancel_text"))
                .setOkButtonText(PermissionResource.getInstance(fragment.getActivity()).getString("lcm_permission_button_default_ok_text"))
                .setMessageText(PermissionResource.getInstance(fragment.getActivity()).getString("lcm_permission_picture_read_external_storage_introduce", Utils.getAppName(fragment.getActivity()), Utils.getAppName(fragment.getActivity())))
                .setCheckBoxVisible(false)
                .setOnItemClickListener(new AbstractPermissionInstructionDialog.OnItemClickListener() {
                    @Override
                    public void onCancelClicked(DialogInterface dialog, boolean isNeverPrompt) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onOkClicked(DialogInterface dialog, boolean isNeverPrompt) {
                        dialog.dismiss();
                        PermissionHelper.requestPermissions(fragment, requestCode, callback, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
    }


    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static void toAppSettingActivity(Context context) {
        com.pichs.permissions.PermissionUtils.toAppSettingActivity(context);
    }
}
