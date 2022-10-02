// Assignment Completed By Ac Hybl On March 19, 2020, 
// In Partial Fulfillment Of CPTR-124-A.
// Code mostly from previous Lab (intersection added)

public class Geometry {
	
	private static void equalExeption( double x1, double x2, double y1, double y2 ) {
		if (( equals( x1, x2 ) && equals( y1, y2 )))
			throw new IllegalArgumentException();
	}
	
	private static double abs( double value ) {
		if ( value < 0 )
			value *= -1 ;
		return value ;
	}
	
	private static double square( double value ) {
		return (value * value) ;
	}
	
	/**
	 * Test to see if two double-precision floating-point
	 * values are "equal." The values are considered
	 * equal when their difference is small.
	 * @param a one of the floating-point numbers to compare
	 * @param b the other floating-point number to compare
	 * @return true, if the two numbers are within 0.0001 of each
	 *         other; otherwise, returns false
	 */
	public static boolean equals(double a, double b) {
		return abs( a - b ) < .00001;
	}

	/**
	 * Computes the distance between two {@code Point} objects.
	 * @param p1 one of the points
	 * @param p2 the other point
	 * @return the distance between 
	 */
	public static double distance(Point p1, Point p2) {
		return Math.sqrt((square( p2.x - p1.x ) + square( p2.y - p1.y )));
	}

	/**
	 * Returns the slope of the line that passes through point {@code p1}
	 * and {@code p2}.
	 * @param p1 the first point
	 * @param p2 the second point
	 * @return the slope of the line that passes through points
	 *         p1 and p2. exception if points are same object
	 */
	public static double slope(Point p1, Point p2) {
		if (p1 == p2) throw new IllegalArgumentException();

		if ( equals( p1.x, p2.x ))
			return Double.POSITIVE_INFINITY ;
		double m = (( p2.y - p1.y ) / ( p2.x - p1.x )) ;
		return m ;
	}

	/**
	 * Returns the y-intercept of the line that passes through
	 * points {@code p1} and {@code p2}.  If the line is vertical, the method returns
	 * the line's x-intercept.
	 * @return the intercept of the line
	 */
	public static double intercept(Point p1, Point p2) {
		equalExeption( p1.x, p2.x, p1.y, p2.y ) ;
		
		if ( equals( p1.x, p2.x ))
			return p1.x ;
		
		double b = p1.y - ( slope( p1, p2 ) * p1.x ) ;
		return b ;
	}

	/**
	 * Returns the human readable string representation of the line with slope 
	 * {@code m} and intercept {@code b}.
	 * @param m the slope of the line
	 * @param b the y- intercept of the line (x-intercept is the line is vertical)
	 * @return the human readable representation of the line
	 */
	public static String lineEquation(double m, double b) {
		if ( m == Double.POSITIVE_INFINITY ) // vertical line
			return ( "x = " + String.format("%.2f", b )) ;
		
		String equation = "y = " ;
		// add slope
		if ( equals( m , 1.0 ))
			equation +=  "x " ;
		else if ( equals( m , -1.0 ))
			equation += "-x " ;
		else if ( equals( m , 0.0 ) )
			return ( "y = " + String.format("%.2f", b )) ; 
		else
			equation += String.format( "%.2f", m ) + "x " ;
		
		// add intercept
		if ( equals( b , 0.0 ) )
			return equation ;
		else if ( b < 0 )
			equation += "- " + String.format( "%.2f", ( -b) ) ;
		else
			equation += "+ " + String.format( "%.2f", b ) ;
		
		return equation ; 
	}

	/**
	 * Returns the human readable representation of the line passing 
	 * through points {@code p1} and {@code p2}.
	 * @param p1 one of the points on the line
	 * @param p2 the other point of the line
	 * @return a human readable representation of the line:
	 *         y = mx + b or x = a.
	 */
	public static String lineEquation(Point p1, Point p2) {
		return lineEquation(slope(p1, p2), intercept(p1, p2));
	}

	/**
	 * Computes the point of intersection of {@code line1} and {@code line2}, 
	 * if it exists.  Returns {@code null} if the lines have no single point
	 * of intersection.
	 * @param line1 one of the lines
	 * @param line2 the other line
	 * @return the point of intersection of {@code line1} and {@code line2}, if it exists
	 */
	public static Point intersection(Line line1, Line line2) {
		double x, y ;
		// if slopes are equal, there is no intersection
		if ( equals( line1.slope() , line2.slope() ) ) {
			return null ;
		} // vertical line czechs
		else if ( line1.slope() == Double.POSITIVE_INFINITY ) {
			if ( line2.slope() == Double.POSITIVE_INFINITY )
				return null ;
			else { // X = x | Y = m2*X + b2
				x	= line1.intercept() ;
				y	= (( line2.slope() * x ) + line2.intercept() ) ;
			}
		}
		else if ( line2.slope() == Double.POSITIVE_INFINITY ) {
			x	= line2.intercept() ;
			y	= (( line1.slope() * x ) + line1.intercept() ) ;
		}
		
		else { // X = (b2 - b1) / (m1 - m2) | Y = m2*X + b2 = m1*X + b1
			x 	= ( line2.intercept() - line1.intercept() ) / ( line1.slope() - line2.slope() ) ;
			y	= (( line2.slope() * x ) + line2.intercept() ) ;
		}
		return new Point( x , y );
	}

	static bend orientation(Point p, Point q, Point r) {
		double val = (q.y - p.y) * (r.x - q.x) -
							(q.x - p.x) * (r.y - q.y);
		if (val == 0) return bend.NONE;  // collinear
		return (val > 0)? bend.CLOCKWISE: bend.COUNTERCLOCKWISE; // clock or counterclock wise
	}

	/**
	 * calculates the angle between the horizontal and the line drawn from 'a' to 'b'
	 * @param p1
	 * @param p2
	 * @return angle as float
	 */ // taken from Andre Violentyev's code (see graham.java)
	public static float angle(Point p1, Point p2) {
		float angle = (float) Math.toDegrees(Math.atan2(p2.y - p1.y, p2.x - p1.x));
		if (angle < 0) angle += 360;
		return angle;
	}
}

