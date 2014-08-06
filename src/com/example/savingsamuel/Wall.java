package com.example.savingsamuel;

import android.opengl.Matrix;


public class Wall {
	private static final float _top = 20f;
	private static float _width = 1;
	private static float[] _mMVPMatrix = new float[16];
    private static final float _vertices[] = {
	    -0.5f,  _top, 0.0f,	// top left
		0.96f, 0.78f, 0.45f, 1.0f,
	    -0.5f, 0f, 0.0f,	// bottom left
	    0.94f, 0.66f, 0.18f, 1.0f,
	    0.5f, 0f, 0.0f, 	// bottom right
	    0.94f, 0.66f, 0.18f, 1.0f,
	 	0.5f,  _top, 0.0f,	// top right
	 	0.96f, 0.78f, 0.45f, 1.0f
	 	};

    private static final short _drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh _mesh;
    
    public static void setAspectRatio(float aspectRatio) {
    	_width = 2 * aspectRatio * GameStateManager.CameraPosition().z;
    	float[] mModel = new float[16];
    	Matrix.setIdentityM(mModel, 0);
    	Matrix.scaleM(mModel, 0, _width, 1, 1);
    	Matrix.multiplyMM(_mMVPMatrix, 0, MyRenderer.mVPMatrix(), 0, mModel, 0);
    }
    
    public static float Top() {
    	return _top;
    }

    public static void Load() {
        _mesh = new ColoredMesh(_vertices, _drawOrder);
    }
    
    public static void draw() {
        _mesh.draw(_mMVPMatrix, Shader.VaryingColor());
    }
}
