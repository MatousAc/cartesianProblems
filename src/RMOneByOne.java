import java.util.HashSet;

public class RMOneByOne extends TwoFactor {
	/**
	 * approximates the minimal vertex cover by a
	 * factor of two. tends to have slightly smaller
	 * solutions by only adding one vertex from the
	 * selected edge
	 */
	public static void approximation() {
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
}
