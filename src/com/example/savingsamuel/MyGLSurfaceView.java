package com.example.savingsamuel;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {
	
	private MyRenderer renderer;

	public MyGLSurfaceView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		renderer = new MyRenderer();
		setRenderer(renderer);
	}
	
	public void setClearRed() {
		renderer.setClearColor(1.0f, 0.0f, 0.0f);
	}
	public void setClearBlue() {
		renderer.setClearColor(0.0f, 0.0f, 1.0f);
	}
}
