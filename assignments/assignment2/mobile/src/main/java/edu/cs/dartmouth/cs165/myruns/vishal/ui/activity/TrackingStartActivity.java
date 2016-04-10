/**
 * Created by Vishal Gaurav
 */
package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs.DateDialogFragment;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs.InputDialogFragment;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs.TimeDialogFragment;


public class TrackingStartActivity extends BaseActivity {

    private ListView mLstTrack = null;
    private Button mBtnSave = null;
    private Button mBtnCancel = null;
    private ArrayAdapter<CharSequence> mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_start);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnSave = (Button) findViewById(R.id.btnSave);
        mBtnCancel.setOnClickListener(mOnClickListener);
        mBtnSave.setOnClickListener(mOnClickListener);
        mLstTrack = (ListView) findViewById(R.id.lstTrack);
        mAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.tract_start, android.R.layout.simple_list_item_1);
        mLstTrack.setAdapter(mAdapter);
        mLstTrack.setOnItemClickListener(mOnItemClickListener);
    }

    private void onSaveClicked() {
        showToast(getString(R.string.saved));
        finish();
    }

    private void onCancelClicked() {
        showToast(getString(R.string.discarded));
        finish();
    }

    private void onTrackingClicked(View view, int position) {
        switch (position) {
            case 0: {
                new DateDialogFragment().show(getSupportFragmentManager(),"date");
            }
            break;
            case 1: {
                new TimeDialogFragment().show(getSupportFragmentManager(),"time");

            }
            break;
            case 2: {
                new InputDialogFragment().show(getSupportFragmentManager(),"nothing",mAdapter.getItem(position).toString(),"", InputType.TYPE_CLASS_NUMBER,1);
            }
            break;
            case 3: {
                new InputDialogFragment().show(getSupportFragmentManager(), "nothing", mAdapter.getItem(position).toString(), "", InputType.TYPE_CLASS_NUMBER, 1);
            }
            break;
            case 4: {
                new InputDialogFragment().show(getSupportFragmentManager(), "nothing", mAdapter.getItem(position).toString(), "", InputType.TYPE_CLASS_NUMBER, 1);
            }
            break;
            case 5: {
                new InputDialogFragment().show(getSupportFragmentManager(), "nothing", mAdapter.getItem(position).toString(), "", InputType.TYPE_CLASS_NUMBER, 1);
            }
            break;
            case 6: {
                new InputDialogFragment().show(getSupportFragmentManager(), "nothing", mAdapter.getItem(position).toString(), getString(R.string.hint_track_comments), InputType.TYPE_CLASS_NUMBER, 1);
            }
            break;
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onTrackingClicked(view,position);
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSave: {
                    onSaveClicked();
                }
                break;
                case R.id.btnCancel: {
                    onCancelClicked();
                }
                break;
            }
        }
    };

}
