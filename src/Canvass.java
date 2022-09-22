
//  GraphicalPanel.java
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import javax.swing.*;

@SuppressWarnings("serial")
public class Canvass extends JPanel {
	protected convexHull hull;
	protected static final int NORMAL_SIZE = 10;
	protected static final int ACTIVE_SIZE = NORMAL_SIZE + 6;
	protected static final Color LIGHT_GRAY = new Color(220, 220, 220);
	protected Point activePoint = null;
	protected boolean isDragging = false;
	

	MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (activePoint != null) {
				isDragging = true;
				repaint();
			}
		}

		@Override // adds a point
		public void mouseReleased(MouseEvent e) {
			int x = e.getX() - getWidth(),
				y = getHeight() - e.getY();
			convexHull.addPoint(new Point(x, y));
			isDragging = false;
			repaint();
		}

		@Override // detect if you run into a point
		public void mouseMoved(MouseEvent e) {
			if (!isDragging) {
				checkActive(e.getX() - getWidth(), getHeight() - e.getY());
			}
			repaint();
		}

		@Override // dragging points around
		public void mouseDragged(MouseEvent e) {
			if (isDragging && activePoint != null) {
				activePoint.x = e.getX() - getWidth();
				activePoint.y = getHeight() - e.getY();
			}
			repaint();
		}
	};

	public Canvass(convexHull h) {
		hull = h;
		setBackground(Color.WHITE);
		setLayout(null);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyTyped(e);
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE: convexHull.points.clear(); break;
				case KeyEvent.VK_DELETE: 
					convexHull.points.remove(activePoint); 
					convexHull.problemSize--;
					break;
				case KeyEvent.VK_W: hull.printWidth(); break;
				case KeyEvent.VK_G: convexHull.generatePoints(); break;
				case KeyEvent.VK_SPACE: convexHull.solve(); break;
				}
				repaint();
			}
		});
	}

	protected void checkActive(int x, int y) {
		for (Point p : convexHull.points) {
			if (near(p, x, y)) {
				activePoint = p;
				return;
			}
		}
		activePoint = null;
	}

	private boolean near(Point p, double x, double y) {
		double dx = p.x - x, dy = p.y - y;
		dx = (dx > 0) ? dx : -dx;
		dy = (dy > 0) ? dy : -dy;
		return dx < ACTIVE_SIZE && dy < ACTIVE_SIZE;
	}

	protected void drawPoint(Graphics2D g, Point p, Boolean coordinate) {
		int x = (int) Math.round(p.x), y = (int) Math.round(p.y);
		g.fillOval(x - (NORMAL_SIZE / 2), y - (NORMAL_SIZE / 2), NORMAL_SIZE, NORMAL_SIZE);
		if (p == activePoint) {
			g.drawOval(x - (ACTIVE_SIZE / 2), y - (ACTIVE_SIZE / 2), ACTIVE_SIZE, ACTIVE_SIZE);
		}
		// in case I want the coordinates
		if (coordinate) {
			g.scale(1, -1);
			g.drawString(p.toString(), x + 10, -y - 10);
			g.scale(1, -1);
		}
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
		AffineTransform at = g2.getTransform();
		g2.translate(0, getHeight());
		g2.scale(1, -1); // Invert y-axis
		g.setColor(Color.BLUE);
		for (Point p : convexHull.points) {
			drawPoint(g2, p);
		}
		g2.scale(1, -1); // print the text right-side up
		g2.setTransform(at);
	}
}