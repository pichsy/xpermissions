package com.pichs.permissions.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pichs.permissions.utils.PermissionResource;
import com.pichs.permissions.utils.Utils;

/**
 * Created by bo.wu on 17/9/4.
 */
public class PermissionDefaultDialog extends AbstractPermissionInstructionDialog implements View.OnClickListener {

    private static final String TAG = "PermissionDefaultDialog";
    private int mDialogWidth;
    private int mDialogHeight;
    public View contentView;
    private Activity mActivity;
    private PermissionResource R;
    private TextView mTitleTv;
    private TextView mMsgTv;
    private TextView cancelBtn;
    private TextView okBtn;
    private String titleTxt, msgTxt, okBtnTxt, cancelBtnTxt;
    private CheckBox checkBox;
    private View checkBoxLayout;
    private boolean isCheckBoxVisible = true;
    private boolean isFullScreen = false;

    public PermissionDefaultDialog(Activity activity) {
        super(activity, PermissionResource.getInstance(activity).getStyle("PermissionsDialogTheme"));
        R = PermissionResource.getInstance(activity);
        mActivity = activity;
        contentView = R.getLayoutForView("permissions_default_dialog");
        this.mDialogWidth = (int) (getScreenWidth(mActivity) * 0.8);
        this.mDialogHeight = PermissionResource.getInstance(mActivity).getDimensionPixelSize("lcm_permission_dialog_height");
        initView();
        initListener();
        initData();
    }

    public PermissionDefaultDialog(Activity activity, boolean isFullScreen) {
        this(activity);
        this.isFullScreen = isFullScreen;
    }

    private void initView() {
        mMsgTv = contentView.findViewById(R.getId("permission_dialog_message"));
        mTitleTv = contentView.findViewById(R.getId("permission_dialog_title"));
        cancelBtn = contentView.findViewById(R.getId("permission_dialog_btn_cancel"));
        okBtn = contentView.findViewById(R.getId("permission_dialog_btn_ok"));
        checkBox = contentView.findViewById(R.getId("lcm_permission_cbox"));
        checkBoxLayout = contentView.findViewById(R.getId("lcm_permission_cbox_layout_id"));
    }

    public PermissionDefaultDialog setTitleText(String title) {
        titleTxt = title;
        if (mTitleTv != null && titleTxt != null) {
            this.mTitleTv.setText(titleTxt);
        }
        return this;
    }

    public PermissionDefaultDialog setMessageText(String message) {
        msgTxt = message;
        if (mMsgTv != null && msgTxt != null) {
            this.mMsgTv.setText(msgTxt);
        }
        return this;
    }

    public PermissionDefaultDialog setCancelButtonText(String btnText) {
        cancelBtnTxt = btnText;
        if (cancelBtn != null && cancelBtnTxt != null) {
            this.cancelBtn.setText(cancelBtnTxt);
        }
        return this;
    }

    public PermissionDefaultDialog setOkButtonText(String btnText) {
        okBtnTxt = btnText;
        if (okBtn != null && okBtnTxt != null) {
            this.okBtn.setText(okBtnTxt);
        }
        return this;
    }

    public PermissionDefaultDialog setCheckBoxVisible(boolean visible) {
        isCheckBoxVisible = visible;
        if (checkBoxLayout != null) {
            if (isCheckBoxVisible) {
                checkBoxLayout.setVisibility(View.VISIBLE);
            } else {
                checkBoxLayout.setVisibility(View.GONE);
            }
        }
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(mDialogWidth, mDialogHeight);
        }
        layoutParams.width = mDialogWidth;
        layoutParams.height = mDialogHeight;
        contentView.setLayoutParams(layoutParams);
        setContentView(contentView);
        if (isFullScreen) {
            final Window window = getWindow();
            if (window != null) {
                setHideVirtualKey(window);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            setHideVirtualKey(window);
                        }
                    });
                }
            }
        }
        View parent = (View) contentView.getParent();
        FrameLayout.LayoutParams parentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parentParams.gravity = Gravity.CENTER;
        View upperParent = null;
        if (parent != null) {
            parent.setLayoutParams(parentParams);
            upperParent = (View) parent.getParent();
        }
        View upperParent2;
        if (upperParent != null) {
            upperParent2 = (View) upperParent.getParent();
            if (upperParent2 != null) {
                upperParent2.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private static void setHideVirtualKey(final Window window) {
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT < 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                decorView.setSystemUiVisibility(View.GONE);
            }
        } else {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 重新刷新一次数据
     */
    public void refreshData() {
        setTitleText(titleTxt);
        setMessageText(msgTxt);
        setOkButtonText(okBtnTxt);
        setCancelButtonText(cancelBtnTxt);
        setCheckBoxVisible(isCheckBoxVisible);
    }

    private void initData() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    private void initListener() {
        cancelBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
    }

    /**
     * 提供对外的接口，外部也可以自己new创建
     *
     * @param activity      Activity
     * @param title         title
     * @param msg           msg
     * @param okBtnText     okBtnText
     * @param cancelBtnText cancelBtnText
     * @return PermissionDefaultDialog
     */
    public static PermissionDefaultDialog create(final Activity activity, final String title, final String msg, final String okBtnText, final String cancelBtnText) {
        return create(activity, title, msg, okBtnText, cancelBtnText, false);
    }

    /**
     * 提供对外的接口，外部也可以自己new创建
     *
     * @param activity          Activity
     * @param title             title
     * @param msg               msg
     * @param okBtnText         okBtnText
     * @param cancelBtnText     cancelBtnText
     * @param isCheckBoxVisible isCheckBoxVisible
     * @return PermissionDefaultDialog
     */
    public static PermissionDefaultDialog create(final Activity activity, final String title, final String msg, final String okBtnText, final String cancelBtnText, boolean isCheckBoxVisible) {
        return create(activity, title, msg, okBtnText, cancelBtnText, false, false);
    }

    /**
     * 提供对外的接口，外部也可以自己new创建
     *
     * @param activity          Activity
     * @param title             title
     * @param msg               msg
     * @param okBtnText         okBtnText
     * @param cancelBtnText     cancelBtnText
     * @param isCheckBoxVisible isCheckBoxVisible
     * @return PermissionDefaultDialog
     */
    public static PermissionDefaultDialog create(final Activity activity, final String title, final String msg, final String okBtnText, final String cancelBtnText, boolean isCheckBoxVisible, boolean isFullScreen) {
        return new PermissionDefaultDialog(activity, isFullScreen)
                .setTitleText(title)
                .setMessageText(msg)
                .setOkButtonText(okBtnText)
                .setCancelButtonText(cancelBtnText)
                .setCheckBoxVisible(isCheckBoxVisible);
    }


    public static PermissionDefaultDialog createFullScreenDefault(final Activity activity, String... permissions) {
        int externalNum = 0;
        boolean hasExternalPermission = false;
        boolean hasReadPhonePermission = false;
        // 顿号
        String dawn = PermissionResource.getInstance(activity).getString("lcm_permission_dawn");
        // 句号
        String fullStop = PermissionResource.getInstance(activity).getString("lcm_permission_full_stop");

        for (String item : permissions) {
            if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(item) || Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(item)) {
                if (externalNum >= 1) {
                    continue;
                }
                externalNum++;
                hasExternalPermission = true;
            } else if (Manifest.permission.READ_PHONE_STATE.equals(item)) {
                hasReadPhonePermission = true;
            }
        }
        String permission1 = "";
        String permission2 = "";
        String body1 = "";
        String body2 = "";
        // 俩都有
        if (hasExternalPermission && hasReadPhonePermission) {
            permission1 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_external_storage") + dawn;
            body1 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_external_storage_introduce");
            permission2 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_phone_state");
            body2 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_phone_state_introduce");
        } else if (hasExternalPermission) { // 只有读写权限
            permission1 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_external_storage");
            body1 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_external_storage_introduce");
        } else if (hasReadPhonePermission) { // 只有设备信息权限
            permission2 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_phone_state");
            body2 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_phone_state_introduce");
        }

        String message = PermissionResource.getInstance(activity)
                .getString("lcm_permission_list_message_body_text",
                        Utils.getAppName(activity),
                        permission1,
                        permission2,
                        fullStop,
                        body1,
                        body2
                );
        return create(
                activity,
                PermissionResource.getInstance(activity).getString("lcm_permission_title_default_text"),
                message,
                PermissionResource.getInstance(activity).getString("lcm_permission_button_default_ok_text"),
                PermissionResource.getInstance(activity).getString("lcm_permission_button_default_cancel_text"),
                false,
                true
        );
    }

    /**
     * 创建默认的文字样式
     *
     * @param activity    Activity
     * @param permissions permissions
     * @return PermissionDefaultDialog
     */
    public static PermissionDefaultDialog createDefault(final Activity activity, String... permissions) {
        int externalNum = 0;
        boolean hasExternalPermission = false;
        boolean hasReadPhonePermission = false;
        // 顿号
        String dawn = PermissionResource.getInstance(activity).getString("lcm_permission_dawn");
        // 句号
        String fullStop = PermissionResource.getInstance(activity).getString("lcm_permission_full_stop");

        for (String item : permissions) {
            if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(item) || Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(item)) {
                if (externalNum >= 1) {
                    continue;
                }
                externalNum++;
                hasExternalPermission = true;
            } else if (Manifest.permission.READ_PHONE_STATE.equals(item)) {
                hasReadPhonePermission = true;
            }
        }
        String permission1 = "";
        String permission2 = "";
        String body1 = "";
        String body2 = "";
        // 俩都有
        if (hasExternalPermission && hasReadPhonePermission) {
            permission1 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_external_storage") + dawn;
            body1 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_external_storage_introduce");
            permission2 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_phone_state");
            body2 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_phone_state_introduce");
        } else if (hasExternalPermission) { // 只有读写权限
            permission1 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_external_storage");
            body1 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_external_storage_introduce");
        } else if (hasReadPhonePermission) { // 只有设备信息权限
            permission2 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_phone_state");
            body2 = PermissionResource.getInstance(activity).getString("lcm_permission_list_read_phone_state_introduce");
        }

        String message = PermissionResource.getInstance(activity)
                .getString("lcm_permission_list_message_body_text",
                        Utils.getAppName(activity),
                        permission1,
                        permission2,
                        fullStop,
                        body1,
                        body2
                );


        return create(
                activity,
                PermissionResource.getInstance(activity).getString("lcm_permission_title_default_text"),
                message,
                PermissionResource.getInstance(activity).getString("lcm_permission_button_default_ok_text"),
                PermissionResource.getInstance(activity).getString("lcm_permission_button_default_cancel_text"),
                false
        );
    }

    /* 获取屏幕高*/
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == okBtn.getId()) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onOkClicked(PermissionDefaultDialog.this, checkBox.isChecked());
            }
        } else if (v.getId() == cancelBtn.getId()) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onCancelClicked(PermissionDefaultDialog.this, checkBox.isChecked());
            }
        }
    }
}
