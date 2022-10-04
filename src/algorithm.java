import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Algorithm {
	static Point start;
	static Point P = null;
	static Point Q = null;
	static Point R = null;
	
	/**
	 * Finds the leftmost lowest point in the set 
	 * as this point is definitely on the hull.
	 * @return starting point for the hull
	 */
	protected static Point findStartPoint(ArrayList<Point> points) {
		Point leftBottom = points.get(0);
		for (Point p : points) {
			if (p.x < leftBottom.x) {
				leftBottom = p;
			} else if (p.x == leftBottom.x && p.y < leftBottom.y) { 
				leftBottom = p;
			}
		}
		return leftBottom;
	}
	
	/**
	 * Determines if the lines make a counterclockwise angle
	 * @param P
	 * @param Q
	 * @param R
	 * @return true | false
	 */
	protected static boolean bendsCCW(Point P, Point Q, Point R) {
		return Geometry.orientation(P, Q, R) == Bend.COUNTERCLOCKWISE;
	}

	protected static void newPQR() {
		P = back(2); Q = back(1); R = back(0);
	}
	protected static Point back(int d) {
		return Core.hull.get(Core.hull.size() - (d + 1));
	}
	protected static void cleanup() {
		P = null; Q = null; R = null; start = null;
		Core.solved = true;
		Core.show();
	}

	public static ArrayList<Point> deepClone(ArrayList<Point> points) {
    ArrayList<Point> clone = new ArrayList<Point>(points.size());
    for (Point p : points) {
        clone.add(new Point(p));
    }
    return clone;
}

	public static boolean containsDuplicates(ArrayList<Point> points) {
		Set<Point> set = new HashSet<Point>(points);
		return set.size() < points.size();
	}

	protected static void printCurrentState() {
		System.out.println("sol: " + Core.hull);
		System.out.println("P: " + P + " Q: " + Q + " R: " + R);
	}
	protected static void solPop() {
		Core.hull.remove(Core.hull.size() - 1);
	}
}
