import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Utility {
	/**
	 * Returns a deep clone of the ArrayList of Points passed in.
	 * @param points
	 * @return deep clone
	 */
	public static ArrayList<Point> deepClone(ArrayList<Point> points) {
    ArrayList<Point> clone = new ArrayList<Point>(points.size());
    for (Point p : points) {
        clone.add(new Point(p));
    }
    return clone;
	}

  /** Determines whether an arraylist of edges
   * already contains an edge defined by the same two points.
   */
	public static boolean containsDeep(ArrayList<Edge> edges, Edge newEdge) {
		for (Edge edge : edges) {
			if (newEdge.equals(edge)) {
				return true;
			}
		}
		return false;
	}

	/** Determines whether the ArrayList of Points contains duplicates. */
	public static boolean containsDuplicates(ArrayList<Point> points) {
		Set<Point> set = new HashSet<Point>(points);
		return set.size() < points.size();
	}

	/**
	 * Determines whether the ArrayList of Points contains 
	 * points with negative coordinates.
	 */
	public static boolean containsNegatives(ArrayList<Point> points) {
		for (Point p : points) {
			if (p.x < 0 || p.y < 0) {
				return true;
			}
		}
		return false;
	}

  /**
   * Chooses a random element from a set.
   * From geeksforgeeks.com.
   * @param <E> set implementation type
   * @param set
   * @return a random set element
   */
	static <E> E randomSetElem(Set<? extends E> set) { 
		Random random = new Random();
		int randomNumber = random.nextInt(set.size()); 
		Iterator<? extends E> iterator = set.iterator(); 

		int currentIndex = 0; 
		E randomElement = null; 
		// iterate the HashSet 
		while (iterator.hasNext()) { 
				randomElement = iterator.next(); 
				// if current index is equal to random number 
				if (currentIndex == randomNumber) 
						return randomElement; 
				// increase the current index 
				currentIndex++; 
		}
	  return randomElement; 
  } 

  /**
	 * Writes specified data to performance.csv.
	 * @param data data to be written
	 */
  public static void dataWrite(String data) {
    dataWrite(data, "performance.csv");
  }

	/**
	 * Writes specified data to the specified a file.
	 * @param data data to be written
	 */
	public static void dataWrite(String data, String filename) {
		try {
			FileWriter fw = new FileWriter(filename, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.flush();
			bw.close();
		} catch (IOException e) {
      e.printStackTrace();
    }
	}
}
