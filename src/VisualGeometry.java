import javax.swing.SwingUtilities;
import java.awt.*;

public class VisualGeometry {
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SwingUtilities.invokeLater(() -> {
			new ApplicationWindow(
				"Convex Hull", 
				(int)screenSize.getWidth(), (int)screenSize.getHeight(), 
				new GraphicalPanel());
		});
	}
}