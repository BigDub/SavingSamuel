package com.example.savingsamuel;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class Game extends Activity {
	
	private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		Log.e("Game", "onCreate");
        super.onCreate(savedInstanceState);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        GameStateManager.Init(this);
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }
    
    @Override
    protected void onResume() {
		Log.e("Game", "onResume");
    	super.onResume();
    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    	GameStateManager.updatePreferences(sharedPref);
    	GameStateManager.OnResume();
    }
    
    @Override
    protected void onPause() {
		Log.e("Game", "onPause");
    	super.onPause();
    	GameStateManager.OnPause();
    }
}
