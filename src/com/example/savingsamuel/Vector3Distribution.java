package com.example.savingsamuel;

public class Vector3Distribution {
	private FloatDistribution fdx, fdy, fdz;
	
	public Vector3Distribution(FloatDistribution x, FloatDistribution y, FloatDistribution z) {
		fdx = x;
		fdy = y;
		fdz = z;
	}
	public Vector3Distribution(
				float x_mean, float x_stdDeviation,
				float y_mean, float y_stdDeviation,
				float z_mean, float z_stdDeviation
				) {
		fdx = new FloatDistribution(x_mean, x_stdDeviation);
		fdy = new FloatDistribution(y_mean, y_stdDeviation);
		fdz = new FloatDistribution(z_mean, z_stdDeviation);
	}
	
	public Vector3 GetRandom() {
		return new Vector3(
				fdx.GetRandom(),
				fdy.GetRandom(),
				fdz.GetRandom()
				);
	}
}
