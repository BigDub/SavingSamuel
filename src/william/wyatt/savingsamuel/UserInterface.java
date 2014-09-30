package william.wyatt.savingsamuel;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.MotionEvent;

public class UserInterface {
	private static final float fCharWidth = 32f, fCharHeight = 49f, fButtonLength = 128f, fButton2 = fButtonLength / 2;
	private static final float fNumVertices[] = {
	    0f,  fCharHeight, 0.0f,	// top left
		0f, 0f,
	    0f, 0f, 0.0f,	// bottom left
	    0f, 1f,
	    fCharWidth, 0f, 0.0f, 	// bottom right
	    0.1f, 1f,
	 	fCharWidth,  fCharHeight, 0.0f,	// top right
	 	0.1f, 0f
	 	};
	private static final float fButtonVertices[] = {
	    0f,  fButtonLength, 0.0f,	// top left
		0f, 0f,
	    0f, 0f, 0.0f,	// bottom left
	    0f, 1f,
	    fButtonLength, 0f, 0.0f, 	// bottom right
	    1f, 1f,
	 	fButtonLength,  fButtonLength, 0.0f,	// top right
	 	1f, 0f
	 	};
	private static final float fScreenVertices[] = {
		0, 1, 0,
		0, 0, 0, 0.5f,
		0, 0, 0,
		0, 0, 0, 0.5f,
		1, 0, 0,
		0, 0, 0, 0.5f,
		1, 1, 0,
		0, 0, 0, 0.5f
	};
	private static float fMidX, fMidY, fWidth, fHeight, fDragDistance;
    private static short sDrawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    private static boolean bPaused = false, bUnpausing = false;
    private static float fPauseTime = 0;
    
    private static Mesh mNumMesh, mPauseButton, mPlayButton, mScreenMesh;
    
    public static void Load() {        
        mNumMesh = new TexturedMesh(fNumVertices, sDrawOrder, Texture.loadTexture("numbers"));
        mPauseButton = new TexturedMesh(fButtonVertices, sDrawOrder, Texture.loadTexture("pause"));
        mPlayButton = new TexturedMesh(fButtonVertices, sDrawOrder, Texture.loadTexture("play"));
        mScreenMesh = new ColoredMesh(fScreenVertices, sDrawOrder);
    }
    
    private static void DrawNumber(int number, float x, float y, float scale) {
    	String str = String.valueOf(number);
    	for(int i = 0; i < str.length(); i++) {
    		drawCharacter(str.charAt(i) - 48, x + i * fCharWidth, y, scale);
    	}
    }
    private static void drawCharacter(int number, float x, float y, float scale) {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, x, y, 0);
        Matrix.scaleM(mWorldMatrix, 0, scale, scale, scale);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mOrthoMatrix(), 0, mWorldMatrix, 0);
        
        GLES20.glUseProgram(Shader.Numbers().Program());
        int hOffset = Shader.Numbers().getUniform("offset");
        GLES20.glUniform1f(hOffset, number * 0.1f);
        
    	mNumMesh.Draw(mMVPMatrix, Shader.Numbers());
    }
    
    public static void Init() {
    	fWidth = MyRenderer.Width();
    	fHeight = MyRenderer.Height();
    	fMidX = fWidth / 2;
    	fMidY = fHeight / 2;
    	fDragDistance = (float) (Math.hypot(fWidth, fHeight) * 0.25f);
    }
    public static void onPause() {
    	bPaused = true;
    	bUnpausing = false;
    	fPauseTime = 3;
    }
    public static void Update(float elapsed) {
    	if(!bPaused)
    		return;
    	if(bUnpausing)
    		fPauseTime -= elapsed;
    	if(fPauseTime <= 0) {
    		bPaused = false;
    		GameStateManager.Unpause();
    	}
    }
    public static void onTouch(float x, float y) {
    	y = (fHeight - y);
    	//TODO: Track touching down and lifting up to get sweeps.
    	if(!bPaused) {
	    	if(x >= fWidth - fButton2 &&
	    			y >= fHeight - fButton2) {
	    		onPause();
	    		GameStateManager.Pause();
	    	}
    	} else if(!bUnpausing) {
    		if(x >= fMidX - fButton2 &&
    				x <= fMidX + fButton2 &&
    				y >= fMidY - fButton2 &&
    				y <= fMidY + fButton2) {
    			bUnpausing = true;
    		}
    	}
    }
    
    public static void TouchEvent(MotionEvent e) {
    	float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onTouch(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
    }
    public static void Draw() {
    	DrawNumber(GameStateManager.Score(), 0f, fHeight - fCharHeight, 1f);
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, fWidth - fButton2, fHeight - fButton2, 0);
        Matrix.scaleM(mWorldMatrix, 0, 0.5f, 0.5f, 1);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mOrthoMatrix(), 0, mWorldMatrix, 0);
        
        GLES20.glUseProgram(Shader.Textured().Program());
        mPauseButton.Draw(mMVPMatrix, Shader.Textured());
        
        if(bPaused) {
            Matrix.setIdentityM(mWorldMatrix, 0);
            Matrix.scaleM(mWorldMatrix, 0, fWidth, fHeight, 1);
            Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mOrthoMatrix(), 0, mWorldMatrix, 0);

            GLES20.glUseProgram(Shader.VaryingColor().Program());
            mScreenMesh.Draw(mMVPMatrix, Shader.VaryingColor());
            
        	if(bUnpausing) {
        		float scale = fPauseTime;
        		int num;
        		if(fPauseTime > 2) {
        			num = 3;
        			scale -= 1;
        		} else if(fPauseTime > 1) {
        			num = 2;
        		} else {
        			num = 1;
        			scale += 1;
        		}
        		drawCharacter(num, fMidX - (fCharWidth * scale / 2), fMidY - (fCharHeight * scale / 2), scale);
        	} else {
                Matrix.setIdentityM(mWorldMatrix, 0);
                Matrix.translateM(mWorldMatrix, 0, fMidX - fButton2, fMidY - fButton2, 0);
                Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mOrthoMatrix(), 0, mWorldMatrix, 0);
                
                GLES20.glUseProgram(Shader.Textured().Program());
                mPlayButton.Draw(mMVPMatrix, Shader.Textured());
        	}
        }
    	
    }
}
