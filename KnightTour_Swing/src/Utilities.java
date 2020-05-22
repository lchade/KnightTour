// Java program to for Kinight's tour problem using 
// Warnsdorff's algorithm
import java.util.concurrent.ThreadLocalRandom; 

public class Utilities {
	
	public static int[] stepArr = new int[Global.N * Global.N]; 

	// Move pattern on basis of the change of
	// x coordinates and y coordinates respectively


	// function restricts the knight to remain within 
	// the 8x8 chessboard
	public static boolean boardLimits(int x, int y) { 
		return ((x >= 0 && y >= 0) && 
				(x < Global.N && y < Global.N)); 
	}

	/* Checks whether a square is valid and
	empty or not */
	public static boolean isEmptyCell(int arr[], int x, int y) { 
		return (boardLimits(x, y)) && (arr[y * Global.N + x] < 0);
	}

	/* Returns the number of empty squares
	adjacent to (x, y) */
	public int nextStepOptions(int arr[], int x, int y) { 
		int count = 0;
			for (int i = 0; i < Global.N; ++i) {
				if (isEmptyCell(arr, (x + Global.CX[i]), (y + Global.CY[i]))) {
					count++;
				}
			}
		return count;
	}
	
	public static boolean matchNextStep(int sx, int sy, int x, int y) {
		boolean match = false;
		for (int i = 0; i < 8 && !match; i++) {
			if(x == sx + Global.CX[i] && y == sy + Global.CY[i]	&& boardLimits(x, y) && stepArr[y * Global.N + x] < 0)
				match = true;
		}
		return match;
	}

	// Picks next point using Warnsdorff's heuristic. 
	// Returns false if it is not possible to pick 
	// next point. 
	Cell nextHeuristicMove(int arr[], Cell cell) { 
		int min_deg_idx = -1, c, 
			min_deg = (Global.N + 1), nx, ny; 

		// Try all N adjacent of (*x, *y) starting 
		// from a random adjacent. Find the adjacent 
		// with minimum degree. 
		int start = ThreadLocalRandom.current().nextInt(1000) % Global.N;
		for (int count = 0; count < Global.N; ++count) { 
			int i = (start + count) % Global.N; 
			nx = cell.x + Global.CX[i]; 
			ny = cell.y + Global.CY[i]; 
			if ((isEmptyCell(arr, nx, ny)) && 
				(c = nextStepOptions(arr, nx, ny)) < min_deg) { 
				min_deg_idx = i; 
				min_deg = c; 
			} 
		} 

		// IF we could not find a next cell 
		if (min_deg_idx == -1) 
			return null; 

		// Store coordinates of next point 
		nx = cell.x + Global.CX[min_deg_idx]; 
		ny = cell.y + Global.CY[min_deg_idx]; 

		// Mark next move 
		arr[ny * Global.N + nx] = arr[(cell.y) * Global.N + (cell.x)] + 1; 

		// Update next point 
		cell.x = nx; 
		cell.y = ny; 

		return cell; 
	} 

	/* displays the chessboard with all the 
	legal knight's moves */
	void saveTrack(int arr[]) { 
		for (int i = 0; i < Global.N; ++i) {
			for (int j = 0; j < Global.N; ++j) {
				int stepIndex = i * Global.N + j;
				//System.out.printf("%d\t", arr[stepIndex]);
				Knight.knight[stepIndex] = new Knight(j, i, arr[stepIndex]);
			}
			//System.out.printf("\n");
		} 
	}

	/* checks its neighbouring sqaures */
	/* If the knight ends on a square that is one 
	knight's move from the beginning square, 
	then tour is closed */
	boolean neighbour(int x, int y, int xx, int yy) { 
		for (int i = 0; i < Global.N; ++i) 
			if (((x + Global.CX[i]) == xx) && 
				((y + Global.CY[i]) == yy)) 
				return true; 

		return false; 
	}
	
	/*
	 * Manual Tour
	 */
//	boolean useManulTour(int sx, int sy) { 
//		// Filling up the chessboard matrix with -1's 
//		
//		for (int i = 0; i < Global.N * Global.N; ++i) {
//			stepArr[i] = -1; 
//		}
//
//		// Current points are same as initial points 
//		Cell cell = new Cell(sx, sy);
//
//		stepArr[cell.y * Global.N + cell.x] = 1; // Mark first move.
//		
//		
//
//		// Check if tour is closed (Can end 
//		// at starting point) 
//		if (!neighbour(ret.x, ret.y, sx, sy)) 
//			return false; 
//
//		saveTrack(stepArr);
//		return true; 
//	}

	/* Generates the legal moves using warnsdorff's 
	heuristics. Returns false if not possible */
	boolean findClosedTour(int sx, int sy) { 
		// Filling up the chessboard matrix with -1's 
		
		for (int i = 0; i < Global.N * Global.N; ++i) {
			stepArr[i] = -1; 
		}

		// Current points are same as initial points 
		Cell cell = new Cell(sx, sy); 

		stepArr[cell.y * Global.N + cell.x] = 1; // Mark first move. 

		// Keep picking next points using 
		// Warnsdorff's heuristic 
		Cell ret = null; 
		for (int i = 0; i < Global.N * Global.N - 1; ++i) {
			ret = nextHeuristicMove(stepArr, cell); 
			if (ret == null) 
				return false;
		} 

		// Check if tour is closed (Can end 
		// at starting point) 
		if (!neighbour(ret.x, ret.y, sx, sy)) 
			return false; 

		saveTrack(stepArr);
		return true; 
	}
} 

class Cell { 
    int x; 
    int y; 
  
    public Cell(int x, int y) { 
        this.x = x; 
        this.y = y; 
    } 
} 
