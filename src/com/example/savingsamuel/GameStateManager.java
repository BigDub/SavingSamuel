package com.example.savingsamuel;

import android.content.Context;
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
	private float timer;
	
	
	public void update() {
        long nuptime = SystemClock.uptimeMillis();
        float elapsed = (float) (nuptime - _uptime) / 1000f;
        _uptime = nuptime;
        
		if (
				_gamestate == GameState.RUNNING || 
				_gamestate == GameState.LOSING
				) {
	        timer += elapsed;
	        float interval = 3f;
	        if(timer >= 1) {
	        	timer = 0;
	        	float
	        		x = (float)Math.random() * 30f - 15f,
	        		z = (float)Math.random() * 4.5f + 0.5f,
	        		vx = (-x + (float)Math.random() * 2f - 1f) / interval,
	        		vz = -z / interval,
	        		vy = (((float)Math.random() * 2f - 1f) + 21 + 9.8f / 2f * (float)Math.pow(interval, 2)) / interval
	        		;
	        	// 0 = vx * t + x
	        	// 0 = vz * t + z
	        	// 21 = -9.8 / 2 * t^2 + vy * t
	        	
	        	Rock.Launch(
	        			(float)Math.random() * 360f,
	        			(float)Math.random() * 720f - 360f,
	        			x,
	        			0, 
	        			z, 
	        			vx, 
	        			vy,
	        			vz
	        			);
	        }
	        Projectile.Update(elapsed);
		}
	}

}
