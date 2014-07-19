package com.example.savingsamuel;

import android.opengl.Matrix;
import android.os.SystemClock;

public class Square {    
    static float vertices[] = {
	    -0.5f,  0.5f, 0.0f,	// top left
		0f, 0f,
	    -0.5f, -0.5f, 0.0f,	// bottom left
	    0f, 1f,
	    0.5f, -0.5f, 0.0f, 	// bottom right
	    1f, 1f,
	 	0.5f,  0.5f, 0.0f,	// top right
	 	1f, 0f
	 	};

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    private long uptime = SystemClock.uptimeMillis();
    private float angle = 0;
    
    private Mesh mesh;

    public Square() {
        mesh = new TexturedMesh(vertices, drawOrder, "icon");
    }
    
    public void draw(float[] mVPMatrix) {
        long nuptime = SystemClock.uptimeMillis();
        float elapsed = (float) (nuptime - uptime) / 1000f;
        angle += elapsed;
        uptime = nuptime;
        float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, (float) Math.cos(angle), (float) Math.sin(angle), (float) Math.cos(angle/10f));
        //Matrix.rotateM(mWorldMatrix, 0, angle * 10f, 0f, 0f, 1f);
        Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, mWorldMatrix, 0);

        mesh.draw(mMVPMatrix);
    }
}
