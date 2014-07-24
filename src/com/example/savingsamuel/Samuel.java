package com.example.savingsamuel;


public class Samuel {
	private static final float
		_height = 2f,
		_width = 2f,
		_left = -0.5f * _width,
		_bottom = Wall.Top();
    private static final float _vertices[] = {
	    -1f,  22f, 0.0f,	// top left
		0f, 0f,
	    -1f, 20f, 0.0f,	// bottom left
	    0f, 1f,
	    1f, 20f, 0.0f, 	// bottom right
	    1f, 1f,
	 	1f,  22f, 0.0f,	// top right
	 	1f, 0f
	 	};

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
        _mesh = new TexturedMesh(_vertices, _drawOrder, "icon");
    }
    
    public static void draw() {
        _mesh.draw(MyRenderer.mVPMatrix());
    }
}
