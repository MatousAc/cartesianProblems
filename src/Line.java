//Assignment Completed By Ac Hybl On March 19, 2020, 
// In Partial Fulfillment Of CPTR-124-A.
// uses Geometry class (no need for import since in the same package)

/**
 * Represents a geometric line
 */
public class Line {
	/**
	 * slope
	 */
	private double m ;
	
	/**
	 * y-intercept
	 */
	private double b ;
	
	/**
	 * the first Point that is on this Line
	 */
	public Point p1;
	/**
	 * the second Point that is on this Line
	 */
	public Point p2;

	/**
	 * Creates a Line object with the given slope and intercept
	 * @param m the slope of the line
	 * @param b the intercept of the line
	 */
	public Line(double m, double b) {
		this.m = m ;
		this.b = b ;
		this.p1 = new Point(0, b);
		this.p2 = new Point(1, b + m);
	}

	/**
	 * Creates a Line object that passes through the two points given
	 * @param p1 one of the points on the line
	 * @param p2 another point of the line
	 */
	public Line(Point p1, Point p2) {
		this.m = slope( p1, p2 ) ;
		this.b = intercept( p1, p2 ) ;
		this.p1 = p1;
		this.p2 = p2;
	}

	/**
	 * Returns the slope of the line
	 * @return the slope of the line
	 */
	public double slope() {
		return this.m ;
	}
	
	/**
	 * Returns the slope of the line
	 * @return the slope of the line
	 */
	public double slope(Point p1, Point p2) { // from lab 7
		return Geometry.slope(p1, p2) ;
	}

	/**
	 * Returns intercept stored in Line object.
	 * @return intercept
	 */
	public double intercept() {
		return this.b ;
	}
	
	/**
	 * Returns the x-intercept of the line if it vertical;
	 * otherwise, returns the y intercept of the line
	 * @param p2 
	 * @param p1 
	 * @return the intercept of the line
	 */
	public double intercept(Point p1, Point p2) {
		return Geometry.intercept(p1, p2) ;
	}

	/**
	 * Returns a string representation of the line in 
	 * y = mx + b (for non-vertical line) or x = a (vertical line).
	 * Uses the services of the Geometry class.
	 * @return a string representation of the line in 
	 *         y = mx + b (for non-vertical line) or x = a (vertical line)
	 */
	@Override
	public String toString() {
		return Geometry.lineEquation(slope(), intercept());
	}
}
