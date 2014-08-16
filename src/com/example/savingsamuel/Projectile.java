package com.example.savingsamuel;

import java.util.Collections;
import java.util.Vector;

import android.opengl.GLES20;
import android.opengl.Matrix;


public abstract class Projectile {
	private static float fEffectTimer = 0, fWarnPercent;
	private static int iPendingKnock = 0;
	private static float fKnockX, fKnockY;
	private static Vector3 vWarningTint = new Vector3(1, 0.5f, 0.5f);
	
	protected static Vector<Projectile> vProjectiles, vPreList, vMidList, vPostList;
	
	public static void Init() {
		vProjectiles = new Vector<Projectile>();
		vPreList = new Vector<Projectile>();
		vMidList = new Vector<Projectile>();
		vPostList = new Vector<Projectile>();
	}
	public static void Update(float elapsed) {
		boolean hit = false;
		// Prevents asynchronous iteration of the projectile list which can cause fatal errors.
		if(iPendingKnock == 1)
			iPendingKnock = 2;
		for(Projectile p : vProjectiles) {
			if(p.bActive) {
				if(iPendingKnock > 0) {
					float dx = fKnockX - p.fScreenX;
					float dy = fKnockY - p.fScreenY;
					float diff = (float)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
					if(diff <= p.fScreenRad * GameStateManager.TapScale()) {
						hit = true;
						if(!p.bTapped) {
							GameStateManager.AddPoint();
							p.bTapped = true;
						}
						
						Vector3 push = Vector3.Subtract(p.vPosition, GameStateManager.CameraPosition());
						//push.y = 0;
						p.fSpin *= 0.75f;
						p.vVelocity.Scale(0.9f).Add(push.Normalize().Scale(6));
						p.updateEffects();
					}
				}
				p.update(elapsed);
			}
		}
		if(hit)
			AudioManager.playSlap();
		iPendingKnock = 0;
        fEffectTimer += elapsed;
        
        if(fEffectTimer >= Math.PI * 2)
        	fEffectTimer -= Math.PI * 2;
	}
	public static void Knock(float x, float y) {
		if(iPendingKnock != 2) {
			iPendingKnock = 1;
			fKnockX = x;
			fKnockY = y;
		}
	}	
	public static void DrawPre() {
    	Vector<Projectile> removeList = new Vector<Projectile>(1);
		float z = Samuel.Position().z;
		boolean mid = (z != 0);
    	for(Projectile p : vProjectiles) {
    		if(p.bActive) {
    			if(!mid) {
	    			if(p.vPosition.z < 0) {
	    				vPreList.add(p);
	    			} else {
	    				vPostList.add(p);
	    			}
    			} else {
    				if(p.vPosition.z < z) {
    					vPreList.add(p);
    				} else if(p.vPosition.z < 0) {
    					vMidList.add(p);
    				} else {
    					vPostList.add(p);
    				}
    			}
    		} else {
    			removeList.add(p);
    		}
    	}
    	for(Projectile p : removeList)
    		vProjectiles.remove(p);
    	Collections.sort(vPreList, ProjectileComparator.Instance());
    	Collections.sort(vMidList, ProjectileComparator.Instance());
    	Collections.sort(vPostList, ProjectileComparator.Instance());
    	
    	if(GameStateManager.WarnEffect()) {
    		fWarnPercent = (float)(Math.cos(fEffectTimer * Math.PI * 4) / 2d + 0.5d);
    	} else {
    		fWarnPercent = 0;
    	}
        
    	
    	for(Projectile p : vPreList) {
    		p.draw();
    	}
    }
	public static void DrawMid() {
    	for(Projectile p : vMidList) {
    		p.draw();
    	}
	}
    public static void DrawShadow() {
    	for(Projectile p : vPostList) {
    		p.drawShadow();
    	}
    }
    public static void DrawPost() {
    	for(Projectile p : vPostList) {
    		p.draw();
    	}
    	vPreList.clear();
    	vMidList.clear();
    	vPostList.clear();
    }
    public static void Reset() {
    	Init();
    }
    
    private boolean bActive, bWarnOn, bTapped;
    
    protected float fRotation, fSpin, fScreenX, fScreenY, fScreenRad;
    protected Vector3 vScale, vPosition, vVelocity, vTint;
    
    public Projectile() {
    	bActive = false;
    	bWarnOn = false;
    	bTapped = false;
    	vPosition = new Vector3();
    	vVelocity = new Vector3();
    }

    private void update(float elapsed) {
    	if (vPosition.y <= 0 && vVelocity.y <= 0) {
    		bActive = false;
    		return;
    	}
    	float colrad = collisionRadius();
    	if (vPosition.z > 0 && vVelocity.z < 0 && vPosition.z - colrad + vVelocity.z * elapsed <= 0) {
    		float wall = Wall.Top();
    		if(Samuel.Hit(vPosition, colrad)) {
    			Samuel.Knock(vVelocity);
	    		AudioManager.playImpact();
    			vVelocity = Vector3.Bounce(vVelocity,
    					Vector3.Subtract(vPosition,
    							new Vector3(
    									Samuel.Left() + 0.5f * Samuel.Width(),
    									Samuel.Bottom() + 0.5f * Samuel.Height(),
    									0
    									)).Normal()).Scale(0.2f);
	    		fSpin /= -1.5f;
	    		bWarnOn = false;
    		}
    		if(vPosition.y <= wall) {
	    		//Impact with wall
	    		AudioManager.playImpact();
	    		vPosition.z = colrad;
	    		vVelocity = Vector3.Bounce(vVelocity, Vector3.UnitZ()).Scale(0.2f);
	    		fSpin /= -2f;
	    		bWarnOn = false;
    		} else if(vPosition.y - colrad <= wall) {
    			//Impact with top of the wall
    			Vector3 normal = new Vector3(
    					0,
    					vPosition.y - wall,
    					vPosition.z
    					).Normalize();
	    		AudioManager.playImpact();
	    		vPosition = Vector3.Scale(normal, colrad).Add(new Vector3(vPosition.x, wall, 0));
	    		vVelocity = Vector3.Bounce(vVelocity, Vector3.UnitZ()).Scale(0.2f);
	    		fSpin /= -2f;
	    		updateEffects();
    		}
    	}
		vPosition.Add(Vector3.Scale(vVelocity, elapsed));
    	vVelocity.y -= 9.8f * elapsed;
    	fRotation += fSpin * elapsed;
        
        float[] test = new float[] { vPosition.x, vPosition.y, vPosition.z, 1 };
        float[] output = new float[4];
        Matrix.multiplyMV(output, 0, MyRenderer.mVPMatrix(), 0, test, 0);
        fScreenX = ((output[0] / output[3]) + 1f) / 2f * MyRenderer.Width();
        fScreenY = ((-output[1] / output[3]) + 1f) / 2f * MyRenderer.Height();
        
        test = new float[] { vPosition.x + colrad, vPosition.y, vPosition.z, 1 };
        Matrix.multiplyMV(output, 0, MyRenderer.mVPMatrix(), 0, test, 0);
        fScreenRad = (((output[0] / output[3]) + 1f) / 2f * MyRenderer.Width()) - fScreenX;
    }   
    private void updateEffects() {
    	if(GameStateManager.WarnEffect()) {
    		bWarnOn = onTarget();
    	} else {
    		bWarnOn = false;
    	}
    }
    private boolean onTarget() {
    	if(vVelocity.z >= 0 || vPosition.z < 0)
    		return false;
    	float colRad = collisionRadius(),
    			flightTime = vPosition.z / -vVelocity.z,
    			projectedX = vPosition.x + vVelocity.x * flightTime,
    			projectedY = vPosition.y + vVelocity.y * flightTime + (-9.8f / 2f) * flightTime * flightTime;
    	
    	return Samuel.Warn(new Vector3(projectedX, projectedY, 0), colRad);
    }
    private Vector3 getTint() {
    	if(!bWarnOn)
    		return vTint;
    	return Vector3.Add(Vector3.Scale(vWarningTint, fWarnPercent), Vector3.Scale(vTint, 1 - fWarnPercent));
    }
    private void draw() {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, vPosition.x, vPosition.y, vPosition.z);
        Matrix.rotateM(mWorldMatrix, 0, fRotation, 0f, 0f, 1f);
        Matrix.scaleM(mWorldMatrix, 0, vScale.x, vScale.y, vScale.z);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mVPMatrix(), 0, mWorldMatrix, 0);
    	
        Vector3 tint = getTint();
        GLES20.glUseProgram(Shader.TintedTexture().Program());
        GLES20.glUniform4f(
        		Shader.TintedTexture().getUniform("uTint"),
        		tint.x, tint.y, tint.z, 1f);
    	mesh().Draw(mMVPMatrix, Shader.TintedTexture());
    }    
    private void drawShadow() {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        float offset = 1.4142135623f * vScale.y;
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, vPosition.x, vPosition.y - vPosition.z, 0f);
        Matrix.scaleM(mWorldMatrix, 0, vScale.x, offset, vScale.z);
        Matrix.rotateM(mWorldMatrix, 0, fRotation, 0f, 0f, 1f);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mVPMatrix(), 0, mWorldMatrix, 0);

        GLES20.glUseProgram(Shader.Shadow().Program());
        GLES20.glUniform4f(
        		Shader.Shadow().getUniform("uTint"),
        		0, 0, 0, 0.2f);
        mesh().Draw(mMVPMatrix, Shader.Shadow());
    }
    
    protected abstract Mesh mesh();
    protected abstract float collisionRadius();
    protected void launch(float rotation, float spin, Vector3 scale, Vector3 position, Vector3 velocity, boolean warn) {
    	bActive = true;
    	fRotation = rotation;
    	fSpin = spin;
    	vScale = scale;
    	vPosition = position;
    	vVelocity = velocity;
    	
    	if(GameStateManager.WarnEffect()) {
    		bWarnOn = warn;
    	} else {
    		bWarnOn = false;
    	}
    }
}
