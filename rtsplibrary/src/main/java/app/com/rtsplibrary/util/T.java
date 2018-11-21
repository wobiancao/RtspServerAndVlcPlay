package app.com.rtsplibrary.util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class T {

    // Toast
    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (context == null) return;
        showToast(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message, View header) {
        if (context == null || header == null) return;
        showToast(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
    }



    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (context == null) return;
        showToast(context.getApplicationContext(), message, Toast.LENGTH_LONG);
    }

    /**
     * Hide the toast, if any.
     */
    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }

    public static void showToast(Context context, CharSequence message, int duration) {
        if (context == null) return;

        Toast.makeText(context, message, duration).show();
    }


    /**
     * 获取状态栏高度——方法1
     */
    public static int getStatusBarHeight(Context context) {
        int defaultHeight = 60;
        if (context == null) return defaultHeight;

        int statusBarHeight = defaultHeight;
//获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
