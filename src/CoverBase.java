import java.util.ArrayList;

public class CoverBase {
	static Graph graph = new Graph();
	static ArrayList<Point> cover = new ArrayList<Point>();
	static Edge curEdge;

	static void cleanup() {
		curEdge = null;
		Core.show();
	}
}
