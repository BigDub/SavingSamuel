package com.example.savingsamuel;


public class Rock extends Projectile{
	private static Vector3Distribution vTints = new Vector3Distribution(0.97f, 0.04f, 0.97f, 0.02f, 0.97f, 0.02f);
	private static FloatDistribution fShades = new FloatDistribution(0.3f, 0.1f);
	private short sVariation;
	private static float fVertices[] = {
	    -0.5f,  0.5f, 0.0f,	// top left
		0f, 0f,
	    -0.5f, -0.5f, 0.0f,	// bottom left
	    0f, 1f,
	    0.5f, -0.5f, 0.0f, 	// bottom right
	    1f, 1f,
	 	0.5f,  0.5f, 0.0f,	// top right
	 	1f, 0f
	 	};

    private static short sDrawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private static Mesh[] mMeshes;
    
    public static void Load() {
    	Texture t0 = Texture.loadTexture("rock0"),
    			t1 = Texture.loadTexture("rock1");
    	mMeshes = new Mesh[] {
    			new TexturedMesh(fVertices, sDrawOrder, t0),
    			new TexturedMesh(fVertices, sDrawOrder, t1)
    	};
    }
    
	private Rock() {
		super();
		sVariation = (short)(Math.random() * 2f);
		vTint = vTints.GetRandom();
		if(vTint.x > 1)
			vTint.x = 1;
		if(vTint.y > 1)
			vTint.y = 1;
		if(vTint.z > 1)
			vTint.z = 1;
		if(vTint.x > vTint.y)
			vTint.y = vTint.x;
		if(vTint.x > vTint.z)
			vTint.z = vTint.x;
		float shade = fShades.GetRandom();
		if(shade > 0) {
			vTint.x -= shade;
			vTint.y -= shade;
			vTint.z -= shade;
		}
		Projectile.vProjectiles.add(this);
	}
	
	public static void Launch(float rotation, float spin, Vector3 scale, Vector3 position, Vector3 velocity, boolean warn) {
		Rock rock = new Rock();
		rock.launch(rotation, spin, scale, position, velocity, warn);
	}

	@Override
	protected Mesh mesh() {
		return mMeshes[sVariation];
	}
	
	@Override
	protected float collisionRadius() {
		return vScale.x * 0.375f;
	}
}
