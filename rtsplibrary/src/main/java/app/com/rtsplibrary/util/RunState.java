package app.com.rtsplibrary.util;

/**
 * Created by ljd-pc on 2016/6/22.
 * 单例模式，用于记录程序运行状态
 */
public class RunState {



    private boolean isRun = false;



    private Integer count = 0;

    private volatile static RunState mRunState;
    private RunState(){};
    public static RunState getInstance(){
        if(mRunState == null){
            synchronized (RunState.class){
                if(mRunState == null){
                    mRunState = new RunState();
                }
            }
        }
        return mRunState;
    }
    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public Integer getCount() {
        return count;
    }
    public void addCount(){
        count++;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
