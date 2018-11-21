package app.com.rtsplibrary.data;

import java.util.concurrent.LinkedBlockingDeque;

import app.com.rtsplibrary.media.H264Data;

public class DataUtil {
    private static int queuesize = 10 * 1024;
    private LinkedBlockingDeque<H264Data> h264Queue = new LinkedBlockingDeque<>(queuesize);
    private volatile static DataUtil mDataUtil;
    private DataUtil(){}
    public static DataUtil getInstance(){
        if(mDataUtil == null){
            synchronized (DataUtil.class){
                if(mDataUtil == null){
                    mDataUtil = new DataUtil();
                }
            }
        }
        return mDataUtil;
    }

    public void putData(byte[] buffer, int type,long ts) {
        if (h264Queue.size() >= queuesize) {
            h264Queue.poll();
        }
        H264Data data = new H264Data(buffer, type, ts);
        h264Queue.add(data);
    }

    public LinkedBlockingDeque<H264Data> getH264Queue() {
        return h264Queue;
    }

    public void putData(H264Data data) {
        if (h264Queue == null){
            h264Queue = new LinkedBlockingDeque<>(queuesize);
        }
        if (h264Queue.size() >= queuesize) {
            h264Queue.poll();
        }
        h264Queue.add(data);
    }
}
