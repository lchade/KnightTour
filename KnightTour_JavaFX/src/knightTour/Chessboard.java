package knightTour;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Chessboard {

	public static final int SQSIDENUM = 8;
	public static final int TOTALSQNUM = SQSIDENUM * SQSIDENUM;
	public static final int SQDIM = 100;
	public static final int BOARDDIM = SQDIM * 8;
	
//    public static void initChessboard(GridPane gp) {
//
//        Rectangle[][] cells = new Rectangle[8][8];
//        
//        for (int y = 0; y < 8; y++) {
//            for (int x = 0; x < 8; x++) {
//                cells[x][y] = new Rectangle(x, y, 100, 100);
//                if ((x + y) % 2 == 0) {
//                    cells[x][y].setFill(Color.AZURE);
////                    System.out.println("WHITE:" +rectangle[x][y]);
//                } else {
//                    cells[x][y].setFill(Color.DARKSLATEGREY);
////                    System.out.println("BLACK:" + rectangle[x][y]);
//                }
//                gp.add(cells[x][y], x, y);
//            }
////            System.out.println(y + "==========" + gp);
//        }
//    }
    
    public static void initChessboard(GridPane gpane){
        int coorX = 0;
        int coorY = 0;

        boolean grid_offset = false;

        for(int i = 0; i < TOTALSQNUM/2; i++){
            if(grid_offset){
                coorX = setWhiteSquare(gpane, coorX, coorY);
            }

            Rectangle blackSq = new Rectangle(SQDIM, SQDIM);
            blackSq.setFill(Color.BLACK);
            gpane.add(blackSq, coorX, coorY);
            coorX++;

            if(!grid_offset) {
                coorX = setWhiteSquare(gpane, coorX, coorY);
            }

            // If coorX has reached end of board, begin new row
            if(coorX >= SQSIDENUM){
                coorX = 0;
                coorY++;
                grid_offset = !grid_offset;
            }
        }
    }

    private static int setWhiteSquare(GridPane gpane, int coorX, int coorY) {
        Rectangle whiteSq = new Rectangle(SQDIM, SQDIM);
        whiteSq.setFill(Color.WHITE);
        gpane.add(whiteSq, coorX, coorY);
        coorX++;

        return coorX;
    }
}