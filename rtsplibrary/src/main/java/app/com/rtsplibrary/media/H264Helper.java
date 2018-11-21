package app.com.rtsplibrary.media;

/**
 * Created by zpf on 2018/3/8.
 */

public class H264Helper {

    /**
     * 获取h264帧类型
     * I 0x05
     * NAL_SLICE 0x01
     * sps 0x07
     * pps 0x08
     *
     * **/
    public static int getFrameType(Byte b){
        if(b == null)
            return 1;
        return b & 0x1F;
    }


}
