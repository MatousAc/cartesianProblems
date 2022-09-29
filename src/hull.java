import java.awt.*;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class hull {
		
	public static void main(String[] args) {
		getHyperParams();
		makeCanvass();
	}
	
	// mode modes
	private enum modes { MANUAL, AUTOVISUAL, AUTOMATIC }
	private enum speeds { UNRESTRAINED, FAST, MEDIUM, SLOW, PROMPT }
	private enum algs { JARVIS, GRAHAM }

	// high level resources
	private static hull single_instance = null;
	protected static Canvass canvass;
	private static modes mode;
	private static speeds speed;
	private static algs alg;
	static int problemSize = 0;
	static boolean solved = false;
	// basic problem resources
	static ArrayList<Point> points = new ArrayList<Point>();
	static ArrayList<Point> solution = new ArrayList<Point>();

	/**
	 * Solves using specified algorithm, visibility, and speed
	 * @throws IOException
	 */
	public static void solve() throws IOException {
		if (points.size() < 3) {
			System.out.print("Convex Hull Impossible");
			return;
		} else  if (solved) { return; }

		System.out.println("Solving using " + alg);
		long startTime = System.currentTimeMillis();
		switch (alg) {
			case JARVIS: jarvisMarch(); break;
			case GRAHAM: grahamScan.makeHull(); break;
		}
		if (mode == modes.AUTOMATIC) {
			long endTime = System.currentTimeMillis();
			double duration = (endTime - startTime) / 1000.0;
			System.out.print("Duration: " + duration);
			Path path = Paths.get("performance.csv");
			String res = alg + "," + duration;

			Files.write(path, res.getBytes());
		}
	}
	
	private static void jarvisMarch() {}

	
	// helpers //
	/**
	 * Adds a point when user interacts with UI.
	 * @param p
	 */
	public static void addPointManually(Point p) {
		solved = false;
		points.add(p);
		problemSize++;
	}
	/**
	 * Removes a point from the problem. Decreases size.
	 * Makes unsolved.
	 * @param p
	 */
	public static void removePointManually(Point p) {
		solved = false;
		points.remove(p);
		problemSize--;
	}

	/**
	 * Resets the problem.
	 */
	public static void reset() {
		solved = false;
		points.clear();
		problemSize = 0;
	}

	/**
	 * Generates n points on canvas in a radial patter.
	 * @param n
	 */
	public static void generatePoints(int n) {
		if (mode == modes.MANUAL) {
			System.out.println("Cannot generate points in manual mode.");
			return;
		}
		solved = false;
		Random rand = new Random();
		// set min && max
		Point midPoint = new Point(canvass.getWidth() / 2, canvass.getHeight() / 2);
		int maxRad = (int) (Math.min(canvass.getWidth() * 0.9, canvass.getHeight() * 0.9) / 2);
		
		// generate points using a midpoint and radius
		for (int i = 0; i < n; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			// double radius = Math.max(rand.nextInt(maxRad), Math.max(rand.nextInt(maxRad), rand.nextInt(maxRad)));
			double radius = Math.max(rand.nextInt(maxRad), rand.nextInt(maxRad));
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			points.add(new Point(x, y));
		}
	}
	
	public static void printWidth(){System.out.println(canvass.getWidth());}
	
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
	
	// handling the different modes of the application
	static void show() {
		if (isAuto()) return;
		canvass.repaint();
	}

	static void addLeftBottom(Point lb) {
		if (isAuto()) return;
		canvass.leftBottom = lb;
	}

	private static boolean isAuto() { return mode == modes.AUTOMATIC; }

	private static void getHyperParams() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Select Mode (manual|autovisual|automatic): ");
		switch (scan.next().toLowerCase()) {
			case "manual": mode = modes.MANUAL; break;
			case "autovisual": mode = modes.AUTOVISUAL; break;
			case "automatic": mode = modes.AUTOMATIC; break;
			default: mode = modes.AUTOMATIC; break;
		}

		System.out.print("Select Algorithm (graham): ");
		alg = scan.next().toLowerCase() == "jarvis" ? algs.JARVIS : algs.GRAHAM;
		
		if (mode == modes.AUTOMATIC) { speed = speeds.UNRESTRAINED; }
		else {
			System.out.print("Select Speed (unrestrained|fast|medium|slow|prompt): ");
			switch (scan.next().toLowerCase()) {
				case "fast": speed = speeds.FAST; break;
				case "medium": speed = speeds.MEDIUM; break;
				case "slow": speed = speeds.SLOW; break;
				case "prompt": speed = speeds.PROMPT; break;
				default: speed = speeds.UNRESTRAINED; break;
			}
		}
		
		if (mode != modes.MANUAL) {
			System.out.print("Select Problem Size (integer): ");
			problemSize = scan.nextInt();
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