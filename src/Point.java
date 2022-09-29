
/**
 * Represents a geometric point
 */
public class Point {
	public double x;
	public double y;
	
	/**
	 * Creates the point (x, y)
	 * @param x the x component of the point
	 * @param y the y component of the point
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the a string representation of the point, (x, y)
	 */
	@Override
	public String toString() {
		return String.format("(%.2f, %.2f)", x, y);
	}
}