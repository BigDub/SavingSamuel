package com.example.savingsamuel;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioManager {
	private static Context _context;
	private static AudioManager _instance;
	private static boolean _mute = false;
	
	public static void updateMute(boolean mute) {
		_mute = mute;
	}

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
		if(_mute)
			return;
		_instance.playSound(0);
	}
	public static void playSlap() {
		if(_mute)
			return;
		int n = (int)(Math.random() * 2.99f);
		_instance.playSound(n + 1);
	}
	public static void playImpact() {
		if(_mute)
			return;
		int n = (int)(Math.random() * 2.99f);
		_instance.playSound(n + 4);
	}
	
	private void playSound(int n) {
		if(_mediaPlayers[n].isPlaying()) {
			_mediaPlayers[n].stop();
			try {
				_mediaPlayers[n].prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_mediaPlayers[n].start();
	}
}
