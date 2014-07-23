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
    
    private static Mesh _mesh = new TexturedMesh(_vertices, _drawOrder, "rock");
    
	private Rock() {
		super();
		Projectile._projectiles.add(this);
	}
	
	public static void Launch(float r, float s, float x, float y, float z, float vx, float vy, float vz) {
		Rock rock = new Rock();
		rock._launch(r, s, x, y, z, vx, vy, vz);
	}

	@Override
	protected Mesh mesh() {
		return _mesh;
	}
}
