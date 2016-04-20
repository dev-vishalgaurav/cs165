package edu.cs.dartmouth.cs165.myruns.vishal.storage.db;

import android.provider.BaseColumns;

/**
 * Created by Vishal Gaurav
 */
public class DBConstants {
    /**
     * DB name for my runs app
     */
    public static final String DATABASE_NAME = "myruns.db";
    /**
     * db version
     */
    public static final int DATABASE_VERSION = 1;


    /**
     * column structure for ExerciseEntry table
     *

     CREATE TABLE IF NOT EXISTS ENTRIES (
     _id INTEGER PRIMARY KEY AUTOINCREMENT,
     input_type INTEGER NOT NULL,
     activity_type INTEGER NOT NULL,
     date_time DATETIME NOT NULL,
     duration INTEGER NOT NULL,
     distance FLOAT,
     avg_pace FLOAT,
     avg_speed FLOAT,
     calories INTEGER,
     climb FLOAT,
     heartrate INTEGER,
     comment TEXT,
     privacy INTEGER,
     gps_data BLOB );
     }
     */
    interface ExerciseEntryColumns extends BaseColumns{



        public static final String TABLE_NAME = "Entires";

        public static final String INPUT_TYPE = "input_type";
        public static final String ACTIVITY_TYPE = "activity_type";
        public static final String DATE_TIME = "date_time";
        public static final String DURATION = "duration";
        public static final String DISTANCE = "distance";
        public static final String AVG_PACE = "avg_pace";
        public static final String AVG_SPEED = "avg_speed";
        public static final String CALORIE = "calories";
        public static final String CLIMB = "climb";
        public static final String HEART_RATE = "heart_rate";
        public static final String COMMENT = "comment";
        public static final String PRIVACY = "privacy";
        public static final String GPS_DATA = "gps_data";

        public static final String QUERY_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME ;
        public static final String QUERY_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" +
                _ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                INPUT_TYPE + "INTEGER NOT NULL, " +
                ACTIVITY_TYPE + "INTEGER NOT NULL, " +
                DATE_TIME+ "DATETIME NOT NULL, " +
                DURATION + "INTEGER NOT NULL, " +
                DISTANCE + "FLOAT NOT NULL, " +
                AVG_PACE + "FLOAT NOT NULL, " +
                AVG_SPEED + "FLOAT NOT NULL, " +
                CALORIE + "INTEGER, " +
                CLIMB + "FLOAT, " +
                HEART_RATE + "FLOAT, " +
                COMMENT + "TEXT, " +
                PRIVACY + "INTEGER, " +
                GPS_DATA + "BLOB " +
                " ) ;";



    }

}
