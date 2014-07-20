package com.example.savingsamuel;


public class Samuel {    
    static float vertices[] = {
	    -1f,  22f, 0.0f,	// top left
		0f, 0f,
	    -1f, 20f, 0.0f,	// bottom left
	    0f, 1f,
	    1f, 20f, 0.0f, 	// bottom right
	    1f, 1f,
	 	1f,  22f, 0.0f,	// top right
	 	1f, 0f
	 	};

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private Mesh mesh;

    public Samuel() {
        mesh = new TexturedMesh(vertices, drawOrder, "icon");
    }
    
    public void draw(float[] mVPMatrix) {
        mesh.draw(mVPMatrix);
    }
}
