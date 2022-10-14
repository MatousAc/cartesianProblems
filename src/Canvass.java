import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import enums.*;

@SuppressWarnings("serial")
public class Canvass extends JPanel {
	protected static final int NORMAL_SIZE = 10;
	protected static final int CLOSENESS = NORMAL_SIZE + 6;
	private static final int LINE_HEIGHT = 18;
	static Map<String, Color> colours = new HashMap<String, Color>();
	MouseAdapter mouseAdapter;
	private static boolean isDragging, isPaused, helpOn;
	private static Point selectedPoint = null;
	static ArrayList<Point> points;

	public Canvass() {
		reset();
		if (Core.isCH()) {
			points = HullBase.points;
		} else {
			points = CoverBase.graph.vertices;
		}
		// design
		setBackground(Color.WHITE);
		setLayout(null);
		colours.put("lightBlue", new Color(168, 213, 226));
		colours.put("darkBlue", new Color(28, 114, 147));
		colours.put("gold", new Color(249, 166, 32));
		colours.put("red", new Color(253, 21, 27));
		colours.put("orange", new Color(237, 106, 90));
		colours.put("lightGreen", new Color(84, 140, 47));
		colours.put("darkGreen", new Color(60, 73, 17));
		colours.put("purple", new Color(161, 22, 146));
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
				case KeyEvent.VK_G: 
					Generator.generatePoints(); break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_SPACE:
					new Thread(() -> Core.solve()).start(); break;
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
				case KeyEvent.VK_EQUALS: Core.genSize++; break;
				case KeyEvent.VK_MINUS: Core.genSize--; break;
				case KeyEvent.VK_UP:
					Core.speed = Core.speed.increase(); break;
				case KeyEvent.VK_DOWN:
					Core.speed = Core.speed.decrease(); break;
				case KeyEvent.VK_P:
					Core.problem = Core.problem.next(); break;
				case KeyEvent.VK_I:
					Core.densityUp(); break;
				case KeyEvent.VK_D:
					Core.densityDown(); break;
					case KeyEvent.VK_A: Core.nextAlg(); break;
				case KeyEvent.VK_F:
					Core.genFx = Core.genFx.next(); break;
				case KeyEvent.VK_ALT:
				case KeyEvent.VK_CONTROL:
				case KeyEvent.VK_WINDOWS:
				case KeyEvent.VK_PAUSE:
					break; // nothing for special keys
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
			points = CoverBase.graph.vertices;
			paintVC(g2);
		}
		
		g2.scale(1, -1);
		drawInfo(g2);
		if (helpOn) drawHelp(g2);
	}

	// convex hull painting
	private void paintCH(Graphics2D g2) {
		drawHull(g2);
		drawSpecialCH(g2);
		drawPoints(g2);
	}
	
	private static void drawHull(Graphics2D g2) {
		g2.setColor(colours.get("darkGreen"));
		g2.setStroke(new BasicStroke(3));
		Iterator<Point> iter = HullBase.hull.iterator();
		Point point = (iter.hasNext()) ? iter.next() : null;
		while (iter.hasNext()) {
			drawLine(g2, point, point = iter.next());
		}
		graphicDefaults(g2);
	}

	private void drawSpecialCH(Graphics2D g2) {
		g2.setColor(colours.get("darkGreen"));
		g2.setStroke(new BasicStroke(3));
		if (Core.solved && Core.isCH()) {
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
			g2.setColor(colours.get("lightBlue"));
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
		for (Edge edge : CoverBase.graph.edges) {
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
		if (p == HullBase.start) {
			g2.setColor(colours.get("darkBlue"));
			size *= 1.5;
		} else if (p == HullBase.P || p == HullBase.Q || p == HullBase.R) {
			g2.setColor(colours.get("gold"));
			size *= 1.08;
		} else if (p == Jarvis.next) {
			size *= 1.5;
			g2.setColor(colours.get("lightBlue"));
		} else if (HullBase.hull.contains(p)) {
			g2.setColor(colours.get("lightGreen"));
		} else if (TwoFactor.curEdge != null && 
			TwoFactor.curEdge.contains(p)) {
			g2.setColor(colours.get("lightBlue"));
			size *= 1.5;
		} else if (CoverBase.cover != null && 
			CoverBase.cover.contains(p)) {
			g2.setColor(colours.get("purple"));
			size *= 1.5;
		}
		// draw point
		int actSz = size + 6;
		int x = (int) Math.round(p.x), y = (int) Math.round(p.y);
		g2.fillOval(x - (size / 2), y - (size / 2), size, size);
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
			g2.setColor(colours.get("red"));
		} else if (p1 == HullBase.Q && p2 == Jarvis.next) {
			g2.setColor(colours.get("purple"));
		}
		g2.drawLine(
			(int) Math.round(p1.x), 
			(int) Math.round(p1.y), 
			(int) Math.round(p2.x), 
			(int) Math.round(p2.y)
		);
		g2.setColor(colours.get("darkGreen"));
	}
	
	private void drawLine(Graphics2D g2, Line l) {
		drawLine(g2, l.p1, l.p2);
	}

	private void drawEdge(Graphics2D g2, Edge e) {
		if (e == TwoFactor.curEdge) {
			g2.setColor(colours.get("red"));
			g2.setStroke(new BasicStroke(4));
		} else if (e == TwoFactor.rmEdge) {
			g2.setColor(colours.get("gold"));
			g2.setStroke(new BasicStroke(4));
		} else if (TwoFactor.edgeSet != null && 
			TwoFactor.edgeSet.contains(e)) {
			g2.setColor(colours.get("darkGreen"));
			g2.setStroke(new BasicStroke(2));
		} else {
			g2.setColor(colours.get("default"));
			g2.setStroke(new BasicStroke(1));
		}

		Iterator<Point> i = e.iterator();
		Point u = i.next();
		Point v = i.next();
		g2.drawLine(
			(int) Math.round(u.x), 
			(int) Math.round(u.y), 
			(int) Math.round(v.x), 
			(int) Math.round(v.y)
		);
	}

	private void drawHelp(Graphics2D g2) {
		ArrayList<String> keys = new ArrayList<>();
		ArrayList<String> cmds = new ArrayList<>();
		keys.add("Key"); 		cmds.add("Command");
		keys.add("=======");	cmds.add("=============================");
		keys.add("G"); 			cmds.add("generate N points");
		keys.add("S"); 			cmds.add("solve problem");
		keys.add("\\n"); 		cmds.add("prompt next step");
		keys.add("ESC"); 		cmds.add("erase points/dismiss help");
		keys.add("DEL"); 		cmds.add("remove active point");
		keys.add("up/down");	cmds.add("increase/decrease speed");
		keys.add("+/-"); 		cmds.add("increase/decrease generation size");
		keys.add("I/D"); 		cmds.add("increase/decrease graph density");
		keys.add("T"); 			cmds.add("toggle algorithm");
		keys.add("F"); 			cmds.add("toggle generation function");

		for (int i = 0; i < keys.size(); i++) {
			g2.drawString(keys.get(i), 20, 20 - y(i * LINE_HEIGHT));
			g2.drawString(cmds.get(i), 100, 20 - y(i * LINE_HEIGHT));
		}
	}

	private void drawInfo(Graphics2D g2) {
		ArrayList<String> labels = new ArrayList<>();
		ArrayList<String> vals = new ArrayList<>();
		labels.add("Information"); 		vals.add("Value");
		labels.add("==========="); 		vals.add("==========");
		labels.add("problem");					vals.add(Core.problem.toString());
		labels.add("algorithm"); 			vals.add(Core.getAlgAsString());
		labels.add("speed"); 					vals.add(Core.speed.toString());
		labels.add("generation f(x)");	vals.add(Core.genFx.toString());

		if (Core.isCH()) {
			labels.add("problem size");
			vals.add(((Integer) HullBase.points.size()).toString());
			labels.add("solution size");
			vals.add(((Integer) HullBase.hull.size()).toString());
		} else {
			labels.add("vertex count");
			vals.add(((Integer) CoverBase.graph.vertices.size()).toString());
			labels.add("edge count");
			vals.add("edges");
			labels.add("graph density");
			vals.add(String.format("%.02f", Core.density));
			labels.add("solution size");
			vals.add(((Integer) CoverBase.cover.size()).toString());
		}
		labels.add("generation size");	vals.add(((Integer) Core.genSize).toString());

		int height = LINE_HEIGHT * labels.size();
		for (int i = 0; i < labels.size(); i++) {
			g2.drawString(labels.get(i), 20, -height + i * LINE_HEIGHT);
			g2.drawString(vals.get(i), 150, -height + i * LINE_HEIGHT);
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
	private int height() { return getHeight();	}
}