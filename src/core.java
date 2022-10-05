import java.awt.*;
import javax.swing.SwingUtilities;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Scanner;

public class Core {
	// high level resources
	// private static Core single_instance = null;
	protected static Canvass canvass;
	static Mode mode;
	static Problem problem = Problem.CONVEX_HULL;
	static Speed speed = Speed.UNRESTRAINED;
	static ChAlg chAlg = ChAlg.JARVIS_MARCH;
	static GenFx genFx = GenFx.RADIAL;
	static int genSize = 0;
	static boolean solved = false;
	// basic problem resources
	static ArrayList<Point> points = new ArrayList<Point>();
	static ArrayList<Point> hull = new ArrayList<Point>();

	public static void main(String[] args) {
		getParams();
		if (isAuto()) auto();
		else makeCanvass();
	}

	/**
	 * conducts an automated run of both algorithms
	 */
	public static void auto() {
		dataWrite("algorithm,generation style,problem size,solution size,duration (s)\n");
		ArrayList<ChAlg> algs = new ArrayList<ChAlg>(EnumSet.allOf(ChAlg.class));
		ArrayList<GenFx> styles = new ArrayList<GenFx>(
			EnumSet.allOf(GenFx.class)
		);
		
		for (ChAlg a : algs) {
			chAlg = a;
			for (GenFx s : styles) {
				genFx = s;
				test();
			}
		}
	}

	/**
	 * tests Core's current algorithm up to genSize.
	 * puts together results in res variable.
	 * @param res
	 * @return the testing duration results in csv format
	 */
	public static void test() {
		for (int size = 4; size < genSize; size *= 2) {
			Generator.generatePoints(size);
			long startTime = System.currentTimeMillis();
			solve();
			long endTime = System.currentTimeMillis();
			double duration = (endTime - startTime) / 1000.0;
			dataWrite(dataPieces(duration));
			reset();
		}
	}

	/**
	 * Solves using specified algorithm, visibility, and speed
	 */
	public static void solve() {
		unsolve();
		if (points.size() < 3) {
			System.out.print("convex hull impossible");
			return;
		}
		switch (chAlg) {
			case JARVIS_MARCH: Jarvis.march(); break;
			case GRAHAM_SCAN: Graham.scan(); break;
		}
	}
	
	/**
	 * resets problem solving progress
	 */
	static void unsolve() {
		solved = false;
		hull.clear();
		Graham.start = null;
		Graham.P = null;
		Graham.Q = null;
		Graham.R = null;
	}
	
	// helpers //
	private static String dataPieces(double duration) {
		return chAlg + "," + genFx + "," + points.size() + "," + 
				hull.size() + "," + duration + "\n";
	}
	private static void dataWrite(String data, String file) {
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.flush();
			bw.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	private static void dataWrite(String data) {
		dataWrite(data, "performance.csv");
	}

	/**
	 * Adds a point when user interacts with UI.
	 * Unsolves problem.
	 * @param p
	 */
	static void addPointManually(Point p) {
		points.add(p);
		unsolve();
	}
	
	/**
	 * Removes a point from the problem.
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
	 * gets our gui to repaint if in visual mode
	 */
	static void show() {
		if (isAuto()) return;
		canvass.repaint();
		Canvass.wait(Canvass.delay());
	}

	private static void getParams() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Select Mode (visual|auto): ");
		switch (scan.next().toLowerCase()) {
			case "v":
			case "visual": mode = Mode.VISUAL; break;
			default: mode = Mode.AUTO; break;
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

	private static boolean isAuto() { return mode == Mode.AUTO; }
}