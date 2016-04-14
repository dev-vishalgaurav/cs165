package cs.dartmouth.edu.cs165.vm.stressmeter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ResultsFragment extends Fragment {

    private ListView mLstStress = null;
    private StressAdapter mStressAdapter = null;
    private List<StressData> mStressData = new ArrayList<>();

    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance() {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // nothing to do
        }
    }

    @Override
    public void onResume() {
        updateData();
        super.onResume();
    }

    private void updateData(){
        mStressData.clear();
        mStressData.addAll(PSM.getStressData());
        mStressAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_results, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView){
        mLstStress = (ListView)rootView.findViewById(R.id.lstStressData);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.row_stress_data_header, null);
        header.setEnabled(false);
        mLstStress.addHeaderView(header);
        mLstStress.setHeaderDividersEnabled(true);
        mStressAdapter = new StressAdapter(getActivity(), mStressData);
        mLstStress.setAdapter(mStressAdapter);

    }
    private static class StressAdapter extends BaseAdapter {

        private List<StressData> mList = null;
        private Context mContext;

        public StressAdapter(Context context, List<StressData> list){
            this.mContext = context;
            this.mList = list;
        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public StressData getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_stress_data,null);
            }
            StressData data = getItem(position);
            ((TextView) convertView.findViewById(R.id.txtTime)).setText(""+data.getTime());
            ((TextView) convertView.findViewById(R.id.txtStress)).setText(""+data.getGridScore());
            return convertView;
        }
    }

}
