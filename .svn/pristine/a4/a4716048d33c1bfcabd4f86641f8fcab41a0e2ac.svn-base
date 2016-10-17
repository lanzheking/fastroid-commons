package com.honestwalker.android.kc_commons.ui.utils;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

/**
 * Created by honestwalker on 15-9-24.
 */
public class TranslucentStatus {

    private static boolean isEnable = false;

    public static boolean isEnable() {
        return isEnable;
    }

    public static void setEnable(Activity activity) {

        if(!support()) return;

        // 标题栏背景色
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        isEnable = true;

    }

    public static boolean support() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            return false;
        }
    }

}
