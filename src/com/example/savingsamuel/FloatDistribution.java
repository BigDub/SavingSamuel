package com.example.savingsamuel;

public class FloatDistribution {
	private float fMean, fStdDeviation;
	public float Mean() { return fMean; }
	public float StandardDeviation() { return fStdDeviation; }
	
	public FloatDistribution(float median, float standardDeviation) {
		fMean = median;
		fStdDeviation = standardDeviation;
	}
	
	public float ShiftMean(float offset) {
		fMean += offset;
		return fMean;
	}
	
	public float ShiftStandardDeviation(float offset) {
		fStdDeviation += offset;
		return fStdDeviation;
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
		return (percentile * 6 - 3) * fStdDeviation + fMean;
	}
	public double GetPercentile(float value) {
		return Math.exp( -(Math.pow( value - fMean, 2 ) / 2 * Math.pow( fStdDeviation, 2 )) )
				/
				(Math.sqrt( 2 * Math.PI ) * fStdDeviation);
	}
}
