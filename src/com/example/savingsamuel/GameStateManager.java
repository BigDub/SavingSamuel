package com.example.savingsamuel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

public class GameStateManager {
	private int _gamestate;
	public int GameState() {
		return _gamestate;
	}
	
	private static GameStateManager _instance;
	public static GameStateManager Instance() {
		return _instance;
	}
	
	public GameStateManager() {
		_uptime = SystemClock.uptimeMillis();
		_gamestate = GameState.RUNNING;
	}
	
	public static void Init(Context context) {
		_instance = new GameStateManager();
        Projectile.Init();
        AudioManager.Init(context);
		ShaderProgram.Init(context);
		Texture.Init(context);
	}
	
	private long _uptime;
	private float timer, distractionTimer;
	
	private static final Vector3 _cameraPosition = new Vector3(0, 17, 10);
	public static Vector3 CameraPosition() { return _cameraPosition; }
	
	private void launchRock(Vector3 position, Vector3 target, float flightTime) {
		Vector3 delta = Vector3.Subtract(target, position);
    	Rock.Launch(
    			(float)Math.random() * 360f,
    			(float)Math.random() * 720f - 360f,
    			new Vector3((float)Math.random() * 0.2f + 0.9f),
    			position, 
    			new Vector3(
    					delta.x / flightTime,
    	        		(delta.y + 9.8f / 2f * (float)Math.pow(flightTime, 2)) / flightTime,
    					delta.z / flightTime)
    			);
	}
	public void update() {
        long nuptime = SystemClock.uptimeMillis();
        float elapsed = (float) (nuptime - _uptime) / 1000f;
        _uptime = nuptime;
        
		if (
				_gamestate == GameState.RUNNING || 
				_gamestate == GameState.LOSING
				) {
	        timer += elapsed;
	        distractionTimer += elapsed;
	        if(distractionTimer >= 3.8f) {
	        	distractionTimer = 0;
	        	Vector3 position = new Vector3(
	        			(float)Math.random() * 30f - 15f,
	        			0,
	        			(float)Math.random() * 4.5f + 0.5f);
	        	launchRock(position,
	        			new Vector3(
	        					Samuel.Left() + (float)Math.random() * 8 * Samuel.Width() - 4 * Samuel.Width(),
	        					Samuel.Bottom() - ((float)Math.random() * 2 + 2) * Samuel.Height(),
	        					0),
	        			(float)Math.random() * 0.5f + 1.5f
	        			); 
	        }
	        if(timer >= 1) {
	        	timer = 0;
	        	Vector3 position = new Vector3(
	        			(float)Math.random() * 30f - 15f,
	        			0,
	        			(float)Math.random() * 4.5f + 0.5f);
	        	launchRock(position,
	        			new Vector3(
	        					Samuel.Left() + (float)Math.random() * Samuel.Width(),
	        					Samuel.Bottom() + (float)Math.random() * Samuel.Height(),
	        					0),
	        			(float)Math.random() * 0.5f + 2.5f
	        			);     	

	        }
	        Projectile.Update(elapsed);
		}
	}

	public static void updatePreferences(SharedPreferences sharedPref) {
		AudioManager.updateMute(sharedPref.getBoolean("pref_mute", false));
		
	}

}
