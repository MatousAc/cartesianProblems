import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
 
public class grahamScan extends convexHullAlgorithm{
	private enum bend { NONE, CLOCKWISE, COUNTERCLOCKWISE }
	static Point P = null;
	static Point Q = null;
	static Point R = null;

	public static void makeHull() {
		ArrayList<Point> points = hull.points, sol = hull.solution;
		start = findBasePoint();
		hull.show();

		Collections.sort(points, slopeCompare); // uses custom comparer
		// construct solution
		Iterator<Point> iter = points.iterator();
		sol.add(iter.next()); sol.add(iter.next()); sol.add(iter.next());
		while (iter.hasNext()) {
			newPQR(); hull.show();
			if (bendsLeft(P, Q, R)) {
				sol.add(iter.next());
			} else { sol.remove(Q); }
		}

		// we can still backtrack indefinitely
		newPQR(); hull.show();
		while (!bendsLeft(P, Q, R)) {
			sol.remove(Q);
			newPQR(); hull.show();
		}
		// cleanup
		P = null; Q = null; R = null;
		hull.solved = true;
		hull.show();
	}

	private static void solPop() {
		hull.solution.remove(hull.solution.size() - 1);
	}

	private static void printCurrentState() {
		System.out.println("sol: " + hull.solution);
		System.out.println("P: " + P + " Q: " + Q + " R: " + R);
	}

	private static void newPQR() {
		P = back(2); Q = back(1); R = back(0);
	}
	private static Point back(int d) {
		return hull.solution.get(hull.solution.size() - d - 1);
	}
	private static boolean bendsLeft(Point P, Point Q, Point R) {
		return orientation(P, Q, R) == bend.COUNTERCLOCKWISE;
	}

	/**
	 * compares using values of slope between each point and
	 * the start point. with colinear points, the one closer
	 * to the starting point is "less". starting point is less
	 * than any other point
	 * @param p1 point on left of comparison
	 * @param p2 point on right of comparison
	 * @return -1 if p1 < p2, 1 otherwise
	 */
	static Comparator<Point> slopeCompare = (Point p1, Point p2) -> {
			// make sure starting point always goes to front
			if (start == p1) return -1;
			else if (start == p2) return 1;
			// order by slope w/ start point
			double dif = (Geometry.slope(start, p1) - Geometry.slope(start, p2));
			if (dif < 0) return -1;
			else if (dif > 0) return 1;
			// here, dif = 0, so these are colinear points. we put the nearest one first
			double d1 = Geometry.distance(start, p1), d2 = Geometry.distance(start, p2);
			if (d1 < d2) return -1;
			// PAINT SLOPES WHILE SORTING
			else return 1;
		};

	/**
	 * Tells us whether a 3-point sequence is 
	 * colinear, clockwise, or counterclockwise. 
	 * @param p
	 * @param q
	 * @param r
	 * @return bend type
	 */
	static bend orientation(Point p, Point q, Point r) {
		double val = (q.y - p.y) * (r.x - q.x) -
							(q.x - p.x) * (r.y - q.y);
		if (val == 0) return bend.NONE;  // collinear
		return (val > 0)? bend.CLOCKWISE: bend.COUNTERCLOCKWISE; // clock or counterclock wise
	}
}
