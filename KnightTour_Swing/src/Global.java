import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Global {
	public static final int N = 8;
	public static final int[] CX = {1, 1, 2, 2, -1, -1, -2, -2};
	public static final int[] CY = {2, -2, 1, -1, 2, -2, 1, -1};
	public static final ImageIcon knightIcon = new ImageIcon(new ImageIcon("src/horseIcon.png").getImage().getScaledInstance(100, 90, Image.SCALE_DEFAULT));
	public static final ImageIcon checkMark = new ImageIcon(new ImageIcon("src/checkmark.png").getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT));
	public static JLabel[][] cells = new JLabel[Global.N][Global.N];
}