package com.example.savingsamuel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

public class Square {

	private final FloatBuffer vertexBuffer, uvBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mUVHandle;
    private int mTextureHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
             0.5f, -0.5f, 0.0f,   // bottom right
             0.5f,  0.5f, 0.0f }; // top right
    
    static float vertUVs[] = {
    	0f, 0f,
    	0f, 1f,
    	1f, 1f,
    	1f, 0f
    };

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    private long uptime = SystemClock.uptimeMillis();
    private float angle = 0;
    
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final int uvStride = 8;

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        
        ByteBuffer uvb = ByteBuffer.allocateDirect(
        		vertUVs.length * 4);
        uvb.order(ByteOrder.nativeOrder());
        uvBuffer = uvb.asFloatBuffer();
        uvBuffer.put(vertUVs);
        uvBuffer.position(0);

        mProgram = ShaderProgram.Textured();
    }
    
    public void draw(float[] mVPMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mUVHandle = GLES20.glGetAttribLocation(mProgram, "vUV");

        GLES20.glEnableVertexAttribArray(mUVHandle);

        GLES20.glVertexAttribPointer(mUVHandle, 2,
        		GLES20.GL_FLOAT, false,
        		uvStride, uvBuffer);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        
        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Texture.getTexture());
        GLES20.glUniform1i(mTextureHandle, 0);
        
        //TODO multiply model matrix by world matrix. Multiply result by mVPMatrix to get mMVPMatrix.
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

        
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        //MyRenderer.checkGlError("glUniformMatrix4fv");
        
        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mUVHandle);
    }
}
