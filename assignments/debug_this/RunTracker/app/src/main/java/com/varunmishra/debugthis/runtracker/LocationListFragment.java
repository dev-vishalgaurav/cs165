package com.varunmishra.debugthis.runtracker;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import com.varunmishra.debugthis.runtracker.RunDatabaseHelper.LocationCursor;


public class LocationListFragment extends ListFragment {

    private LocationCursor mCursor;
    private static final String COLUMN_LOCATION_LATITUDE = "latitude";
    private static final String COLUMN_LOCATION_LONGITUDE = "longitude";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCursor = RunManager.get(getActivity()).queryLocations(LocationListActivity.runId);
        /**
         * Bug 2 fixed Null pointer exception due to non initialization of values array
         */
        String[] values = new String[mCursor.getCount()];
        while (mCursor.moveToNext()){
            /**
             * Bug 4 fixed changed int to double
             */
            String str = "Lat: " + mCursor.getDouble(mCursor.getColumnIndex(COLUMN_LOCATION_LATITUDE)) + ", Lon: " + mCursor.getDouble(mCursor.getColumnIndex(COLUMN_LOCATION_LONGITUDE));
            values[mCursor.getPosition()] = str;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        setListAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
        super.onDestroy();
    }

}
