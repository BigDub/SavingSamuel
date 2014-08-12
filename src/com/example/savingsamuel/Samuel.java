package com.example.savingsamuel;

import android.opengl.GLES20;
import android.opengl.Matrix;


public class Samuel {
	private static Samuel sInstance;

	private static float
		fHeight,
		fWidth,
		fLeft,
		fBottom = Wall.Top();
    private static float fVertices[];

    private static final short sDrawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh mMesh;

	public static void Init() {
		sInstance = new Samuel();
	}
	public static Vector3 Position() { return sInstance.vPosition; }
    public static float Left() { return fLeft; }
    public static float Width() { return fWidth; }
    public static float Height() { return fHeight; }
    public static float Bottom() { return fBottom; }
    public static void Load() {
    	Texture texture = Texture.loadTexture("samuel");
    	
    	float scale = 4;
        fHeight = scale;
		fWidth = ((float)texture.Width() / (float)texture.Height()) * scale;
		fLeft = -0.5f * fWidth;
        fVertices = new float[] {
        	    fLeft,  fHeight, 0.0f,	// top left
        		0f, 0f,
        	    fLeft, 0f, 0.0f,	// bottom left
        	    0f, 1f,
        	    fLeft + fWidth, 0f, 0.0f, 	// bottom right
        	    1f, 1f,
        	 	fLeft + fWidth, fHeight, 0.0f,	// top right
        	 	1f, 0f
        	 	};
        
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
    public static boolean Hit(Vector3 position, float radius) {
    	return !sInstance.bFalling &&
		position.x + radius > fLeft &&
		position.x - radius < fLeft + fWidth &&
		position.y + radius > fBottom &&
		position.y - radius < fBottom + fHeight;
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
    }
}
