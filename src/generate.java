import java.util.Random;

public class generate {
	public static void radial() {
		Canvass c = Core.canvass;
		// set min && max
		Point midPoint = new Point(c.getWidth() / 2, c.getHeight() / 2);
		int maxRad = (int) (Math.min(c.getWidth() * 0.95, c.getHeight() * 0.95) / 2);
		
		Random rand = new Random();
		// generate points using a midpoint and radius
		for (int i = 0; i < Core.genSize; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			double radius = Math.max(rand.nextInt(maxRad), rand.nextInt(maxRad));
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			Core.points.add(new Point(x, y));
		}
	}

	public static void rectangular() {
		int width = Core.canvass.getWidth();
		int height = Core.canvass.getHeight();
		Random rand = new Random();
		for (int i = 0; i < Core.genSize; i++) {
			int x = rand.nextInt((int) (width * 0.96));
			int y = rand.nextInt((int) (height * 0.96));
			x += width * 0.02;
			y += height * 0.02;
			Core.points.add(new Point(x, y));
		}
	}

	public static void maxInt() {
		Random rand = new Random();
		for (int i = 0; i < Core.genSize; i++) {
			int x = rand.nextInt();
			int y = rand.nextInt();
			Core.points.add(new Point(x, y));
		}
	}
}
