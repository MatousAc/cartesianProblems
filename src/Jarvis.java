// code from Andre Violentyev was used to guide the writing of some of this algorithm
// https://bitbucket.org/StableSort/play/src/master/src/com/stablesort/convexhull/ConvexHullJarvisMarch.java
import java.util.ArrayList;
import java.util.Iterator;

import enums.ChHeur;

/**
 * Contains a solution to the convex hull problem. 
 * Use {@code march()} method to solve.
 * */
public class Jarvis extends Hull {
	/** The point we currently think will be added to the hull next. */
	protected static Point next;
	/**
	 * Solves the convex hull problem in {@code HullBase.points}
	 * in O(nh) time (h being the number of points in the hull). 
	 * {@code march()} "walks" along the outer edge of the problem
	 * scanning all points to continue to the point furthest 
	 * counterclockwise. Once back at the starting point, the 
	 * {@code march} is over.
	 */
	public static void march() {
		ArrayList<Point> pointCopy = (ArrayList<Point>) points.clone();
		start = findStartPoint(pointCopy);
    if (Hull.heuristic == ChHeur.AKL_TOUSSAINT) aklToussaint(pointCopy);

		P = new Point(start.x, start.y - 1); 
		Q = start; next = start;
		while (next != start || hull.size() == 0) {
			hull.add(next); Core.show();
			double maxAngle = Double.MIN_VALUE;
			double maxDist = 0;

			// pick point that creates the largest angle
			Iterator<Point> iter = pointCopy.iterator();
			while (iter.hasNext()) {
				R = iter.next();
				if (P == Q || Q == R || R == P) continue;
				
				double angle = Geometry.angleCCW(P, Q, R);
				Core.show();
				// if angle > 180 the points are too close to distinguish
				// a point can not be outside this range as the algorithms
				// ALWAYS keeps the full set of points to the right of Line(Q, R)
				if (angle > 180) continue;
				double dist = Geometry.distance(Q, R);
				if ((angle > maxAngle) || (angle == maxAngle && dist > maxDist)) {
					// found better point
					maxAngle = angle;
					maxDist = dist;
					next = R;						
				}
			}
			P = Q; Q = next;
			if (next == start) break;
		}
		next = null;
		cleanup();
	}
}
