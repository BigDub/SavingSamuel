package com.example.savingsamuel;


public class Samuel {
	private static float
		_height,
		_width,
		_left,
		_bottom = Wall.Top();
    private static float _vertices[];

    private static final short _drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh _mesh;

    public static float Left() {
    	return _left;
    }
    public static float Width() {
    	return _width;
    }
    public static float Height() {
    	return _height;
    }
    public static float Bottom() {
    	return _bottom;
    }
    public static void Load() {
    	Texture texture = Texture.loadTexture("samuel");
    	
    	float scale = 4;
        _height = scale;
		_width = ((float)texture.Width() / (float)texture.Height()) * scale;
		_left = -0.5f * _width;
        _vertices = new float[] {
        	    _left,  _bottom + _height, 0.0f,	// top left
        		0f, 0f,
        	    _left, _bottom, 0.0f,	// bottom left
        	    0f, 1f,
        	    _left + _width, _bottom, 0.0f, 	// bottom right
        	    1f, 1f,
        	 	_left + _width,  _bottom + _height, 0.0f,	// top right
        	 	1f, 0f
        	 	};
        
        _mesh = new TexturedMesh(_vertices, _drawOrder, Texture.loadTexture("samuel"));

    }
    
    public static void draw() {
        _mesh.draw(MyRenderer.mVPMatrix(), Shader.Textured());
    }
}
