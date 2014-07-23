package com.example.savingsamuel;


public class Wall {
	private static final float _top = 20f;
    private static final float _vertices[] = {
	    -20f,  20f, 0.0f,	// top left
		0.96f, 0.78f, 0.45f, 1.0f,
	    -20f, 0f, 0.0f,	// bottom left
	    0.94f, 0.66f, 0.18f, 1.0f,
	    20f, 0f, 0.0f, 	// bottom right
	    0.94f, 0.66f, 0.18f, 1.0f,
	 	20f,  20f, 0.0f,	// top right
	 	0.96f, 0.78f, 0.45f, 1.0f
	 	};

    private static final short _drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh _mesh;
    
    public static float Top() {
    	return _top;
    }

    public static void Init() {
        _mesh = new ColoredMesh(_vertices, _drawOrder);
    }
    
    public static void draw() {
        _mesh.draw(MyRenderer.mVPMatrix());
    }
}
