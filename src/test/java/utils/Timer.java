package utils;

/**
 * Created by shachar on 15/02/2016.
 */
public class Timer  {

    private long startTime = 0;
    private long endTime   = 0;
    public Timer(){
        this.start();
    }

    public void start(){
        this.startTime = System.currentTimeMillis();
    }

    public void end() {
        this.endTime   = System.currentTimeMillis();
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public long getTotalTime() {
        return this.endTime - this.startTime;
    }

    public void print() throws Exception  {
        System.out.println("total time: "+ this.getTotalTime() );
    }
}

