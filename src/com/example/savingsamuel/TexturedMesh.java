package com.example.savingsamuel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class TexturedMesh extends Mesh {
	protected int iTexture;
	protected final FloatBuffer vertexBuffer;
	protected final ShortBuffer indexBuffer;
	
	private static int BYTES_PER_VERTEX = 4 * (3 + 2);
	
	public TexturedMesh(float[] vertices, short[] indices, String texture) {
		ByteBuffer vb = ByteBuffer.allocateDirect(4 * vertices.length);
		vb.order(ByteOrder.nativeOrder());
		vertexBuffer = vb.asFloatBuffer();
		vertexBuffer.put(vertices);
		
		ByteBuffer ib = ByteBuffer.allocateDirect(2 * indices.length);
		ib.order(ByteOrder.nativeOrder());
		indexBuffer = ib.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		iTexture = Texture.loadTexture(texture);
	}

	@Override
	public void draw(float[] mMVPMatrix, int mProgram) {
		//int mProgram = ShaderProgram.Textured();
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

		vertexBuffer.position(0);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                                     GLES20.GL_FLOAT, false,
                                     BYTES_PER_VERTEX, vertexBuffer);

        // get handle to fragment shader's vColor member
        int mUVHandle = GLES20.glGetAttribLocation(mProgram, "vUV");

        GLES20.glEnableVertexAttribArray(mUVHandle);

		vertexBuffer.position(3);
        GLES20.glVertexAttribPointer(mUVHandle, 2,
        		GLES20.GL_FLOAT, false,
        		BYTES_PER_VERTEX, vertexBuffer);

        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        
        int mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iTexture);
        GLES20.glUniform1i(mTextureHandle, 0);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        //MyRenderer.checkGlError("glUniformMatrix4fv");
        
        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, indexBuffer.capacity(),
                GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mUVHandle);
	}
}
