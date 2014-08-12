package com.example.savingsamuel;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class NumberWriter {
	private static final float fCharWidth = 32f, fCharHeight = 49f;
	private static float fVertices[] = {
	    0f,  fCharHeight, 0.0f,	// top left
		0f, 0f,
	    0f, 0f, 0.0f,	// bottom left
	    0f, 1f,
	    fCharWidth, 0f, 0.0f, 	// bottom right
	    0.1f, 1f,
	 	fCharWidth,  fCharHeight, 0.0f,	// top right
	 	0.1f, 0f
	 	};

    private static short sDrawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh mMesh;
    
    public static void Load() {        
        mMesh = new TexturedMesh(fVertices, sDrawOrder, Texture.loadTexture("numbers"));
    }
    
    public static void DrawNumber(int number) {
    	String str = String.valueOf(number);
    	for(int i = 0; i < str.length(); i++) {
    		drawCharacter(str.charAt(i) - 48, i);
    	}
    }
    private static void drawCharacter(int number, int position) {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, position * fCharWidth, 0, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mOrthoMatrix(), 0, mWorldMatrix, 0);
        
        GLES20.glUseProgram(Shader.Numbers().Program());
        int hOffset = Shader.Numbers().getUniform("offset");
        GLES20.glUniform1f(hOffset, number * 0.1f);
        
    	mMesh.Draw(mMVPMatrix, Shader.Numbers());
    }
}
