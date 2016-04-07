package edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import edu.cs.dartmouth.cs165.myruns.vishal.R;

;
;



public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_start_settings);

    }



}
