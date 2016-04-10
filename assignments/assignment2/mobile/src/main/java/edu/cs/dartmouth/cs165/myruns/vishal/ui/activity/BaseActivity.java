/**
 * Created by Vishal Gaurav
 */
package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.global.MyRunsApp;

/**
 * Common parent for most of the activities of this app.
 * should not be used as an activity to display layouts.
 */
public class BaseActivity extends AppCompatActivity {


    private ProgressDialog mProgressDialog = null;
    private AlertDialog mAlertDialog = null;

    /**
     * @param progressMessage
     * @param isCancelable
     */
    protected void showProgressDialog(String progressMessage, boolean isCancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BaseActivity.this);
        }
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setMessage(progressMessage);
        mProgressDialog.show();
    }
    protected void showError(boolean result,EditText editText,String errorMessage){
        if(!result) {
            editText.setError(errorMessage);
            editText.requestFocus();
        }
    }
    /**
     * @param title
     * @param alertMessage
     * @param isCancelable
     */
    protected void showAlertDialog(String title, String alertMessage, boolean isCancelable) {
        if (mAlertDialog == null) {
            mAlertDialog = getNewAlertDialog();
        }
        mAlertDialog.setCancelable(isCancelable);
        mAlertDialog.setTitle(title);
        mAlertDialog.setMessage(alertMessage);
        mAlertDialog.show();

    }

    /**
     * @return
     */
    private AlertDialog getNewAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setPositiveButton(getString(R.string.ok), null);
        return builder.create();
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        dismissAlertDialog();
        super.onDestroy();
    }

    protected void dismissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = null;
    }

    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    /**
     * method to return simple name of class. Override this to return your own custom tag.
     *
     * @return returns simple name of class.
     */
    protected String getTag() {
        return MyRunsApp.getTag(this.getClass().getSimpleName());
    }


    /**
     * prints trace in Log.v format . warps the implementation to pass TAG as a parameter and provides a single point to print logs.
     *
     * @param traceMessage message to be printed
     */
    public void printTrace(String traceMessage) {
        MyRunsApp.printTrace(getTag(), traceMessage);
    }

    public void printError(String traceMsg, Exception ex) {
        printTrace(traceMsg);
        printTrace(ex.getMessage());
        ex.printStackTrace();
    }

    public void showToast(String msg) {
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
