import java.awt.*;
import javax.swing.SwingUtilities;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Scanner;
import enums.*;

public class Core {
	// high level resources
	// private static Core single_instance = null;
	protected static Canvass canvass;
	static Mode mode;
	static Problem problem;
	static Speed speed = Speed.UNRESTRAINED;
	static ChAlg chAlg = ChAlg.JARVIS_MARCH;
	static VcAlg vcAlg = VcAlg.EXACT_EXHAUSTIVE;
	static GenFx genFx = GenFx.RADIAL;
	static int genSize = 0;
	static double density = 0.1;
	static boolean solved = false;

	public static void main(String[] args) {
		getParams();
		if (isAuto()) {
			dataWriteHeader();
			
		}
		else makeCanvass();
	}

	/**
	 * generates problem of specified {@code size}.
	 * times one run of solve(). results printed and
	 * written to performance.csv
	 * @param size - the size of problem to be generated
	 */
	public static void timedTest(int size) {
		Generator.generatePoints(size);
		long startTime = System.currentTimeMillis();
		solve();
		long endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) / 1000.0;
		String data = dataPieces(duration);
		dataWrite(data);
		System.out.print(data);
		reset();
	}

	/**
	 * Solves using specified algorithm, visibility, and speed
	 */
	public static void solve() {
		unsolve();
		if (isCH()) {
			if (HullBase.points.size() < 3) {
				System.out.print("convex hull impossible");
				return;
			}
			switch (chAlg) {
				case JARVIS_MARCH: Jarvis.march(); break;
				case GRAHAM_SCAN: Graham.scan(); break;
			}
		} else {
			switch (vcAlg) {
				case EXACT_EXHAUSTIVE: Exact.exhaustive(); break;
				case EXACT_INCREASING_SIZE: Exact.increasingSize(); break;
				case APPROXIMATION_ONE_BY_ONE: Approximation.oneByOne(); break;
				case APPROXIMATION_TWO_FACTOR: Approximation.twoFactor(); break;
			}
		}
	}
	
	/**
	 * resets problem solving progress
	 */
	static void unsolve() {
		solved = false;
		if (isCH()) {
			HullBase.hull.clear();
			HullBase.start = null;
			HullBase.P = null;
			HullBase.Q = null;
			HullBase.R = null;
		} else {
			CoverBase.cover.clear();
			Approximation.curEdge = null;
		}
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
		System.out.print("Select Mode (\033[4mv\033[0misual|\033[4ma\033[0muto): ");
		switch (scan.next().toLowerCase()) {
			case "v":
			case "visual": mode = Mode.VISUAL; break;
			default: mode = Mode.AUTO; break;
		}

		if (isAuto()) {
			System.out.print("Select Problem (\033[4mc\033[0monvex hull|\033[4mm\033[0minimum \033[4mv\033[0mertex cover): ");
			switch (scan.next().toLowerCase()) {
				case "c":
				case "convex hull": problem = Problem.CONVEX_HULL; break;
				default: problem  = Problem.MINIMUM_VERTEX_COVER; break;
			}
		}  else { problem = Problem.MINIMUM_VERTEX_COVER; }
		
		String msg = "Enter " + ((isAuto()) ? "max " : "") + "generation size: ";
		System.out.print(msg);
		genSize = scan.nextInt();
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
		dataWrite("algorithm,generation style,");
		if (isCH()) dataWrite("point count,hull size,");
		else dataWrite("vertex count,edge count,density,cover size,");
		dataWrite("duration (s)\n");
	}
	
	private static String dataPieces(double duration) {
		if (isCH()) {
			return String.join(",", 
				chAlg.toString(),
				genFx.toString(),
				HullBase.pointCount(),
				HullBase.hullSize(),
				duration + "\n"
			);
		} else if (!isCH()) {
			return String.join(",", 
				vcAlg.toString(),
				genFx.toString(),
				CoverBase.vertexCount(),
				CoverBase.edgeCount(),
				CoverBase.graphDensity(),
				CoverBase.coverSize(),
				duration + "\n"
			);
		} else return "";
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

	/**
	 * Resets the problem.
	 */
	public static void reset() {
		unsolve();
		if (isCH()) HullBase.points.clear();
		else if (!isCH()) {
			CoverBase.vertices.clear();
			CoverBase.edges.clear();
		}
		canvass.reset();
	}
	
	public static void densityUp() {
		if (densityEnds()) density += 0.01;
		else density += 0.1;

		if (density > 1) density = 1;
	}

	public static void densityDown() {
		if (densityEnds()) density -= 0.01;
		else density -= 0.1;

		if (density < 0) density = 0;
	}

	public static String getDensityAsString() {
		return String.format("%.02f", density);
	}


	public static boolean isAuto() { return mode == Mode.AUTO; }
	public static boolean isCH() { return problem == Problem.CONVEX_HULL; }
	public static boolean densityEnds() { return density <= 0.1001 || density >= 0.8999; }
}