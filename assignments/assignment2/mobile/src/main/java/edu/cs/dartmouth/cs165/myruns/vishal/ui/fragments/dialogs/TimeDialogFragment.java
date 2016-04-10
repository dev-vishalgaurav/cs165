package edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import edu.cs.dartmouth.cs165.myruns.vishal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeDialogFragment extends DialogFragment {


    public TimeDialogFragment() {
        // Required empty public constructor
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

        }
    };
}
