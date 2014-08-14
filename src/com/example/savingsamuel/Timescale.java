package com.example.savingsamuel;

public class Timescale {
	private static float fTimescale = 1, fTarget = 1, fRate = 0;
	
	public static float value() { return fTimescale; }
	public static void Reset() {
		fTimescale = 1;
		fTarget = 1;
		fRate = 0;
	}
	public static void Pause() {
		fTarget = 0;
		fTimescale = 0;
	}
	public static void Unpause() {
		fTarget = 1;
		fRate = (fTarget - fTimescale);
	}
	public static void Update(float elapsed) {
		if(fTimescale == fTarget)
			return;
		if(fTimescale < fTarget) {
			if(fRate < 0) {
				fTimescale = fTarget;
			} else {
				float step = fRate * elapsed;
				fTimescale = (fTimescale + step > fTarget) ? fTarget : fTimescale + step; 
			}
		} else {
			if(fRate > 0) {
				fTimescale = fTarget;
			} else {
				float step = fRate * elapsed;
				fTimescale = (fTimescale + step < fTarget) ? fTarget : fTimescale + step; 
			}
		}
	}
}
