import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.awt.Graphics2D;
import enums.*;

/** Base class for a convex hull solver. */
public class Hull implements Problem {
  /** Identifies what algorithm is being used. */
	static ChAlg alg = ChAlg.JARVIS_MARCH;
  /** What heuristic is being used. */
  static ChHeur heuristic = ChHeur.NO_HEURISTIC;
	/** The points that make up the current convex hull problem. */
	static ArrayList<Point> points = new ArrayList<Point>();
	/**
	 * The list of points along the convex hull for the current
	 * problem, {@code points}
	 */
	static Polygon hull = new Polygon();
	/**
	 * The leftmost lowest point in the problem. This is 
	 * guaranteed to be on the hull, so it is used as a
	 * "starting point" to traversing the rest of the problem.
	 */
	static Point start;
	/** The first point of interest for a convex hull algorithm.*/
	static Point P = null;
	/** The second point of interest for a convex hull algorithm. */
	static Point Q = null;
	/** The third point of interest for a convex hull algorithm. */
	static Point R = null;
  /** A Polygon used for the Akl-Toussaint heuristic. */
  static Polygon poly = new Polygon();
	
  public void solve() {
    Core.isSolving = true;
		unsolve();
    if (points.size() < 3) {
      System.out.println("Convex hull impossible. Solve Aborted.");
    } else {
      switch (alg) {
        case JARVIS_MARCH: Jarvis.march(); break;
        case GRAHAM_SCAN: Graham.scan(); break;
      }
    }
    hull.close();
		cleanup();
    Core.show();
		Core.isSolving = false;
  }

  public void unsolve() {
		Core.isSolved = false;
    hull.erase();
		start = null;
		P = null;
		Q = null;
		R = null;
    poly.erase();
  }

  public void reset() {
		Core.isSolving = false;
		unsolve();
		points.clear();
		Core.show();
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
    poly.erase();
		Core.isSolved = true;
		Core.show();
	}

	public void test() {
		ArrayList<ChAlg> algs = 
      new ArrayList<ChAlg>(EnumSet.allOf(ChAlg.class));
		ArrayList<GenFx> styles = 
      new ArrayList<GenFx>(EnumSet.allOf(GenFx.class));
    ArrayList<ChHeur> heuristics = 
      new ArrayList<ChHeur>(EnumSet.allOf(ChHeur.class));
		
    Utility.dataWrite(getDataHead());
		for (int n = 4; n < Core.maxSize; n *= 2) {
			Generator.N = n;
			for (GenFx s : styles) {
				Generator.fx = s;
				Generator.generateProblem();
				for (ChAlg a : algs) {
					alg = a;
          for (ChHeur h : heuristics) {
            heuristic = h;
  					Core.timedTest(n);
          }
				}
				reset();
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

  protected static void aklToussaint(ArrayList<Point> points) {
    poly.erase();
    Point xMin = Collections.min(points, xCompare);
    Point yMin = Collections.min(points, yCompare);
    Point xMax = Collections.max(points, xCompare);
    Point yMax = Collections.max(points, yCompare);
    poly.add(xMin); Core.show();
    poly.add(yMin); Core.show();
    poly.add(xMax); Core.show();
    poly.add(yMax); Core.show();
    poly.close(); Core.show();

    Iterator<Point> iter = points.iterator();
    while (iter.hasNext()) {
      if (poly.surrounds(iter.next())) { 
        iter.remove();
      }
    }
  }
  
 static Comparator<Point> xCompare = (Point p1, Point p2) -> 
   ((Double)p1.x).compareTo(p2.x);

  static Comparator<Point> yCompare = (Point p1, Point p2) -> 
    ((Double)p1.y).compareTo(p2.y);

  /// utility f(x)s ///
  public String getDataHead() {
    return "algorithm,generation style," + 
    "point count,hull size,heuristic,duration (s)\n";
  }

  public String getData(Double duration) {
    DecimalFormat df = new DecimalFormat("#.#");
    df.setMaximumFractionDigits(10);

		return String.join(",", 
			alg.toString(),
			Generator.fx.toString(),
			pointCount(),
			hullSize(),
      heuristic.toString(),
			df.format(duration) + "\n"
		);
  }

	public void nextAlg() { alg = alg.next(); }

  public void addPoint(Point p) {
		points.add(p);
		unsolve();
	}

  public void removePoint(Point p) {
		points.remove(p);
		unsolve();
	}
	
  public String probAsString() { return "convex hull"; }
  public String algAsString() { return alg.toString(); }
  public String heurAsString() { return heuristic.toString(); }
  public ArrayList<Point> getPoints() { return points; }

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

  /// delegation f(x)s ///
  public void paint(Canvass c) {
    c.drawPolygon(poly, "lightBlue");
    c.drawPolygon(hull, "darkGreen");

    // drawing slopes
    c.setStroke(3);
		if (Graham.m1 != null && Graham.m2 != null) {
			c.setColor("red");
			c.drawLine(Graham.m1);
			c.drawLine(Graham.m2);
		}
    // drawing PQR Next
		if (Q != null && Jarvis.next != null) {
			c.setColor("purple");
			c.drawLine(Q, Jarvis.next);
    }
		if (Q != null && R != null) {
			c.setColor("red");
			c.drawLine(Q, R);
    }
		if (P != null && Q != null) {
			c.setColor("red");
			c.drawLine(P, Q);
    }
    c.graphicDefaults();
    
    // finally drawing points on top
    for (Point p : points) {
      if (p == start) {
			  c.drawPoint(p, "darkBlue", 1.5);
      } else if (p == P || p == Q || p == R) {
			  c.drawPoint(p, "gold", 1.08);
      } else if (p == Jarvis.next) {
			  c.drawPoint(p,"lightBlue", 1.5);
      } else if (hull.contains(p)) {
			  c.drawPoint(p,"lightGreen");
      } else {
        c.drawPoint(p);
      }
		}
  }

	// debugging f(x)s
	protected static void printCurrentState() {
		System.out.println("hull: " + hull);
		System.out.println("P: " + P + " Q: " + Q + " R: " + R);
	}

	/** Removes last element from hull. */
	protected static void hullPop() {
		hull.remove(hull.size() - 1);
	}
}
