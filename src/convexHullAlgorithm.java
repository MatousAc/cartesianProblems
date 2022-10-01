public class convexHullAlgorithm {
	protected static Point start;
	
	protected static Point findBasePoint() {
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
}
