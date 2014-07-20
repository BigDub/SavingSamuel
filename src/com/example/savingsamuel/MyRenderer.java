package com.example.savingsamuel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

public class MyRenderer implements GLSurfaceView.Renderer {
	
	private float[] mProjectionMatrix = new float[16], 
			mViewMatrix = new float[16], 
			mVPMatrix = new float[16];
	
	private Wall wWall;
	private Samuel sSamuel;
	private Rock[] rRocks = new Rock[5];

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.725f, 0.913f, 1.0f, 1.0f);
		GLES20.glClearDepthf(1f);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        Matrix.setLookAtM(mViewMatrix, 0, 
        		0f, 15f, 10f, // Position
        		0f, 15f, 0f, // Target
        		0f, 1f, 0f // Up
        		);
        wWall = new Wall();
        sSamuel = new Samuel();
        for(int i = 0; i < 5; i++)
        	rRocks[i] = new Rock();
	}
	
    private long uptime = SystemClock.uptimeMillis();
	@Override
	public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);

        long nuptime = SystemClock.uptimeMillis();
        float elapsed = (float) (nuptime - uptime) / 1000f;
        uptime = nuptime;
        
        wWall.draw(mVPMatrix);
        sSamuel.draw(mVPMatrix);
        
        for(int i = 0; i < 5; i++) {
	        if(!rRocks[i].active()) {
	        	rRocks[i].Launch((i * 2) - 5, 0, 5, 
	        			((float)Math.random() * 5) - 2.5f, 
	        			(float)Math.random() * 30, 
	        			(float)Math.random() * -10f);
	        }
	        
	        rRocks[i].update(elapsed);

	        rRocks[i].draw(mVPMatrix);
        }
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float aspectRatio = (float) width / height;
        
        Matrix.frustumM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1, 1, 50);
        //Matrix.perspectiveM(mProjectionMatrix, 0, 90, aspectRatio, 1, 11);
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
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
