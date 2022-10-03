import java.awt.*;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class core {
	// high level resources
	private static core single_instance = null;
	protected static Canvass canvass;
	static modes mode;
	static problems problem = problems.CONVEX_HULL;
	static speeds speed = speeds.UNRESTRAINED;
	static algs alg = algs.JARVIS;
	static distributions dist = distributions.RADIAL;
	static int genSize = 0;
	static boolean solved = false;
	// basic problem resources
	static ArrayList<Point> points = new ArrayList<Point>();
	static ArrayList<Point> hull = new ArrayList<Point>();

	public static void main(String[] args) {
		getParams();
		if (isAuto()) test();
		else makeCanvass();
	}

	public static void test() {
		Path path = Paths.get("performance.csv");
		for (int i = 0; i < genSize;) {}
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
			System.out.print("convex hull impossible");
			return;
		}
		switch (alg) {
			case JARVIS: jarvis.march(); break;
			case GRAHAM: graham.scan(); break;
		}
	}
	
	static void unsolve() {
		solved = false;
		hull.clear();
		graham.start = null;
		graham.P = null;
		graham.Q = null;
		graham.R = null;
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
	public static void visualPointGeneration() {
		unsolve();
		if (dist == distributions.RADIAL) generate.radial();
		else generate.rectangular();
	}
	
	// utility f(x)s //
	/**
	 * Returns singleton of this class.
	 * @return
	 */
	public static core getInstance() {
		if (single_instance == null) {
			single_instance = new core();
		}
		return single_instance;
	}
	// private constructor
	private core() {}
	
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

	private static void getParams() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Select Mode (visual|automatic): ");
		switch (scan.next().toLowerCase()) {
			case "v":
			case "visual": mode = modes.VISUAL; break;
			default: mode = modes.AUTOMATIC; break;
		}
		
		String msg = "Enter " + ((isAuto()) ? "max " : "") + "generation size : ";
		System.out.print(msg);
		genSize = scan.nextInt();
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

	private static boolean isAuto() { return mode == modes.AUTOMATIC; }
}