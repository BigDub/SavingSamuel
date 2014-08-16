package com.example.savingsamuel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class UniformColorMesh extends Mesh {
	private static int BYTES_PER_VERTEX = 4 * 3;

	protected final FloatBuffer fbVertexBuffer;
	protected final ShortBuffer sbIndexBuffer;
	
	public UniformColorMesh(float[] vertices, short[] indices) {
		ByteBuffer vb = ByteBuffer.allocateDirect(4 * vertices.length);
		vb.order(ByteOrder.nativeOrder());
		fbVertexBuffer = vb.asFloatBuffer();
		fbVertexBuffer.put(vertices);
		
		ByteBuffer ib = ByteBuffer.allocateDirect(2 * indices.length);
		ib.order(ByteOrder.nativeOrder());
		sbIndexBuffer = ib.asShortBuffer();
		sbIndexBuffer.put(indices);
		sbIndexBuffer.position(0);
	}
	
	public static void SetColor(Shader mProgram, float red, float green, float blue, float alpha) {
		int mColorHandle = mProgram.getUniform("uColor");
		
		GLES20.glUniform4f(mColorHandle, red, green, blue, alpha);
	}

	@Override
	public void Draw(float[] mMVPMatrix, Shader mProgram) {

        // get handle to vertex shader's vPosition member
        int mPositionHandle = mProgram.getVertexAttribute("vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

		fbVertexBuffer.position(0);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                                     GLES20.GL_FLOAT, false,
                                     BYTES_PER_VERTEX, fbVertexBuffer);

        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = mProgram.getUniform("uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        //MyRenderer.checkGlError("glUniformMatrix4fv");
        
        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, sbIndexBuffer.capacity(),
                GLES20.GL_UNSIGNED_SHORT, sbIndexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
