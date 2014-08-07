package com.example.savingsamuel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

public class GameStateManager {	
	private static final Vector3 _cameraPosition = new Vector3(0, Wall.Top(), Wall.Top() / 2);
	private static final float 
		_initialSafeTime = 2,
		_difficultyRampTime = 1,
		_difficultyRampLength = 180f / _difficultyRampTime,
		
		_rockDelayMeanInitial = 2,
		_rockDelayMeanFinal = 0.8f,
		_rockDelayMeanDecay = (_rockDelayMeanFinal - _rockDelayMeanInitial) / _difficultyRampLength,
		
		_rockDelayDeviationInitial = 0.25f,
		_rockDelayDeviationFinal = 0.1f,
		_rockDelayDeviationDecay = (_rockDelayDeviationFinal - _rockDelayDeviationInitial) / _difficultyRampLength;
	private static GameStateManager _instance;
	
	public static Vector3 CameraPosition() { return _cameraPosition; }
	public static void updatePreferences(SharedPreferences sharedPref) {
		AudioManager.updateMute(sharedPref.getBoolean("pref_mute", false));
		_instance._difficulty = Integer.parseInt(sharedPref.getString("pref_dif", "1"));
		_instance._warnEffect = sharedPref.getBoolean("pref_warn", true);
	}
	public static void Init(Context context) {
		_instance = new GameStateManager();
        Projectile.Init();
        AudioManager.Init(context);
		Shader.Init(context);
		Texture.Init(context);
		Samuel.Init();
		_instance.newGame();
	}
	public static boolean WarnEffect() {
		return _instance._warnEffect;
	}
	public static float TapScale() {
		switch(_instance._difficulty) {
		case 0:
			return 1;
		case 1:
			return 3;
		case 2:
			return 6;
		case 3:
			return 9;
		default:
			return 6;	
		}
	}
	public static void Update() {
		_instance._update();
	}
	
	private long _uptime;
	private float _rockTimer, _distractionTimer, _difficultyTimer;
	private int _gamestate, _difficulty;
	// Lower numbers means harder difficulty
	private boolean _warnEffect;
	private FloatDistribution _rockFlightTime, _rockDelayTime, _distractionDelayTime;
	private Vector3Distribution _crowdArea;
	private float _crowdOffset = 12;
	
	public GameStateManager() {}
	
	private Vector3 launchOrigin() {
		Vector3 origin = _crowdArea.GetRandom();
		if(Math.random() < 0.5f) {
			origin.x -= _crowdOffset;
		} else {
			origin.x += _crowdOffset;
		}
		return origin;
	}
	private void launchRock(Vector3 target, boolean warn) {
		Vector3 position = launchOrigin();
		Vector3 delta = Vector3.Subtract(target, position);
		float flightTime = _rockFlightTime.GetRandom();
    	Rock.Launch(
    			(float)Math.random() * 360f,
    			(float)Math.random() * 720f - 360f,
    			new Vector3((float)Math.random() * 0.2f + 0.9f),
    			position, 
    			new Vector3(
    					delta.x / flightTime,
    	        		(delta.y + 9.8f / 2f * (float)Math.pow(flightTime, 2)) / flightTime,
    					delta.z / flightTime),
    			warn
    			);
	}
	private void _update() {
        long nuptime = SystemClock.uptimeMillis();
        float elapsed = (float) (nuptime - _uptime) / 1000f;
        _uptime = nuptime;
        
		if(_gamestate == GameState.RUNNING) {
	        _rockTimer -= elapsed;
	        _distractionTimer -= elapsed;
	        _difficultyTimer -= elapsed;
	        if(_difficultyTimer <= 0) {
	        	_difficultyTimer += _difficultyRampTime;
	        	if(_rockDelayTime.Mean() > _rockDelayMeanFinal) {
	        		_rockDelayTime.ShiftMean(_rockDelayMeanDecay);
	        	}
	        	if(_rockDelayTime.StandardDeviation() > _rockDelayDeviationFinal) {
	        		_rockDelayTime.ShiftStandardDeviation(_rockDelayDeviationDecay);
	        	}
	        }
	        if(_distractionTimer <= 0) {
	        	_distractionTimer += _distractionDelayTime.GetRandom();
	        	launchRock(new Vector3(
	        					Samuel.Left() + (float)Math.random() * 6 * Samuel.Width() - 3 * Samuel.Width(),
	        					Samuel.Bottom() - ((float)Math.random() + 1) * Samuel.Height(),
	        					0),
	        					false
	        			); 
	        }
	        if(_rockTimer <= 0) {
	        	_rockTimer += _rockDelayTime.GetRandom();
	        	launchRock(new Vector3(
	        					Samuel.Left() + (float)(Math.random() * 0.5 + 0.5) * Samuel.Width(),
	        					Samuel.Bottom() + (float)(Math.random() * 0.5 + 0.5) * Samuel.Height(),
	        					0),
	        					true
	        			);     	

	        }
	        Projectile.Update(elapsed);
		} else if (_gamestate == GameState.LOSING) {
			Samuel.Update(elapsed);
	        Projectile.Update(elapsed);
		}
	}
	private void newGame() {
		Samuel.Reset();
		Projectile.Reset();
		_rockFlightTime = new FloatDistribution(3f, 0.1f);
		_rockDelayTime = new FloatDistribution(_rockDelayMeanInitial, _rockDelayDeviationInitial);
		_distractionDelayTime = new FloatDistribution(3, 0.5f);
		_crowdArea = new Vector3Distribution(
				0, 3,
				0, 0,
				5, 1
				);
		_rockTimer = _rockDelayTime.GetRandom() + _initialSafeTime;
		_distractionTimer = _distractionDelayTime.GetRandom() + _initialSafeTime;
		_difficultyTimer = _difficultyRampTime + _initialSafeTime;
		_gamestate = GameState.RUNNING;
		_uptime = SystemClock.uptimeMillis();
	}
}
