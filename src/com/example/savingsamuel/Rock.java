package com.example.savingsamuel;

public class Rock extends Projectile{
	private static float _vertices[] = {
	    -0.5f,  0.5f, 0.0f,	// top left
		0f, 0f,
	    -0.5f, -0.5f, 0.0f,	// bottom left
	    0f, 1f,
	    0.5f, -0.5f, 0.0f, 	// bottom right
	    1f, 1f,
	 	0.5f,  0.5f, 0.0f,	// top right
	 	1f, 0f
	 	};

    private static short _drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh _mesh = new TexturedMesh(_vertices, _drawOrder, "icon");
    
	public Rock() {
		super();
	}
	
	public void draw(float[] mVPMatrix) {
		super.draw(mVPMatrix);
	}

	@Override
	protected Mesh mesh() {
		return _mesh;
	}
}
