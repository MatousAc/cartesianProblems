import java.util.ArrayList;
import java.util.Iterator;

public class CoverBase {
	static Graph graph = new Graph();
	static ArrayList<Point> cover = new ArrayList<Point>();
	static Edge curEdge;

	static boolean isCover(ArrayList<Point> cover) {
		for (Edge edge : graph.edges) {
			curEdge = edge; Core.show();
			Iterator<Point> i = edge.iterator();
			Point u = i.next();
			Point v = i.next();
			if (!(cover.contains(u) || cover.contains(v))) {
				return false;
			}
		}
		return true;
	}

	static void cleanup() {
		curEdge = null;
		Core.show();
	}
}
