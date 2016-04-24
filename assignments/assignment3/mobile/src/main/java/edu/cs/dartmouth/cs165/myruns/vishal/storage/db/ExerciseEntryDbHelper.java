package edu.cs.dartmouth.cs165.myruns.vishal.storage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Vishal Gaurav
 */
public class ExerciseEntryDbHelper extends SQLiteOpenHelper {
    /**
     * Constructor for DB helper. Only context is required in this wrapper.
     *
     * @param context
     */
    public ExerciseEntryDbHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("VVV","onCreateDB");
        db.execSQL(DBConstants.ExerciseEntryColumns.QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("VVV","onUpgradeDB");
        db.execSQL(DBConstants.ExerciseEntryColumns.QUERY_DROP);
        db.execSQL(DBConstants.ExerciseEntryColumns.QUERY_CREATE);
    }

    /**
     * Insert values into table
     */
    protected long insert(String tableName, ContentValues values) {
        SQLiteDatabase writableDB = getWritableDatabase();
        long rowId = -1;
        synchronized (writableDB) {
            rowId = writableDB.insert(tableName, null, values);
        }
        return rowId;
    }

    /**
     * Delete from table
     */
    protected long delete(String tableName, String whereClause, String[] selectionArgs) {
        SQLiteDatabase writableDB = getWritableDatabase();
        int updatedRows = 0;
        synchronized (writableDB) {
            updatedRows = writableDB.delete(tableName, whereClause, selectionArgs);
        }
        return updatedRows;
    }

    protected Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupby, String having, String orderBy) {
        SQLiteDatabase redableDB = getReadableDatabase();
        Cursor resultCursor = null;
        synchronized (redableDB) {
            resultCursor = redableDB.query(table, columns, selection, selectionArgs, groupby, having, orderBy);
        }
        return resultCursor;
    }

    /**
     * Update table
     */
    protected int update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase writableDB = getWritableDatabase();
        int updatedRows = 0;
        synchronized (writableDB) {
            updatedRows = writableDB.update(tableName, values, whereClause, whereArgs);
        }
        return updatedRows;
    }

    /**
     * Insert a item given each column value
     */
    public long insertEntry(ExerciseEntry entry) {
       return  insert(DBConstants.ExerciseEntryColumns.TABLE_NAME,entry.toContentValues());
    }

    /**
     * Remove an entry by giving its index
     */
    public long removeEntry(long rowIndex) {
        String selection = DBConstants.ExerciseEntryColumns._ID +  " = ? " ;
        String[] selectionArgs = {String.valueOf(rowIndex)};
        return delete(DBConstants.ExerciseEntryColumns.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Query a specific entry by its index
     */
    public ExerciseEntry fetchEntryByIndex(long rowId) {
        String selection = DBConstants.ExerciseEntryColumns._ID +  " = ? " ;
        String[] selectionArgs = {String.valueOf(rowId)};
        Cursor result =  query(DBConstants.ExerciseEntryColumns.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        return result.moveToNext() ? new ExerciseEntry(result) : null ;
    }
    public Cursor fetchAllEntryCursor(){
        return query(DBConstants.ExerciseEntryColumns.TABLE_NAME, null, null, null, null, null, null);
    }

    /**
     * Query the entire table, return all rows
     */
    public ArrayList<ExerciseEntry> fetchEntries() {
        return ExerciseEntry.getListFromCursorList(fetchAllEntryCursor());
    }
}
