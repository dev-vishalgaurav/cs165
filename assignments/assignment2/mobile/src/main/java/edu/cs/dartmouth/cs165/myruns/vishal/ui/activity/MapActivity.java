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



public class MapActivity extends BaseActivity {

    private Button mBtnSave = null;
    private Button mBtnCancel = null;

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
    }

    private void onSaveClicked() {
        finish();
    }

    private void onCancelClicked() {
        //showToast(getString(R.string.discarded));
        finish();
    }
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
