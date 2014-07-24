package com.example.savingsamuel;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

public class Game extends Activity {
	
	private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        GameStateManager.Init(this);
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    	//boolean white = sharedPref.getBoolean("pref_test", false);
    }
}
