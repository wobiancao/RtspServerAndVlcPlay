package app.com.rtsplibrary.media;

import android.media.MediaCodec;

import app.com.rtsplibrary.data.H264DataCollecter;



public abstract class MediaCodecBase {

    protected MediaCodec mEncoder;

    protected boolean isRun = false;

    public abstract void prepare();

    public abstract void release();

    protected H264DataCollecter mH264Collecter;

}
