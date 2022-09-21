
//  GraphicalPanel.java
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class GraphicalPanel extends JPanel {
	// algs
	protected ArrayList<Point> points = new ArrayList<Point>(); 
	// ui
	protected static final int NORMAL_OFFSET = 4;
	protected static final int ACTIVE_OFFSET = 8;
	protected static final int NORMAL_SIZE = 8;
	protected static final int ACTIVE_SIZE = 16;
	protected static final int CLOSENESS = 25;
	protected static final Color LIGHT_GRAY = new Color(220, 220, 220);
	private static final Color DARK_GREEN = new Color(0, 150, 0);
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

		@Override
		public void mouseReleased(MouseEvent e) {
			int x = e.getX() - getWidth(),
				y = getHeight() - e.getY();
			points.add(new Point(x, y));
			isDragging = false;
			repaint();
		}

		@Override // dragging points
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

	public GraphicalPanel() {
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
				case KeyEvent.VK_ESCAPE: points.clear(); break;
				}
				repaint();
			}
		});
	}

	protected void checkActive(int x, int y) {
		for (Point p : points) {
			if (near(p, x, y)) {
				activePoint = p;
				break;
			}
		}
	}

	private boolean near(Point p, double x, double y) {
		double dx = p.x - x, dy = p.y - y;
		dx = (dx > 0) ? dx : -dx;
		dy = (dy > 0) ? dy : -dy;
		return dx < CLOSENESS && dy < CLOSENESS;
	}

	protected void drawPoint(Graphics2D g, Point p, Boolean coordinate) {
		int x = (int) Math.round(p.x), y = (int) Math.round(p.y);
		g.fillOval(x - NORMAL_OFFSET, y - NORMAL_OFFSET, NORMAL_SIZE, NORMAL_SIZE);
		if (p == activePoint) {
			g.drawOval(x - ACTIVE_OFFSET, y - ACTIVE_OFFSET, ACTIVE_SIZE, ACTIVE_SIZE);
		}
		// in case I want the coordinates
		if (drawCoord) {
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
		g2.translate(getWidth(), getHeight());
		g2.scale(1, -1); // Invert y-axis
		g.setColor(Color.BLUE);
		for (Point p : points) {
			drawPoint(g2, p);
		}
		g2.scale(1, -1); // print the text right-side up
		g2.setTransform(at);
	}

	private void drawGrid(Graphics2D g, int interval) {
		int w = getWidth(), h = getHeight(), maxX = w / 2, maxY = h / 2;
		g.setColor(LIGHT_GRAY);
		// Draw the minor grid lines
		for (int i = 0; i < w / 2; i += interval) {
			g.drawLine(-maxX, i, maxX, i);
			g.drawLine(-maxX, -i, maxX, -i);
			g.drawLine(i, -maxY, i, maxY);
			g.drawLine(-i, -maxY, -i, maxY);

		}
		// Draw the axes
		g.setColor(Color.BLACK);
		var strokeSaved = g.getStroke();
		g.setStroke(new BasicStroke(2));
		g.drawLine(-maxX, 0, maxX, 0);
		g.drawLine(0, -maxY, 0, maxY);

		// Draw the x-axis arrow heads
		g.drawLine(-maxX, 0, -maxX + 10, -7);
		g.drawLine(-maxX, 0, -maxX + 10, 7);
		g.drawLine(maxX, 0, maxX - 10, -7);
		g.drawLine(maxX, 0, maxX - 10, 7);

		// Draw the y-axis arrow heads
		g.drawLine(0, -maxY, -7, -maxY + 10);
		g.drawLine(0, -maxY, 7, -maxY + 10);
		g.drawLine(0, maxY, -7, maxY - 10);
		g.drawLine(0, maxY, 7, maxY - 10);

		g.setStroke(strokeSaved);
	}

	protected void drawExtendedLine(Graphics2D g2, Point pt1, Point pt2) {
		double m = Geometry.slope(pt1, pt2), b = Geometry.intercept(pt1, pt2), 
			   new_x1, new_y1, new_x2, new_y2;
		if (m == Double.POSITIVE_INFINITY) {
			new_x1 = new_x2 = pt1.x;
			new_y1 = -getHeight() / 2;
			new_y2 = getHeight() / 2;
		} else if (m >= -1.0 && m <= 1.0) {
			Point i1 = Geometry.intersection(new Line(m, b), new Line(Double.POSITIVE_INFINITY, -getWidth() / 2)),
				  i2 = Geometry.intersection(new Line(m, b), new Line(Double.POSITIVE_INFINITY, getWidth() / 2));
			new_x1 = i1.x;
			new_y1 = i1.y;
			new_x2 = i2.x;
			new_y2 = i2.y;
		} else {
			Point i1 = Geometry.intersection(new Line(m, b), new Line(0, -getHeight() / 2)),
				  i2 = Geometry.intersection(new Line(m, b), new Line(0, getHeight() / 2));
			new_x1 = i1.x;
			new_y1 = i1.y;
			new_x2 = i2.x;
			new_y2 = i2.y;
		}
		var strokeSaved = g2.getStroke();
		g2.setStroke(new BasicStroke(2));
		g2.drawLine((int) Math.round(new_x1), (int) Math.round(new_y1), (int) Math.round(new_x2),
				(int) Math.round(new_y2));
		g2.setStroke(strokeSaved);
	}
}
