package com.example.savingsamuel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

public class MyRenderer implements GLSurfaceView.Renderer {
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
        		0f, 15f, 10f, // Position
        		0f, 15f, 0f, // Target
        		0f, 1f, 0f // Up
        		);
        Wall.Init();
        Samuel.Init();
        Projectile.Init();
	}
	
    private long uptime = SystemClock.uptimeMillis();
    private float timer = 0;
	@Override
	public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_ALPHA_BITS);

        long nuptime = SystemClock.uptimeMillis();
        float elapsed = (float) (nuptime - uptime) / 1000f;
        uptime = nuptime;
        
        timer += elapsed;
        
        if(timer >= 0.05f) {
        	timer = 0;
        	Rock.Launch(
        			(float)Math.random() * 360f,
        			(float)Math.random() * 720f - 360f,
        			(float)Math.random() * 20f - 10f,
        			0, 
        			(float)Math.random() * 10, 
        			(float)Math.random() * 5f - 2.5f, 
        			(float)Math.random() * 20f + 10f,
        			(float)Math.random() * -5f - 2f
        			);
        }
        
        Projectile.Update(elapsed);
        
        Projectile.drawPre();
        Wall.draw();
        Samuel.draw();
        Projectile.drawPost();
        GLES20.glDisable(GLES20.GL_ALPHA_BITS);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float aspectRatio = (float) width / height;
        
        Matrix.frustumM(_mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1, 1, 50);
        //Matrix.perspectiveM(mProjectionMatrix, 0, 90, aspectRatio, 1, 11);
        Matrix.multiplyMM(_mVPMatrix, 0, _mProjectionMatrix, 0, _mViewMatrix, 0);
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
