import java.util.HashSet;
import java.util.Iterator;

/**
 * Represents a geometric segment
 */
public class Edge extends HashSet<Point> {
	/**
	 * creates a Edge object defined by the two (unordered) points given
	 * @param p1 one of the points on the segment
	 * @param p2 another point of the segment
	 */
	public Edge(Point u, Point v) {
		this.add(u);
		this.add(v);
	}

	/**
	 * Adds up to two points to Edge. 
	 * After the Edge is full, this method does simply returns false.
	 * @param e element to be added to this Edge
	 * @return {@code true} if point was added successfully.
	 * {@code false} if point is already in Edge or 
	 */
	@Override
	public boolean add(Point p) {
		if (this.size()>= 2) return false;
		else return super.add(p);
	}

	/**
	 * Edge does not allow removing points. This method does nothing.
	 * @param o element to be removed from this Edge
	 * @return {@code false}
	 */
	@Override
	public boolean remove(Object o) {
		return false;
	}

	/**
	 * Returns a string representation of the Edge
	 */
	@Override
	public String toString() {
		Iterator<Point> i = this.iterator();
		return "U: " + i.next() + " V: " + i.next() ;
	}
}
