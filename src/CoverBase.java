import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoverBase {
	static ArrayList<Point> vertices = new ArrayList<Point>();
	static ArrayList<Edge> edges = new ArrayList<Edge>();
	static ArrayList<Point> cover = new ArrayList<Point>();
	static Edge curEdge;
	static Edge rmEdge;
	static Point u;
	static Point v;

		/**
	 * removes any edges from edgeSet that are incident on the 
	 * HashSet passed in. any edges connected to any of the vertices
	 * specified will be removed from edges.
	 * @param uv
	 * @param rmFrom
	 */
	static void removeIncidentEdges(HashSet<Point> uv, Collection<Edge> rmFrom) {
		newUV();
		Iterator<Edge> i = rmFrom.iterator();
		while (i.hasNext()) {
			rmEdge = i.next();
			if (rmEdge.containsAny(uv)) i.remove();
			Core.show();
		}
		rmEdge = null;
	}
	
	static boolean isCover(ArrayList<Point> cover) {
		for (Edge edge : edges) {
			curEdge = edge;
			Core.show();
			newUV();
			if (!(cover.contains(u) || cover.contains(v))) {
				return false;
			}
		}
		return true;
	}

	static void newUV() {
		Iterator<Point> i = curEdge.iterator();
		u = i.next();
		v = i.next();
	}

	static void cleanup() {
		curEdge = null;
		u = null; v = null;
		Core.show();
	}

	// helpers
	public static String vertexCount() {
		return ((Integer) vertices.size()).toString();
	}
	public static String edgeCount() {
		return ((Integer) edges.size()).toString();
	}
	public static String coverSize() {
		return ((Integer) cover.size()).toString();
	}
	public static String graphDensity() {
		int v = vertices.size();
		int e = edges.size();
		double density = v*(v - 1)/e;
		return String.format("%.04f", density);
	}
}
