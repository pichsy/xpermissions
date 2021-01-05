package com.pichs.permissions.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

/**
 * 权限【使用前说明】弹窗
 * 应Google需求
 */
public abstract class AbstractPermissionInstructionDialog extends AlertDialog {

    protected OnItemClickListener mOnItemClickListener;

    public AbstractPermissionInstructionDialog(@NonNull Context context) {
        super(context);
    }

    public AbstractPermissionInstructionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public <T extends AbstractPermissionInstructionDialog> T setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return (T) this;
    }

    public interface OnItemClickListener {
        /**
         * 点击了Cancel按钮的回调
         *
         * @param dialog        DialogInterface Dialog对象
         * @param isNeverPrompt 禁用后是否再次提示（勾选了禁用后不再提示 为 true，反之为false）
         */
        void onCancelClicked(DialogInterface dialog, boolean isNeverPrompt);

        /**
         * 点击了Ok按钮的回调
         *
         * @param dialog        DialogInterface Dialog对象
         * @param isNeverPrompt 禁用后是否再次提示 （勾选了禁用后不再提示 为 true，反之为false）
         */
        void onOkClicked(DialogInterface dialog, boolean isNeverPrompt);
    }
}
