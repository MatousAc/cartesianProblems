import java.util.ArrayList;

/** A parent class for Cartesian Problems. */
public interface Problem {

  /** Delegates solving to an algorithm. */
  public void solve();

	/** Clears the solution and intermediate data for a problem. */
  public void unsolve();

	/** Resets current problem. */
	public void reset();

	/**
	 * Conducts an automated test of all algorithms that
	 * can be used to solve the current problem type.
   * The size of the problem increases each iteration.
	 * Every combination of other relevant hyperparameters
   * is tried for each size and algorithm.
	 */
  public void test();

  /// utility f(x)s ///
  /** For automatic testing and data generation,
   * returns the CSV header line for this problem.
   */
  public String getDataHead();

	/**
	 * Generates a cvs-format string of information 
   * from the latest solve including algorithm, 
   * generation f(x), solution size, problem size and others.
	 * @param duration the time elapsed during the latest solve
	 * @return string of comma-separated data
   */
  public String getData(Double duration);

	/** Moves to the next algorithm that can solve this problem. */ 
	public void nextAlg();

	/**
	 * Adds a point when user interacts with UI.
	 * Unsolves problem.
	 * @param p point to add
	 */
	public void addPoint(Point p);

  /**
	 * Removes a point from the problem.
	 * Unsolves problem.
	 * @param p point to remove
	 */
	void removePoint(Point p);

	/** @return the current problem as a String */
  public String probAsString();

	/** @return the current algorithm being used by as a String */
	public String algAsString();
  
	/** @return the current heuristic being used by as a String */
  public String heurAsString();

  /** @return the destination to which points should be added  */
  public ArrayList<Point> getPoints();

  /// delegation f(x)s ///
  /** Delegates what to do to draw the current problem. */
  public void paint(Canvass canvass);
}
