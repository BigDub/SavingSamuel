package com.example.savingsamuel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class MyRenderer implements GLSurfaceView.Renderer {
	private static int _width, _height;
	public static int Width() {
		return _width;
	}
	public static int Height() {
		return _height;
	}
	private static float[] _mProjectionMatrix = new float[16], 
			_mViewMatrix = new float[16], 
			_mVPMatrix = new float[16];
	
	public static float[] mVPMatrix() {
		return _mVPMatrix;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.725f, 0.913f, 1.0f, 1.0f);
		GLES20.glClearDepthf(1f);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        Matrix.setLookAtM(_mViewMatrix, 0, 
        		GameStateManager.CameraPosition().x,
        		GameStateManager.CameraPosition().y,
        		GameStateManager.CameraPosition().z,
        		0f, 17f, 0f, // Target
        		0f, 1f, 0f // Up
        		);
        Wall.Load();
        Samuel.Load();
        Rock.Load();
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		GameStateManager.Instance().update();
		
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_ALPHA_BITS);
        Projectile.drawPre();
        Wall.draw();
        Samuel.draw();
        Projectile.drawPost();
        GLES20.glDisable(GLES20.GL_ALPHA_BITS);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		_width = width;
		_height = height;
        GLES20.glViewport(0, 0, width, height);

        float aspectRatio = (float) width / height;
        
        Matrix.frustumM(_mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1, 1, 50);
        //Matrix.perspectiveM(mProjectionMatrix, 0, 90, aspectRatio, 1, 11);
        Matrix.multiplyMM(_mVPMatrix, 0, _mProjectionMatrix, 0, _mViewMatrix, 0);
	}
}
