package com.example.savingsamuel;

import android.opengl.Matrix;


public abstract class Projectile {        
    protected abstract Mesh mesh();
    private boolean _active;
    private float _x, _y, _z, _vx, _vy, _vz;
    
    public boolean active() {
    	return _active;
    }
    
    public Projectile() {
    	_active = false;
    }
    
    public void Launch(float x, float y, float z, float vx, float vy, float vz) {
    	_active = true;
    	_x = x;
    	_y = y;
    	_z = z;
    	_vx = vx;
    	_vy = vy;
    	_vz = vz;
    }
    
    public void update(float elapsed) {
    	if (!_active)
    		return;
    	
    	_y += _vy * elapsed;
    	if (_y <= 0 && _vy <= 0) {
    		_active = false;
    		return;
    	}
    	if (_z > 0 && _z + _vz * elapsed <= 0 && _y < 20) {
    		_z = 0.1f;
    		_vz = 0;
    		_vx /= 2f;
    		_vy /= 2f;
    	} else {
    		_z += _vz * elapsed;
    	}
    	_x += _vx * elapsed;
    	_vy -= 9.8f * elapsed;
    }
    
    public void draw(float[] mVPMatrix) {
    	if (!_active)
    		return;
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, _x, _y, _z);
        //Matrix.rotateM(mWorldMatrix, 0, angle * 10f, 0f, 0f, 1f);
        Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, mWorldMatrix, 0);
        mesh().draw(mMVPMatrix);
    }
}
