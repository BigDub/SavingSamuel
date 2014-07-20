package com.example.savingsamuel;


public class Wall {    
    static float vertices[] = {
	    -20f,  20f, 0.0f,	// top left
		0.96f, 0.78f, 0.45f, 1.0f,
	    -20f, 0f, 0.0f,	// bottom left
	    0.94f, 0.66f, 0.18f, 1.0f,
	    20f, 0f, 0.0f, 	// bottom right
	    0.94f, 0.66f, 0.18f, 1.0f,
	 	20f,  20f, 0.0f,	// top right
	 	0.96f, 0.78f, 0.45f, 1.0f
	 	};

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private Mesh mesh;

    public Wall() {
        mesh = new ColoredMesh(vertices, drawOrder);
    }
    
    public void draw(float[] mVPMatrix) {
        mesh.draw(mVPMatrix);
    }
}
