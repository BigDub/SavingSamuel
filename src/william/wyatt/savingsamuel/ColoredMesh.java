package william.wyatt.savingsamuel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class ColoredMesh extends Mesh {
	private static int BYTES_PER_VERTEX = 4 * (3 + 4);

	protected final FloatBuffer fbVertexBuffer;
	protected final ShortBuffer sbIndexBuffer;
	
	public ColoredMesh(float[] vertices, short[] indices) {
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

        // get handle to fragment shader's vColor member
        int mUVHandle = mProgram.getVertexAttribute("vColor");

        GLES20.glEnableVertexAttribArray(mUVHandle);

		fbVertexBuffer.position(3);
        GLES20.glVertexAttribPointer(mUVHandle, 4,
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
        GLES20.glDisableVertexAttribArray(mUVHandle);
	}
}
