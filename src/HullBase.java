import java.util.ArrayList;

public class HullBase {
	static ArrayList<Point> points = new ArrayList<Point>();
	static ArrayList<Point> hull = new ArrayList<Point>();
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

	protected static void cleanup() {
		P = null; Q = null; R = null; start = null;
		Core.solved = true;
		Core.show();
	}

	/**
	 * returns ith element from back of the current hull
	 * @param i
	 * @return
	 */
	protected static Point back(int i) {
		return hull.get(hull.size() - (i + 1));
	}
	
	// debugging f(x)s
	protected static void printCurrentState() {
		System.out.println("hull: " + hull);
		System.out.println("P: " + P + " Q: " + Q + " R: " + R);
	}

	protected static void solPop() {
		hull.remove(hull.size() - 1);
	}
}
