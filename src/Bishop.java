
public class Bishop extends ChessPiece {

	public Bishop(String im, boolean tm, Square lc) {
		super(im, tm, lc);
		
	}

	
	public boolean isMoveLegal(Square dest) {
		if(this.dRow(dest) == this.dCol(dest)) { //the absolute value delta must be equal to each other (diagonal)
			return true;
		}
		return false;
	}

}
