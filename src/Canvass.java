
//  GraphicalPanel.java
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

@SuppressWarnings("serial")
public class Canvass extends JPanel {
	protected static final int NORMAL_SIZE = 10;
	protected static final int CLOSENESS = NORMAL_SIZE + 6;
	Map<String, Color> colours = new HashMap<String, Color>();
	private Point selectedPoint = null;
	Point leftBottom = null;
	
	protected boolean isDragging = false;
	MouseAdapter mouseAdapter;

	public Canvass() {
		// design
		setBackground(Color.WHITE);
		setLayout(null);
		colours.put("green", new Color(0, 168, 120));
		colours.put("yellow", new Color(216, 241, 160));
		colours.put("gold", new Color(243, 193, 120));
		colours.put("orange", new Color(254, 94, 65));
		colours.put("black", new Color(11, 5, 0));
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
					y = getHeight() - e.getY();
				hull.addPointManually(new Point(x, y));
				isDragging = false;
				repaint();
			}
	
			@Override // detect if you run into a point
			public void mouseMoved(MouseEvent e) {
				if (!isDragging) {
					checkActive(e.getX(), getHeight() - e.getY());
				}
				repaint();
			}
	
			@Override // dragging points around
			public void mouseDragged(MouseEvent e) {
				if (isDragging && selectedPoint != null) {
					selectedPoint.x = e.getX();
					selectedPoint.y = getHeight() - e.getY();
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
				case KeyEvent.VK_ESCAPE: hull.reset(); break;
				case KeyEvent.VK_DELETE: hull.removePointManually(selectedPoint);
				case KeyEvent.VK_G: 
					hull.generatePoints(hull.problemSize); 
					break;
				case KeyEvent.VK_W: hull.printWidth(); break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_SPACE: try {
							hull.solve();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} break;
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

	protected void drawPoint(Graphics2D g, Point p, Boolean coordinate) {
		// control colour and size
		int size = NORMAL_SIZE;
		if (p == leftBottom) {
			g.setColor(colours.get("orange"));
			size *= 1.6;
		}
		
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

		g.setColor(colours.get("black"));
	}

	protected void drawPoint(Graphics2D g, Point p) {
		drawPoint(g, p, false);
	}

	protected void drawLine(Graphics2D g2, Point pt1, Point pt2) {
		g2.drawLine(
			(int) Math.round(pt1.x), 
			(int) Math.round(pt1.y), 
			(int) Math.round(pt2.x), 
			(int) Math.round(pt2.y)
		);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(colours.get("black"));
		AffineTransform at = g2.getTransform();
		g2.translate(0, getHeight());
		g2.scale(1, -1); // Invert y-axis
		for (Point p : hull.points) {
			drawPoint(g2, p);
		}
		g2.scale(1, -1); // print the text right-side up
		g2.setTransform(at);
	}
}