package edu.cs.dartmouth.cs165.myruns.vishal.backend.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vishal gaurav on 5/17/16.
 */
public class ExerciseEntryDataStore {
    public static final Logger mLogger = Logger.getLogger(ExerciseEntryDataStore.class.getName());
    public static final DatastoreService mDatastore = DatastoreServiceFactory.getDatastoreService();

    private static Key getKey() {
        return KeyFactory.createKey(ExerciseEntry.EXERCISE_ENTRY_PARENT_ENTITY_NAME, ExerciseEntry.EXERCISE_ENTRY_PARENT_KEY_NAME);
    }

    private static void createParentEntity() {
        Entity entity = new Entity(getKey());
        mDatastore.put(entity);
    }

    public static boolean add(ExerciseEntry entry) {
        if (getEntryById(entry.id, null) != null) {
            mLogger.log(Level.INFO, "entry exists updating");
            return update(entry);
        }
        Key parentKey = getKey();
        Entity entity = new Entity(ExerciseEntry.EXERCISE_ENTRY_ENTITY_NAME, entry.id, parentKey);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns._ID, entry.id);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.INPUT_TYPE, entry.mInputType);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.ACTIVITY_TYPE, entry.mActivityType);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.DATE_TIME, new Date(entry.mDateTime));
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.DURATION, entry.mDuration);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.DISTANCE, entry.mDistance);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.AVG_PACE, entry.mAvgPace);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.AVG_SPEED, entry.mAvgSpeed);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.CALORIE, entry.mCalorie);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.CLIMB, entry.mClimb);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.HEART_RATE, entry.mHeartRate);
        entity.setProperty(ExerciseEntry.ExerciseEntryColumns.COMMENT, entry.mComment);
        mDatastore.put(entity);

        return true;
    }

    public static boolean update(ExerciseEntry entry) {
        Entity entity = null;
        try {
            entity = mDatastore.get(KeyFactory.createKey(getKey(), ExerciseEntry.ExerciseEntryColumns._ID, entry.id));
            if (entity != null) {
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns._ID, entry.id);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.INPUT_TYPE, entry.mInputType);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.ACTIVITY_TYPE, entry.mActivityType);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.DATE_TIME, new Date(entry.mDateTime));
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.DURATION, entry.mDuration);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.DISTANCE, entry.mDistance);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.AVG_PACE, entry.mAvgPace);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.AVG_SPEED, entry.mAvgSpeed);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.CALORIE, entry.mCalorie);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.CLIMB, entry.mClimb);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.HEART_RATE, entry.mHeartRate);
                entity.setProperty(ExerciseEntry.ExerciseEntryColumns.COMMENT, entry.mComment);
                mDatastore.put(entity);
                return true;
            }
        } catch (Exception ex) {

        }
        return false;
    }

    public static void deleteAll(){
        mDatastore.delete(getKey());
    }

    public Entity getEntityFromId(String id){
        // query
        Query.Filter filter = new Query.FilterPredicate(ExerciseEntry.ExerciseEntryColumns._ID, Query.FilterOperator.EQUAL, id);

        Query query = new Query(ExerciseEntry.EXERCISE_ENTRY_ENTITY_NAME);
        query.setFilter(filter);
        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = mDatastore.prepare(query);
        Entity result = pq.asSingleEntity();
        return  result;
    }

    public static boolean delete(String id) {
        // you can also use name to get key, then use the key to delete the
        // entity from datastore directly
        // because name is also the entity's key

        // query
        Query.Filter filter = new Query.FilterPredicate(ExerciseEntry.ExerciseEntryColumns._ID, Query.FilterOperator.EQUAL, id);

        Query query = new Query(ExerciseEntry.EXERCISE_ENTRY_ENTITY_NAME);
        query.setFilter(filter);
        // Use PreparedQuery interface to retrieve results
        PreparedQuery pq = mDatastore.prepare(query);
        Entity result = pq.asSingleEntity();
        boolean ret = false;
        if (result != null) {
            // delete
            mDatastore.delete(result.getKey());
            ret = true;
        }
        return ret;
    }

    public static ArrayList<ExerciseEntry> query(String id) {
        ArrayList<ExerciseEntry> resultList = new ArrayList<ExerciseEntry>();
        if (id != null && !id.equals("")) {
            ExerciseEntry contact = getEntryById(id, null);
            if (contact != null) {
                resultList.add(contact);
            }
        } else {
            Query query = new Query(ExerciseEntry.EXERCISE_ENTRY_ENTITY_NAME);
            // get every record from datastore, no filter
            query.setFilter(null);
            // set query's ancestor to get strong consistency
            query.setAncestor(getKey());
            PreparedQuery pq = mDatastore.prepare(query);

            for (Entity entity : pq.asIterable()) {
                ExerciseEntry entry = getEntryFromEntity(entity);
                if (entity != null) {
                    resultList.add(entry);
                }
            }
        }
        return resultList;
    }


    public static ExerciseEntry getEntryById(String id, Transaction txn) {
        Entity result = null;
        try {
            result = mDatastore.get(KeyFactory.createKey(getKey(), ExerciseEntry.ExerciseEntryColumns._ID,id));
        } catch (Exception ex) {

        }
        return getEntryFromEntity(result);
    }

    private static ExerciseEntry getEntryFromEntity(Entity entity) {
        if (entity == null) {
            return null;
        }
        String id =  (String)entity.getProperty(ExerciseEntry.ExerciseEntryColumns._ID);
        int inputType = Integer.parseInt(entity.getProperty(ExerciseEntry.ExerciseEntryColumns.INPUT_TYPE).toString());
        int activityType = Integer.parseInt(entity.getProperty(ExerciseEntry.ExerciseEntryColumns.ACTIVITY_TYPE).toString());
        long date = ((Date) entity.getProperty(ExerciseEntry.ExerciseEntryColumns.DATE_TIME)).getTime();
        int duration =  Integer.parseInt(entity.getProperty(ExerciseEntry.ExerciseEntryColumns.DURATION).toString());
        double distance = Double.parseDouble(entity.getProperty(ExerciseEntry.ExerciseEntryColumns.DISTANCE).toString());
        double avgPace =  Double.parseDouble(entity.getProperty(ExerciseEntry.ExerciseEntryColumns.AVG_PACE).toString());
        double avgSpeed = Double.parseDouble(entity.getProperty(ExerciseEntry.ExerciseEntryColumns.AVG_SPEED).toString());
        int calorie =     Integer.parseInt(entity.getProperty(ExerciseEntry.ExerciseEntryColumns.CALORIE).toString());
        double climb =    Double.parseDouble(entity.getProperty(ExerciseEntry.ExerciseEntryColumns.CLIMB).toString());
        int heartRate =   Integer.parseInt(entity.getProperty(ExerciseEntry.ExerciseEntryColumns.HEART_RATE).toString());
        String comment = (String) entity.getProperty(ExerciseEntry.ExerciseEntryColumns.COMMENT);
        return new ExerciseEntry(id,inputType,activityType,date,duration,distance,avgPace,avgSpeed,calorie,climb,heartRate,comment);
    }

}
