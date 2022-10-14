import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Approximation extends CoverBase {
	static Set<Edge> edgeSet;
	static Edge rmEdge;

	/**
	 * approximates the minimal vertex cover. it is 
	 * at most too large by a factor of two, though
	 * this is pretty large. runs much faster than the 
	 * brute force method
	 */
	public static void twoFactor() {
		edgeSet = new HashSet<Edge>(graph.edges);

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
		edgeSet = new HashSet<Edge>(graph.edges);

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

	/**
	 * removes any edges from edgeSet that are incident on the 
	 * edge passed in. any edges connected to any of the vertices
	 * on either end of Edge {u, v} will be removed from edgeSet
	 * @param uv
	 */
	static void removeIncidentEdges(HashSet<Point> uv) {
		newUV();
		Iterator<Edge> i = edgeSet.iterator();
		while (i.hasNext()) {
			rmEdge = i.next();
			if (rmEdge.containsAny(uv)) i.remove();
			Core.show();
		}
		rmEdge = null;
	}
}
