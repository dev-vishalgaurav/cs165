package edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.BaseAdapter;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.BaseActivity;
import edu.cs.dartmouth.cs165.myruns.vishal.ui.activity.ProfileSettings;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getString(R.string.pref_profile_key);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_start_settings);
        findPreference(getString(R.string.pref_profile_key)).setOnPreferenceClickListener(mOnPreferenceClicked);
        findPreference(getString(R.string.pref_webpage_key)).setOnPreferenceClickListener(mOnPreferenceClicked);
    }

    private Preference.OnPreferenceClickListener mOnPreferenceClicked = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            if(preference.getKey().equals(getString(R.string.pref_profile_key))){
                onPreferenceClickProfile();
                return true;
            }else if(preference.getKey().equals(getString(R.string.pref_webpage_key))){
                onPreferenceClickWeb();
                return true;
            }
            return false;
        }
    };

    private void onPreferenceClickWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(getString(R.string.pref_webpage_summary));
        intent.setData(uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.chooser_web)));
        }catch (Exception ex){
            ((BaseActivity) getActivity()).showToast(getString(R.string.error));
        }
    }
    private void onPreferenceClickProfile() {
        Intent intent = new Intent(getActivity(), ProfileSettings.class);
        startActivity(intent);
    }

}