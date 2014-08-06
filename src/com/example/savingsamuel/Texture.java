package com.example.savingsamuel;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {
	private static Context _context;
	private static Map<String, Texture> _textureMap;
	private int _width, _height, _handle;
	
	public int Handle() { return _handle; }
	public int Width() { return _width; }
	public int Height() { return _height; }
	
	public static void Init(Context context) {
		_context = context;
		_textureMap = new HashMap<String, Texture>();
	}

	public static Texture loadTexture(String filename) {
		if(_textureMap.containsKey(filename)) {
			return _textureMap.get(filename);
		}
		
		int resourceId = _context.getResources().getIdentifier(filename, "drawable", "com.example.savingsamuel");
		if (resourceId == 0)
	    {
	        throw new RuntimeException("Error texture not found: " + filename);
	    }
	    final int[] textureHandle = new int[1];
	 
	    GLES20.glGenTextures(1, textureHandle, 0);
	    Texture newTexture = new Texture();
	 
	    if (textureHandle[0] != 0)
	    {
	    	newTexture._handle = textureHandle[0];
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inScaled = false;   // No pre-scaling
	 
	        // Read in the resource
	        final Bitmap bitmap = BitmapFactory.decodeResource(_context.getResources(), resourceId, options);
	        newTexture._width = bitmap.getWidth();
	        newTexture._height = bitmap.getHeight();
	        // Bind to the texture in OpenGL
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, newTexture._handle);
	 
	        // Set filtering
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
	 
	        // Load the bitmap into the bound texture.
	        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	 
	        // Recycle the bitmap, since its data has been loaded into OpenGL.
	        bitmap.recycle();
	    }
	 
	    if (textureHandle[0] == 0)
	    {
	        throw new RuntimeException("Error loading texture.");
	    }
	    
	    _textureMap.put(filename, newTexture);
	 
	    return newTexture;
	}

}
