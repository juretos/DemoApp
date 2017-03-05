package com.outfit.jure.demoapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Sorn stacionarc on 18.2.2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}