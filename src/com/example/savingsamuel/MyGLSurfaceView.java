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
		GameStateManager.onTouchEvent(e);
		return true;
	}

	public MyGLSurfaceView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 8);
		
		renderer = new MyRenderer();
		setRenderer(renderer);
	}
}
