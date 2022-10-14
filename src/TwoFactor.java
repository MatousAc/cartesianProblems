import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TwoFactor extends CoverBase {
	static Set<Edge> edgeSet;
	static Edge rmEdge;

	/**
	 * approximates the minimal vertex cover by a
	 * factor of two. runs much faster than the 
	 * brute force method, though it over-approximates
	 * the minimal vertex cover significantly
	 */
	public static void approximation() {
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
	 * removes any edges from edgeSet that are incident on the 
	 * edge passed in. any edges connected to any of the vertices
	 * on either end of Edge {u, v} will be removed from edgeSet
	 * @param uv
	 */
	static void removeIncidentEdges(HashSet<Point> uv) {
		Iterator<Edge> i = edgeSet.iterator();
		while (i.hasNext()) {
			rmEdge = i.next();
			if (rmEdge.containsAny(uv)) i.remove();
			Core.show();
		}
		rmEdge = null;
	}
}
