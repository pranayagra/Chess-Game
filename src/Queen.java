
public class Queen extends ChessPiece {

	public Queen(String im, boolean tm, Square lc) {
		super(im, tm, lc);
		
	}

	
	public boolean isMoveLegal(Square dest) {
		if(( this.dRow(dest) == this.dCol(dest) ) || ( this.dRow(dest) == 0 || this.dCol(dest) == 0 ) ) { //combine Rook + Bishop
			return true;
		}
		return false;
	}

}
