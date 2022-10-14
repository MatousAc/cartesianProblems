import java.util.ArrayList;
import java.util.Iterator;

public class CoverBase {
	static Graph graph = new Graph();
	static ArrayList<Point> cover = new ArrayList<Point>();
	static Edge curEdge;
	static Point u;
	static Point v;

	static boolean isCover(ArrayList<Point> cover) {
		for (Edge edge : graph.edges) {
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
}
