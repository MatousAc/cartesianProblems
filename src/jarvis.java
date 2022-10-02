// code from Andre Violentyev was used to write part of this algorithm
// https://bitbucket.org/StableSort/play/src/master/src/com/stablesort/convexhull/ConvexHullJarvisMarch.java
import java.util.ArrayList;

public class jarvis extends hullSolver{
	public static void march() {
		// setup
		ArrayList<Point> points = core.points;
		ArrayList<Point> hull = core.hull;
		start = findStartPoint();
		hull.add(start);
		
		P = start;
		float prevAngle = -1;
		while (true) {
			float minAngle = Float.MAX_VALUE;
			double maxDist = 0;
			Point next = null;
		
			// iterate over every point and pick the one that creates the largest angle
			for (Point q : points) {
				if (q == P) continue;
				Q = q; core.show();
				
				float angle = Geometry.angle(P, q);
				double dist = Geometry.distance(P, q);
				int compareAngles = Float.compare(angle, minAngle);
				
				if (compareAngles <= 0 && angle > prevAngle) {
					if (compareAngles < 0 || dist > maxDist) {
						/*
						 * found a better Point. It either has a smaller angle, or if it's collinear, then it's further way
						 */
						minAngle = angle;
						maxDist = dist;
						next = q;						
					}
				}
			}
			
			if (next == start) break; // came back to the starting point, so we are done
			
			hull.add(next);
			
			prevAngle =  Geometry.angle(P, next);
			P = next;			
		}	
	}
}
