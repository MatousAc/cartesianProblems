import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GeoPoint extends JPanel {
	private static final int RADIUS = 4;

	public int x;
	public int y;

	public GeoPoint(int x, int y) {
		setBounds(x, y, RADIUS, RADIUS);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		g2.fillOval(x, y, RADIUS, RADIUS);

	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}