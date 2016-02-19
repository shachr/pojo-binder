package utils;

public class PerfTimer {

    Timer _timer ;
    int count = 0;
    int totalMS = 0;
    public PerfTimer(){

    }

    public void start(){
        _timer = new Timer();
        _timer.start();
    }

    public void end(){
        _timer.end();
        count += 1;
        totalMS += _timer.getTotalTime();
    }

    public int getAvgTime(){
        return Math.round(totalMS * 100/count);
    }

    public void print()  {
        System.out.println("avg time: "+ this.getAvgTime() );
    }
}
