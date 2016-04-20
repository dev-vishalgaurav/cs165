package edu.cs.dartmouth.cs165.myruns.vishal.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vishal Gaurav
 */
public class ExerciseEntryDbHelper extends SQLiteOpenHelper {
    /**
     * constructor for DB helper. only context is required in this wrapper.
     * @param context
     */
    public ExerciseEntryDbHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstants.ExerciseEntryColumns.QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBConstants.ExerciseEntryColumns.QUERY_DROP);
        db.execSQL(DBConstants.ExerciseEntryColumns.QUERY_CREATE);
    }
}
