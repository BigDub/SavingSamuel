package com.example.savingsamuel;

import android.opengl.GLES20;
import android.opengl.Matrix;


public class Samuel {
	private static Samuel sInstance;

	private static final float
		fHeight = 4f,
		fWidth = (148f / 256f) * 4f,
		fLeft = -0.5f * fWidth,
		fRight = fLeft + fWidth,
		fBottom = Wall.Top(),
		fTop = fBottom + fHeight;
    private static final float fVertices[] = new float[] {
    	    fLeft,  fHeight, 0.0f,	// top left
    		0f, 0f,
    	    fLeft, 0f, 0.0f,	// bottom left
    	    0f, 1f,
    	    fLeft + fWidth, 0f, 0.0f, 	// bottom right
    	    1f, 1f,
    	 	fLeft + fWidth, fHeight, 0.0f,	// top right
    	 	1f, 0f
    	 	};

    private static final short sDrawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh mMesh;
    private static final CollisionBox[] collisionBoxes = new CollisionBox[] {
    	new CollisionBox(new Vector3(0, 3.6f, 0), 0.5f, 0.5f),
    	new CollisionBox(new Vector3(0.1f, 2.4f, 0), 0.75f, 1.75f),
    	new CollisionBox(new Vector3(-0.35f, 0.75f, 0), 0.35f, 1.5f),
    	new CollisionBox(new Vector3(0.35f, 0.75f, 0), 0.35f, 1.5f),
    	new CollisionBox(new Vector3(-0.4f, 2.6f, 0), 0.3f, 0.70f),
    	new CollisionBox(new Vector3(0.6f, 3f, 0), 0.4f, 0.4f)
    };

	public static void Init() {
		sInstance = new Samuel();
	}
	public static Vector3 Position() { return sInstance.vPosition; }
    public static float Left() { return fLeft; }
    public static float Width() { return fWidth; }
    public static float Height() { return fHeight; }
    public static float Bottom() { return fBottom; }
    public static float Right() { return fHeight; }
    public static float Top() { return fTop; }
    public static void Load() {
    	Texture texture = Texture.loadTexture("samuel");        
        mMesh = new TexturedMesh(fVertices, sDrawOrder, texture);
    }
    public static void Draw() {
        sInstance.draw();
    }
    public static void Update(float elapsed) {
    	sInstance.update(elapsed);
    }
    public static void Reset() {
    	sInstance.reset();
    }
    public static void Knock(Vector3 incomingVelocity) {
    	if(sInstance.bFalling)
    		return;
    	sInstance.bFalling = true;
    	
		AudioManager.playWilhelm();
    	sInstance.vVelocity = Vector3.Scale(incomingVelocity, 0.2f);
    	sInstance.vVelocity.y = 0;
    	GameStateManager.SamuelHit();
    }
    public static boolean Warn(Vector3 position, float radius) {
    	if(sInstance.bFalling)
    		return false;
    	Vector3 nearest = new Vector3(position.x, position.y, 0);
    	if(position.x < fLeft) {
			nearest.x = fLeft;
    	} else if(position.x > fRight) {
    		nearest.x = fRight;
    	}
    	if(position.y < fBottom) {
    		nearest.y = fBottom;
    	} else if(position.y > fTop) {
    		nearest.y = fTop;
    	}
    	return Vector3.Subtract(position, nearest).Magnitude() <= radius;
    }
    public static boolean Hit(Vector3 position, float radius) {    	
    	if(Warn(position, radius)) {
    		Vector3 localspace = Vector3.Subtract(position, sInstance.vPosition);
    		for(CollisionBox cb : collisionBoxes) {
    			if(cb.Hit(localspace, radius))
    				return true;
    		}
    	}
    	return false;
    }
    
    private Vector3 vPosition, vVelocity;
    private boolean bFalling;
    
    private Samuel() {
    	reset();
    }
    
    private void reset() {
    	bFalling = false;
    	vVelocity = new Vector3(0);
    	vPosition = new Vector3(0, fBottom, 0);
    }
    
    private void update(float elapsed) {
    	if(!bFalling)
    		return;
    	vPosition.Add(Vector3.Scale(vVelocity, elapsed));
    	vVelocity.y -= 9.8f * elapsed;
    }
    
    private void draw() {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, vPosition.x, vPosition.y, vPosition.z);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mVPMatrix(), 0, mWorldMatrix, 0);
        
        GLES20.glUseProgram(Shader.Textured().Program());
    	mMesh.Draw(mMVPMatrix, Shader.Textured());
    	
    	GLES20.glUseProgram(Shader.UniformColor().Program());
    	int mColorHandle = Shader.UniformColor().getUniform("uColor");
    	GLES20.glUniform4f(mColorHandle, 1, 0, 0, 0.5f);
    	for(CollisionBox cb : collisionBoxes) {
    		cb.Draw(mMVPMatrix);
    	}
    }
    
    private static class CollisionBox {        
    	private Mesh mMesh;
    	private final float fLeft, fRight, fTop, fBottom;
    	private final float[] fVertices;
    	
    	public CollisionBox(Vector3 center, float width, float height) {
    		fLeft = center.x - width / 2;
    		fRight = center.x + width / 2;
    		fTop = center.y + height / 2;
    		fBottom = center.y - height / 2;
    		fVertices = new float[] {
    			fLeft, fTop, center.z,
    			fLeft, fBottom, center.z,
    			fRight, fBottom, center.z,
    			fRight, fTop, center.z
    		};
    		mMesh = new UniformColorMesh(fVertices, sDrawOrder);
    	}
    	
    	public void Draw(float[] matrix) {
    		mMesh.Draw(matrix, Shader.UniformColor());
    	}
    	
    	public boolean Hit(Vector3 position, float radius) {
    		Vector3 nearest = new Vector3(position.x, position.y, 0);
        	if(position.x < fLeft) {
    			nearest.x = fLeft;
        	} else if(position.x > fRight) {
        		nearest.x = fRight;
        	}
        	if(position.y < fBottom) {
        		nearest.y = fBottom;
        	} else if(position.y > fTop) {
        		nearest.y = fTop;
        	}
    		return Vector3.Subtract(position, nearest).Magnitude() <= radius;
    	}
    }
}
