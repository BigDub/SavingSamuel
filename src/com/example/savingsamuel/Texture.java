package com.example.savingsamuel;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {
	private static Map<String, Texture> mTextureMap;
	private int iWidth, iHeight, iHandle;
	
	public int Handle() { return iHandle; }
	public int Width() { return iWidth; }
	public int Height() { return iHeight; }
	
	public static void Load() {
		mTextureMap = new HashMap<String, Texture>();
	}

	public static Texture loadTexture(String filename) {
		if(mTextureMap.containsKey(filename)) {
			return mTextureMap.get(filename);
		}
		
		int resourceId = GameStateManager.context().getResources().getIdentifier(filename, "drawable", "com.example.savingsamuel");
		if (resourceId == 0)
	    {
	        throw new RuntimeException("Error texture not found: " + filename);
	    }
	    final int[] textureHandle = new int[1];
	 
	    GLES20.glGenTextures(1, textureHandle, 0);
	    Texture newTexture = new Texture();
	 
	    if (textureHandle[0] != 0)
	    {
	    	newTexture.iHandle = textureHandle[0];
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inScaled = false;   // No pre-scaling
	 
	        // Read in the resource
	        final Bitmap bitmap = BitmapFactory.decodeResource(GameStateManager.context().getResources(), resourceId, options);
	        newTexture.iWidth = bitmap.getWidth();
	        newTexture.iHeight = bitmap.getHeight();
	        // Bind to the texture in OpenGL
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, newTexture.iHandle);
	 
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
	    
	    mTextureMap.put(filename, newTexture);
	 
	    return newTexture;
	}

}
