package com.example.savingsamuel;


public class Rock extends Projectile{
	private static Vector3Distribution _tints = new Vector3Distribution(0.97f, 0.04f, 0.97f, 0.02f, 0.97f, 0.02f);
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
    	Texture t0 = Texture.loadTexture("rock0"),
    			t1 = Texture.loadTexture("rock1");
    	_meshes = new Mesh[] {
    			new TexturedMesh(_vertices, _drawOrder, t0),
    			new TexturedMesh(_vertices, _drawOrder, t1)
    	};
    }
    
	private Rock() {
		super();
		_variation = (short)(Math.random() * 2f);
		_tint = _tints.GetRandom();
		if(_tint.x > 1)
			_tint.x = 1;
		if(_tint.y > 1)
			_tint.y = 1;
		if(_tint.z > 1)
			_tint.z = 1;
		if(_tint.x > _tint.y)
			_tint.y = _tint.x;
		if(_tint.x > _tint.z)
			_tint.z = _tint.x;
		Projectile._projectiles.add(this);
	}
	
	public static void Launch(float rotation, float spin, Vector3 scale, Vector3 position, Vector3 velocity, boolean warn) {
		Rock rock = new Rock();
		rock._launch(rotation, spin, scale, position, velocity, warn);
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
