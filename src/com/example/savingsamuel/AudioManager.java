package com.example.savingsamuel;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioManager {
	private static Context _context;
	private static AudioManager _instance;

	public static void Init(Context context) {
		_context = context;
		_instance = new AudioManager();
	}
	
	private MediaPlayer[] _mediaPlayers;
	
	public AudioManager() {
		_mediaPlayers = new MediaPlayer[7];
		_mediaPlayers[0] = MediaPlayer.create(_context, R.raw.wilhelm);
		_mediaPlayers[1] = MediaPlayer.create(_context, R.raw.slap0);
		_mediaPlayers[2] = MediaPlayer.create(_context, R.raw.slap1);
		_mediaPlayers[3] = MediaPlayer.create(_context, R.raw.slap2);
		_mediaPlayers[4] = MediaPlayer.create(_context, R.raw.impact0);
		_mediaPlayers[5] = MediaPlayer.create(_context, R.raw.impact1);
		_mediaPlayers[6] = MediaPlayer.create(_context, R.raw.impact2);
	}
	
	public static void playWilhelm() {
		_instance._mediaPlayers[0].start();
	}
	public static void playSlap() {
		int n = (int)(Math.random() * 2.99f);
		_instance.playSlap(n);
	}
	public static void playImpact() {
		int n = (int)(Math.random() * 2.99f);
		_instance.playImpact(n);
	}
	
	private void playSlap(int n) {
		_mediaPlayers[n + 1].start();
	}
	private void playImpact(int n) {
		_mediaPlayers[n + 4].start();
	}
}
