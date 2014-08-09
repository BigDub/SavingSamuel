package com.example.savingsamuel;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class NumberWriter {
	private static final float _charWidth = 32f, _charHeight = 49f;
	private static float _vertices[] = {
	    0f,  _charHeight, 0.0f,	// top left
		0f, 0f,
	    0f, 0f, 0.0f,	// bottom left
	    0f, 1f,
	    _charWidth, 0f, 0.0f, 	// bottom right
	    0.1f, 1f,
	 	_charWidth,  _charHeight, 0.0f,	// top right
	 	0.1f, 0f
	 	};

    private static short _drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh _mesh;
    
    public static void Load() {        
        _mesh = new TexturedMesh(_vertices, _drawOrder, Texture.loadTexture("numbers"));
    }
    
    public static void DrawNumber(int number) {
    	String str = String.valueOf(number);
    	for(int i = 0; i < str.length(); i++) {
    		_drawCharacter(str.charAt(i) - 48, i);
    	}
    }
    private static void _drawCharacter(int number, int position) {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, position * _charWidth, 0, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mOrthoMatrix(), 0, mWorldMatrix, 0);
        
        GLES20.glUseProgram(Shader.Numbers().Program());
        int hOffset = Shader.Numbers().getUniform("offset");
        GLES20.glUniform1f(hOffset, number * 0.1f);
        
    	_mesh.Draw(mMVPMatrix, Shader.Numbers());
    }
}
