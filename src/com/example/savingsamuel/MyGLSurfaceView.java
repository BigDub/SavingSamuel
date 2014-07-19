package com.example.savingsamuel;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {
	
	private MyRenderer renderer;
	
	/*@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		}
		return true;
	}*/

	public MyGLSurfaceView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		renderer = new MyRenderer();
		setRenderer(renderer);
		ShaderProgram.init(context);
		Texture.init(context);
	}
	
	public void setClearWhite() {
		renderer.setClearColor(1.0f, 1.0f, 1.0f);
	}
	public void setClearBlack() {
		renderer.setClearColor(0.0f, 0.0f, 0.0f);
	}
}
