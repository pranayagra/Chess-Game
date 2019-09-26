
public class Pawn extends ChessPiece{

	public Pawn(String im, boolean tm, Square lc) {
		super(im, tm, lc);
		
	}

	
	public boolean isMoveLegal(Square dest) {

		if(isTeamBlack() && this.getMySquare().getRow() < dest.getRow() && this.dCol(dest) == 1) { //if attacking on black side
			if(dest.getPiece() != null && this.dRow(dest) == 1) {
				return true;
			}
		}
		
		if(isTeamBlack() && this.getMySquare().getRow() < dest.getRow() && this.dCol(dest) == 0) {//I am going down rows...
			if(this.getMySquare().getRow() == 1) { //I haven't moved yet
				if(this.dRow(dest) <= 2 && dest.getPiece() == null) { //Let me "jump"
					return true;
				}
			}
			else {
				if(this.dRow(dest) <= 1) { //I have moved... Don't let me "jump"
					if(dest.getPiece() == null)
						return true;
				}
			}
		}
		
		if(!isTeamBlack() && this.getMySquare().getRow() > dest.getRow() && this.dCol(dest) == 1) { //if attacking on white white
			if(dest.getPiece() != null && this.dRow(dest) == 1) {
				return true;
			}
		}
				
		if(!isTeamBlack() && this.getMySquare().getRow() > dest.getRow() && this.dCol(dest) == 0) {//I am going up rows...
			if(this.getMySquare().getRow() == 6) { //I haven't moved yet
				if(this.dRow(dest) <= 2 && dest.getPiece() == null) { //Let me "jump"
					return true;
				}
			}
			else {
				if(this.dRow(dest) <= 1) { //I have moved... Don't let me "jump"
					if(dest.getPiece() == null)
						return true;
				}
			}
		}

		return false;
		
	}

}
