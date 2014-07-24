package com.example.savingsamuel;


public class Rock extends Projectile{
	private short _variation;
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
    
    private static Mesh[] _meshes;
    
    public static void Load() {
    	_meshes = new Mesh[] {
    			new TexturedMesh(_vertices, _drawOrder, "rock0"),
    			new TexturedMesh(_vertices, _drawOrder, "rock1")
    	};
    }
    
	private Rock() {
		super();
		_variation = (short)(Math.random() * 2f);
		Projectile._projectiles.add(this);
	}
	
	public static void Launch(float rotation, float spin, Vector3 scale, Vector3 position, Vector3 velocity) {
		Rock rock = new Rock();
		rock._launch(rotation, spin, scale, position, velocity);
	}

	@Override
	protected Mesh mesh() {
		return _meshes[_variation];
	}
	
	@Override
	protected float collisionRadius() {
		return _scale.x * 0.375f;
	}
}
