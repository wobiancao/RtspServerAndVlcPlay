package app.meibei.com.rtspserverdemo.util;

import android.content.Context;
import android.view.WindowManager;

public class DisplayUtil {
    public static int getDisplayW(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int windowWidth = wm.getDefaultDisplay().getWidth();
        return windowWidth;
    }

    public static int getDisplayH(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int windowHeight = wm.getDefaultDisplay().getHeight();
        return windowHeight;
    }
}
