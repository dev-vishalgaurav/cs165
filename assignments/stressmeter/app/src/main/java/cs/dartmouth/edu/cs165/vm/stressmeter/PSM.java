package cs.dartmouth.edu.cs165.vm.stressmeter;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruiwang on 8/23/15.
 */
public class PSM {

    public static String getLoggerFIlePath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/stress_data.csv";
    }

    public static boolean saveRecordSuccess(int imagePosition){
        printSavedDataToLogs();
        boolean result = false;
        String file = PSM.getLoggerFIlePath();
        Log.e("VVV", "will write to file - " + file);
        StressData data = new StressData(System.currentTimeMillis(),imagePosition);
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(file),true))){
            writer.append(data.toString());
            Log.e("VVV", "writing :- " + data.toString());
            writer.close();
            result = true;
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e("VVV", "writing :- " + data.toString());
        }
        return result;
    }
    private static void printSavedDataToLogs(){
        List<StressData> data = getStressData();
        for (StressData item : data){
            Log.e("VVV","Data = " + item  );
        }
    }
    public static List<StressData> getStressData(){
        List<StressData> list = new ArrayList<>();
        String file = PSM.getLoggerFIlePath();
        Log.e("VVV", "will read from file - " + file);
        String line = null;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null){
                StressData data = new StressData(line);
                list.add(data);
            }
        }catch (Exception ex){
            Log.e("VVV","Error in reading stress data file " + ex.getMessage());
            ex.printStackTrace();
        }
        return list;
    }

    public static int getScore(int position){
        int score = 0 ;
        switch (position){
            case 0 : score = 6; break;
            case 1 : score = 8; break;
            case 2 : score = 14; break;
            case 3 : score = 16; break;
            case 4 : score = 5; break;
            case 5 : score = 7; break;
            case 6 : score = 13; break;
            case 7 : score = 15; break;
            case 8 : score = 2; break;
            case 9 : score = 4; break;
            case 10 : score = 10; break;
            case 11 : score = 12; break;
            case 12 : score = 1; break;
            case 13 : score = 3; break;
            case 14 : score = 9; break;
            case 15 : score = 11; break;
        }
        return score;
    }
    public static int[] getGridById(int id) {
        switch (id) {
            case 1:
                return getGrid1();
            case 2:
                return getGrid2();
            case 3:
                return getGrid3();
        }

        return null;
    }

    public static int[] getGrid1() {
        int[] grid = new int[16];
        grid[0] = R.drawable.psm_talking_on_phone2;
        grid[1] = R.drawable.psm_stressed_person;
        grid[2] = R.drawable.psm_stressed_person12;
        grid[3] = R.drawable.psm_lonely;
        grid[4] = R.drawable.psm_gambling4;
        grid[5] = R.drawable.psm_clutter3;
        grid[6] = R.drawable.psm_reading_in_bed2;
        grid[7] = R.drawable.psm_stressed_person4;
        grid[8] = R.drawable.psm_lake3;
        grid[9] = R.drawable.psm_cat;
        grid[10] = R.drawable.psm_puppy3;
        grid[11] = R.drawable.psm_neutral_person2;
        grid[12] = R.drawable.psm_beach3;
        grid[13] = R.drawable.psm_peaceful_person;
        grid[14] = R.drawable.psm_alarm_clock2;
        grid[15] = R.drawable.psm_sticky_notes2;

        return grid;
    }

    public static int[] getGrid2() {
        int[] grid = new int[16];

        grid[0] = R.drawable.psm_anxious;
        grid[1] = R.drawable.psm_hiking3;
        grid[2] = R.drawable.psm_stressed_person3;
        grid[3] = R.drawable.psm_lonely2;
        grid[4] = R.drawable.psm_dog_sleeping;
        grid[5] = R.drawable.psm_running4;
        grid[6] = R.drawable.psm_alarm_clock;
        grid[7] = R.drawable.psm_headache;
        grid[8] = R.drawable.psm_baby_sleeping;
        grid[9] = R.drawable.psm_puppy;
        grid[10] = R.drawable.psm_stressed_cat;
        grid[11] = R.drawable.psm_angry_face;
        grid[12] = R.drawable.psm_bar;
        grid[13] = R.drawable.psm_running3;
        grid[14] = R.drawable.psm_neutral_child;
        grid[15] = R.drawable.psm_headache2;

        return grid;
    }

    public static int[] getGrid3() {
        int[] grid = new int[16];

        grid[0] = R.drawable.psm_mountains11;
        grid[1] = R.drawable.psm_wine3;
        grid[2] = R.drawable.psm_barbed_wire2;
        grid[3] = R.drawable.psm_clutter;
        grid[4] = R.drawable.psm_blue_drop;
        grid[5] = R.drawable.psm_to_do_list;
        grid[6] = R.drawable.psm_stressed_person7;
        grid[7] = R.drawable.psm_stressed_person6;
        grid[8] = R.drawable.psm_yoga4;
        grid[9] = R.drawable.psm_bird3;
        grid[10] = R.drawable.psm_stressed_person8;
        grid[11] = R.drawable.psm_exam4;
        grid[12] = R.drawable.psm_kettle;
        grid[13] = R.drawable.psm_lawn_chairs3;
        grid[14] = R.drawable.psm_to_do_list3;
        grid[15] = R.drawable.psm_work4;

        return grid;
    }
}
