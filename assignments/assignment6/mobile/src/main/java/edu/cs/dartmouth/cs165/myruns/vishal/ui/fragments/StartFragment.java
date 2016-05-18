package edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.MapDisplayActivity;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.TrackingStartActivity;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.adapters.HomeTabAdapter;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.adapters.HomeTabAdapter.OnFragmentInteractionListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Spinner mSpnInputType = null;
    private Spinner mSpnActivityType = null;
    private Button mBtnStart = null;
    private Button mBtnSYnc = null;
    private View mRootView = null;
    private ArrayAdapter<CharSequence> mAdapterInputType = null;
    private ArrayAdapter<CharSequence> mAdapterActivityType = null;

    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Main setup for views
     */
    private void initViews() {
        mBtnStart = (Button) mRootView.findViewById(R.id.btnStart);
        mBtnSYnc = (Button) mRootView.findViewById(R.id.btnSync);
        mSpnInputType = (Spinner) mRootView.findViewById(R.id.spnInputType);
        mSpnActivityType = (Spinner) mRootView.findViewById(R.id.spnActivityType);
        mAdapterActivityType = ArrayAdapter.createFromResource(getActivity(), R.array.activity_type, android.R.layout.simple_spinner_dropdown_item);
        mAdapterInputType = ArrayAdapter.createFromResource(getActivity(), R.array.input_type, android.R.layout.simple_spinner_dropdown_item);
        mSpnInputType.setAdapter(mAdapterInputType);
        mSpnActivityType.setAdapter(mAdapterActivityType);
        mBtnSYnc.setOnClickListener(mOnClickListener);
        mBtnStart.setOnClickListener(mOnClickListener);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_start, container, false);
        initViews();
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("VVV", "StartFragment : onAttach");
        if (context instanceof HomeTabAdapter.OnFragmentInteractionListener) {
            mListener = (HomeTabAdapter.OnFragmentInteractionListener) context;
        }
    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.e("VVV", "StartFragment : onAttach");
        if (context instanceof HomeTabAdapter.OnFragmentInteractionListener) {
            mListener = (HomeTabAdapter.OnFragmentInteractionListener) context;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Start new activity when start button clicked.
     * If input type is manual entry, start tracking activity
     * If input type is GPS or automatic, start map activity
     */
    private void onStartClicked() {
        if(mSpnInputType.getSelectedItemPosition() > 0){
            Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
            intent.putExtra(MapDisplayActivity.EXTRA_INPUT_TYPE,mSpnInputType.getSelectedItemPosition());
            intent.putExtra(MapDisplayActivity.EXTRA_ACTIVITY_TYPE,mSpnActivityType.getSelectedItemPosition());
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getActivity(), TrackingStartActivity.class);
            intent.putExtra(TrackingStartActivity.EXTRA_INPUT_TYPE,mSpnInputType.getSelectedItemPosition());
            intent.putExtra(TrackingStartActivity.EXTRA_ACTIVITY_TYPE,mSpnActivityType.getSelectedItemPosition());
            startActivity(intent);
        }
    }

    private void onSyncClicked() {
       if(mListener != null){
           mListener.onSyncClicked();
       }
    }

    private void onItemClickInputType(View view, int position, long id){
    }

    private void onItemClickActivityType(View view, int position, long id){
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()){
                case R.id.spnInputType:{
                    onItemClickActivityType(view,position,id);
                }
                break;
                case R.id.spnActivityType:{
                    onItemClickInputType(view, position, id);
                }
                break;

            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart: {
                    onStartClicked();
                }
                break;
                case R.id.btnSync: {
                    onSyncClicked();
                }
                break;
            }
        }
    };

}
