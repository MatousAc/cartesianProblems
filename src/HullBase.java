import java.util.ArrayList;
import java.util.EnumSet;
import enums.*;

/** Base class for a convex hull solver. */
public class HullBase {
	/** The points that make up the current convex hull problem. */
	static ArrayList<Point> points = new ArrayList<Point>();
	/**
	 * The list of points along the convex hull for the current
	 * problem, {@code HullBase.points}
	 */
	static ArrayList<Point> hull = new ArrayList<Point>();
	/**
	 * The leftmost lowest point in the problem. This is 
	 * guaranteed to be on the hull, so it is used as a
	 * "starting point" to traversing the rest of the problem.
	 */
	static Point start;
	/** The first point of interest for a convex hull algorithm.*/
	static Point P = null;
	/** The first point of interest for a convex hull algorithm. */
	static Point Q = null;
	static Point R = null;
	
		/**
	 * Conducts an automated test of all algorithms that
	 * can be used to solve the Convex Hull problem. The
	 * size of the problem doubles over each iteration.
	 * Every combination of generation function and 
	 * algorithm is tried for each size.
	 */
	protected static void test() {
		ArrayList<ChAlg> algs = new ArrayList<ChAlg>(EnumSet.allOf(ChAlg.class));
		ArrayList<GenFx> styles = new ArrayList<GenFx>(
			EnumSet.allOf(GenFx.class)
		);
		
		for (int size = 4; size < Core.genSize; size *= 2) {
			for (GenFx s : styles) {
				Core.genFx = s;
				Generator.generateProblem(size);
				for (ChAlg a : algs) {
					Core.chAlg = a;
					Core.timedTest(size);
				}
				Core.reset();
			}
		}
	}

	/**
	 * Finds the leftmost lowest point in the set.
	 * It is used as the "starting point" from all 
	 * convex hull algorithms, as this point is 
	 * guaranteed to be on the hull.
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
	 * Cleans up the HullBase's intermediate data 
	 * structures once the convex hull is found.
	 */
	protected static void cleanup() {
		start = null;
		P = null;
		Q = null;
		R = null;
		Core.solved = true;
		Core.show();
	}

	/**
	 * @param i
	 * @return {@code i}th element from back of the current hull.
	 */
	protected static Point back(int i) {
		return hull.get(hull.size() - (i + 1));
	}

	/** @return Number of points in problem as {@code String}. */
	public static String pointCount() {
		return ((Integer) points.size()).toString();
	}
	/** @return Number of points in hull as {@code String}. */
	public static String hullSize() {
		return ((Integer) hull.size()).toString();
	}

	// debugging f(x)s
	protected static void printCurrentState() {
		System.out.println("hull: " + hull);
		System.out.println("P: " + P + " Q: " + Q + " R: " + R);
	}

	/** Removes last element from hull. */
	protected static void solPop() {
		hull.remove(hull.size() - 1);
	}
}