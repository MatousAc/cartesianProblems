import javax.swing.*;

/**
 * A convenience superclass for creating simple graphical applications.
 */
@SuppressWarnings("serial")
public class AppWindow extends JFrame {
	/**
	 * Creates a new application window. The client provides application-specific
	 * information.
	 * 
	 * @param title  the title of the window; appears in the window's title bar
	 * @param x      the <i>x</i> coordinate of the window's top-left corner
	 * @param y      the <i>y</i> coordinate of the window's top-left corner
	 * @param width  the window's width
	 * @param height the window's height
	 * @param panel  the application's drawing area and application-specific
	 *               functionality
	 */
	public AppWindow(String title, int width, int height, JComponent panel) {
		super(title);
		setSize(width, height);
		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setVisible(true);
	}
}