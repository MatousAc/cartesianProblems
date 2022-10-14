import java.util.ArrayList;
import java.util.EnumSet;

import enums.*;

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

	/**
	 * cleans up the convex hull problem once
	 * the solution is found
	 */
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
	
	/**
	 * conducts an automated test of all algorithms that
	 * can be used to solve the Convex Hull problem. the
	 * size of the problem doubles over each iteration.
	 * every combination of generation function and algorithm
	 * is tried for all sizes
	 */
	protected void test() {
		ArrayList<ChAlg> algs = new ArrayList<ChAlg>(EnumSet.allOf(ChAlg.class));
		ArrayList<GenFx> styles = new ArrayList<GenFx>(
			EnumSet.allOf(GenFx.class)
		);
		
		for (int size = 4; size < Core.genSize; size *= 2) {
			for (GenFx s : styles) {
				Core.genFx = s;
				for (ChAlg a : algs) {
					Core.chAlg = a;
						Core.timedTest(size);
				}
			}
		}
	}

	// debugging f(x)s
	protected static void printCurrentState() {
		System.out.println("hull: " + hull);
		System.out.println("P: " + P + " Q: " + Q + " R: " + R);
	}

	protected static void solPop() {
		hull.remove(hull.size() - 1);
	}
	public static String pointCount() {
		return ((Integer) points.size()).toString();
	}
	public static String hullSize() {
		return ((Integer) hull.size()).toString();
	}
}
