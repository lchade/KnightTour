package knightTour;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import knightTour.Chessboard;

public class Knight {
	
	private int posX;
	private int posY;
	private int stepNo;
	private ImageView[] moveNumberIcon;

	
	public static final int[] MOVE_X = {2, 1, -1, -2, -2, -1, 1, 2};
	public static final int[] MOVE_Y = {-1, -2, -2, -1, 1, 2, 2, 1};
	public static final ImageView KNIGHT_ICON = new ImageView(new Image("src/horseIcon.png", 100, 100, false, false));
	public static final ImageView CHECKMARK_ICON = new ImageView(new Image("src/checkmark.png", 85, 85, false, false));
	
	private int[][] accessibility = {
        {2, 3, 4, 4, 4, 4, 3, 2},
        {3, 4, 6, 6, 6, 6, 4, 3},
        {4, 6, 8, 8, 8, 8, 6, 4},
        {4, 6, 8, 8, 8, 8, 6, 4},
        {4, 6, 8, 8, 8, 8, 6, 4},
        {4, 6, 8, 8, 8, 8, 6, 4},
        {3, 4, 6, 6, 6, 6, 4, 3},
        {2, 3, 4, 4, 4, 4, 3, 2}};
	
	public Knight() {
		stepNo = 0;
		moveNumberIcon = new ImageView[8];
		for (int i = 0; i < moveNumberIcon.length; i++) {
			moveNumberIcon[i] = CHECKMARK_ICON;
		}
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public int getStepNo() {
		return stepNo;
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public void setStepNo(int stepNo) {
		this.stepNo = stepNo;
	}
	
	public ImageView[] getMoveNumberIcon() {
		return moveNumberIcon;
	}
	
	@Override
	public String toString() {
		return ("Step " + stepNo + ": X = " + posX + ", Y = " + posY);
	}
	
	public void setPreMoveIndicator(GridPane gp) {
        Label prevMoveIndicator = new Label(Integer.toString(++stepNo));
        prevMoveIndicator.setFont(new Font(30));
        prevMoveIndicator.setTextFill(Color.RED);
        GridPane.setHalignment(prevMoveIndicator, HPos.CENTER);
        gp.add(prevMoveIndicator, posX, posY);
	}
	
	public void setMoveNumberIcon(GridPane gp) {
        for (int i = 0; i < moveNumberIcon.length; i++) {
            int iconPosX = posX + MOVE_X[i];
            int iconPosY = posY + MOVE_Y[i];
            FilteredList<Node> addedLabels = new FilteredList<>(gp.getChildren(), s -> s instanceof Label);
            boolean valid = false;

            gp.getChildren().remove(moveNumberIcon[i]);

            // Check if icon positions are in-screen/on the board
            // and isn't on location already visited
            if (iconPosX >= 0 && (iconPosX <= 7)) {
                if (iconPosY >= 0 && (iconPosY <= 7)) {
                    if (addedLabels.filtered(s -> (GridPane.getColumnIndex(s) == iconPosX && GridPane.getRowIndex(s) == iconPosY)).isEmpty()) {
                        gp.add(moveNumberIcon[i], iconPosX, iconPosY);
                        moveNumberIcon[i].setVisible(true);
                        valid = true;
                    }
                }
            }

            // If not valid then set icon at location of knight and make it invisible
            if (!valid) {
                gp.add(moveNumberIcon[i], posX, posY);
                moveNumberIcon[i].setVisible(false);
            }
        }
	}
	
    public int findLeastAccessibleSquare(int posX, int posY, GridPane gp) {


        int moveNumber = MOVE_X.length;

        int leastAccessibleSq = Chessboard.TOTALSQNUM;


        FilteredList<Node> addedLabels = new FilteredList<>(gp.getChildren(), s -> s instanceof Label);

        // Find least accessible square between available moves
        for (int i = 0; i < MOVE_X.length; i++) {
            int iconPosX = posX + MOVE_X[i];
            int iconPosY = posY + MOVE_Y[i];

            if (iconPosX != posX && iconPosY != posY) {
                if (iconPosX >= 0 && (iconPosX <= Chessboard.SQSIDENUM - 1)) {
                    if (iconPosY >= 0 && (iconPosY <= Chessboard.SQSIDENUM - 1)) {
                        if (addedLabels.filtered(s -> (GridPane.getColumnIndex(s) == iconPosX && GridPane.getRowIndex(s) == iconPosY)).isEmpty()) {
                            if (accessibility[iconPosY][iconPosX] < leastAccessibleSq) {
                                leastAccessibleSq = accessibility[iconPosY][iconPosX];
                                moveNumber = i;
                            }
                        }
                    }
                }
            }
        }

        return moveNumber;
    }
	
    public void moveKnightHeuristically(GridPane gp) {
        moveKnight(findLeastAccessibleSquare(posX, posY, gp), gp);
    }
    
	public void initKnight(GridPane gp) {
		gp.add(KNIGHT_ICON, posX, posY);
		setMoveNumberIcon(gp);
	}
	
	public void moveKnight(int moveNumber, GridPane gp) {
        if (moveNumber >= 0 && moveNumber <= 7) {
            setPreMoveIndicator(gp);

            posX += MOVE_X[moveNumber];
            posY += MOVE_Y[moveNumber];

            gp.getChildren().remove(KNIGHT_ICON);
            gp.add(KNIGHT_ICON, posX, posY);

            setMoveNumberIcon(gp);
        }
	}
}