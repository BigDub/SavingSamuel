package william.wyatt.savingsamuel;

public class FloatRange {
	private float fMean, fRange;
	public float mean() { return fMean; }
	public float range() { return fRange; }
	
	public FloatRange(float mean, float range) {
		fMean = mean;
		fRange = range;
	}
	
	public float setMean(float mean) {
		fMean = mean;
		return fMean;
	}
	public float shiftMean(float offset) {
		fMean += offset;
		return fMean;
	}
	
	public float shiftRange(float offset) {
		fRange += offset;
		return fRange;
	}
	
	public float getRandom() {
		return getValue((float)Math.random());
	}
	/**
	 * Calculates an approximate value for a percent out of about 99.8% of all possible results
	 * @param percentile
	 * @return
	 */
	public float getValue(float percentile) {
		return (percentile * 2 - 1) * fRange + fMean;
	}
}
