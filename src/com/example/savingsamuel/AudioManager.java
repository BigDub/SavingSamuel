package com.example.savingsamuel;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioManager {
	private static AudioManager aInstance;
	private static boolean bMute = false;
	
	public static void updateMute(boolean mute) {
		bMute = mute;
	}

	public static void Init() {
		aInstance = new AudioManager();
	}
	
	private MediaPlayer[] mMediaPlayers;
	
	public AudioManager() {
		Context cContext = GameStateManager.context();
		mMediaPlayers = new MediaPlayer[7];
		mMediaPlayers[0] = MediaPlayer.create(cContext, R.raw.wilhelm);
		mMediaPlayers[1] = MediaPlayer.create(cContext, R.raw.slap0);
		mMediaPlayers[2] = MediaPlayer.create(cContext, R.raw.slap1);
		mMediaPlayers[3] = MediaPlayer.create(cContext, R.raw.slap2);
		mMediaPlayers[4] = MediaPlayer.create(cContext, R.raw.impact0);
		mMediaPlayers[5] = MediaPlayer.create(cContext, R.raw.impact1);
		mMediaPlayers[6] = MediaPlayer.create(cContext, R.raw.impact2);
	}
	
	public static void playWilhelm() {
		if(bMute)
			return;
		aInstance.playSound(0);
	}
	public static void playSlap() {
		if(bMute)
			return;
		int n = (int)(Math.random() * 2.99f);
		aInstance.playSound(n + 1);
	}
	public static void playImpact() {
		if(bMute)
			return;
		int n = (int)(Math.random() * 2.99f);
		aInstance.playSound(n + 4);
	}
	
	private void playSound(int n) {
		if(mMediaPlayers[n].isPlaying()) {
			mMediaPlayers[n].stop();
			try {
				mMediaPlayers[n].prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mMediaPlayers[n].start();
	}
}
