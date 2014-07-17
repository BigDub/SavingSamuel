package com.example.savingsamuel;

import android.os.Bundle;
import android.preference.PreferenceFragment;

// This should only be used with API version >= 11 (Honeycomb)
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}