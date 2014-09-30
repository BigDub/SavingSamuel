package william.wyatt.savingsamuel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceActivity;

// This should only be used with API version >= 11 (Honeycomb)
@SuppressLint("NewApi")
public class SettingsActivityHC extends PreferenceActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);// Display the fragment as the main content.
        
        getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();
    }
}