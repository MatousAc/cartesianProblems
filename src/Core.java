import java.awt.*;
import javax.swing.SwingUtilities;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Scanner;
import enums.*;

public class Core {
	// high level resources
	/** The GUI for visualizing the algorithms. */
	protected static Canvass canvass;
	static Mode mode;
	static Problem problem;
	static Speed speed = Speed.UNRESTRAINED;
	static ChAlg chAlg = ChAlg.JARVIS_MARCH;
	static VcAlg vcAlg = VcAlg.EXACT_EXHAUSTIVE;
	static GenFx genFx = GenFx.RADIAL;
	static int maxSize = 0;
	static boolean isSolving = false;
	static boolean isSolved = false;

	public static void main(String[] args) {
		getParams();
		if (isAuto()) {
			dataWriteHeader();
			if (isCH()) HullBase.test();
			else CoverBase.test();
		}
		else makeCanvass();
	}

	/**
	 * Generates problem of size {@code Generator.N}.
	 * Times one run of solve(). Results printed and
	 * written to performance.csv
	 */
	public static void timedTest(int size) {
		long startTime = System.nanoTime();
		solve();
		long endTime = System.nanoTime();
		Double duration = (endTime - startTime) / 1000000000.0;
		String data = dataPieces(duration);
		dataWrite(data);
		System.out.print(data);
	}

	/**
	 * Solves using specified algorithm, visibility, and speed
	 */
	public static void solve() {
		isSolving = true;
		unsolve();
		if (isCH()) {
			if (HullBase.points.size() < 3) {
				System.out.println("Convex hull impossible. Solve Aborted.");
			} else {
				switch (chAlg) {
				case JARVIS_MARCH: Jarvis.march(); break;
				case GRAHAM_SCAN: Graham.scan(); break;
			}
		}
		} else {
			if (CoverBase.vertices.size() == 0) {
				System.out.println("No vertices. Solve aborted.");
			} else {
				switch (vcAlg) {
				case EXACT_EXHAUSTIVE: Exact.exhaustive(); break;
				case EXACT_INCREASING_SIZE: Exact.increasingSize(); break;
				case EXACT_ALEX_OPTIMIZATION: Exact.alexOptimization(); break;
				case APPROXIMATION_ONE_BY_ONE: Approximation.oneByOne(); break;
				case APPROXIMATION_TWO_FACTOR: Approximation.twoFactor(); break;
				}
			}
		}
		isSolving = false;
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
		// choose mode
		System.out.print("Select Mode (\033[4mv\033[0misual|\033[4ma\033[0muto): ");
		switch (scan.next().toLowerCase()) {
			case "v":
			case "visual": mode = Mode.VISUAL; break;
			default: mode = Mode.AUTO; break;
		}
		// choose problem
		System.out.print(
			"Select Problem (\033[4mc\033[0monvex hull|" + 
			"minimum \033[4mv\033[0mertex cover): "
		);
		switch (scan.next().toLowerCase()) {
			case "c":
			case "convex hull": problem = Problem.CONVEX_HULL; break;
			default: problem  = Problem.MINIMUM_VERTEX_COVER; break;
		}
		// choose problem size
		if (isAuto()) {
			System.out.print("Enter max problem size: ");
			Core.maxSize = scan.nextInt();
		} else {
			System.out.print("Enter generation size: ");
			Generator.N = scan.nextInt();
		}
		scan.close();
	}

	public static void makeCanvass() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SwingUtilities.invokeLater(() -> {
			new AppWindow(
				"Cartesian Problems", 
				(int)screenSize.getWidth(), (int)screenSize.getHeight(), 
				canvass = new Canvass());
		});
	}

	// helpers //
	private static void dataWriteHeader() {
		if (isCH()) {
			dataWrite("algorithm,generation style," + 
			"point count,hull size,duration (s)\n");
		}
		else {
			dataWrite("algorithm,vertex count,edge count," + 
			"density,cover size,duration (s)\n"
		);}
	}
	
	/**
	 * Generates a string of information from the 
	 * latest solve including algorithm, generation
	 * f(x), solution size, problem size and others.
	 * @param duration the time elapsed during the latest solve
	 * @return string of comma-separated data
	 */
	private static String dataPieces(Double duration) {
		DecimalFormat df = new DecimalFormat("#.#");
    df.setMaximumFractionDigits(10);

		if (isCH()) {
			return String.join(",", 
				chAlg.toString(),
				genFx.toString(),
				HullBase.pointCount(),
				HullBase.hullSize(),
				df.format(duration) + "\n"
			);
		} else {
			return String.join(",", 
				vcAlg.toString(),
				CoverBase.vertexCount(),
				CoverBase.edgeCount(),
				CoverBase.graphDensity(),
				CoverBase.coverSize(),
				df.format(duration) + "\n"
			);
		}
	}
	
	/**
	 * writes specified data to the performance.csv file
	 * @param data - data to be written
	 */
	private static void dataWrite(String data) {
		try {
			FileWriter fw = new FileWriter("performance.csv", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.flush();
			bw.close();
		} catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * advances Core.vcAlg or Core.chAlg
	 * to the next algorithm
	 */ 
	public static void nextAlg() {
		if (isCH()) {
			chAlg = chAlg.next();
		} else if (problem == Problem.MINIMUM_VERTEX_COVER) {
			vcAlg = vcAlg.next();
		}
	}

	/**
	 * returns the current algorithms being used by 
	 * Core as a String
	 * @return
	 */
	public static String getAlgAsString() {
		if (isCH()) {
			return chAlg.toString();
		} else if (problem == Problem.MINIMUM_VERTEX_COVER) {
			return vcAlg.toString();
		} else {
			return "None";
		}
	}

	/**
	 * Adds a point when user interacts with UI.
	 * Unsolves problem.
	 * @param p
	 */
	static void addPointManually(Point p) {
		if (isCH()) HullBase.points.add(p);
		else if (!isCH()) {
			CoverBase.vertices.add(p);
		}
		unsolve();
	}
	
	/**
	 * Removes a point from the problem.
	 * Makes unsolved.
	 * @param p
	 */
	static void removePointManually(Point p) {
		if (isCH()) HullBase.points.remove(p);
		else if (!isCH()) {
			CoverBase.vertices.remove(p);
			HashSet<Point> hs = new HashSet<Point>();
			hs.add(p);
			CoverBase.removeIncidentEdges(hs, CoverBase.edges);
		}
		unsolve();
	}

		
	/** Clears the solution and intermediate data for a problem. */
	static void unsolve() {
		isSolved = false;
		if (isCH()) {
			HullBase.hull.clear();
			HullBase.start = null; // might not need these null assignments
			HullBase.P = null;
			HullBase.Q = null;
			HullBase.R = null;
		} else {
			CoverBase.cover.clear();
			CoverBase.curEdge = null;
			CoverBase.rmEdge = null;
			CoverBase.u = null;
			CoverBase.v = null;
		}
	}
	
	/** Deletes current problem. */
	public static void reset() {
		isSolving = false;
		unsolve();
		if (isCH()) HullBase.points.clear();
		else if (!isCH()) {
			CoverBase.vertices.clear();
			CoverBase.edges.clear();
		}
		show();
	}

	public static boolean isAuto() { return mode == Mode.AUTO; }
	public static boolean isCH() { return problem == Problem.CONVEX_HULL; }
}