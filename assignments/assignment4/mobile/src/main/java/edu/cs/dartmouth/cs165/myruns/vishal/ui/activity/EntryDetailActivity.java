package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Date;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.global.MyRunsApp;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.db.ExerciseEntry;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.preferences.PreferenceUtils;


/**
 * For the entry details saved by the user
 */
public class EntryDetailActivity extends BaseActivity {

    public static final String EXTRA_ENTRY_ID = "extra_entry_id";

    private EditText mEdtInputType = null;
    private EditText mEdtActivityType = null;
    private EditText mEdtDateTime = null;
    private EditText mEdtDuration = null;
    private EditText mEdtDistance = null;
    private EditText mEdtCalories = null;
    private EditText mEdtHeartRate = null;
    private AlertDialog mDeleteConfirmDialog = null;
    private long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);
        initViews();
        updateData();
    }

    /**
     * Update the entry data to the user input
     */
    private void updateData() {
        id = getIntent().getLongExtra(EXTRA_ENTRY_ID, id);
        ExerciseEntry entry = MyRunsApp.getDb(getBaseContext()).fetchEntryByIndex(id);
        if (entry != null) {
            mEdtInputType.setText(getBaseContext().getResources().getStringArray(R.array.input_type)[entry.getInputType()]);
            mEdtActivityType.setText(getBaseContext().getResources().getStringArray(R.array.activity_type)[entry.getActivityType()]);
            mEdtDateTime.setText(new Date(entry.getDateTime()).toString());
            mEdtDuration.setText(entry.getDuration() + " " + getString(R.string.secs));
            mEdtDistance.setText(entry.getDistance() + " " + PreferenceUtils.getDistanceUnit(getBaseContext()));
            mEdtCalories.setText(entry.getCalorie() + " " + getString(R.string.cals));
            mEdtHeartRate.setText(entry.getHeartRate() + " " + getString(R.string.bpm));
        } else {
            showToast(getString(R.string.error));
        }
    }

    /**
     * Main setup for display
     */
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEdtInputType = (EditText) findViewById(R.id.edtInputType);
        mEdtActivityType = (EditText) findViewById(R.id.edtActivityType);
        mEdtDateTime = (EditText) findViewById(R.id.edtDateTime);
        mEdtDuration = (EditText) findViewById(R.id.edtDuration);
        mEdtDistance = (EditText) findViewById(R.id.edtDistance);
        mEdtCalories = (EditText) findViewById(R.id.edtCalories);
        mEdtHeartRate = (EditText) findViewById(R.id.edtHeartRate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry_details, menu);
        return true;
    }

    /**
     * Delete entry associated with the provided ID
     */
    private boolean onDeleteEntry(long id){
        if(MyRunsApp.getDb(getBaseContext()).removeEntry(id) > 0){
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete: {
                showDiscardConfirmDialog();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Show message upon entry discarding
     */
    private void showDiscardConfirmDialog() {
        initDiscardConfirmDialog();
        mDeleteConfirmDialog.show();
    }

    /**
     * Confirm entry deletion by the user
     */
    private void initDiscardConfirmDialog() {
        Dialog.OnClickListener mOnDialogClickDiscardConfirm = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE: {
                        new DeleteTask().execute(id);
                    }
                    break;
                    case Dialog.BUTTON_NEGATIVE: {
                    }
                    break;
                }
            }
        };
        if (mDeleteConfirmDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EntryDetailActivity.this);
            builder.setTitle(R.string.dialog_title_delete_confirm);
            builder.setMessage(String.format(getString(R.string.dialog_message_delete_confirm),id));
            builder.setPositiveButton(getString(R.string.yes), mOnDialogClickDiscardConfirm);
            builder.setNegativeButton(getString(R.string.no), mOnDialogClickDiscardConfirm);
            mDeleteConfirmDialog = builder.create();
        }
    }

    /**
     * Entry deletion
     */
    private class DeleteTask extends AsyncTask<Long,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.deleting_message),false);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dismissAlertDialog();
            if(result){
                showToast(getString(R.string.success));
                finish();
            }else{
                showToast(getString(R.string.error_in_delete));
                finish();
            }
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            return onDeleteEntry(params[0]);
        }
    }
}
