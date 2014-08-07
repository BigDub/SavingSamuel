package com.example.savingsamuel;

public class Vector3Distribution {
	private FloatDistribution _fdx, _fdy, _fdz;
	
	public Vector3Distribution(FloatDistribution x, FloatDistribution y, FloatDistribution z) {
		_fdx = x;
		_fdy = y;
		_fdz = z;
	}
	public Vector3Distribution(
				float x_mean, float x_stdDeviation,
				float y_mean, float y_stdDeviation,
				float z_mean, float z_stdDeviation
				) {
		_fdx = new FloatDistribution(x_mean, x_stdDeviation);
		_fdy = new FloatDistribution(y_mean, y_stdDeviation);
		_fdz = new FloatDistribution(z_mean, z_stdDeviation);
	}
	
	public Vector3 GetRandom() {
		return new Vector3(
				_fdx.GetRandom(),
				_fdy.GetRandom(),
				_fdz.GetRandom()
				);
	}
}
