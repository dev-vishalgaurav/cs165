package edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.global.MyRunsApp;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.preferences.PreferenceUtils;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.adapters.HomeTabAdapter.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<ExerciseEntry>>, SharedPreferences.OnSharedPreferenceChangeListener {

    public interface OnItemSelectedListener {
        void onItemSelected(long rowid);
    }

    private View mRootView = null;
    private ListView mLstHistory = null;
    private String[] mInputTypes = null;
    private String[] mActivityTypes = null;
    ArrayList<ExerciseEntry> historyData = new ArrayList<>();
    private HistoryAdapter historyAdapter = null;
    private OnItemSelectedListener onItemSelectedListener = null;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StartFragment.
     */
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("VVV", "History : onCreate");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("VVV", "History : onCreateView");
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_history, container, false);
        initViews(mRootView);
        return mRootView;
    }

    /**
     * Main setup display
     */
    private void initViews(View rootView) {
        mLstHistory = (ListView) rootView.findViewById(R.id.lstHistory);
        historyAdapter = new HistoryAdapter(getActivity(), historyData);
        mLstHistory.setAdapter(historyAdapter);
        mLstHistory.setOnItemClickListener(mOnItemClickListener);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("VVV", "History : onAttach");
        if (context instanceof OnItemSelectedListener) {
            onItemSelectedListener = (OnItemSelectedListener) context;
        }
    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.e("VVV", "History : onAttach");
        if (context instanceof OnItemSelectedListener) {
            onItemSelectedListener = (OnItemSelectedListener) context;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.e("VVV", "History : onAttach");
        super.onResume();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onDetach() {
        Log.e("VVV", "History : onDeAttach");
        mOnItemClickListener = null;
        super.onDetach();
    }

    @Override
    public Loader<ArrayList<ExerciseEntry>> onCreateLoader(int id, Bundle args) {
        return new HistoryLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ExerciseEntry>> loader, ArrayList<ExerciseEntry> data) {
        historyData.clear();
        historyData.addAll(data);
        historyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ExerciseEntry>> loader) {
        historyData.clear();
        historyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        historyAdapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("VVV","OnItemClick History fragment :- " + position);
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelected(id);
            }
        }
    };

    /**
     * Private class
     */
    private static class HistoryLoader extends AsyncTaskLoader<ArrayList<ExerciseEntry>> {

        public HistoryLoader(Context context) {
            super(context);
        }

        @Override

        protected void onStartLoading() {
            forceLoad(); //Force an asynchronous load.

        }

        @Override
        public ArrayList<ExerciseEntry> loadInBackground() {
            return MyRunsApp.getDb(getContext()).fetchEntries();
        }

        @Override
        public void deliverResult(ArrayList<ExerciseEntry> data) {
            super.deliverResult(data);
        }
    }

    /**
     * Private class
     */
    private class HistoryAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<ExerciseEntry> data = null;

        public HistoryAdapter(Context context, ArrayList<ExerciseEntry> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public ExerciseEntry getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.row_history, null);
            }
            ExerciseEntry entry = getItem(position);
            TextView txtHistory = (TextView) convertView.findViewById(R.id.txtHistoryData);
            txtHistory.setText(entry.getFormattedString(context, PreferenceUtils.getUnitType(context)));
            return convertView;
        }

    }

}
