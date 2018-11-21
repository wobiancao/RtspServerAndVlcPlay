package app.com.rtsplibrary.rtsp;



import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import app.com.rtsplibrary.data.DataUtil;
import app.com.rtsplibrary.media.H264Data;

/**
 * Created by user111 on 2018/3/14.
 */

public class ScreenInputStream extends InputStream {

    private long ts = 0;
    private ByteBuffer mBuffer = null;
    private H264Data data = null;

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int min = 0;

        if(mBuffer == null){
            data = DataUtil.getInstance().getH264Queue().poll();
            if(data == null) return 0;
            ts = data.ts;
            mBuffer = ByteBuffer.wrap(data.data);
            mBuffer.position(0);
        }
        min = length < data.data.length - mBuffer.position() ? length : data.data.length - mBuffer.position();
        mBuffer.get(buffer, offset, min);
        if (mBuffer.position()>=data.data.length) {
            mBuffer = null;
        }
        return min;
    }


    @Override
    public int read() throws IOException {
        return 0;
    }

    public int available() {
        if (mBuffer != null)
            return data.data.length - mBuffer.position();
        else
            return 0;
    }


    public long getLastts(){
        return ts;
    }
}
