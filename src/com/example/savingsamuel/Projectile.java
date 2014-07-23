package com.example.savingsamuel;

import java.util.Collections;
import java.util.Vector;

import android.opengl.Matrix;


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
	
    protected abstract Mesh mesh();
    private boolean _active;
    protected float
    	_r, _s,
    	_x, _y, _z,
    	_vx, _vy, _vz;
    
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
