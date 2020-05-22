import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessBoard extends JFrame {

	public static final JLabel[][] cells = new JLabel[8][8];
	public static int step = 1;
	
	public ChessBoard(int gridSize, JPanel panel) {
		panel.setLayout(null);
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Color color = Color.white;
				
				cells[i][j] = new JLabel();
				JLabel cell = cells[i][j];
				cell.setSize(gridSize, gridSize);
				cell.setLocation(i * gridSize, j * gridSize);
				
				if((i + j) % 2 != 0) {
					color = Color.black;
				}
				
				cell.setOpaque(true);
				cell.setBackground(color);
				cell.setBorder(BorderFactory.createLineBorder(Color.black));
				
				panel.add(cell);
				
				add(panel, BorderLayout.CENTER);
			}
		}
	}

	// record knight footprint
	public static void recordFootPrint(int x, int y, int num) {
		JLabel cell = cells[x][y];
		cell.setFont(new Font("Serif", Font.BOLD, 64));
		cell.setText(Integer.toString(num));
		cell.setForeground(Color.RED);
		cell.setHorizontalAlignment(JLabel.CENTER);
	    cell.setVerticalAlignment(JLabel.CENTER);
	}
	
	public static void showIcons(int x, int y) {
		JLabel knightCell = cells[x][y];
		knightCell.setIcon(Global.knightIcon);
		
		for (int i = 0; i < 8; i++) {
			int nextX = x + Global.CX[i];
			int nextY = y + Global.CY[i];
			
			if ((Utilities.boardLimits(nextX, nextY)) && (Utilities.stepArr[nextY * Global.N + nextX] >= Utilities.stepArr[y * Global.N + x]))
				cells[nextX][nextY].setIcon(Global.checkMark);
		}
	}
	
	public static void showIconsManual(int x, int y) {
		JLabel knightCell = cells[x][y];
		knightCell.setIcon(Global.knightIcon);
		
		for (int i = 0; i < 8; i++) {
			int nextX = x + Global.CX[i];
			int nextY = y + Global.CY[i];
			
			if ((Utilities.boardLimits(nextX, nextY)) && (Utilities.stepArr[nextY * Global.N + nextX] < 0))
				cells[nextX][nextY].setIcon(Global.checkMark);
		}
	}
		
	public static void clearIcons() {
		for (JLabel[] cr: cells) {
			for (JLabel c: cr) {
				c.setIcon(null);
			}
		}
	}
	
	public static void initChessBoard(JPanel panel) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		ChessBoard chessBord = new ChessBoard(100, panel);
		chessBord.setTitle("Knight Tour");
		chessBord.setSize(816,839);
		
		chessBord.setLocationRelativeTo(null);
		chessBord.setVisible(true);
		chessBord.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void startManual() {
		try {
			JPanel panel = new JPanel();
			initChessBoard(panel);
			
			for (int i = 0; i < Global.N * Global.N; ++i) {
				Utilities.stepArr[i] = -1; 
			}
			
			String initialPosX = JOptionPane.showInputDialog("Enter knight's initial position X (0-7)");
			String initialPosY = JOptionPane.showInputDialog("Enter knight's initial position Y (0-7)");

			Knight.knight[0] = new Knight(Integer.parseInt(initialPosX), Integer.parseInt(initialPosY), step);
			 
			showIcons(Knight.knight[0].getX(), Knight.knight[0].getY());
			Utilities.stepArr[Integer.parseInt(initialPosY) * Global.N + Integer.parseInt(initialPosX)] = step;  
			step++;
			
			if (step < 64) {
				panel.addMouseListener(new MouseAdapter() {
		             @Override
		             public void mouseReleased(MouseEvent e) {
					    int clickX = (int)(e.getX() / 100);
					    int clickY = (int)(e.getY() / 100);
					    	        
					    if (Utilities.matchNextStep(Knight.knight[step - 2].getX(), Knight.knight[step - 2].getY(), clickX, clickY)) {
			            	clearIcons();
					        
					    	Knight.knight[step - 1] = new Knight(clickX, clickY, step);
					    	Utilities.stepArr[clickY * Global.N + clickX] = step;
					    	
					    	
					        for (int i = 0; i < 8; ++i)  
					        { 
					            for (int j = 0; j < 8; ++j) 
					                System.out.printf("%d\t", Utilities.stepArr[i * 8 + j]); 
					            System.out.printf("\n"); 
					        }
				            System.out.printf("\n"); 

					        
					    	System.out.println("arr index " + (clickY * Global.N + clickX) + " is step " + step);
					    	
					    	System.out.println("x = " + Knight.knight[step - 1].getX() + " y = " + Knight.knight[step - 1].getY());
					    	showIconsManual(Knight.knight[step - 1].getX(), Knight.knight[step - 1].getY());
					    	recordFootPrint(Knight.knight[step - 2].getX(), Knight.knight[step - 2].getY(), step - 1);
					    	step++;
	            	    }
		             }	
				});
			}
		} catch (Throwable e) {
			System.err.println("out of bound!");
		}
	}
	
	public static void startHeuristic() {
		try {
			JPanel panel = new JPanel();
			initChessBoard(panel);
			
			String initialPosX = JOptionPane.showInputDialog("Enter knight's initial position X (0-7)");
			String initialPosY = JOptionPane.showInputDialog("Enter knight's initial position Y (0-7)");

	      // convert String inputs to int values for use in a calculation
			int sx = Integer.parseInt(initialPosX); 
			int sy = Integer.parseInt(initialPosY);
			
			// While we don't get a solution 
			while (!new Utilities().findClosedTour(sx, sy)) { ; }
			
			if (step < 64) {
				panel.addMouseListener(new MouseAdapter() {
		             @Override
		             public void mouseReleased(MouseEvent e) {
		            	 clearIcons();
		            	 boolean stop1 = false, stop2 = false;
		            	 for (int i = 0; i < 64 && !stop1; i++) {
		            		 if (step > 1) {
			            		 if (Knight.knight[i].getStepNo() == step - 1) {
			            			 recordFootPrint(Knight.knight[i].getX(), Knight.knight[i].getY(), step - 1);
			            			 stop1 = true;
			            		 }
		            		 }
		            	 }
		            	 for (int i = 0; i < 64 && !stop2; i++) {	            	 
		            		 
		            		 if (Knight.knight[i].getStepNo() == step) {
	            				 showIcons(Knight.knight[i].getX(), Knight.knight[i].getY());
	            				 System.out.println(Knight.knight[i]);
		            			 stop2 = true;
		            		 }
		            	 }
		             
		            	 step++;
		             }	
				});			
			}
		} catch (Throwable e) {
			System.err.println("out of bound!");
		}
	}
}