import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import enums.*;

@SuppressWarnings("serial")
public class Canvass extends JPanel {
	protected static final int NORMAL_SIZE = 10;
	protected static final int CLOSENESS = NORMAL_SIZE + 6;
	private static final int LINE_HEIGHT = 18;
	static Map<String, Color> colors = new HashMap<String, Color>();
	MouseAdapter mouseAdapter;
	private static boolean isDragging, isPaused, helpOn;
	private static Point selectedPoint = null;
	static ArrayList<Point> points;

	public Canvass() {
		reset();
		if (Core.isCH()) {
			points = HullBase.points;
		} else {
			points = CoverBase.vertices;
		}
		// design
		setBackground(Color.WHITE);
		setLayout(null);
		colors.put("lightBlue", new Color(168, 213, 226));
		colors.put("darkBlue", new Color(28, 114, 147));
		colors.put("gold", new Color(249, 166, 32));
		colors.put("red", new Color(253, 21, 27));
		colors.put("orange", new Color(237, 106, 90));
		colors.put("lightGreen", new Color(84, 140, 47));
		colors.put("darkGreen", new Color(60, 73, 17));
		colors.put("purple", new Color(161, 22, 146));
		colors.put("default", new Color(11, 5, 0));
		colors.put("white", new Color(255, 255, 255));

		// events
		mouseAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (selectedPoint != null) {
					isDragging = true;
					repaint();
				}
			}
	
			@Override // adds a point if 
			public void mouseReleased(MouseEvent e) {
				if (selectedPoint != null) return; // leave point where it is
				int x = e.getX(),
					y = y(e.getY());
				Core.addPointManually(new Point(x, y));
				isDragging = false;
				repaint();
			}
	
			@Override // detect if you run into a point
			public void mouseMoved(MouseEvent e) {
				checkActive(e.getX(), y(e.getY()));
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
				case KeyEvent.VK_G: Generator.generateProblem(); break;
        case KeyEvent.VK_P: Core.problem = Core.problem.next(); break;
        case KeyEvent.VK_I: Generator.densityUp(); break;
        case KeyEvent.VK_D: Generator.densityDown(); break;
        case KeyEvent.VK_A: Core.nextAlg(); break;
        case KeyEvent.VK_F: Core.genFx = Core.genFx.next(); break;
        case KeyEvent.VK_H: Core.chHeur = Core.chHeur.next(); break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_SPACE:
					if (!Core.isSolving) {
						new Thread(() -> Core.solve()).start();
					} else {
						System.out.println("Already solving.");
					}
					break;
				case KeyEvent.VK_C:
				case KeyEvent.VK_ENTER: isPaused = false; break;
				case KeyEvent.VK_ESCAPE:
					if (helpOn) helpOn = false;
					else Core.reset();
					break;
				case KeyEvent.VK_DELETE:
				case KeyEvent.VK_BACK_SPACE:
					Core.removePointManually(selectedPoint);
					selectedPoint = null;
					break;
				case KeyEvent.VK_PLUS:
				case KeyEvent.VK_EQUALS: Generator.N++; break;
				case KeyEvent.VK_MINUS: Generator.N--; break;
				case KeyEvent.VK_UP: Core.speed = Core.speed.increase(); break;
				case KeyEvent.VK_DOWN: Core.speed = Core.speed.decrease(); break;
				case KeyEvent.VK_ALT:
				case KeyEvent.VK_CONTROL:
				case KeyEvent.VK_WINDOWS:
				case KeyEvent.VK_PAUSE: break; // nothing for special keys
				default: helpOn = true; // otherwise help
				}
				repaint();
			}
		});
	}

	public void reset() {
		isDragging = false;
		isPaused = false;
	 	helpOn = false;
	 	selectedPoint = null;
	}

	protected void checkActive(int x, int y) {
		for (Point p : points) {
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

	/**
	 * thread sleeps for designated number of milliseconds
	 * @param ms
	 */
	protected static void wait(int ms) {
		if (Core.speed == Speed.PROMPT && isPaused == false) { 
			prompt();
		}
		try { Thread.sleep(ms); }
		catch (Exception e) { e.printStackTrace(); }
	}

	/**
	 * pauses calling thread until isPaused == false
	 */
	protected static void prompt() {
		isPaused = true;
		while (isPaused) {
			wait(10);
		};
	}
	
	/**
	 * determines the delay that the calling thread should be
	 * paused for depending on the speed setting in Core
	 * @return delay time in milliseconds
	 */
	protected static int delay() {
		switch (Core.speed) {
			case UNRESTRAINED: 	return 0;
			case LIGHTNING:			return 1;
			case SUPER: 				return 5;
			case FAST: 					return 25;
			case MEDIUM: 				return 100;
			case SLOW: 					return 500;
			case SLOTH: 				return 1000;
			case PROMPT:				return 0;
			default: return 0;
		}
	}

	///////// painting the UI /////////	
	@Override // main delegator
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		graphicDefaults(g2);
		g2.translate(0, height());
		g2.scale(1, -1); // Invert y-axis
		if (Core.isCH()) {
			points = HullBase.points;
			paintCH(g2);
		} else if (!Core.isCH()) {
			points = CoverBase.vertices;
			paintVC(g2);
		}
		
		g2.scale(1, -1);
		drawInfo(g2);
		if (helpOn) drawHelp(g2);
	}

	// convex hull painting
	private void paintCH(Graphics2D g2) {
    drawHull(g2);
    if (Core.chHeur != ChHeur.NO_HEURISTIC) {
      drawPolygon(g2, HullBase.poly, colors.get("lightblue"));
    }
		drawSpecialCH(g2);
		drawPoints(g2);
	}
	
	private static void drawHull(Graphics2D g2) {
		g2.setColor(colors.get("darkGreen"));
		g2.setStroke(new BasicStroke(3));
		Iterator<Point> iter = HullBase.hull.iterator();
		Point point = (iter.hasNext()) ? iter.next() : null;
		while (iter.hasNext()) {
			drawLine(g2, point, point = iter.next());
		}
		graphicDefaults(g2);
	}

	private void drawSpecialCH(Graphics2D g2) {
		g2.setColor(colors.get("darkGreen"));
		g2.setStroke(new BasicStroke(3));
		if (Core.isSolved && Core.isCH()) {
			drawLine(g2, HullBase.back(0), HullBase.hull.get(0));
		}
		if (Core.chAlg == ChAlg.JARVIS_MARCH) {
			if (HullBase.Q != null && Jarvis.next != null)
				drawLine(g2, HullBase.Q, Jarvis.next);
			if (HullBase.Q != null && HullBase.R != null)
				drawLine(g2, HullBase.Q, HullBase.R);
			if (HullBase.P != null && HullBase.Q != null)
				drawLine(g2, HullBase.P, HullBase.Q);
		}

		if (Graham.m1 != null && Graham.m2 != null) {
			g2.setColor(colors.get("lightBlue"));
			drawLine(g2, Graham.m1);
			drawLine(g2, Graham.m2);
		}
		
		graphicDefaults(g2);
	}

	// vertex cover painting
	private void paintVC(Graphics2D g2) {
		drawEdges(g2);
		drawSpecialVC(g2);
		if (points != null) drawPoints(g2);
		
		graphicDefaults(g2);
	}

	private void drawEdges(Graphics2D g2) {
		for (Edge edge : CoverBase.edges) {
			drawEdge(g2, edge);
		}
	}

	private void drawSpecialVC(Graphics2D g2) {

	}
	
	// general or universal UI drawing functions
	private static void drawPoints(Graphics2D g2) {
		for (Point p : points) {
			drawPoint(g2, p);
		}
	}

	private static void drawPoint(Graphics2D g2, Point p, Boolean coordinate) {
		// control colour and size
		int size = NORMAL_SIZE;
		boolean filled = true;
		if (p == HullBase.start) {
			g2.setColor(colors.get("darkBlue"));
			size *= 1.5;
		} else if (p == HullBase.P || p == HullBase.Q || p == HullBase.R) {
			g2.setColor(colors.get("gold"));
			size *= 1.08;
		} else if (p == Jarvis.next) {
			size *= 1.5;
			g2.setColor(colors.get("lightBlue"));
		} else if (HullBase.hull.contains(p)) {
			g2.setColor(colors.get("lightGreen"));
		} else if (p == CoverBase.u || p == CoverBase.v) {
			g2.setColor(colors.get("orange"));
			size *= 1.5;
		} else if (CoverBase.cover != null && 
			CoverBase.cover.contains(p)) {
			g2.setColor(colors.get("purple"));
			size *= 1.5;
		}
		if (Exact.currentCover.contains(p)) filled = false;
		
		// draw point
		int actSz = size + 6;
		int x = (int) Math.round(p.x), y = (int) Math.round(p.y);
		if (filled) {
			g2.fillOval(x - (size / 2), y - (size / 2), size, size);
		} else {
			Color tempColor = g2.getColor();
			g2.setColor(colors.get("white"));
			g2.fillOval(x - (size / 3), y - (size / 3), size, size);
			g2.setColor(tempColor);
			g2.drawOval(x - (size / 2), y - (size / 2), size, size);
		}
		
		if (p == selectedPoint) {
			g2.drawOval(x - (actSz / 2), y - (actSz / 2), actSz, actSz);
		}
		// in case I want the coordinates
		if (coordinate) {
			g2.scale(1, -1);
			g2.drawString(p.toString(), x + 10, -y - 10);
			g2.scale(1, -1);
		}
		graphicDefaults(g2);
	}

	private static void drawPoint(Graphics2D g, Point p) {
		drawPoint(g, p, false);
	}

	private static void drawLine(Graphics2D g2, Point p1, Point p2) {
		if (p1 == HullBase.P || (p1 == HullBase.Q && p2 != Jarvis.next)) {
			g2.setColor(colors.get("red"));
		} else if (p1 == HullBase.Q && p2 == Jarvis.next) {
			g2.setColor(colors.get("purple"));
		}
		g2.drawLine(
			(int) Math.round(p1.x), 
			(int) Math.round(p1.y), 
			(int) Math.round(p2.x), 
			(int) Math.round(p2.y)
		);
		g2.setColor(colors.get("darkGreen"));
	}
	
	private void drawLine(Graphics2D g2, Line l) {
		drawLine(g2, l.p1, l.p2);
	}

	private void drawEdge(Graphics2D g2, Edge e) {
		Iterator<Point> i = e.iterator();
		Point u = i.next();
		Point v = i.next();
		if (e == Approximation.curEdge) {
			g2.setColor(colors.get("red"));
			g2.setStroke(new BasicStroke(4));
		} else if (e == Approximation.rmEdge) {
			g2.setColor(colors.get("gold"));
			g2.setStroke(new BasicStroke(4));
		} else if (Approximation.edgeSet != null && 
			Approximation.edgeSet.contains(e)) {
			g2.setColor(colors.get("darkGreen"));
			g2.setStroke(new BasicStroke(3));
		} else if ( CoverBase.cover != null &&
			(CoverBase.cover.contains(u) || CoverBase.cover.contains(v))) {
			g2.setColor(colors.get("lightGreen"));
			g2.setStroke(new BasicStroke(2));			
		} else {
			g2.setColor(colors.get("default"));
			g2.setStroke(new BasicStroke(1));
		}

		g2.drawLine(
			(int) Math.round(u.x), 
			(int) Math.round(u.y), 
			(int) Math.round(v.x), 
			(int) Math.round(v.y)
		);
	}

  private void drawPolygon(Graphics2D g2, Polygon poly) {
    drawPolygon(g2, poly, g2.getColor());
  }

  private void drawPolygon(Graphics2D g2, Polygon poly, Color color) {
    drawPolygon(g2, poly, color, true);
  }

  private void drawPolygon(Graphics2D g2, Polygon poly, Color color, boolean close) {
    if (poly.isEmpty()) return;
    g2.setColor(color);
		g2.setStroke(new BasicStroke(3));
		Iterator<Point> iter = poly.iterator();
		Point point = (iter.hasNext()) ? iter.next() : null;
    Point start = point;
		while (iter.hasNext()) {
			drawLine(g2, point, point = iter.next());
		}
    if (close) drawLine(g2, point, start);
		graphicDefaults(g2);
  }

	private void drawHelp(Graphics2D g2) {
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> cmds = new ArrayList<String>();
		keys.add("Key"); 		cmds.add("Command");
		keys.add("=======");	cmds.add("=============================");
		keys.add("G"); 			cmds.add("generate N points");
		keys.add("S"); 			cmds.add("solve problem");
		keys.add("A"); 			cmds.add("toggle algorithm");
		keys.add("H"); 			cmds.add("toggle heuristic");
		keys.add("F"); 			cmds.add("toggle generation function");
		keys.add("up/down");	cmds.add("increase/decrease speed");
		keys.add("+/-"); 		cmds.add("increase/decrease generation size");
		keys.add("I/D"); 		cmds.add("increase/decrease graph density");
		keys.add("\\n"); 		cmds.add("prompt next step");
		keys.add("DEL"); 		cmds.add("remove active point");
		keys.add("ESC"); 		cmds.add("erase points/dismiss help");

		for (int i = 0; i < keys.size(); i++) {
			g2.drawString(keys.get(i), 20, 20 - y(i * LINE_HEIGHT));
			g2.drawString(cmds.get(i), 100, 20 - y(i * LINE_HEIGHT));
		}
	}

	private void drawInfo(Graphics2D g2) {
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<String> vals = new ArrayList<String>();
		labels.add("Information"); 		vals.add("Value");
		labels.add("===============");	vals.add("==========");
		labels.add("problem");					vals.add(Core.problem.toString());
		labels.add("algorithm"); 			vals.add(Core.getAlgAsString());
		labels.add("heuristic"); 			vals.add(Core.getHeurAsString());
		labels.add("speed"); 					vals.add(Core.speed.toString());
		labels.add("generation f(x)");	vals.add(Core.genFx.toString());

		if (Core.isCH()) {
		labels.add("point count"); 		vals.add(HullBase.pointCount());
		labels.add("hull size"); 			vals.add(HullBase.hullSize());
		} else {
		labels.add("vertex count"); 		vals.add(CoverBase.vertexCount());
		labels.add("edge count"); 			vals.add(CoverBase.edgeCount());
		labels.add("cover size"); 			vals.add(CoverBase.coverSize());
		labels.add("generation density");
		vals.add(Generator.getDensityAsString());
		}
		labels.add("generation size");	vals.add(((Integer) Generator.N).toString());

		int height = LINE_HEIGHT * labels.size();
		for (int i = 0; i < labels.size(); i++) {
			g2.drawString(labels.get(i), 20, -height + i * LINE_HEIGHT);
			g2.drawString(vals.get(i), 180, -height + i * LINE_HEIGHT);
		}
	}

	private static void graphicDefaults(Graphics2D g2) {
		g2.setColor(colors.get("default"));
		g2.setStroke(new BasicStroke(1));
		g2.setFont(new Font("Georgia", Font.BOLD, 16));
	}

	private int y(int y) {
		return getHeight() - y;
	}
	private int height() { return getHeight();	}
}