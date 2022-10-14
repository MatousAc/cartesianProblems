import java.util.ArrayList;

public class Brute extends CoverBase {
	public static void force() {
		cover = (ArrayList<Point>) graph.vertices.clone();
		for (ArrayList<Point> sub : subsets(graph.vertices)) {
			if (sub.size() < cover.size() && isCover(sub)) {
				cover = sub;
			}
		}
	}

	static ArrayList<ArrayList<Point>> subsets(ArrayList<Point> vertices) {
		return null;
	}

	static boolean isCover(ArrayList<Point> vertices) {

		return false;
	}
}
