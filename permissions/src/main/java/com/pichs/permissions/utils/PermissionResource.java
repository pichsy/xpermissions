package com.pichs.permissions.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;

public class PermissionResource {
    private final static String ID_CLASS_NAME = "id";
    private final static String LAYOUT_CLASS_NAME = "layout";
    private final static String STYLE_CLASS_NAME = "style";
    private final static String STRING_CLASS_NAME = "string";
    private final static String DIMEN_CLASS_NAME = "dimen";

    private Configuration mConfiguration = new Configuration();

    private static PermissionResource mInstance = null;
    private Context mContext = null;


    private Resources mResources = null;

    private LayoutInflater mInflater = null;

    private int ErrorCode = 0;

    private PermissionResource(Context context) {
        mContext = context;
        mResources = mContext.getResources();
        mInflater = LayoutInflater.from(mContext);
    }

    public static PermissionResource getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PermissionResource(context);
        }
        return mInstance;
    }

    /*
     * 取 String 资源
     */
    public String getString(String strname) {
        if (mResources != null) {
            int id = mResources.getIdentifier(strname, STRING_CLASS_NAME, mContext.getPackageName());
            if (id == 0) {
                return null;
            }
            return mResources.getString(id);
        } else {
            return null;
        }

    }

    public String getString(String strname, Object... formatArgs) {
        final String raw = getString(strname);
        return String.format(mConfiguration.locale, raw, formatArgs);
    }

    /*
     * 取Layout资源
     */
    public View getLayoutForView(String layoutname) {
        if (mResources != null) {
            int id = mResources.getIdentifier(layoutname, LAYOUT_CLASS_NAME, mContext.getPackageName());
            if ((mInflater != null) && (id != 0)) {
                return mInflater.inflate(id, null);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /*
     * 取id资源
     */
    public int getId(String idname) {
        if (mResources != null) {
            int id = mResources.getIdentifier(idname, ID_CLASS_NAME, mContext.getPackageName());
            return id;
        } else {
            return ErrorCode;
        }
    }




    public int getStyle(String stylename) {
        if (mResources != null) {
            int id = mResources.getIdentifier(stylename, STYLE_CLASS_NAME, mContext.getPackageName());
            return id;
        } else {
            return ErrorCode;
        }
    }


    public int getDimenId(String dimenName) {
        if (mResources != null) {
            int id = mResources.getIdentifier(dimenName, DIMEN_CLASS_NAME, mContext.getPackageName());
            return id;
        } else {
            return ErrorCode;
        }
    }

    public int getDimensionPixelSize(String dimenName) {
        if (mResources != null) {
            int id = getDimenId(dimenName);
            return mResources.getDimensionPixelSize(id);
        } else {
            return ErrorCode;
        }
    }

}
