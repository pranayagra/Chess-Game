
public class Rook extends ChessPiece {
	
	public Rook(String im, boolean tm, Square lc) {
		super(im, tm, lc);
		
	}

	
	public boolean isMoveLegal(Square dest) {
		if(this.dRow(dest) == 0 ^ this.dCol(dest) == 0) {//either one has to be 0... 
			return true;
	}
		return false;
	}
	
	public void move(Square dest) {
		super.move(dest);
		setNeverMoved(false);
	}
	
}

