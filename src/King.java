
public class King extends ChessPiece{
	
	public King(String im, boolean tm, Square lc) {
		super(im, tm, lc);
		
	}

	
	public boolean isMoveLegal(Square dest) {
		if(this.dRow(dest) <= 1 && this.dCol(dest) <= 1) {
			return true;
		}
		return false;
	}
	
	public void move(Square dest) {
		super.move(dest);
		setNeverMoved(false);
	}

}
