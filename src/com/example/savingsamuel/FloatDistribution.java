package com.example.savingsamuel;

public class FloatDistribution {
	private float _mean, _stdDeviation;
	public float Mean() { return _mean; }
	public float StandardDeviation() { return _stdDeviation; }
	
	public FloatDistribution(float median, float standardDeviation) {
		_mean = median;
		_stdDeviation = standardDeviation;
	}
	
	public float ShiftMean(float offset) {
		_mean += offset;
		return _mean;
	}
	
	public float ShiftStandardDeviation(float offset) {
		_stdDeviation += offset;
		return _stdDeviation;
	}
	
	public float GetRandom() {
		return GetValue((float)Math.random());
	}
	/**
	 * Calculates an approximate value for a percent out of about 99.8% of all possible results
	 * @param percentile
	 * @return
	 */
	public float GetValue(float percentile) {
		return (percentile * 6 - 3) * _stdDeviation + _mean;
	}
	public double GetPercentile(float value) {
		return Math.exp( -(Math.pow( value - _mean, 2 ) / 2 * Math.pow( _stdDeviation, 2 )) )
				/
				(Math.sqrt( 2 * Math.PI ) * _stdDeviation);
	}
}
