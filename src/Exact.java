import java.util.ArrayList;

public class Exact extends CoverBase {
	static ArrayList<Point> currentCover = new ArrayList<Point>();
	
	public static void exhaustive() {
		cover = (ArrayList<Point>) graph.vertices.clone();
		int n = graph.vertices.size();
		int cardinality = (1 << n);
		for (int i = 1; i < cardinality; i++) {
			
			for (int j = 0; j < n; j++) {
				if ((i & (1 << j)) > 0) { //The j-th element is used
					currentCover.add(graph.vertices.get(j));
					Core.show();
				}
			}
			Core.show();
			if (currentCover.size() < cover.size() &&
				isCover(currentCover)) {
				cover = (ArrayList<Point>) currentCover.clone();
				Core.show();
			}
			currentCover.clear();
			curEdge = null;
		}
		currentCover.clear();
		cleanup();
	}

	public static void increasingOrder() {
		cover = (ArrayList<Point>) graph.vertices.clone();
		int n = graph.vertices.size();
		int cardinality = (1 << n);
		for (int i = 1; i < cardinality; i++) {
			
			for (int j = 0; j < n; j++) {
				if ((i & (1 << j)) > 0) { //The j-th element is used
					currentCover.add(graph.vertices.get(j));
					Core.show();
				}
			}
			Core.show();
			if (currentCover.size() < cover.size() &&
				isCover(currentCover)) {
				cover = (ArrayList<Point>) currentCover.clone();
				Core.show();
			}
			currentCover.clear();
			curEdge = null;
		}
		currentCover.clear();
		cleanup();
	}
}
