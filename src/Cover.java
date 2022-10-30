import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import enums.*;

/** Base class for a minimum vertex cover solver or approximator. */
public class Cover implements Problem {
  /** Indicates which algorithm is being used to solve. */
	static VcAlg alg = VcAlg.EXACT_EXHAUSTIVE;
	/** The list of vertices V in graph {V, E}. */
	static ArrayList<Point> vertices = new ArrayList<Point>();
	/** The list of edges E in graph {V, E}. */
	static ArrayList<Edge> edges = new ArrayList<Edge>();
	/**
	 * A list of vertices that cover the graph 
	 * {{@code vertices}, {@code edges}}. At the end of a 
	 * minimum vertex cover algorithm, this list is the minimum
	 * vertex cover of the graph or an approximation of it.
	 */
	static ArrayList<Point> cover = new ArrayList<Point>();
	/** Edge of current interest in a minimum vertex cover algorithm. */
	static Edge curEdge;
	/** An edge that is being removed in some way from the graph. */
	static Edge rmEdge;
	/** A point of interest in a minimum vertex cover algorithm. */
	static Point u;
	/** A point of interest in a minimum vertex cover algorithm. */
	static Point v;

  public void solve() {
    Core.isSolving = true;
		unsolve();
    if (vertices.size() == 0) {
      System.out.println("No vertices present. Solve aborted.");
    } else {
      switch (alg) {
        case EXACT_EXHAUSTIVE: Exact.exhaustive(); break;
        case EXACT_INCREASING_SIZE: Exact.increasingSize(); break;
        case EXACT_ALEX_OPTIMIZATION: Exact.alexOptimization(); break;
        case APPROXIMATION_ONE_BY_ONE: Approximation.oneByOne(); break;
        case APPROXIMATION_TWO_FACTOR: Approximation.twoFactor(); break;
      }
    }
    Core.isSolving = false;
  }

  public void unsolve() {
		Core.isSolved = false;
    cover.clear();
		curEdge = null;
		rmEdge = null;
		u = null;
		v = null;
  }

	public void reset() {
		Core.isSolving = false;
		unsolve();
		vertices.clear();
		edges.clear();
		Core.show();
	}

	public void test() {
		ArrayList<VcAlg> algs = 
      new ArrayList<VcAlg>(EnumSet.allOf(VcAlg.class));
		Generator.fx = GenFx.RECTANGULAR;
		
    Utility.dataWrite(getDataHead());
		for (int n = 2; n <= Core.maxSize; n++) {
			Generator.density = 0;
			Generator.N = n;
			while (Generator.density < 1) {
				Generator.densityUp();
				Generator.generateProblem();
				for (VcAlg a : algs) {
					alg = a;
					try {
						Core.timedTest(n);
					} catch (OutOfMemoryError e) {
						System.out.printf(
							"Ran out of memory while using %s to solve a " + 
							"graph with %d vertices at %s density.\n", 
							alg.toString(),
							vertices.size(), 
							graphDensity()
						);
					}
				}
				reset();
			}
		}
	}

	/**
	 * Removes any edges from {@code rmFrom} that are 
	 * incident on the {@code HashSet<Point> uv} passed in.
	 * @param hs
	 * @param rmFrom
	 */
	static void removeIncidentEdges(HashSet<Point> hs, Collection<Edge> rmFrom) {
		newUV();
		Iterator<Edge> i = rmFrom.iterator();
		while (i.hasNext()) {
			rmEdge = i.next();
			if (rmEdge.containsAny(hs)) i.remove();
			Core.show();
		}
		rmEdge = null;
	}
	
	/**
	 * Determines if the candidate subset covers all edges
	 * in {@code edges}
	 * @param candidate
	 * @return {@code true} if candidate covers. otherwise {@code false} 
	 */
	static boolean isCover(ArrayList<Point> candidate) {
		for (Edge edge : edges) {
			curEdge = edge;
			newUV(); Core.show();
			if (!(candidate.contains(u) || candidate.contains(v))) {
				return false;
			}
		}
		return true;
	}

	/** Assigns points from curEdge into {@code CoverBase.u} and {@code CoverBase.v} */
	static void newUV() {
		if (curEdge == null) return;
		Iterator<Point> i = curEdge.iterator();
		u = i.next();
		v = i.next();
	}

	/**
	 * Cleans up the {@code CoverBase}'s intermediate data 
	 * structures once the minimum vertex cover (or
	 * its approximation) is found.
	 */
	static void cleanup() {
		curEdge = null;
		rmEdge = null;
		u = null;
		v = null;
		Core.isSolved = true;
		Core.show();
	}

	/// utility f(x)s ///
  public String getDataHead() {
    return "algorithm,vertex count,edge count," + 
    "density,cover size,duration (s)\n";
  }

  public String getData(Double duration) {
    DecimalFormat df = new DecimalFormat("#.#");
    df.setMaximumFractionDigits(10);

    return String.join(",", 
      alg.toString(),
      vertexCount(),
      edgeCount(),
      graphDensity(),
      coverSize(),
      df.format(duration) + "\n"
    );
  }

	public void nextAlg() {
		alg = alg.next();
	}

	public void addPoint(Point p) {
		vertices.add(p);
		unsolve();
	}

  public void removePoint(Point p) {
		vertices.remove(p);
		HashSet<Point> hs = new HashSet<Point>();
		hs.add(p);
		removeIncidentEdges(hs, Cover.edges);
		unsolve();
	}

  public String probAsString() { return "minimum vertex cover"; }
  public String algAsString() { return alg.toString(); }
  public String heurAsString() { return "no heuristic"; }
  public ArrayList<Point> getPointDestination() { return vertices; }

	/** @return the size of  {@code vertices} as a String. */
	public static String vertexCount() {
		return ((Integer) vertices.size()).toString();
	}
	/** @return the size of  {@code edges} as a String. */
	public static String edgeCount() {
		return ((Integer) edges.size()).toString();
	}
	/** Returns the size of  {@code cover} as a String. */
	public static String coverSize() {
		return ((Integer) cover.size()).toString();
	}
	/** @return the density of the graph represented by of 
	 * {@code vertices} and 
	 * {@code edges} as a {@code String}.
	 */
	public static String graphDensity() {
		int v = vertices.size();
		int e = edges.size();
		double density = (double) e / (v * (v - 1) / 2);
		return String.format("%.04f", density);
	}
}
