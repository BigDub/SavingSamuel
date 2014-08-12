package com.example.savingsamuel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

public class Home extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.layout_menu);
        HighScoresManager.Init(this.getFilesDir());
    }
    
    public void openSettings(View view) {
    	Intent intent;
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
    		intent = new Intent(this, SettingsActivity.class);
    	} else {
    		intent = new Intent(this, SettingsActivityHC.class);
    	}
    	startActivity(intent);
    }
    
    public void playGame(View view) {
    	Intent intent = new Intent(this, Game.class);
    	startActivity(intent);
    }
    
    public void showScores(View view) {
    	Intent intent = new Intent(this, ScoresActivity.class);
    	startActivity(intent);
    }
}
