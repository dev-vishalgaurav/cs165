package edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.MapActivity;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.TrackingStartActivity;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_start, container, false);
        initViews();
        return mRootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void onStartClicked() {
        if(mSpnInputType.getSelectedItemPosition() > 0){
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getActivity(), TrackingStartActivity.class);
            startActivity(intent);
        }
    }

    private void onSyncClicked() {

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
