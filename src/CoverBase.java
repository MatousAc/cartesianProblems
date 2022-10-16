import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import enums.*;

/** Base class for a minimum vertex cover solver or approximator. */
public class CoverBase {
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

	/**
	 * Conducts an automated test of all algorithms that
	 * can be used to solve the minimum vertex cover 
	 * problem. The size of the problem increases by one
	 * for each iteration and densities from 0-1 are tried
	 * for each. Every size-density combination cycles through
	 * every VcAlg algorithm.
	 */
	protected static void test() {
		ArrayList<VcAlg> algs = new ArrayList<VcAlg>(EnumSet.allOf(VcAlg.class));
		Core.genFx = GenFx.RECTANGULAR;
		
		for (int n = 2; n <= Core.maxSize; n++) {
			Generator.density = 0;
			Generator.N = n;
			while (Generator.density < 1) {
				Generator.densityUp();
				Generator.generateProblem();
				for (VcAlg a : algs) {
					Core.vcAlg = a;
					try {
						Core.timedTest(n);
					} catch (OutOfMemoryError e) {
						System.out.printf(
							"Ran out of memory while using %s to solve a " + 
							"graph with %d vertices at %s density.\n", 
							Core.vcAlg.toString(),
							vertices.size(), 
							graphDensity()
						);
					}
				}
				Core.reset();
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
	 * in {@code CoverBase.edges}
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
		Core.solved = false;
		curEdge = null;
		rmEdge = null;
		u = null;
		v = null;
		Core.show();
	}

	// helper f(x)s
	/** @return the size of  {@code CoverBase.vertices} as a String. */
	public static String vertexCount() {
		return ((Integer) vertices.size()).toString();
	}
	/** @return the size of  {@code CoverBase.edges} as a String. */
	public static String edgeCount() {
		return ((Integer) edges.size()).toString();
	}
	/** Returns the size of  {@code CoverBase.cover} as a String. */
	public static String coverSize() {
		return ((Integer) cover.size()).toString();
	}
	/** @return the density of the graph represented by of 
	 * {@code CoverBase.vertices} and 
	 * {@code CoverBase.edges} as a {@code String}.
	 */
	public static String graphDensity() {
		int v = vertices.size();
		int e = edges.size();
		double density = (double) e / (v * (v - 1) / 2);
		return String.format("%.04f", density);
	}
}
