package com.example.savingsamuel;

import java.util.Collections;
import java.util.Vector;

import android.opengl.Matrix;
import android.util.Log;


public abstract class Projectile {
	protected static Vector<Projectile> _projectiles, _preList, _postList;
	public static void Init() {
		_projectiles = new Vector<Projectile>();
		_preList = new Vector<Projectile>();
		_postList = new Vector<Projectile>();
	}
	public static void Update(float elapsed) {
		for(Projectile p : _projectiles)
			if(p._active)
				p._update(elapsed);
	}
	
	public static void Knock(float x, float y, float radius) {
		boolean hit = false;
		for(Projectile p : _projectiles) {
			if(!p._active || p._z < 0)
				continue;
			float dx = x - p._ssx;
			float dy = y - p._ssy;
			float diff = (float)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
			if(diff <= radius) {
				hit = true;
				p._vz -= 10f;
				p._vx *= 0.9f;
				p._vy *= 0.9f;
			}
		}
		if(hit)
			AudioManager.playSlap();
	}
	
    protected abstract Mesh mesh();
    private boolean _active;
    protected float
    	_r, _s,
    	_x, _y, _z,
    	_vx, _vy, _vz,
    	_ssx, _ssy;
    
    public Projectile() {
    	_active = false;
    }
    
    protected void _launch(float r, float s, float x, float y, float z, float vx, float vy, float vz) {
    	_active = true;
    	_r = r;
    	_s = s;
    	_x = x;
    	_y = y;
    	_z = z;
    	_vx = vx;
    	_vy = vy;
    	_vz = vz;
    }
    
    private void _update(float elapsed) {    	
    	_y += _vy * elapsed;
    	if (_y <= 0 && _vy <= 0) {
    		_active = false;
    		return;
    	}
    	if (_z > 0 && _vz < 0 && _z + _vz * elapsed <= 0 && _y < 20) {
    		//Impact with wall
    		AudioManager.playImpact();
    		_z = 0.1f;
    		_vz /= -4f;
    		_vx /= 2f;
    		_vy /= 2f;
    		_s /= -2f;
    	} else {
    		_z += _vz * elapsed;
    	}
    	_x += _vx * elapsed;
    	_vy -= 9.8f * elapsed;
    	_r += _s * elapsed;
        
        float[] test = new float[] { _x, _y, _z, 1 };
        float[] output = new float[4];
        Matrix.multiplyMV(output, 0, MyRenderer.mVPMatrix(), 0, test, 0);
        _ssx = ((output[0] / output[3]) + 1f) / 2f * MyRenderer.Width();
        _ssy = ((-output[1] / output[3]) + 1f) / 2f * MyRenderer.Height();
    }
    
    public static void drawPre() {
    	Vector<Projectile> removeList = new Vector<Projectile>(1);
    	for(Projectile p : _projectiles) {
    		if(p._active) {
    			if(p._z < 0) {
    				_preList.add(p);
    			} else {
    				_postList.add(p);
    			}
    		} else {
    			removeList.add(p);
    		}
    	}
    	for(Projectile p : removeList)
    		_projectiles.remove(p);
    	Collections.sort(_preList, ProjectileComparator.Instance());
    	Collections.sort(_postList, ProjectileComparator.Instance());
    	
    	for(Projectile p : _preList) {
    		p._draw();
    	}
    }
    public static void drawPost() {
    	for(Projectile p : _postList) {
    		p._draw();
    	}
    	_preList.clear();
    	_postList.clear();
    }
    
    private void _draw() {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, _x, _y, _z);
        Matrix.rotateM(mWorldMatrix, 0, _r, 0f, 0f, 1f);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mVPMatrix(), 0, mWorldMatrix, 0);
        mesh().draw(mMVPMatrix);
    }
}
