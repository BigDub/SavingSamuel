package william.wyatt.savingsamuel;

public class Vector3Range {
	private FloatRange fdx, fdy, fdz;
	
	public Vector3Range(FloatRange x, FloatRange y, FloatRange z) {
		fdx = x;
		fdy = y;
		fdz = z;
	}
	public Vector3Range(
				float x_mean, float x_range,
				float y_mean, float y_range,
				float z_mean, float z_range
				) {
		fdx = new FloatRange(x_mean, x_range);
		fdy = new FloatRange(y_mean, y_range);
		fdz = new FloatRange(z_mean, z_range);
	}
	
	public Vector3 getRandom() {
		return new Vector3(
				fdx.getRandom(),
				fdy.getRandom(),
				fdz.getRandom()
				);
	}
}
