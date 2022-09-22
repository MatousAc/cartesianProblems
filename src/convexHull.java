import javax.swing.SwingUtilities;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class convexHull {
		
	public static void main(String[] args) {
		convexHull hull = convexHull.getInstance();
		getHyperParams();
		System.out.println(mode);
		System.out.println(alg);
		System.out.println(speed);
		makeCanvass();

	}
	
	// mode modes
	private enum modes { MANUAL, AUTOVISUAL, AUTOMATIC }
	private enum speeds { UNRESTRAINED, FAST, MEDIUM, SLOW, PROMPT }
	private enum algs { JARVIS, GRAHAM }

	// resources
	private static convexHull single_instance = null;
	protected static AppWindow canvass;
	protected static ArrayList<Point> points;
	private static modes mode;
	private static speeds speed;
	private static algs alg;
	protected static int problemSize;


	public static void solve() {
		;
	}
	
	private void show() {
		canvass.repaint();
	}
	
	// helpers //
	/**
	 * Adds a point to UI.
	 * @param point
	 */
	public static void addPoint(Point point) {
		points.add(point);
		problemSize++;
	}

	public static void generatePoints() {
		if (mode == modes.MANUAL) {
			System.out.println("Cannot generate points in manual mode.");
			return;
		}
		points.clear();
		Random rand = new Random();
		// set min && max
		Point midPoint = new Point(canvass.getWidth() / 2, canvass.getHeight() / 2);
		int maxRad = (int) (Math.min(canvass.getWidth() * 0.9, canvass.getHeight() * 0.9) / 2);
		
		// generate points using a midpoint and radius
		for (int i = 0; i < problemSize; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			// double radius = Math.max(rand.nextInt(maxRad), Math.max(rand.nextInt(maxRad), rand.nextInt(maxRad)));
			double radius = Math.max(rand.nextInt(maxRad), rand.nextInt(maxRad));
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			points.add(new Point(x, y));
		}
	}
	
	public void printWidth(){System.out.println(canvass.getWidth());}
	
	// utility f(x)s //
	// returns singleton
	public static convexHull getInstance() {
		if (single_instance == null) {
			single_instance = new convexHull();
		}
		return single_instance;
	}
	// private constructor
	private convexHull() {
		points = new ArrayList<Point>();
		problemSize = 0;
	}
	
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
			canvass = new AppWindow(
				"Convex Hull", 
				(int)screenSize.getWidth(), (int)screenSize.getHeight(), 
				new Canvass(single_instance));
		});
	}
}