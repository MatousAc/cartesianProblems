public class algorithm {
	static Point start;
	static Point P = null;
	static Point Q = null;
	static Point R = null;
	
	/**
	 * Finds the leftmost lowest point in the set 
	 * as this point is definitely on the hull.
	 * @return starting point for the hull
	 */
	protected static Point findStartPoint() {
		Point leftBottom = core.points.get(0);
		for (Point p : core.points) {
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
		return Geometry.orientation(P, Q, R) == bend.COUNTERCLOCKWISE;
	}

	protected static void newPQR() {
		P = back(2); Q = back(1); R = back(0);
	}
	protected static Point back(int d) {
		return core.hull.get(core.hull.size() - (d + 1));
	}
	protected static void printCurrentState() {
		System.out.println("sol: " + core.hull);
		System.out.println("P: " + P + " Q: " + Q + " R: " + R);
	}
	protected static void cleanup() {
		P = null; Q = null; R = null; start = null;
		core.solved = true;
		core.show();
	}
	protected static void solPop() {
		core.hull.remove(core.hull.size() - 1);
	}
}
