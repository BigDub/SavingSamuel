package com.example.savingsamuel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

public class GameStateManager {	
	private static final Vector3 vCameraPosition = new Vector3(0, Wall.Top(), Wall.Top() / 2);
	private static Context cContext;
	private static final float 
		fInitialSafeTime = 2,
		fLossWait = 5,
		fDifficultyRampTime = 1,
		fDifficultyRampLength = 60f / fDifficultyRampTime,
		
		fRockDelayMeanInitial = 2,
		fRockDelayMeanFinal = 0.8f,
		fRockDelayMeanDecay = (fRockDelayMeanFinal - fRockDelayMeanInitial) / fDifficultyRampLength,
		
		fRockDelayDeviationInitial = 0.25f,
		fRockDelayDeviationFinal = 0.1f,
		fRockDelayDeviationDecay = (fRockDelayDeviationFinal - fRockDelayDeviationInitial) / fDifficultyRampLength;
	private static GameStateManager gInstance = new GameStateManager();
	
	public static GameStateManager Instance() { return gInstance; }
	public static Vector3 CameraPosition() { return vCameraPosition; }
	public static void updatePreferences(SharedPreferences sharedPref) {
		AudioManager.updateMute(sharedPref.getBoolean("pref_mute", false));
		gInstance.iDifficulty = Integer.parseInt(sharedPref.getString("pref_dif", "1"));
		gInstance.bWarnEffect = sharedPref.getBoolean("pref_warn", true);
	}
	public static void Init(Context context) {
		cContext = context;
        Projectile.Init();
        AudioManager.Init(context);
		Shader.Init(context);
		Texture.Init(context);
		Samuel.Init();
		gInstance.newGame();
	}
	public static boolean WarnEffect() {
		if(gInstance.iGamestate == GameState.RUNNING)
			return gInstance.bWarnEffect;
		return false;
	}
	public static float TapScale() {
		switch(gInstance.iDifficulty) {
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
	public static float Score() { return gInstance.iScore; }
	public static void Update() {
		if(gInstance.bHasFocus)
			gInstance.update();
	}
	public static void SamuelHit() { gInstance.samuelHit(); }
	public static void NewGame() { gInstance.newGame(); }
	public static void GoToScores() {
		Log.e("GameStateManager", "Difficulty: " + gInstance.iDifficulty);
		gInstance.iGamestate = GameState.GAMEOVER;
    	Intent intent = new Intent(cContext, ScoresActivity.class);
    	intent.setAction("OPEN_SCORE_" + gInstance.iDifficulty);
    	cContext.startActivity(intent);
	}
	public static void RampDifficulty() { gInstance.rampDifficulty(); }
	public static void NewRock() { gInstance.newRock(); }
	public static void NewDistraction() { gInstance.newDistraction(); }
	public static void AddPoint() {
		if(gInstance.iGamestate != GameState.RUNNING)
			return;
		gInstance.iScore++;
	}
	public static void OnResume() { gInstance.onResume(); }
	public static void OnPause() { gInstance.onPause(); }
	
	private long lUptime;
	private int iGamestate, iDifficulty, iScore;
	// Lower numbers means harder difficulty
	private boolean bWarnEffect, bHasFocus;
	private FloatDistribution fRockFlightTime, fRockDelayTime, fDistractionDelayTime;
	private Vector3Distribution vCrowdArea;
	private float fCrowdOffset = 12, fTimescale = 1;
	
	public GameStateManager() {}
	
	private Vector3 launchOrigin() {
		Vector3 origin = vCrowdArea.GetRandom();
		if(Math.random() < 0.5f) {
			origin.x -= fCrowdOffset;
		} else {
			origin.x += fCrowdOffset;
		}
		return origin;
	}
	private void launchRock(Vector3 target, boolean warn) {
		Vector3 position = launchOrigin();
		Vector3 delta = Vector3.Subtract(target, position);
		float flightTime = fRockFlightTime.GetRandom();
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
	private void update() {
        long nuptime = SystemClock.uptimeMillis();
        float elapsed = ((nuptime - lUptime) / 1000f) * fTimescale;
        lUptime = nuptime;
        
        Timer.Update(elapsed);
        
		if(iGamestate == GameState.RUNNING) {
	        Projectile.Update(elapsed);
		} else if (iGamestate == GameState.LOSING) {
			Samuel.Update(elapsed);
	        Projectile.Update(elapsed);
		}
	}
	private void newGame() {
		Samuel.Reset();
		Projectile.Reset();
		iScore = 0;
		fTimescale = 1;
		fRockFlightTime = new FloatDistribution(3f, 0.1f);
		fRockDelayTime = new FloatDistribution(fRockDelayMeanInitial, fRockDelayDeviationInitial);
		fDistractionDelayTime = new FloatDistribution(3, 0.5f);
		vCrowdArea = new Vector3Distribution(
				0, 3,
				0, 0,
				5, 1
				);
		   
    	new Timer(this, fRockDelayTime.GetRandom(), "NewRock", fInitialSafeTime);
    	new Timer(this, fDistractionDelayTime.GetRandom(), "NewDistraction", fInitialSafeTime);
		new Timer(this, fDifficultyRampTime, "RampDifficulty", fInitialSafeTime, true);
		
		lUptime = SystemClock.uptimeMillis();
		iGamestate = GameState.RUNNING;
	}
	private void samuelHit() {
		if(iGamestate == GameState.RUNNING) {
			iGamestate = GameState.LOSING;
			Timer.Drop();
			HighScoresManager.addScore(iDifficulty, iScore);
			new Timer(this, fLossWait, "GoToScores");
		}
	}
	private void rampDifficulty() {
		if(fRockDelayTime.Mean() > fRockDelayMeanFinal) {
    		fRockDelayTime.ShiftMean(fRockDelayMeanDecay);
    	}
    	if(fRockDelayTime.StandardDeviation() > fRockDelayDeviationFinal) {
    		fRockDelayTime.ShiftStandardDeviation(fRockDelayDeviationDecay);
    	}
	}
	private void newRock() {
		launchRock(new Vector3(
				Samuel.Left() + (float)(Math.random() * 0.5 + 0.5) * Samuel.Width(),
				Samuel.Bottom() + (float)(Math.random() * 0.5 + 0.5) * Samuel.Height(),
				0),
				true
		);   
    	new Timer(this, fRockDelayTime.GetRandom(), "NewRock");
	}
	private void newDistraction() {
    	launchRock(new Vector3(
    					Samuel.Left() + (float)Math.random() * 6 * Samuel.Width() - 3 * Samuel.Width(),
    					Samuel.Bottom() - ((float)Math.random() + 1) * Samuel.Height(),
    					0),
    					false
    			);
    	new Timer(this, fDistractionDelayTime.GetRandom(), "NewDistraction");
	}
	private void onResume() {
		bHasFocus = true;
        lUptime = SystemClock.uptimeMillis();
        if(iGamestate == GameState.GAMEOVER)
        	newGame();
	}
	private void onPause() {
		bHasFocus = false;
	}
}
