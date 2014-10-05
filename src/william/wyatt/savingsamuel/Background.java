package william.wyatt.savingsamuel;

import android.opengl.GLES20;


public class Background {
	private static final float TOP = 30f;
	private static final float BOTTOM = 20f;
    private static float fVertices[];

    private static final short sDrawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh mMesh;
    
    public static void setAspectRatio() {
    	Texture texture = Texture.loadTexture("background"); 
    	float tWidth = texture.Width();
    	float tHeight = texture.Height();
    	float tAspectRatio = tWidth / tHeight;
    	float bgAspectRatio = (float) MyRenderer.Width() / ((float) MyRenderer.Height() / 2);
    	float uvHeight, uvWidth;
    	if (bgAspectRatio > tAspectRatio) {
    		uvWidth = 0.5f;
    		uvHeight = (tWidth / bgAspectRatio) / tHeight;
    	} else {
    		uvHeight = 1;
    		uvWidth = ((tHeight * bgAspectRatio) / tWidth) / 2;
    	}
    	float fWidth = bgAspectRatio * GameStateManager.CameraPosition().z * 0.5f;

    	fVertices = new float[] {
		    -fWidth,  TOP, 0.0f,	// top left
		    0.5f - uvWidth, 1f - uvHeight,
		    -fWidth, BOTTOM, 0.0f,	// bottom left
		    0.5f - uvWidth, 1f,
		    fWidth, BOTTOM, 0.0f, 	// bottom right
		    0.5f + uvWidth, 1f,
		 	fWidth,  TOP, 0.0f,	// top right
		 	0.5f + uvWidth, 1f - uvHeight
	 	};
        mMesh = new TexturedMesh(fVertices, sDrawOrder, texture);
    }

    public static void Load() {
    	if (fVertices != null) {
	    	Texture texture = Texture.loadTexture("background"); 
	        mMesh = new TexturedMesh(fVertices, sDrawOrder, texture);
    	}
    }
    
    public static void Draw() {
    	GLES20.glUseProgram(Shader.Textured().Program());
        mMesh.Draw(MyRenderer.mVPMatrix(), Shader.Textured());
    }
}
