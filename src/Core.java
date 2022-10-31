import java.awt.*;
import javax.swing.SwingUtilities;
import java.util.Scanner;
import enums.*;

public class Core {
	/** The GUI for visualizing the algorithms. */
	protected static Canvass canvass;
  /** The mode the program will run in. */
	static Mode mode;
  /** Indicates speed at which to show algorithm progression. */
	static Speed speed = Speed.UNRESTRAINED;
	static int maxSize = 0;
  /** The problem that is currently being solved. */
  static Problem prob;
	static boolean isSolving = false;
	static boolean isSolved = false;

	public static void main(String[] args) {
		getParams();
		if (isAuto()) prob.test();
		else makeCanvass();
	}

	/**
	 * Times one run of problem.solve(). Results
   * printed and  written to performance.csv
	 */
	public static void timedTest(int size) {
		long startTime = System.nanoTime();
		prob.solve();
		long endTime = System.nanoTime();
		Double duration = (endTime - startTime) / 1000000000.0;
		String data = prob.getData(duration);
		Utility.dataWrite(data);
		System.out.print(data);
	}
	
	/** Schedules a canvass repaint if in visual mode. */
	static void show() {
		if (isAuto()) return;
		canvass.repaint();
		canvass.wait(canvass.getDelay());
	}

  /** Assigns prob to next problem. */
  static void nextProblem() {
    if (prob instanceof Hull) {
      prob = new Cover();
    } else {
      prob = new Hull();
    }
  }

  /** Gets mode and size params. Instantiates a problem. */
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
			case "convex hull": prob = new Hull(); break;
			default: prob  = new Cover(); break;
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

	public static boolean isAuto() { return mode == Mode.AUTO; }
	public static boolean isCH() { return prob instanceof Hull; }
}