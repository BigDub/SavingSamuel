package com.example.savingsamuel;

import android.opengl.GLES20;
import android.opengl.Matrix;


public class Samuel {
	private static Samuel _instance;

	private static float
		_height,
		_width,
		_left,
		_bottom = Wall.Top();
    private static float _vertices[];

    private static final short _drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh _mesh;

	public static void Init() {
		_instance = new Samuel();
	}
	public static Vector3 Position() { return _instance._position; }
    public static float Left() { return _left; }
    public static float Width() { return _width; }
    public static float Height() { return _height; }
    public static float Bottom() { return _bottom; }
    public static void Load() {
    	Texture texture = Texture.loadTexture("samuel");
    	
    	float scale = 4;
        _height = scale;
		_width = ((float)texture.Width() / (float)texture.Height()) * scale;
		_left = -0.5f * _width;
        _vertices = new float[] {
        	    _left,  _height, 0.0f,	// top left
        		0f, 0f,
        	    _left, 0f, 0.0f,	// bottom left
        	    0f, 1f,
        	    _left + _width, 0f, 0.0f, 	// bottom right
        	    1f, 1f,
        	 	_left + _width, _height, 0.0f,	// top right
        	 	1f, 0f
        	 	};
        
        _mesh = new TexturedMesh(_vertices, _drawOrder, texture);

    }
    public static void Draw() {
        _instance._draw();
    }
    public static void Update(float elapsed) {
    	_instance._update(elapsed);
    }
    public static void Reset() {
    	_instance._reset();
    }
    public static void Knock(Vector3 incomingVelocity) {
    	if(_instance._falling)
    		return;
    	_instance._falling = true;
    	
		AudioManager.playWilhelm();
    	_instance._velocity = Vector3.Scale(incomingVelocity, 0.2f);
    	_instance._velocity.y = 0;
    	GameStateManager.SamuelHit();
    }
    public static boolean Hit(Vector3 position, float radius) {
    	return !_instance._falling &&
		position.x + radius > _left &&
		position.x - radius < _left + _width &&
		position.y + radius > _bottom &&
		position.y - radius < _bottom + _height;
    }
    
    private Vector3 _position, _velocity;
    private boolean _falling;
    
    private Samuel() {
    	_reset();
    }
    
    private void _reset() {
    	_falling = false;
    	_velocity = new Vector3(0);
    	_position = new Vector3(0, _bottom, 0);
    }
    
    private void _update(float elapsed) {
    	if(!_falling)
    		return;
    	_position.Add(Vector3.Scale(_velocity, elapsed));
    	_velocity.y -= 9.8f * elapsed;
    }
    
    private void _draw() {
    	float[] mMVPMatrix = new float[16];
        float[] mWorldMatrix = new float[16];
        Matrix.setIdentityM(mWorldMatrix, 0);
        Matrix.translateM(mWorldMatrix, 0, _position.x, _position.y, _position.z);
        Matrix.multiplyMM(mMVPMatrix, 0, MyRenderer.mVPMatrix(), 0, mWorldMatrix, 0);
        
        GLES20.glUseProgram(Shader.Textured().Program());
    	_mesh.Draw(mMVPMatrix, Shader.Textured());
    }
}
