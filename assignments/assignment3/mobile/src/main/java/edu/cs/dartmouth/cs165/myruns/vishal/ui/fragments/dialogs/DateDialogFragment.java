package edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * wrapper to date dialog
 */
public class DateDialogFragment extends DialogFragment {

    public interface OnDateSelectedListener{
        void onDateSelected(String tag, int year, int month, int dayOfMonth);
    }

    private OnDateSelectedListener mOnDateSelectedListener = null;

    public DateDialogFragment() {
        // Required empty public constructor
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener){
        this.mOnDateSelectedListener = onDateSelectedListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), mOnDateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if(mOnDateSelectedListener!=null){
                mOnDateSelectedListener.onDateSelected(getTag(),year,monthOfYear,dayOfMonth);
            }
        }
    };
}
