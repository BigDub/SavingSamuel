package com.example.savingsamuel;

import java.util.Collections;
import java.util.Vector;

import android.opengl.GLES20;
import android.opengl.Matrix;


public abstract class Projectile {
	protected static Vector<Projectile> _projectiles, _preList, _postList;
	private static float _effectTimer = 0, _tint;
	private boolean _effectOn;
	private static int _pendingKnock = 0;
	private static float _knockX, _knockY, _knockRadius;
	public static void Init() {
		_projectiles = new Vector<Projectile>();
		_preList = new Vector<Projectile>();
		_postList = new Vector<Projectile>();
	}
	public static void Update(float elapsed) {
		boolean hit = false;
		// Prevents asynchronous iteration of the projectile list which can cause fatal errors.
		if(_pendingKnock == 1)
			_pendingKnock = 2;
		for(Projectile p : _projectiles) {
			if(p._active) {
				if(_pendingKnock > 0) {
					float dx = _knockX - p._screenX;
					float dy = _knockY - p._screenY;
					float diff = (float)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
					if(diff <= _knockRadius) {
						hit = true;
						Vector3 push = Vector3.Subtract(p._position, GameStateManager.CameraPosition());
						//push.y = 0;
						p._spin *= 0.75f;
						p._velocity.Scale(0.9f).Add(push.Normalize().Scale(6));
						p._effectOn = p._onTarget();
					}
				}
				p._update(elapsed);
			}
		}
		if(hit)
			AudioManager.playSlap();
		_pendingKnock = 0;
        _effectTimer += elapsed;
        
        if(_effectTimer >= Math.PI * 2)
        	_effectTimer -= Math.PI * 2;
	}
	
	public static void Knock(float x, float y, float radius) {
		if(_pendingKnock != 2) {
			_pendingKnock = 1;
			_knockX = x;
			_knockY = y;
			_knockRadius = radius;
		}
	}
	
    protected abstract Mesh mesh();
    protected abstract float collisionRadius();
    private boolean _active;
    protected float
    	_rotation, _spin,
    	_screenX, _screenY;
    protected Vector3
    	_scale,
    	_position,
    	_velocity;
    
    public Projectile() {
    	_active = false;
    	_effectOn = false;
    	_position = new Vector3();
    	_velocity = new Vector3();
    }
    
    protected void _launch(float rotation, float spin, Vector3 scale, Vector3 position, Vector3 velocity) {
    	_active = true;
    	_rotation = rotation;
    	_spin = spin;
    	_scale = scale;
    	_position = position;
    	_velocity = velocity;
    	
    	//dependent on other values
    	_effectOn = _onTarget();
    }
    
    private void _update(float elapsed) {
    	if (_position.y <= 0 && _velocity.y <= 0) {
    		_active = false;
    		return;
    	}
    	float colrad = collisionRadius();
    	if (_position.z > 0 && _velocity.z < 0 && _position.z + _velocity.z * elapsed <= 0) {
    		float wall = Wall.Top();
    		if(
    				_position.x + colrad >= Samuel.Left() &&
    				_position.x - colrad <= Samuel.Left() + Samuel.Width() &&
    				_position.y >= wall &&
    				_position.y - colrad <= wall + Samuel.Height()
    				) {
    			AudioManager.playWilhelm();
    		}
    		if(_position.y <= wall) {
	    		//Impact with wall
	    		AudioManager.playImpact();
	    		_position.z = colrad;
	    		_velocity = Vector3.Bounce(_velocity, Vector3.UnitZ()).Scale(0.2f);
	    		_spin /= -2f;
	    		_effectOn = false;
    		} else if(_position.y - colrad <= wall) {
    			//Impact with top of the wall
    			Vector3 normal = new Vector3(
    					0,
    					_position.y - wall,
    					_position.z
    					).Normalize();
	    		AudioManager.playImpact();
	    		_position = Vector3.Scale(normal, colrad).Add(new Vector3(_position.x, wall, 0));
	    		_velocity = Vector3.Bounce(_velocity, Vector3.UnitZ()).Scale(0.2f);
	    		_spin /= -2f;
	    		_effectOn = _onTarget();
    		}
    	}
		_position.Add(Vector3.Scale(_velocity, elapsed));
    	_velocity.y -= 9.8f * elapsed;
    	_rotation += _spin * elapsed;
        
        float[] test = new float[] { _position.x, _position.y, _position.z, 1 };
        float[] output = new float[4];
        Matrix.multiplyMV(output, 0, MyRenderer.mVPMatrix(), 0, test, 0);
        _screenX = ((output[0] / output[3]) + 1f) / 2f * MyRenderer.Width();
        _screenY = ((-output[1] / output[3]) + 1f) / 2f * MyRenderer.Height();
    }
    
    public static void drawPre() {
    	Vector<Projectile> removeList = new Vector<Projectile>(1);
    	for(Projectile p : _projectiles) {
    		if(p._active) {
    			if(p._position.z < 0) {
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
    	
        _tint = (float)(Math.cos(_effectTimer * Math.PI * 4) / 4.0d + 0.75d);
        GLES20.glUniform4f(
        		GLES20.glGetUniformLocation(Shader.TintedTexture().Program(), "uTint"),
        		1f, _tint, _tint, 1f);
    	
    	for(Projectile p : _preList) {
    		p._draw();
    	}
    }
    public static void drawShadow() {
    	for(Projectile p : _postList) {
    		p._drawShadow();
    	}
    }
    public static void drawPost() {
    	for(Projectile p : _postList) {
    		p._draw();
    	}
    	_preList.clear();
    	_postList.clear();
    }
    
    private boolean _onTarget() {
    	if(_velocity.z >= 0 || _position.z < 0)
    		return false;
    	float colRad = collisionRadius(),
    			flightTime = _position.z / -_velocity.z,
    			projectedX = _position.x + _velocity.x * flightTime,
    			projectedY = _position.y + _velocity.y * flightTime + (-9.8f / 2f) * flightTime * flightTime;
    	
    	return (
    			projectedX + colRad >= Samuel.Left() - 0.5f * Samuel.Width() &&
    			projectedX - colRad <= Samuel.Left() + 1.5f * Samuel.Width() &&
    			projectedY + colRad >= Samuel.Bottom() //&&
    			//projectedY - colRad * 2 <= Samuel.Bottom() + Samuel.Height()
    			);
    }
    private void _draw() {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, _position.x, _position.y, _position.z);
        Matrix.rotateM(mWorldMatrix, 0, _rotation, 0f, 0f, 1f);
        Matrix.scaleM(mWorldMatrix, 0, _scale.x, _scale.y, _scale.z);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mVPMatrix(), 0, mWorldMatrix, 0);

        if(_effectOn)
        	mesh().draw(mMVPMatrix, Shader.TintedTexture());
        else
        	mesh().draw(mMVPMatrix, Shader.Textured());
    }
    
    private void _drawShadow() {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        float offset = 1.4142135623f * _scale.y;
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, _position.x, _position.y - _position.z, 0f);
        Matrix.scaleM(mWorldMatrix, 0, _scale.x, offset, _scale.z);
        Matrix.rotateM(mWorldMatrix, 0, _rotation, 0f, 0f, 1f);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mVPMatrix(), 0, mWorldMatrix, 0);
        mesh().draw(mMVPMatrix, Shader.Shadow());
    }
}
