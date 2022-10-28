import java.util.ArrayList;
import java.util.Iterator;

class Polygon {
  private ArrayList<Point> points = new ArrayList<Point>();

  public boolean add(Point p) { return points.add(p); }

  public void erase() { points.clear(); }

  /** Returns Iterator to the points that define the Polygon */
  public Iterator<Point> iterator() { return points.iterator(); }

  public boolean isEmpty() { return points.size() == 0; }

  /**
   * Return true if the given point is contained inside the boundary.
   * Code taken from https://stackoverflow.com/a/8721483/14062356
   * @param p The point to check.
   * @return true if the point is inside the boundary, false otherwise
   *
   */
  public boolean contains(Point p) {
    if (points.contains(p)) return false;
    int i;
    int j;
    boolean result = false;
    for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
      if ((points.get(i).y > p.y) != (points.get(j).y > p.y) &&
        (p.x < (points.get(j).x - points.get(i).x) * 
        (p.y - points.get(i).y) / (points.get(j).y-points.get(i).y) 
        + points.get(i).x)) {
        result = !result;
       }
    }
    return result;
  }
}