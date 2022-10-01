import java.awt.*;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class hull {
	// high level resources
	private static hull single_instance = null;
	protected static Canvass canvass;
	static modes mode;
	// static speeds speed = speeds.UNRESTRAINED;
	static speeds speed = speeds.SLOW;
	// static algs alg = algs.JARVIS;
	static algs alg = algs.GRAHAM;
	static int n = 0;
	static boolean solved = false;
	// basic problem resources
	static ArrayList<Point> points = new ArrayList<Point>();
	static ArrayList<Point> solution = new ArrayList<Point>();

	public static void main(String[] args) {
		getParams();
		if (isAuto()) test();
		else makeCanvass();
	}

	public static void test() {
		Path path = Paths.get("performance.csv");
		for (int i = 0; i < n;) {}
		long startTime = System.currentTimeMillis();
		solve();
		long endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) / 1000.0;
		System.out.print("Duration: " + duration);
		String res = alg + "," + duration;

		try {
			Files.write(path, res.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Solves using specified algorithm, visibility, and speed
	 * @throws IOException
	 */
	public static void solve() {
		unsolve();
		if (points.size() < 3) {
			System.out.print("Convex Hull Impossible");
			return;
		}

		switch (alg) {
			case JARVIS: jarvisMarch.makeHull(); break;
			case GRAHAM: grahamScan.makeHull(); break;
		}
	}
	
	static void unsolve() {
		solved = false;
		solution.clear();
	}
	
	// helpers //
	/**
	 * Adds a point when user interacts with UI.
	 * @param p
	 */
	static void addPointManually(Point p) {
		points.add(p);
	}
	/**
	 * Removes a point from the problem. Decreases size.
	 * Makes unsolved.
	 * @param p
	 */
	static void removePointManually(Point p) {
		points.remove(p);
		unsolve();
	}

	/**
	 * Resets the problem.
	 */
	public static void reset() {
		unsolve();
		points.clear();
	}

	/**
	 * Generates points on canvas in a radial patter.
	 */
	public static void generatePoints() {
		if (mode == modes.MANUAL) {
			System.out.println("Cannot generate points in manual mode.");
			return;
		}
		unsolve();
		Random rand = new Random();
		// set min && max
		Point midPoint = new Point(canvass.getWidth() / 2, canvass.getHeight() / 2);
		int maxRad = (int) (Math.min(canvass.getWidth() * 0.9, canvass.getHeight() * 0.9) / 2);
		
		// generate points using a midpoint and radius
		for (int i = 0; i < n; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			double radius = Math.max(rand.nextInt(maxRad), rand.nextInt(maxRad));
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			points.add(new Point(x, y));
		}
	}
	
	// utility f(x)s //
	/**
	 * Returns singleton of this class.
	 * @return
	 */
	public static hull getInstance() {
		if (single_instance == null) {
			single_instance = new hull();
		}
		return single_instance;
	}
	// private constructor
	private hull() {}
	
	/**
	 * gets our gui to repaint if applicable
	 */
	static void show() {
		if (isAuto()) return;
		canvass.repaint();
		wait(delay());
	}

	private static int delay() {
		switch (speed) {
			case UNRESTRAINED: 	return 0;
			case LIGHTNING:			return 1;
			case FAST: 					return 25;
			case MEDIUM: 				return 100;
			case SLOW: 					return 500;
			case SLOTH: 				return 1000;
			case PROMPT:
				canvass.isPaused = true;
				while (canvass.isPaused) {
					wait(10);
				};
				default: return 0;
		}
	}

	private static void wait(int ms) {
		try { Thread.sleep(ms); }
		catch (Exception e) { e.printStackTrace(); }
	}

	static void setStart(Point p) {
		if (isAuto()) return;
		canvass.start = p;
	}

	private static boolean isAuto() { return mode == modes.AUTOMATIC; }

	private static void getParams() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Select Mode (manual|autovisual|automatic): ");
		switch (scan.next().toLowerCase()) {
			case "manual": mode = modes.MANUAL; break;
			case "autovisual": mode = modes.AUTOVISUAL; break;
			case "automatic": mode = modes.AUTOMATIC; break;
			default: mode = modes.AUTOMATIC; break;
		}
		
		if (mode != modes.MANUAL) {
			String msg = "Select " + ((isAuto()) ? "max " : "") + "N : ";
			System.out.print(msg);
			n = scan.nextInt();
		}

		scan.close();
	}

	public static void makeCanvass() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SwingUtilities.invokeLater(() -> {
			new AppWindow(
				"Convex Hull", 
				(int)screenSize.getWidth(), (int)screenSize.getHeight(), 
				canvass = new Canvass());
		});
	}
}