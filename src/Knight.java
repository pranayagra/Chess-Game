
public class Knight extends ChessPiece {

	public Knight(String im, boolean tm, Square lc) {
		super(im, tm, lc);
		
	}

	
	public boolean isMoveLegal(Square dest) {
		if(this.dRow(dest) == 2 && this.dCol(dest) == 1 || this.dRow(dest) == 1 && this.dCol(dest) == 2) { //either 2r1c, or 2c1r
			return true;
		}
		return false;
	}

}