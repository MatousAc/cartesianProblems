/**
 * Represents a geometric segment
 */
public class Segment {
	/**
	 * start of this Segment
	 */
	public Point start;
	/**
	 * end of this Segment
	 */
	public Point end;

	/**
	 * creates a Segment object defined by the two points given
	 * @param p1 one of the points on the segment
	 * @param p2 another point of the segment
	 */
	public Segment(Point p1, Point p2) {
		this.start = p1;
		this.end = p2;
	}

	/**
	 * Returns the slope of the segment
	 * @return the slope of the segment
	 */
	public double slope() {
		return Geometry.slope(start, end);
	}

	/**
	 * Returns intercept stored in Segment object.
	 * @return intercept
	 */
	public double intercept() {
		return Geometry.intercept(start, end);
	}
	
	/**
	 * Returns a string representation of the segment in 
	 * y = mx + b (for non-vertical segment) or x = a (vertical segment).
	 * Uses the services of the Geometry class.
	 * @return a string representation of the segment in 
	 *         y = mx + b (for non-vertical segment) or x = a (vertical segment)
	 */
	@Override
	public String toString() {
		return "Segment: " + start + " -> " + end ;
	}
}
