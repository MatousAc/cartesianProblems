import java.util.ArrayList;

/**
 * Contains two variants of a brute force solution 
 * to the minimum vertex cover problem. Use {@code exhaustive()}
 * for a traditional approach, and {@code increasingSize()} for 
 * a slightly faster variant.
 * */
public class Exact extends CoverBase {
	static ArrayList<Point> currentCover = new ArrayList<Point>();
	/**
	 * Solves the minimum vertex cover problem in 
	 * {@code CoverBase.vertices} and {@code CoverBase.edges}
	 * in O(2^V*E) time. {@code exhaustive()} considers every
	 * possible subset of {@code Hullbase.vertices} and finds
	 * the smallest cover possible.
	 */
	public static void exhaustive() {
		if  (edges.size() == 0) return;
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

	/**
	 * Solves the minimum vertex cover problem in 
	 * {@code CoverBase.vertices} and {@code CoverBase.edges}
	 * in O(2^V*E) time (worst case). {@code exhaustive()} 
	 * considers every possible subset of {@code Hullbase.vertices}
	 * in order of increasing size. Thus, as soon as any cover 
	 * is found, it is guaranteed to be the minimum.
	 */
	public static void increasingSize() {
		if  (edges.size() == 0) return;
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

	/**
	 * Puts all subsets of size {@code k} in {@code result}.
	 * @param points - the complete set of point (as ArrayList)
	 * @param k - the number of elements in the subsets
	 * @param idx
	 * @param curArr
	 * @param result - where to put the subsets.
	 */
	public static void kSubsets(ArrayList<Point> points, int k, int idx, 
		ArrayList<Point> curArr, ArrayList<ArrayList<Point>> result) {
		if (k <= 0) {
			ArrayList<Point> tmp = new ArrayList<Point>(curArr);
			result.add(tmp);
			return;
		}
		for (int i = idx; i < points.size(); i++) {
			curArr.add(points.get(i));
			kSubsets(points, k - 1, i + 1, curArr, result);
			curArr.remove(curArr.size() - 1);
		}
	}
}
