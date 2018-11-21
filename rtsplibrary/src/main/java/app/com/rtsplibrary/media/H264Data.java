package app.com.rtsplibrary.media;



public class H264Data {

    public byte[] data;

    public int type;

    public long ts;

    public H264Data(byte[] data, int type, long ts) {
        this.data = data;
        this.type = type;
        this.ts = ts;
    }
}
