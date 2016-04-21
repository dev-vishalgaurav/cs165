package edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * wrapper to time dialog
 */
public class TimeDialogFragment extends DialogFragment {

    public interface OnTimeSelectedListener{
            void onTimeSelected(String tag, int hourOfDay, int minute);
    }

    private OnTimeSelectedListener mOnTimeSelectedListener = null;

    public TimeDialogFragment() {
        // Required empty public constructor
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener){
        this.mOnTimeSelectedListener = onTimeSelectedListener;
    }

   @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
       final Calendar c = Calendar.getInstance();
       int hour = c.get(Calendar.HOUR_OF_DAY);
       int minute = c.get(Calendar.MINUTE);
       return new TimePickerDialog(getActivity(), mOnTimeSetListener, hour, minute, DateFormat.is24HourFormat(getActivity()));
   }

    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(mOnTimeSelectedListener!=null){
                mOnTimeSelectedListener.onTimeSelected(getTag(),hourOfDay,minute);
            }
        }
    };
}
