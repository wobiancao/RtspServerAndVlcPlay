package app.com.rtsplibrary.util;

import android.content.Context;
import android.view.WindowManager;

import app.com.rtsplibrary.constants.Constant;


public class DisplayUtils {

    public static int getDisplayW(Context context){
        return Constant.isPad() ? 1280: 720;
    }


    public static int getDisplayH(Context context){

        return Constant.isPad() ? 720: 1280;

    }

    public static int align(int d, int a) {
        return (((d) + (a - 1)) & ~(a - 1));
    }
}
