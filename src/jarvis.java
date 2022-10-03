// code from Andre Violentyev was used to write part of this algorithm
// https://bitbucket.org/StableSort/play/src/master/src/com/stablesort/convexhull/ConvexHullJarvisMarch.java
import java.util.ArrayList;
import java.util.Iterator;

public class jarvis extends algorithm {
	protected static Point next;
	
	public static void march() {
		// setup
		ArrayList<Point> points = core.points;
		ArrayList<Point> hull = core.hull;
		start = findStartPoint();
		
		P = new Point(start.x, start.y + 10); 
		Q = start; next = start;
		// double prevAngle = -1;
		while (next != start || hull.size() == 0) {
			hull.add(next); core.show();
			double minAngle = Float.MAX_VALUE;
			double maxDist = 0;

			// pick point that creates the largest angle
			Iterator<Point> iter = points.iterator();
			while (iter.hasNext()) {
				core.show();
				R = iter.next();
				if (R == Q || R == P) continue;
				core.show();
				
				double angle = Geometry.angleCCW(P, Q, R);
				if (angle >= 360) continue;
				double dist = Geometry.distance(Q, R);
				if ((angle < minAngle || (angle == minAngle && dist > maxDist))) {
				// if (angle <= minAngle && angle > prevAngle) {
				// if (angle < minAngle || dist > maxDist) {
				// if (angle < minAngle) {
					// found better point
					minAngle = angle;
					maxDist = dist;
					next = R;						
					}
				// }
			}
			
			// prevAngle =  Geometry.angleCCW(P, Q, next);
			P = Q; Q = next;
			if (next == start) break;
		}
		next = null;
		cleanup();
		core.show();
	}
}
