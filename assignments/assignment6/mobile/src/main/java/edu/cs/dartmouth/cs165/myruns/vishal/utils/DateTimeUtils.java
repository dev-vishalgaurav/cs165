package edu.cs.dartmouth.cs165.myruns.vishal.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vishal Gaurav on 4/23/16.
 */
public class DateTimeUtils {


    public static final String EXCERCISE_ENTRY_FORMAT = "HH:mm:ss MMMM dd yyyy";

    /**
     * Returns formatted date according to the format passed
     * @param timeInMillis
     * @param format
     * @return
     */
    public static String getFormattedDate(long timeInMillis, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(new Date(timeInMillis));

    }

}
