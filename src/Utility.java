import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Utility {
	public static ArrayList<Point> deepClone(ArrayList<Point> points) {
    ArrayList<Point> clone = new ArrayList<Point>(points.size());
    for (Point p : points) {
        clone.add(new Point(p));
    }
    return clone;
}

	public static boolean containsDuplicates(ArrayList<Point> points) {
		Set<Point> set = new HashSet<Point>(points);
		return set.size() < points.size();
	}

	public static boolean containsNegatives(ArrayList<Point> points) {
		for (Point p : points) {
			if (p.x < 0 || p.y < 0) {
				return true;
			}
		}
		return false;
	}

}
