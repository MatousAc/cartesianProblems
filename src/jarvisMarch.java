import java.util.ArrayList;

public class jarvisMarch extends convexHullAlgorithm{
	private static Point start;

	public static void makeHull() {
		ArrayList<Point> points = hull.points;
		ArrayList<Point> sol = hull.solution;
		start = findBasePoint();
	}
}
