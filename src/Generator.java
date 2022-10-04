import java.util.Random;

public class Generator {
	private static Random rand = new Random();

	/**
	 * Generates points on canvas in a radial
	 * or rectangular pattern. restricts it to the
	 * screen size when in visual mode.
	 */
	public static void generatePoints(int n) {
		Core.unsolve();
		if (Core.mode == Mode.VISUAL && Core.genFx == GenFx.RADIAL) {
			Generator.radialScreen();
		} else if (Core.mode == Mode.VISUAL && Core.genFx == GenFx.RECTANGULAR) {
			Generator.rectangularScreen();
		} else if (Core.mode == Mode.VISUAL && Core.genFx == GenFx.CIRCULAR) {
			Generator.circularScreen();
		} else if (Core.mode == Mode.AUTO && Core.genFx == GenFx.RADIAL) {
			Generator.radialAuto(n);
		} else if (Core.mode == Mode.AUTO && Core.genFx == GenFx.RECTANGULAR) {
			Generator.rectangularAuto(n);
		} else if (Core.mode == Mode.AUTO && Core.genFx == GenFx.CIRCULAR) {
			Generator.circularAuto(n);
		}
	}

	public static void generatePoints() { generatePoints(Core.genSize); }

	public static void radialScreen() {
		Canvass c = Core.canvass;
		// set min && max
		Point midPoint = new Point(c.getWidth() / 2, c.getHeight() / 2);
		int maxRad = (int) (Math.min(c.getWidth() * 0.95, c.getHeight() * 0.95) / 2);
		
		// generate points using a midpoint and radius
		for (int i = 0; i < Core.genSize; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			double radius = Math.max(rand.nextInt(maxRad), rand.nextInt(maxRad));
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			Core.points.add(new Point(x, y));
		}
	}

	public static void rectangularScreen() {
		int width = Core.canvass.getWidth();
		int height = Core.canvass.getHeight();
		for (int i = 0; i < Core.genSize; i++) {
			int x = rand.nextInt((int) (width * 0.96));
			int y = rand.nextInt((int) (height * 0.96));
			x += width * 0.02;
			y += height * 0.02;
			Core.points.add(new Point(x, y));
		}
	}

	public static void circularScreen() {
		Canvass c = Core.canvass;
		Point midPoint = new Point(c.getWidth() / 2, c.getHeight() / 2);
		int radius = (int) (Math.min(c.getWidth() * 0.95, c.getHeight() * 0.95) / 2);
		
		for (int i = 0; i < Core.genSize; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			Core.points.add(new Point(x, y));
		}
	}

	public static void radialAuto(int n) {
		int halfMax = (int) (Integer.MAX_VALUE / 2.1);
		// I divide by 2.1 to make sure to avoid integer overflow
		Point midPoint = new Point(halfMax, halfMax);
		
		for (int i = 0; i < n; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			double radius = Math.max(rand.nextInt(halfMax), rand.nextInt(halfMax));
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			Core.points.add(new Point(x, y));
		}
	}

	public static void rectangularAuto(int n) {
		for (int i = 0; i < n; i++) {
			int x = rand.nextInt();
			int y = rand.nextInt();
			Core.points.add(new Point(x, y));
		}
	}

	public static void circularAuto(int n) {
		int halfMax = (int) (Integer.MAX_VALUE / 2.1);
		Point midPoint = new Point(halfMax, halfMax);
		int radius = halfMax;
		
		for (int i = 0; i < n; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			Core.points.add(new Point(x, y));
			// System.out.println("(" + x + ", " + y + ")");
		}
	}
}
