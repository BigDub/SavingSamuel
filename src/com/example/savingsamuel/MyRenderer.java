package com.example.savingsamuel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class MyRenderer implements GLSurfaceView.Renderer {
	
	private float[] mProjectionMatrix = new float[16], 
			mViewMatrix = new float[16], 
			mVPMatrix = new float[16];
	private float _r = 0.0f, _g = 0.0f, _b = 0.0f;
	private Square sSquare;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(_r, _g, _b, 1.0f);
        Matrix.setLookAtM(mViewMatrix, 0, 
        		0f, 0f, -3f, // Position
        		0f, 0f, 0f, // Target
        		0f, 1f, 0f // Up
        		);
        sSquare = new Square();
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {		
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        sSquare.draw(mVPMatrix);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float aspectRatio = (float) width / height;
        
        Matrix.frustumM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1, 3, 7);
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}

	public void setClearColor(float r, float g, float b) {
		_r = r;
		_g = g;
		_b = b;
	}
	
	public static int loadShader(int type, String shaderCode){

	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
}
