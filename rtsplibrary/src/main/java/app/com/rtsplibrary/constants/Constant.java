package app.com.rtsplibrary.constants;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Locale;

public class Constant {
    public static final String MIME_TYPE = "video/avc";

    public static final int VIDEO_WIDTH = 1280;

    public static final int VIDEO_HEIGHT = 720;

    public static  int VIDEO_DPI = 1;

    public static  int VIDEO_BITRATE = 800000;// 800Kbps

    public static  int VIDEO_FRAMERATE = 30;

    public static  int VIDEO_IFRAME_INTER = 1;

    private  static int DEFAULT_RTSP_PORT = 1234;
    public static final int DEFAULT_CHANNEL_COUNT = 2;

    public static boolean ISPAD = true;

    private static final float BPP = 0.25f;

    public static boolean isPad(){
        return ISPAD;
    }

    public static int getPort(){
        return DEFAULT_RTSP_PORT;
    }

    public static void changePort(int port){
        DEFAULT_RTSP_PORT = port;
    }


    public static String displayIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ipaddress = "";
        if (info!=null && info.getNetworkId()>-1) {
            int i = info.getIpAddress();
            String ip = String.format(Locale.ENGLISH,"%d.%d.%d.%d", i & 0xff, i >> 8 & 0xff,i >> 16 & 0xff,i >> 24 & 0xff);
            ipaddress += "rtsp://";
            ipaddress += ip;
            ipaddress += ":";
            ipaddress += DEFAULT_RTSP_PORT;
        }
        return ipaddress;
    }
}
