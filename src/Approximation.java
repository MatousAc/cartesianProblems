import java.util.HashSet;
import java.util.Set;

public class Approximation extends CoverBase {
	static Set<Edge> edgeSet;

	/**
	 * approximates the minimal vertex cover. it is 
	 * at most too large by a factor of two, though
	 * this is pretty large. runs much faster than the 
	 * brute force method
	 */
	public static void twoFactor() {
		edgeSet = new HashSet<Edge>(edges);

		while (edgeSet.size() > 0) {
			curEdge = Utility.randomSetElem(edgeSet);
			Core.show();
			cover.addAll(curEdge);
			removeIncidentEdges(curEdge);
		}
		cleanup();
	}

	/**
	 * approximates the minimal vertex cover a little
	 * better than {@code twoFactor} by only adding 
	 * one vertex from the selected edge
	 */
	public static void oneByOne() {
		edgeSet = new HashSet<Edge>(edges);

		while (edgeSet.size() > 0) {
			curEdge = Utility.randomSetElem(edgeSet);
			Core.show();
			Point u = curEdge.iterator().next();
			cover.add(u);
			Core.show();
			HashSet<Point> hs = new HashSet<Point>();
			hs.add(u);
			removeIncidentEdges(hs);
		}
		cleanup();
	}

	static void removeIncidentEdges(HashSet<Point> uv) {
		removeIncidentEdges(uv, edgeSet);
	}
}
