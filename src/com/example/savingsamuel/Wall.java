package com.example.savingsamuel;

import android.opengl.Matrix;


public class Wall {
	private static final float TOP = 20f;
	private static float fWidth = 1;
	private static float[] mMVPMatrix = new float[16];
    private static final float fVertices[] = {
	    -0.5f,  TOP, 0.0f,	// top left
		0.757f, 0.792f, 0.769f, 1.0f,
	    -0.5f, 0f, 0.0f,	// bottom left
	    0.310f, 0.443f, 0.376f, 1.0f,
	    0.5f, 0f, 0.0f, 	// bottom right
	    0.310f, 0.443f, 0.376f, 1.0f,
	 	0.5f,  TOP, 0.0f,	// top right
	 	0.757f, 0.792f, 0.769f, 1.0f
	 	};

    private static final short sDrawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh mMesh;
    
    public static void setAspectRatio(float aspectRatio) {
    	fWidth = 2 * aspectRatio * GameStateManager.CameraPosition().z;
    	float[] mModel = new float[16];
    	Matrix.setIdentityM(mModel, 0);
    	Matrix.scaleM(mModel, 0, fWidth, 1, 1);
    	Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mVPMatrix(), 0, mModel, 0);
    }
    
    public static float Top() {
    	return TOP;
    }

    public static void Load() {
        mMesh = new ColoredMesh(fVertices, sDrawOrder);
    }
    
    public static void Draw() {
        mMesh.Draw(mMVPMatrix, Shader.VaryingColor());
    }
}
