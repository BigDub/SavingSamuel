package com.example.savingsamuel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

@SuppressLint("ClickableViewAccessibility")
public class MyGLSurfaceView extends GLSurfaceView {
	
	private MyRenderer renderer;
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Projectile.Knock(x, y, 30);
			break;
		}
		return true;
	}

	public MyGLSurfaceView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 8);
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		renderer = new MyRenderer();
		setRenderer(renderer);
	}
}
