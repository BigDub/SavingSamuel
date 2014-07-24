package com.example.savingsamuel;


public class Vector3 {
	public float x, y, z;
	
	public Vector3() {
		x = 0;
		y = 0;
		z = 0;
	}
	public Vector3(float value) {
		x = value;
		y = value;
		z = value;
	}
	public Vector3(float X, float Y, float Z) {
		x = X;
		y = Y;
		z = Z;
	}
	public Vector3(float[] array) {
		x = array[0];
		y = array[1];
		z = array[2];
	}
	public Vector3(float[] array, int offset) {
		x = array[offset];
		y = array[offset + 1];
		z = array[offset + 2];
	}
	
	public float Magnitude() {
		return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}
	public Vector3 Normal() {
		float magnitude = Magnitude();
		return new Vector3(
				x / magnitude,
				y / magnitude,
				z / magnitude
				);
	}
	public Vector3 Normalize() {
		float magnitude = Magnitude();
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;
		return this;
	}
	
	public String toString() {
		return "{X:" + x + ", Y:" + y + ", Z:" + z + "}";
	}
	public float[] toArray() {
		return new float[] { x, y, z };
	}
	
	/**
	 * Returns the cross product of two vectors.
	 * @param a Vector a
	 * @param b Vector b
	 * @return The cross product of vectors a and b.
	 */
	public static Vector3 Cross(Vector3 a, Vector3 b) {
		return new Vector3(
				a.y * b.z - a.z * b.y,
				a.z * b.x - a.x * b.z,
				a.x * b.y - a.y * b.x
				);
	}

	/**
	 * Returns the dot product of two vectors.
	 * @param a The first vector
	 * @param b The second vector
	 * @return The dot product
	 */
	public static float Dot(Vector3 a, Vector3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	/**
	 * Returns the dot product of two vectors after they've been normalized.
	 * @param a The first vector
	 * @param b The second vector
	 * @return The dot product
	 */
	public static float NormalDot(Vector3 a, Vector3 b) {
		Vector3 an = a.Normal(),
				bn = b.Normal();
		return an.x * bn.x + an.y * bn.y + an.z * bn.z;
	}

	public static Vector3 Reflect(Vector3 vector, Vector3 normal) {
		return Vector3.Scale(normal, 2 * Vector3.Dot(vector, normal)).Subtract(vector);
	}
	public static Vector3 Bounce(Vector3 vector, Vector3 normal) {
		return Vector3.Scale(normal, -2 * Vector3.Dot(vector, normal)).Add(vector);
	}
	
	/**
	 * Divides the vector's components by a value.
	 * @param value The value to divide by
	 * @return this
	 */
	public Vector3 Divide(float value) {
		x /= value;
		y /= value;
		z /= value;
		return this;
	}
	/**
	 * Returns a new vector with components equal to vector divided by value.
	 * @param vector The vector to be divided
	 * @param value The value to divide by
	 * @return The new vector
	 */
	public static Vector3 Divide(Vector3 vector, float value) {
		return new Vector3(
				vector.x / value,
				vector.y / value,
				vector.z / value
				);
	}
	
	/**
	 * Scales the vector's components by a value.
	 * @param value The value to scale by
	 * @return this
	 */
	public Vector3 Scale(float value) {
		x *= value;
		y *= value;
		z *= value;
		return this;
	}
	/**
	 * Returns a new vector formed by scaling a vector by value.
	 * @param vector The vector to be scaled
	 * @param value The value to scale by
	 * @return The new vector
	 */
	public static Vector3 Scale(Vector3 vector, float value) {
		return new Vector3(
				vector.x * value,
				vector.y * value,
				vector.z * value
				);
	}

	/**
	 * Subtracts the vector's components by the components of another vector.
	 * @param b The other vector
	 * @return this
	 */
	public Vector3 Subtract(Vector3 b) {
		x -= b.x;
		y -= b.y;
		z -= b.z;
		return this;
	}
	/**
	 * Returns a new vector formed by subtracting two vectors
	 * @param a The vector to be subtracted from
	 * @param b The vector to subtract by
	 * @return The new vector
	 */
	public static Vector3 Subtract(Vector3 a, Vector3 b) {
		return new Vector3(
				a.x - b.x,
				a.y - b.y,
				a.z - b.z
				);
	}
	

	/**
	 * Negates the values of the vector.
	 * @return this
	 */
	public Vector3 Negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	/**
	 * Returns the negative of a vector as a new vector.
	 * @param a The vector
	 * @return The new vector
	 */
	public static Vector3 Negate(Vector3 a) {
		return new Vector3(
				-a.x,
				-a.y,
				-a.z
				);
	}
	
	/**
	 * Adds to the vector's components the components of another vector
	 * @param b The other vector
	 * @return this
	 */
	public Vector3 Add(Vector3 b) {
		x += b.x;
		y += b.y;
		z += b.z;
		return this;
	}
	/**
	 * Returns a new vector whose components are the added components of two vectors.
	 * @param a The first vector
	 * @param b The second vector
	 * @return The new vector
	 */
	public static Vector3 Add(Vector3 a, Vector3 b) {
		return new Vector3(
				a.x + b.x,
				a.y + b.y,
				a.z + b.z
				);
	}

	private static final Vector3 
		_zero = new Vector3(0),
		_unitX = new Vector3(1, 0, 0),
		_unitY = new Vector3(0, 1, 0),
		_unitZ = new Vector3(0, 0, 1),
		_one = new Vector3(1);
	/**
	 * Returns the vector (0, 0, 0).
	 */
	public static Vector3 Zero() { return _zero; }
	/**
	 * Returns the vector (1, 0, 0).
	 */
	public static Vector3 UnitX() { return _unitX; }
	/**
	 * Returns the vector (0, 1, 0).
	 */
	public static Vector3 UnitY() { return _unitY; }
	/**
	 * Returns the vector (0, 0, 1).
	 */
	public static Vector3 UnitZ() { return _unitZ; }
	/**
	 * Returns the vector (1, 1, 1).
	 */
	public static Vector3 One() { return _one; }
	
	/**
	 * Returns the distance between two points.
	 * @param a Point a
	 * @param b Point b
	 * @return The distance between points a and b.
	 */
	public static float Distance(Vector3 a, Vector3 b) {
		return (float)Math.sqrt(
				Math.pow(a.x - b.x, 2) + 
				Math.pow(a.y - b.y, 2) + 
				Math.pow(a.z - b.z, 2)
				);
	}

	/**
	 * Returns a new normalized Vector3 pointing from the origin to the target.
	 * @param origin Where to point from
	 * @param target Where to point to
	 * @return The new vector
	 */
	public static Vector3 Direction(Vector3 origin, Vector3 target) {
		return Subtract(target, origin).Normal();
	}
}
