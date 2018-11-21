package app.com.rtsplibrary.util;

import android.content.Context;
import android.view.WindowManager;

import app.com.rtsplibrary.constants.Constant;


public class DisplayUtils {

    public static int getDisplayW(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int windowWidth = wm.getDefaultDisplay().getWidth();
        return Constant.isPad() ? align(windowWidth * 2 / 5, 16) : windowWidth;
    }

    public static int getDisplayH(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int windowHeight = wm.getDefaultDisplay().getHeight();
        return Constant.isPad() ? align(windowHeight * 2 / 5, 16) : windowHeight;
    }

    public static int align(int d, int a) {
        return (((d) + (a - 1)) & ~(a - 1));
    }
}
