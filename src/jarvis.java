// code from Andre Violentyev was used to guide the writing of some of this algorithm
// https://bitbucket.org/StableSort/play/src/master/src/com/stablesort/convexhull/ConvexHullJarvisMarch.java
import java.util.ArrayList;
import java.util.Iterator;

public class Jarvis extends HullAlg {
	protected static Point next;
	
	public static void march() {
		pointCopy = (ArrayList<Point>) Core.points.clone();
		start = findStartPoint(pointCopy);
		
		P = new Point(start.x, start.y - 1); 
		Q = start; next = start;
		while (next != start || Core.hull.size() == 0) {
			Core.hull.add(next); Core.show();
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
