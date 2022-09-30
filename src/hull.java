import java.awt.*;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class hull {
		
	public static void main(String[] args) {
		getHyperParams();
		makeCanvass();
	}
	
	// mode modes
	enum modes { MANUAL, AUTOVISUAL, AUTOMATIC }

	// high level resources
	private static hull single_instance = null;
	protected static Canvass canvass;
	static modes mode;
	static speeds speed = speeds.UNRESTRAINED;
	static algs alg = algs.JARVIS;
	static int n = 0;
	static boolean solved = false;
	// basic problem resources
	static ArrayList<Point> points = new ArrayList<Point>();
	static ArrayList<Point> solution = new ArrayList<Point>();

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

		long startTime = System.currentTimeMillis();
		switch (alg) {
			case JARVIS: jarvisMarch(); break;
			case GRAHAM: grahamScan.makeHull(); break;
		}
		solved = true;
		
		if (isAuto()) {
			long endTime = System.currentTimeMillis();
			double duration = (endTime - startTime) / 1000.0;
			System.out.print("Duration: " + duration);
			Path path = Paths.get("performance.csv");
			String res = alg + "," + duration;

			try {
				Files.write(path, res.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static void unsolve() {
		solved = false;
		solution.clear();
	}

	private static void jarvisMarch() {}

	
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
	
	// handling the different modes of the application
	static void show() {
		if (isAuto()) return;
		// SwingUtilities.invokeLater(new Runnable() {
		// 	public void run() {
		// 		canvass.repaint();
		// 	}
		// });
		
		canvass.repaint();
		// wait(200);
	}

	public static void wait(int ms) {
		try {
			TimeUnit.MILLISECONDS.sleep(300);
		} catch(InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	static void setStart(Point p) {
		if (isAuto()) return;
		canvass.start = p;
	}

	static void setPQR(Point p, Point q, Point r) {
		if (isAuto()) return;
		canvass.P = p;
		canvass.Q = q;
		canvass.R = r;
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
		
		if (mode != modes.MANUAL) {
			System.out.print("Select n (integer): ");
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