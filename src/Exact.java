import java.util.ArrayList;

public class Exact extends CoverBase {
	static ArrayList<Point> currentCover = new ArrayList<Point>();
	
	public static void exhaustive() {
		cover = (ArrayList<Point>) vertices.clone();
		int n = vertices.size();
		int cardinality = (1 << n);
		for (int i = 1; i < cardinality; i++) {
			for (int j = 0; j < n; j++) {
				if ((i & (1 << j)) > 0) { //The j-th element is used
					currentCover.add(vertices.get(j));
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

	public static void increasingSize() {
		int n = vertices.size();
		for (int i = 1; i <= n; i++) {
			int left = i; // specifies the subset size
			int idx = 0;
			ArrayList<Point> curArr = new ArrayList<>();
			ArrayList<ArrayList<Point>> result = new ArrayList<>(); // contains all subsets size k
			kSubsets(vertices, left, idx, curArr, result);
			for (ArrayList<Point> subset : result) {
				cover = subset; Core.show();
				if (isCover(cover)) {
					cleanup();
					return;
				}
			}
		}
	}

	public static void kSubsets(ArrayList<Point> points, int left, int idx, 
		ArrayList<Point> curArr, ArrayList<ArrayList<Point>> result) {
		if (left <= 0) {
			ArrayList<Point> tmp = new ArrayList<Point>(curArr);
			result.add(tmp);
			return;
		}
		for (int i = idx; i < points.size(); i++) {
			curArr.add(points.get(i));
			kSubsets(points, left - 1, i + 1, curArr, result);
			curArr.remove(curArr.size() - 1);
		}
	}
}
