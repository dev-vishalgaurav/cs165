package cs.dartmouth.edu.cs165.vm.stressmeter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StressGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Display and activity of the images grid
 */

public class StressGridFragment extends Fragment {

    public interface OnImageSelectedListener{
        void onImageSelected(int gridPosition, int resId);
        void onMoreClicked();
    }

    private GridView mGvStressImages = null;
    private Button mBtnMoreImages = null;
    private ImageAdapter mAdapter = null;
    private int mCurrentGridId = 1;
    private List<Integer> mList = new ArrayList<>();
    private OnImageSelectedListener mOnImageSelectedListener = null;


    public StressGridFragment() {
        // Required empty public constructor
    }
    public void setOnImageSelectedListener(OnImageSelectedListener listener){
        this.mOnImageSelectedListener = listener;
    }

    public static StressGridFragment newInstance(OnImageSelectedListener listener) {
        StressGridFragment fragment = new StressGridFragment();
        fragment.setOnImageSelectedListener(listener);
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
        View view = inflater.inflate(R.layout.fragment_stress_grid, container, false);
        initViews(view);
        return view;
    }

    /*
    Main setup
     */
    private void initViews(View rootView){
        mGvStressImages = (GridView)rootView.findViewById(R.id.gvStressImages);
        mBtnMoreImages = (Button)rootView.findViewById(R.id.btnMoreImages);
        mBtnMoreImages.setOnClickListener(mOnClickListener);
        mAdapter = new ImageAdapter(getActivity(),mList);
        mGvStressImages.setAdapter(mAdapter);
        mGvStressImages.setOnItemClickListener(mOnItemClickListener);
        updateGrid();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    private void updateGrid(){
        mList.clear();
        int[] ids  = PSM.getGridById(mCurrentGridId);
        for (int i = 0 ; i < ids.length ; i++)
            mList.add(ids[i]);
        mAdapter.notifyDataSetChanged();
        mCurrentGridId++;
        if(mCurrentGridId == 4){
            mCurrentGridId = 1;
        }
    }

    /*
    Update grid with new images
     */
    private void onClickMoreImages(){
        updateGrid();
        if(mOnImageSelectedListener != null){
            mOnImageSelectedListener.onMoreClicked();
        }
    }
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(mOnImageSelectedListener != null){
                mOnImageSelectedListener.onImageSelected(position, mAdapter.getItem(position));
            }
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnMoreImages:{
                    onClickMoreImages();
                }
                break;
            }
        }
    };

    private static class ImageAdapter extends BaseAdapter{

        private List<Integer> mList = null;
        private Context mContext;

        public ImageAdapter(Context context,List<Integer> list ){
            this.mContext = context;
            this.mList = list;
        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Integer getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_stress_image,null);
            }
            ImageView imgStress = (ImageView) convertView.findViewById(R.id.imgStress);
            imgStress.setImageResource(getItem(position));
            return convertView;
        }
    }

}
