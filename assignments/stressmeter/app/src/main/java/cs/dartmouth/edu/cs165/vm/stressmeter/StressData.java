package cs.dartmouth.edu.cs165.vm.stressmeter;

import android.util.Log;

/**
 * Created by Vishal Gaurav on 4/13/16.
 */
public class StressData {

    private long time;
    private int gridScore;

    public StressData(long time, int gridScore){
        this.time = time;
        this.gridScore = gridScore;
    }

    /*
    Init stress data
     */
    public StressData(String line){
        Log.e("VVV","Line = " + line);
        String[] csvs = line.split(",");
        if(csvs.length >= 2){
            this.time = Long.parseLong(csvs[0]);
            this.gridScore = Integer.parseInt(csvs[1]);
        }else{
            Log.e("VVV","Error in init StressData");
        }

    }

    /*
    Get variables
     */
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getGridScore() {
        return gridScore;
    }

    public void setGridScore(int gridScore) {
        this.gridScore = gridScore;
    }

    @Override
    public String toString(){
        return time + "," + gridScore + "\n";
    }
}
