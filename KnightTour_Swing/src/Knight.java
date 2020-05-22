public class Knight {

	private int posX;
	private int posY;
	private int stepNo;
	
	public static Knight[] knight = new Knight[64];
	
	public Knight(int x, int y, int stepNo) {
		this.posX = x;
		this.posY = y;
		this.stepNo = stepNo;
	}
	
	public int getX() {
		return posX;
	}
	
	public int getY() {
		return posY;
	}
	
	public int getStepNo() {
		return stepNo;
	}
	
	public void setX(int posX) {
		this.posX = posX;
	}
	
	public void setY(int posY) {
		this.posY = posY;
	}
	
	public void setStepNo(int stepNo) {
		this.stepNo = stepNo;
	}
	
	public boolean inBoard(int x, int y) {
		return (x >= 0 && y >= 0 && x <= 7 && y <= 7);
	}
	
	@Override
	public String toString() {
		return ("Step " + stepNo + ": X = " + posX + ", Y = " + posY);
	}
}