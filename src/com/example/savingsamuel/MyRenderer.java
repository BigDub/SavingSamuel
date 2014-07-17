package com.example.savingsamuel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.*;

public class MyRenderer implements GLSurfaceView.Renderer {
	
	private float _r = 1.0f, _g = 0.0f, _b = 0.0f;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(_r, _g, _b, 1.0f);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {		
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
	}

	public void setClearColor(float r, float g, float b) {
		_r = r;
		_g = g;
		_b = b;
	}
}
