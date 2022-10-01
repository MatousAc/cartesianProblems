import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("serial")
public class Canvass extends JPanel {
	protected static final int NORMAL_SIZE = 10;
	protected static final int CLOSENESS = NORMAL_SIZE + 6;
	private static final int LINE_HEIGHT = 18;
	static Map<String, Color> colours = new HashMap<String, Color>();
	MouseAdapter mouseAdapter;
	protected boolean isDragging = false;
	boolean isPaused = false;
	private boolean helpOn = false;
	
	private Point selectedPoint = null;
	Point start = null;


	public Canvass() {
		// design
		setBackground(Color.WHITE);
		setLayout(null);
		colours.put("lightBlue", new Color(168, 213, 226));
		colours.put("darkBlue", new Color(28, 114, 147));
		colours.put("gold", new Color(249, 166, 32));
		colours.put("red", new Color(253, 21, 27));
		colours.put("lightGreen", new Color(84, 140, 47));
		colours.put("darkGreen", new Color(60, 73, 17));
		colours.put("default", new Color(11, 5, 0));

		// events
		mouseAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (selectedPoint != null) {
					isDragging = true;
					repaint();
				}
			}
	
			@Override // adds a point
			public void mouseReleased(MouseEvent e) {
				int x = e.getX(),
					y = y(e.getY());
				hull.addPointManually(new Point(x, y));
				isDragging = false;
				repaint();
			}
	
			@Override // detect if you run into a point
			public void mouseMoved(MouseEvent e) {
				if (!isDragging) {
					checkActive(e.getX(), y(e.getY()));
				}
				repaint();
			}
	
			@Override // dragging points around
			public void mouseDragged(MouseEvent e) {
				if (isDragging && selectedPoint != null) {
					selectedPoint.x = e.getX();
					selectedPoint.y = y(e.getY());
				}
				repaint();
			}
		};
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyTyped(e);
				switch (e.getKeyCode()) {
				case KeyEvent.VK_G: 
					hull.generatePoints(); break;
				case KeyEvent.VK_S:
				new Thread(() -> hull.solve()).start(); break;
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_C: isPaused = false; break;
				case KeyEvent.VK_ESCAPE:
					if (helpOn) helpOn = false;
					else hull.reset(); break;
				case KeyEvent.VK_DELETE:
				case KeyEvent.VK_BACK_SPACE:
					hull.removePointManually(selectedPoint);
					break;
				case KeyEvent.VK_PLUS:
				case KeyEvent.VK_EQUALS:
					hull.speed = hull.speed.increase(); break;
				case KeyEvent.VK_MINUS:
					hull.speed = hull.speed.decrease(); break;
				case KeyEvent.VK_T:
					hull.alg = hull.alg.toggle(); break;
				default: helpOn = true;
				}
				repaint();
			}
		});
	}

	protected void checkActive(int x, int y) {
		for (Point p : hull.points) {
			if (near(p, x, y)) {
				selectedPoint = p;
				return;
			}
		}
		selectedPoint = null;
	}

	private boolean near(Point p, double x, double y) {
		double dx = p.x - x, dy = p.y - y;
		dx = (dx > 0) ? dx : -dx;
		dy = (dy > 0) ? dy : -dy;
		return dx < CLOSENESS && dy < CLOSENESS;
	}

	// painting the UI //	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		graphicDefaults(g2);
		g2.translate(0, y());
		g2.scale(1, -1); // Invert y-axis
		drawHull(g2);
		drawPoints(g2);
		
		g2.scale(1, -1);
		drawSettings(g2);
		if (helpOn) drawHelp(g2);
	}

	private void drawPoints(Graphics2D g2) {
		for (Point p : hull.points) {
			drawPoint(g2, p);
		}
	}

	private void drawHull(Graphics2D g2) {
		g2.setColor(colours.get("darkGreen"));
		g2.setStroke(new BasicStroke(3));
		Iterator<Point> i = hull.solution.iterator();
		Point curPoint = (i.hasNext()) ? i.next() : null;
		while (i.hasNext()) {
			drawLine(g2, curPoint, curPoint = i.next());
		}
		if (hull.solved) drawLine(g2, curPoint, start);
		graphicDefaults(g2);
	}

	protected void drawPoint(Graphics2D g, Point p, Boolean coordinate) {
		// control colour and size
		int size = NORMAL_SIZE;
		if (p == start) {
			g.setColor(colours.get("darkBlue"));
			size *= 1.5;
		} else if (p == grahamScan.P || p == grahamScan.Q || p == grahamScan.R) {
			g.setColor(colours.get("gold"));
			size *= 1.1;
		} else if (hull.solution.contains(p)) {
			g.setColor(colours.get("lightGreen"));
		}
		// draw point
		int actSz = size + 6;
		int x = (int) Math.round(p.x), y = (int) Math.round(p.y);
		g.fillOval(x - (size / 2), y - (size / 2), size, size);
		if (p == selectedPoint) {
			g.drawOval(x - (actSz / 2), y - (actSz / 2), actSz, actSz);
		}
		// in case I want the coordinates
		if (coordinate) {
			g.scale(1, -1);
			g.drawString(p.toString(), x + 10, -y - 10);
			g.scale(1, -1);
		}
		graphicDefaults(g);
	}

	protected void drawPoint(Graphics2D g, Point p) {
		drawPoint(g, p, false);
	}

	protected void drawLine(Graphics2D g2, Point p1, Point p2) {
		if (p1 == grahamScan.P || p1 == grahamScan.Q) {
			g2.setColor(colours.get("red"));
		}
		g2.drawLine(
			(int) Math.round(p1.x), 
			(int) Math.round(p1.y), 
			(int) Math.round(p2.x), 
			(int) Math.round(p2.y)
		);
		g2.setColor(colours.get("darkGreen"));
	}

	private void drawHelp(Graphics2D g2) {
		ArrayList<String> keys = new ArrayList<>();
		ArrayList<String> commands = new ArrayList<>();
		keys.add("Key"); 	commands.add("Command");
		keys.add("======");commands.add("===================");
		keys.add("G"); 		commands.add("generate N points");
		keys.add("S"); 		commands.add("solve problem");
		keys.add("space"); commands.add("prompt next step");
		keys.add("ESC"); 	commands.add("erase points/dismiss help");
		keys.add("DEL"); 	commands.add("remove active point");
		keys.add("+/-"); 	commands.add("increase/decrease speed");
		keys.add("T"); 		commands.add("toggle algorithm");

		for (int i = 0; i < keys.size(); i++) {
			g2.drawString(keys.get(i), 20, 20 - y(i * LINE_HEIGHT));
			g2.drawString(commands.get(i), 80, 20 - y(i * LINE_HEIGHT));
		}
	}

	private void drawSettings(Graphics2D g2) {
		ArrayList<String> settings = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();
		settings.add("Setting"); 		values.add("Value");
		settings.add("========="); 	values.add("=============");
		settings.add("mode"); 				values.add(hull.mode.toString());
		settings.add("algorithm"); 	values.add(hull.alg.toString());
		settings.add("speed"); 			values.add(hull.speed.toString());
		settings.add("N"); 					values.add(((Integer) hull.n).toString());

		for (int i = 0; i < settings.size(); i++) {
			g2.drawString(settings.get(i), 20, -120 + i * LINE_HEIGHT);
			g2.drawString(values.get(i), 120, -120 + i * LINE_HEIGHT);
		}
	}

	private static void graphicDefaults(Graphics2D g2) {
		g2.setColor(colours.get("default"));
		g2.setStroke(new BasicStroke(1));
		g2.setFont(new Font("Georgia", Font.BOLD, 16));
	}

	private int y(int y) {
		return getHeight() - y;
	}
	private int y() { return y(0);	}
}