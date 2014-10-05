package william.wyatt.savingsamuel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class MyRenderer implements GLSurfaceView.Renderer {
	private static int iWidth, iHeight;
	public static int Width() {
		return iWidth;
	}
	public static int Height() {
		return iHeight;
	}
	private static float[] mProjectionMatrix = new float[16], 
			mViewMatrix = new float[16], 
			mVPMatrix = new float[16],
			mOrthoMatrix = new float[16];
	
	public static float[] mVPMatrix() {
		return mVPMatrix;
	}
	public static float[] mOrthoMatrix() {
		return mOrthoMatrix;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.725f, 0.913f, 1.0f, 1.0f);
		GLES20.glClearDepthf(1f);
		GLES20.glClearStencil(0x00);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_ALPHA_BITS);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);
        Matrix.setLookAtM(mViewMatrix, 0, 
        		GameStateManager.CameraPosition().x,
        		GameStateManager.CameraPosition().y,
        		GameStateManager.CameraPosition().z,
        		0f, GameStateManager.CameraPosition().y, 0f, // Target
        		0f, 1f, 0f // Up
        		);
        GameStateManager.Load();
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		GameStateManager.Update();
		
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        Background.Draw();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        
        Projectile.DrawPre();
        
        Samuel.Draw();
        
        Projectile.DrawMid();
        
        GLES20.glEnable(GLES20.GL_STENCIL_TEST);
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 0xFF);
        GLES20.glStencilMask(0xFF);
        GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT);

        Wall.Draw();
        
        GLES20.glStencilFunc(GLES20.GL_EQUAL, 1, 0xFF);
        GLES20.glStencilMask(0x00);
        
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        Projectile.DrawShadow();
        GLES20.glDisable(GLES20.GL_STENCIL_TEST);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        
        
        Projectile.DrawPost();

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        UserInterface.Draw();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		iWidth = width;
		iHeight = height;
        GLES20.glViewport(0, 0, width, height);

        float aspectRatio = (float) width / height;
        
        Matrix.orthoM(mOrthoMatrix, 0, 0, width, 0, height, -1, 1);
        Matrix.frustumM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1, 1, 50);
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        
        GameStateManager.SurfaceChange();
	}
}
