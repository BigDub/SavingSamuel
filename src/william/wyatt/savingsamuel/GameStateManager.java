package william.wyatt.savingsamuel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.view.MotionEvent;

public class GameStateManager {
	private static boolean bInitialized = false;
	private static final Vector3 vCameraPosition = new Vector3(0, Wall.Top(), Wall.Top() / 2);
	private static Context cContext;

	private static float fRockDelayMeanX = 1.5f;
	private static final float 
		fInitialSafeTime = 1.5f,
		fLossWait = 5,
		fDifficultyRampTime = 1,
		fDifficultyRampLength = 60f / fDifficultyRampTime,
		
		fRockDelayMeanInitial = 2,
		fRockDelayMeanFinal = 0.5f,
		
		fRockDelayRangeInitial = 0.75f,
		fRockDelayDeviationFinal = 0.3f,
		fRockDelayDeviationDecay = (fRockDelayDeviationFinal - fRockDelayRangeInitial) / fDifficultyRampLength;
	private static GameStateManager gInstance = new GameStateManager();
	
	public static GameStateManager Instance() { return gInstance; }
	public static Vector3 CameraPosition() { return vCameraPosition; }
	public static void updatePreferences(SharedPreferences sharedPref) {
		AudioManager.updateMute(sharedPref.getBoolean("pref_mute", false));
		gInstance.iDifficulty = Integer.parseInt(sharedPref.getString("pref_dif", "1"));
		gInstance.bWarnEffect = sharedPref.getBoolean("pref_warn", true);
	}
	public static Context context() { return cContext; }
	public static void Init(Context context) {
		cContext = context;
		if(bInitialized)
			return;
		bInitialized = true;
        Projectile.Init();
        AudioManager.Init();
		Samuel.Init();
		gInstance.newGame();
	}
	public static void Load() {
        Shader.Load();
        Texture.Load();
        Wall.Load();
        Samuel.Load();
        Rock.Load();
        UserInterface.Load();
        Background.Load();
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
	public static int Score() { return gInstance.iScore; }
	public static void Update() {
		if(gInstance.bHasFocus)
			gInstance.update();
	}
	public static void SamuelHit() { gInstance.samuelHit(); }
	public static void NewGame() { gInstance.newGame(); }
	public static void GoToScores() {
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
	public static void SurfaceChange() {
		Wall.setAspectRatio();
		Background.setAspectRatio();
		UserInterface.Init();
	}
	public static void Pause() { gInstance.pause(); }
	public static void Unpause() { gInstance.unpause(); }
	public static void onTouchEvent(MotionEvent e) { gInstance.TouchEvent(e); }
	
	private long lUptime;
	private int iGamestate, iPrePauseGamestate, iDifficulty, iScore;
	// Lower numbers means harder difficulty
	private boolean bWarnEffect, bHasFocus;
	private FloatRange fRockFlightTime, fRockDelayTime, fDistractionDelayTime;
	private Vector3Range vCrowdArea;
	private float fCrowdOffset = 12;
	
	public GameStateManager() {}
	
	private Vector3 launchOrigin() {
		Vector3 origin = vCrowdArea.getRandom();
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
		float flightTime = fRockFlightTime.getRandom();
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
        float elapsed = ((nuptime - lUptime) / 1000f);
        lUptime = nuptime;
        
        UserInterface.Update(elapsed);
        Timescale.Update(elapsed);
        
        elapsed *= Timescale.value();
        
        if(iGamestate != GameState.PAUSED)
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
		Timescale.Reset();
		iScore = 0;
		fRockFlightTime = new FloatRange(3f, 0.3f);
		fRockDelayMeanX = fRockDelayMeanInitial - fRockDelayMeanFinal;
		fRockDelayTime = new FloatRange(fRockDelayMeanInitial, fRockDelayRangeInitial);
		fDistractionDelayTime = new FloatRange(3, 1.5f);
		vCrowdArea = new Vector3Range(
				0, 6,
				0, 0,
				5, 3
				);
		   
    	new Timer(this, fRockDelayTime.getRandom(), "NewRock", fInitialSafeTime);
    	new Timer(this, fDistractionDelayTime.getRandom(), "NewDistraction", fInitialSafeTime);
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
		fRockDelayMeanX *= 0.975f;
    	fRockDelayTime.setMean(fRockDelayMeanX + fRockDelayMeanFinal);
    	if(fRockDelayTime.range() > fRockDelayDeviationFinal) {
    		fRockDelayTime.shiftRange(fRockDelayDeviationDecay);
    	}
	}
	private void newRock() {
		launchRock(new Vector3(
				Samuel.Left() + (float)(Math.random() * 0.5 + 0.25) * Samuel.Width(),
				Samuel.Bottom() + (float)(Math.random() * 0.5 + 0.25) * Samuel.Height(),
				0),
				true
		);   
    	new Timer(this, fRockDelayTime.getRandom(), "NewRock");
	}
	private void newDistraction() {
    	launchRock(new Vector3(
    					Samuel.Left() + (float)Math.random() * 6 * Samuel.Width() - 3 * Samuel.Width(),
    					Samuel.Bottom() - ((float)Math.random() + 1) * Samuel.Height(),
    					0),
    					false
    			);
    	new Timer(this, fDistractionDelayTime.getRandom(), "NewDistraction");
	}
	private void onResume() {
		bHasFocus = true;
        lUptime = SystemClock.uptimeMillis();
        if(iGamestate == GameState.GAMEOVER)
        	newGame();
	}
	private void onPause() {
		bHasFocus = false;
		if(pause())
			UserInterface.onPause();
	}
	private boolean pause() {
		if(iGamestate != GameState.PAUSED &&
				iGamestate != GameState.GAMEOVER) {
			iPrePauseGamestate = iGamestate;
			iGamestate = GameState.PAUSED;
			Timescale.Pause();
			return true;
		}
		return false;
	}
	private void unpause() {
		if(iGamestate == GameState.PAUSED) {
			iGamestate = iPrePauseGamestate;
			Timescale.Unpause();
		}
	}
	private void TouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(iGamestate == GameState.RUNNING ||
					iGamestate == GameState.PAUSED ||
					iGamestate == GameState.LOSING) {
				Projectile.Knock(x, y);
			}
			UserInterface.TouchEvent(e);
			break;
		}
	}
}
