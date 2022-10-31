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
	private boolean isDragging, isPaused, helpOn;
	private Point selectedPoint = null;
  private Graphics2D g2;

	public Canvass() {
		reset();
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
				Core.prob.addPoint(new Point(x, y));
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
        case KeyEvent.VK_P: Core.nextProblem(); break;
        case KeyEvent.VK_I: Generator.densityUp(); break;
        case KeyEvent.VK_D: Generator.densityDown(); break;
        case KeyEvent.VK_A: Core.prob.nextAlg(); break;
        case KeyEvent.VK_F: Generator.fx = Generator.fx.next(); break;
        case KeyEvent.VK_G:
          Core.prob.unsolve();
          Generator.generateProblem();
          break;
        case KeyEvent.VK_H: 
          Hull.heuristic = Hull.heuristic.next();
          break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_SPACE:
					if (!Core.isSolving) {
						new Thread(() -> Core.prob.solve()).start();
					} else {
						System.out.println("Already solving.");
					}
					break;
				case KeyEvent.VK_C:
				case KeyEvent.VK_ENTER: isPaused = false; break;
				case KeyEvent.VK_ESCAPE:
					if (helpOn) helpOn = false;
					else Core.prob.reset();
					break;
				case KeyEvent.VK_DELETE:
				case KeyEvent.VK_BACK_SPACE:
					Core.prob. removePoint(selectedPoint);
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
		for (Point p : Core.prob.getPoints()) {
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

	/** Thread sleeps for {@code ms} milliseconds. Stalls on prompt speed. */
	protected void wait(int ms) {
		if (Core.speed == Speed.PROMPT && isPaused == false) { 
			isPaused = true;
  		while (isPaused) {
  			wait(10);
  		};
		}
		try { Thread.sleep(ms); }
		catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Determines the delay that the calling thread should be
	 * paused for depending on the speed setting in Core
	 * @return delay time in milliseconds.
	 */
	protected int getDelay() {
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

	/// painting the UI ///
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		graphicDefaults();
		// place origin on bottom left
    g2.translate(0, height());
		g2.scale(1, -1);
    // draw Points and Polygons
		Core.prob.paint(this);
		// drawing text
		g2.scale(1, -1);
		drawInfo();
		if (helpOn) drawHelp();
	}

	void drawPoint(Point p) {
		drawPoint(p, "default");
	}
  void drawPoint(Point p, String color) {
    drawPoint(p, color, 1);
  }
	void drawPoint(Point p, String color, double relSize) {
    drawPoint(p, color, relSize, true);
  }
  void drawPoint(Point p, String color, double relSize, boolean filled) {
    Color saveColor = g2.getColor();
    g2.setColor(colors.get(color));

		int size = (int) (NORMAL_SIZE * relSize);
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

    g2.setColor(saveColor);
	}


	void drawLine(Line l) {
		drawLine(l.p1, l.p2);
	}
  void drawLine(Point p1, Point p2) {
		g2.drawLine(
			(int) Math.round(p1.x), 
			(int) Math.round(p1.y), 
			(int) Math.round(p2.x), 
			(int) Math.round(p2.y)
		);
	}
	
  void drawPolygon(Polygon poly) {
    drawPolygon(poly, g2.getColor());
  }
  void drawPolygon(Polygon poly, String color) {
    drawPolygon(poly, colors.get(color));
  }
  void drawPolygon(Polygon poly, Color color) {
    Color saveColor = g2.getColor();
    Stroke saveStroke = g2.getStroke();
    
    if (poly.isEmpty()) return;
    g2.setColor(color);
		g2.setStroke(new BasicStroke(3));
		Iterator<Point> iter = poly.iterator();
		Point point = (iter.hasNext()) ? iter.next() : null;
    Point start = point;
		while (iter.hasNext()) {
			drawLine(point, point = iter.next());
		}
    if (poly.isClosed()) drawLine(point, start);

		g2.setColor(saveColor);
		g2.setStroke(saveStroke);
  }

	private void drawHelp() {
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

	private void drawInfo() {
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<String> vals = new ArrayList<String>();
		labels.add("Information"); 		vals.add("Value");
		labels.add("===============");	vals.add("==========");
		labels.add("problem");					vals.add(Core.prob.probAsString());
		labels.add("algorithm"); 			vals.add(Core.prob.algAsString());
		labels.add("heuristic"); 			vals.add(Core.prob.heurAsString());
		labels.add("speed"); 					vals.add(Core.speed.toString());
		labels.add("generation f(x)");	vals.add(Generator.fx.toString());

		if (Core.isCH()) {
		labels.add("point count"); 		vals.add(Hull.pointCount());
		labels.add("hull size"); 			vals.add(Hull.hullSize());
		} else {
		labels.add("vertex count"); 		vals.add(Cover.vertexCount());
		labels.add("edge count"); 			vals.add(Cover.edgeCount());
		labels.add("cover size"); 			vals.add(Cover.coverSize());
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

  /// basic helper functions ///
	void graphicDefaults() {
		g2.setColor(colors.get("default"));
		g2.setStroke(new BasicStroke(1));
		g2.setFont(new Font("Georgia", Font.BOLD, 16));
	}

  void setColor(String color) {
    g2.setColor(colors.get(color));
  }

  void setStroke(int stroke) {
    g2.setStroke(new BasicStroke(stroke));
  }

	private int y(int y) {
		return getHeight() - y;
	}
	private int height() { return getHeight();	}
}