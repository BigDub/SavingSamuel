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
		Shader.Init(context);
		Texture.Init(context);
	}
	
	private long _uptime;
	private float timer, distractionTimer;
	
	private static final Vector3 _cameraPosition = new Vector3(0, Wall.Top(), Wall.Top() / 2);
	public static Vector3 CameraPosition() { return _cameraPosition; }
	
	private Vector3 launchOrigin() {
		float x;
		if(Math.random() < 0.5f) {
			x = (float)Math.random() * 17f - 20f;
		} else {
			x = (float)Math.random() * 17f + 3f;
		}
		return new Vector3(x,
    			0,
    			(float)Math.random() * 4f + 1.0f);
	}
	private void launchRock(Vector3 target) {
		Vector3 position = launchOrigin();
		Vector3 delta = Vector3.Subtract(target, position);
		float flightTime = (float)Math.random() * 0.2f + 2.8f;
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
	        if(distractionTimer >= 2.65f) {
	        	distractionTimer = 0;
	        	launchRock(new Vector3(
	        					Samuel.Left() + (float)Math.random() * 6 * Samuel.Width() - 3 * Samuel.Width(),
	        					Samuel.Bottom() - ((float)Math.random() + 1) * Samuel.Height(),
	        					0)
	        			//(float)Math.random() * 0.5f + 1.5f
	        			); 
	        }
	        if(timer >= 1) {
	        	timer = 0;
	        	launchRock(new Vector3(
	        					Samuel.Left() + (float)(Math.random() * 0.5 + 0.5) * Samuel.Width(),
	        					Samuel.Bottom() + (float)(Math.random() * 0.5 + 0.5) * Samuel.Height(),
	        					0)
	        			//(float)Math.random() * 0.5f + 2.5f
	        			);     	

	        }
	        Projectile.Update(elapsed);
		}
	}

	public static void updatePreferences(SharedPreferences sharedPref) {
		AudioManager.updateMute(sharedPref.getBoolean("pref_mute", false));
		
	}

}
