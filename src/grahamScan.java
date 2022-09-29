import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
 
public class grahamScan {
	private enum bend { NONE, CLOCKWISE, COUNTERCLOCKWISE }
	private static Point start;
	public static void makeHull() {
		ArrayList<Point> points = hull.points;
		ArrayList<Point> res = hull.solution;
		start = findLeftBottomPoint();
		// Collections.swap(hull.points, hull.points.indexOf(start), 0);
		hull.addLeftBottom(start);
		hull.show();

		Collections.sort(points, compareBySlope); // uses custom comparer
		// construct solution
		res.add(points.get(0));
		res.add(points.get(1));
		int size = res.size();
		for (int i = 2; i < points.size(); i++) {
			size = res.size();
			Point R = points.get(i);
			if (orientation(res.get(size - 2), getQ(), R) == bend.COUNTERCLOCKWISE) {
				res.add(R);
			} else {
				// sol.set(sol.size() - 1, R);
				res.remove(size - 1);
				i--;
			}
			hull.show();
		}
		
	}

	private static Point findLeftBottomPoint() {
		Point leftBottom = hull.points.get(0);
		for (Point p : hull.points) {
			if (p.x < leftBottom.x) {
				leftBottom = p;
			} else if (p.x == leftBottom.x && p.y < leftBottom.y) { 
				leftBottom = p;
			}
		}
		return leftBottom;
	}

	// compares using slope relative to start point. start point's relative slope is -1
	static Comparator<Point> compareBySlope = (Point p1, Point p2) ->
		((Double) Geometry.slope(start, p1)).compareTo(Geometry.slope(start, p2));

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
	
	static Point getP() { return hull.solution.get(hull.solution.size() - 2); }
	static Point getQ() { return hull.solution.get(hull.solution.size() - 1); }
	
	}
