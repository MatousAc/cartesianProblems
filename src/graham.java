import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
 
public class Graham extends HullAlg{
	public static Line m1 = null;
	public static Line m2 = null;
	
	public static void scan() {
		pointCopy = (ArrayList<Point>) Core.points.clone();
		start = findStartPoint(pointCopy);
		Core.show();

		Collections.sort(pointCopy, slopeCompare); // use custom comparer
		m1 = null; m2 = null;
		// construct solution
		Iterator<Point> iter = pointCopy.iterator();
		Core.hull.add(iter.next()); Core.hull.add(iter.next()); Core.hull.add(iter.next());
		while (iter.hasNext()) {
			newPQR(); Core.show();
			if (bendsCCW(P, Q, R)) {
				Core.hull.add(iter.next());
			} else { Core.hull.remove(Q); }
		}

		// at the last point, we can still backtrack indefinitely
		newPQR(); Core.show();
		while (!bendsCCW(P, Q, R)) {
			Core.hull.remove(Q);
			newPQR(); Core.show(); // (don't move this line. it's correct)
		}
		cleanup(); Core.show();
	}

	/**
	 * compares using values of slope between each point and
	 * the start point. with colinear points, the one closer
	 * to the starting point is "less". starting point is less
	 * than any other point. visualizes the sort if Core is in
	 * visual mode
	 * @param p1 point on left of comparison
	 * @param p2 point on right of comparison
	 * @return -1 if p1 < p2, 1 otherwise
	 */
	static Comparator<Point> slopeCompare = (Point p1, Point p2) -> {
			// make sure starting point always goes to front
			if (start == p1) return -1;
			else if (start == p2) return 1;

			// visualize slopes if visual mode
			if (Core.mode == Mode.VISUAL) {
				m1 = new Line(start, p1);
				m2 = new Line(start, p2);
				Core.show();
			}
			
			// order by slope w/ start point
			double dif = (Geometry.slope(start, p1) - Geometry.slope(start, p2));
			if (dif < 0) return -1;
			else if (dif > 0) return 1;
			// here, dif = 0, so these are colinear points. we put the nearest one first
			double d1 = Geometry.distance(start, p1), d2 = Geometry.distance(start, p2);
			if (d1 < d2) return -1;
			else return 1;
		};
}
