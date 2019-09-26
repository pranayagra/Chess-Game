import java.awt.*;
import javax.swing.*;

public class GameBoard extends JFrame {
	private static final int ROWS = 8, COLS = 8;
	private Square[][] squares;
	int counter = 0;
	private boolean whiteCastleAlready = false;
	private boolean blackCastleAlready = false;
	Square whiteKing = null;
	Square blackKing = null;
	private boolean whiteCanCastleL = false;
	private boolean whiteCanCastleR = false;
	private boolean blackCanCastleL = false;
	private boolean blackCanCastleR = false;

	Square first = null;
	Square second = null;
	

	
	public GameBoard(){
		super("CHESS");
		
		this.setLayout(new GridLayout(ROWS,COLS));

		squares = new Square[ROWS][COLS];

		for(int r = 0; r<ROWS; r+=2) {
			for(int c= 1; c<COLS; c+=2) {
				squares[r][c] = new Square(r, c, this, true);
			}
		}
		for(int r = 1; r<ROWS; r+=2) {
			for(int c= 0; c<COLS; c+=2) {
				squares[r][c] = new Square(r, c, this, true);
			}
		}
		for(int r = 0; r<ROWS; r++) {
			for(int c= 0; c<COLS; c++) {
				if(squares[r][c] == null) {
					squares[r][c] = new Square(r, c, this, false);
				}
				this.add(squares[r][c]);
			}
		}
		
		for(int c = 0; c<COLS; c++) {
			squares[1][c].setPiece(new Pawn("pics/bPawn.png", true, squares[1][c]));
		}
		
		for(int c = 0; c<COLS; c++) {
			squares[6][c].setPiece(new Pawn("pics/wPawn.png", false, squares[6][c]));
		}
		squares[7][0].setPiece(new Rook("pics/wRook.png", false, squares[7][0]));
		squares[7][7].setPiece(new Rook("pics/wRook.png", false, squares[7][7]));
		squares[7][1].setPiece(new Knight("pics/wKnight.png", false, squares[7][1]));
		squares[7][6].setPiece(new Knight("pics/wKnight.png", false, squares[7][6]));
		squares[7][2].setPiece(new Bishop("pics/wBishop.png", false, squares[7][2]));
		squares[7][5].setPiece(new Bishop("pics/wBishop.png", false, squares[7][5]));
		squares[7][3].setPiece(new Queen("pics/wQueen.png", false, squares[7][3]));
		squares[7][4].setPiece(new King("pics/wKing.png", false, squares[7][4]));
		
		squares[0][0].setPiece(new Rook("pics/bRook.png", true, squares[0][0]));
		squares[0][7].setPiece(new Rook("pics/bRook.png", true, squares[0][7]));
		squares[0][1].setPiece(new Knight("pics/bKnight.png", true, squares[0][1]));
		squares[0][6].setPiece(new Knight("pics/bKnight.png", true, squares[0][6]));
		squares[0][2].setPiece(new Bishop("pics/bBishop.png", true, squares[0][2]));
		squares[0][5].setPiece(new Bishop("pics/bBishop.png", true, squares[0][5]));
		squares[0][3].setPiece(new Queen("pics/bQueen.png", true, squares[0][3]));
		squares[0][4].setPiece(new King("pics/bKing.png", true, squares[0][4]));

		
		
		//some finishing touches
		this.setSize(750,750);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//one of the squares will call this function to tell the board it was clicked
	public void clicked(Square whoGotClicked){
		
		findKings();
		
		if(whoGotClicked.getPiece() != null && first == null && second == null) { //first click
			if(counter % 2 == 0) { //white's turn
				if(whoGotClicked.getPiece().isTeamBlack() == false) {
					first = whoGotClicked;
				}
				else {
					JOptionPane.showMessageDialog(this, "It is not your turn!");
				}
			}
			if(counter % 2 == 1) { //white's turn
				if(whoGotClicked.getPiece().isTeamBlack() == true)
					first = whoGotClicked;
				else {
					JOptionPane.showMessageDialog(this, "It is not your turn!");
				}
			}
		}
		
		//To make a second click
		else if(first != null && second == null) {
			if(whoGotClicked.getPiece() != null && counter % 2 == 0 && !whoGotClicked.getPiece().isTeamBlack()) {//team white clicked white piece (change piece selected)
				HighlightAllOff();
				first = whoGotClicked;
			}
			else if(whoGotClicked.getPiece() != null && counter % 2 == 1 && whoGotClicked.getPiece().isTeamBlack()) {//team black clicked black piece (change piece selected)
				HighlightAllOff();
				first = whoGotClicked;
			}
			else {
				if(whoGotClicked.getPiece() == null) //no piece got 2nd clicked
					second = whoGotClicked;
				else if(whoGotClicked.getPiece() != null && whoGotClicked.getPiece().isTeamBlack() && counter % 2 == 0)
					second = whoGotClicked;
				else if(whoGotClicked.getPiece() != null && whoGotClicked.getPiece().isTeamBlack() == false && counter % 2 == 1)
					second = whoGotClicked;
			}
		}
		
		if(first != null && first.getPiece() != null) {
			for(int r = 0; r<squares.length; r++) {
				for(int c = 0; c<squares[0].length; c++) {
					if(first.getPiece().isMoveLegal(squares[r][c]) && (first.getPiece() instanceof Knight || !isBlocked(first, squares[r][c]))&& !squares[r][c].equals(first)) { //if legal (regardless of blocks)
						
						squares[r][c].setHighlighted(true);
						if(counter % 2 == 0 && squares[r][c].getPiece() != null && !squares[r][c].getPiece().isTeamBlack())
							squares[r][c].setHighlighted(false); //highlight all the moves... (ignore blocks)
						if(counter % 2 == 1 && squares[r][c].getPiece() != null && squares[r][c].getPiece().isTeamBlack())
							squares[r][c].setHighlighted(false); //highlight all the moves... (ignore blocks)
						findKings();

						if(checkHelp()) { //a king is in trouble...
							if(squares[r][c].isHighlighted()) { //check all highlighted squares
								ChessPiece temp = null;
								if(squares[r][c].getPiece() != null) {
									temp = squares[r][c].getPiece();
								}
								first.getPiece().move(squares[r][c]); //move my piece...
								findKings(); //I know where the kings are... hopefully both :)
								if(checkHelp()) { //Uhh... still in check
									squares[r][c].setHighlighted(false); //Dehighlight the square, rendering it as an Illegal move
								}
								squares[r][c].getPiece().move(first); //move back the piece
								if(temp != null)
									squares[r][c].setPiece(temp);
							}
						}
						findKings();
					}
				}
			}
		}
		
		castling();
		
		if(first != null && second != null) {
				if(squares[second.getRow()][second.getCol()].isHighlighted()) {
					if(first.getPiece() != null && !first.getPiece().isTeamBlack() && first.getPiece() instanceof King && whiteCanCastleL && whoGotClicked.getRow() == 7 && whoGotClicked.getCol() == 2) {
						System.out.println("I can castle! WL");
						squares[7][0].getPiece().move(squares[7][3]);
					}
					if(first.getPiece() != null && !first.getPiece().isTeamBlack() && first.getPiece() instanceof King && whiteCanCastleR && whoGotClicked.getRow() == 7 && whoGotClicked.getCol() == 6) {
						System.out.println("I can castle! WR");
						squares[7][7].getPiece().move(squares[7][5]);
					}
					if(first.getPiece() != null && first.getPiece().isTeamBlack() && first.getPiece() instanceof King && blackCanCastleL && whoGotClicked.getRow() == 0 && whoGotClicked.getCol() == 2) {
						System.out.println("I can castle! BL");
						squares[0][0].getPiece().move(squares[0][3]);
					}
					if(first.getPiece() != null && first.getPiece().isTeamBlack() && first.getPiece() instanceof King && blackCanCastleR && whoGotClicked.getRow() == 0 && whoGotClicked.getCol() == 6) {
						System.out.println("I can castle! BR");
						squares[0][7].getPiece().move(squares[0][5]);
					}
					first.getPiece().move(second);
					check(); //someone is in trouble...
					counter++;
				}
			//}
			HighlightAllOff();
			first = null;
			second = null;
			whiteCanCastleL = false;
			whiteCanCastleR = false;
			blackCanCastleL = false;
			blackCanCastleR = false;
		}
	
		
	}	
	
	public boolean findKings() {
		blackKing = null;
		whiteKing = null;
		for(int r = 0; r<squares.length; r++) {
			for(int c = 0; c<squares[r].length; c++) {
				if(squares[r][c] != null && squares[r][c].getPiece() instanceof King) { //a king.. but... black or white?
					if(squares[r][c].getPiece().isTeamBlack())
						blackKing = squares[r][c]; //2 names for the black king...
					if(!squares[r][c].getPiece().isTeamBlack())
						whiteKing = squares[r][c]; //2 names for the white king...
					//We have located the BLACK and WHITE king
				}
			}
		}
		if(blackKing != null && whiteKing != null) {
			return true;
		}
		return false;
			
	}
	
	public boolean check() { //if x piece has someone the poss. of attacking it
		for(int r = 0; r<squares.length; r++) {
			for(int c = 0; c<squares[r].length; c++) {
				if(squares[r][c].getPiece() != null) { //I found a piece... cool
					for(int cr = 0; cr<squares.length; cr++) {
						for(int cc = 0; cc<squares[cr].length; cc++) { //go to every squares and check if it is a possible "hightlight"
							if(squares[r][c].getPiece().isMoveLegal(squares[cr][cc]) && (squares[r][c].getPiece() instanceof Knight || !isBlocked(squares[r][c], squares[cr][cc]))&& !squares[cr][cc].equals(squares[r][c])) {
								// x piece is allowed to move in these squares... y piece is allowed to move in these squares...
								//a black piece just moved, but did it force me into check!? Let's check... it's now the white's turn
								if(counter % 2 == 1 && squares[r][c].getPiece().isTeamBlack() && whiteKing/*squares[xr][xc]*/ != null && whiteKing /*squares[xr][xc]*/.equals(squares[cr][cc])) {
									JOptionPane.showMessageDialog(this, "White King is in check");
									return true;
								}
								if(counter % 2 == 0 && !squares[r][c].getPiece().isTeamBlack() && blackKing != null && blackKing.equals(squares[cr][cc])) {
									JOptionPane.showMessageDialog(this, "Black King is in check");
									return true;
								}
							}
						}							
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkHelp() { //if x piece has someone the poss. of attacking it
		for(int r = 0; r<squares.length; r++) {
			for(int c = 0; c<squares[r].length; c++) {
				if(squares[r][c].getPiece() != null) { //I found a piece... cool
					for(int cr = 0; cr<squares.length; cr++) {
						for(int cc = 0; cc<squares[cr].length; cc++) { //go to every squares and check if it is a possible "hightlight"
							if(squares[r][c].getPiece().isMoveLegal(squares[cr][cc]) && (squares[r][c].getPiece() instanceof Knight || !isBlocked(squares[r][c], squares[cr][cc]))&& !squares[cr][cc].equals(squares[r][c])) {
								// x piece is allowed to move in these squares... y piece is allowed to move in these squares...
								//a black piece just moved, but did it force me into check!? Let's check... it's now the white's turn
								if(counter % 2 == 0 && squares[r][c].getPiece().isTeamBlack() && whiteKing/*squares[xr][xc]*/ != null && whiteKing /*squares[xr][xc]*/.equals(squares[cr][cc])) {
									return true;
								}
								if(counter % 2 == 1 && !squares[r][c].getPiece().isTeamBlack() && blackKing != null && blackKing.equals(squares[cr][cc])) {
									return true;
								}
							}
						}							
					}
				}
			}
		}
		return false;
	}
	
	public void castling() {
		if(whiteKing.getPiece() != null && whiteKing.getPiece().isNeverMoved()) {
			if(squares[7][0].getPiece() != null && squares[7][0].getPiece() instanceof Rook && squares[7][0].getPiece().isNeverMoved()) {
				//So our whiteLRook is good.
				if(squares[7][1].getPiece() == null && squares[7][2].getPiece() == null && squares[7][3].getPiece() == null)
					if(first != null && first.getPiece() != null && first.getPiece() instanceof King && !first.getPiece().isTeamBlack()) {
						squares[7][2].setHighlighted(true);
						whiteCanCastleL = true;
					}
			}
			if(squares[7][7].getPiece() != null && squares[7][7].getPiece() instanceof Rook && squares[7][7].getPiece().isNeverMoved()) {
				//So our whiteRRook is good.
				if(squares[7][6].getPiece() == null && squares[7][5].getPiece() == null) //ISSUE
					if(first != null && first.getPiece() != null && first.getPiece() instanceof King && !first.getPiece().isTeamBlack()) {
						squares[7][6].setHighlighted(true);
						whiteCanCastleR = true;
					}

			}
		}
		
		if(blackKing.getPiece() != null && blackKing.getPiece().isNeverMoved()) { //Good king
			if(squares[0][0].getPiece() != null && squares[0][0].getPiece() instanceof Rook && squares[0][0].getPiece().isNeverMoved()) {
				//So our blackLRook is good.
				if(squares[0][1].getPiece() == null && squares[0][2].getPiece() == null && squares[0][3].getPiece() == null)
					if(first != null && first.getPiece() != null && first.getPiece() instanceof King && first.getPiece().isTeamBlack()) {
						squares[0][2].setHighlighted(true);
						blackCanCastleL = true;
					}

			}
			if(squares[0][7].getPiece() != null && squares[0][7].getPiece() instanceof Rook && squares[0][7].getPiece().isNeverMoved()) {
				//So our blackRRook is good.
				if(squares[0][6].getPiece() == null && squares[0][5].getPiece() == null)
					if(first != null && first.getPiece() != null && first.getPiece() instanceof King && first.getPiece().isTeamBlack()) {
						squares[0][6].setHighlighted(true);
						blackCanCastleR = true;
					}
			}
		}
	}

	public boolean isBlocked(Square orig, Square dest) {
		int vert = dest.getRow() - orig.getRow(); //if positive, we want to move down...
		int horz = dest.getCol() - orig.getCol(); //if positive, we want to move right...
		int vertadd = 0;
		int horzadd = 0;
		if(vert < 0)
			vertadd = -1;
		else if(vert > 0)
			vertadd = 1;
		if(horz < 0)
			horzadd = -1;
		else if(horz > 0)
			horzadd = 1;
		int sr = orig.getRow(), sc = orig.getCol();
		sr+=vertadd;
		sc+=horzadd;
		while(!squares[sr][sc].equals(dest) && isInBound(sr, sc)) { //until I've checked all the pieces to my dest
			if(squares[sr][sc].getPiece() != null) { //a piece is there!
				return true;
			}
			sr+=vertadd;
			sc+=horzadd;
		
		}
		return false;
	}

	public static void main(String[] args) {
		new GameBoard();
	}
	
	public boolean isInBound(int r, int c) {
		if(r>= 0 && c>=0 && r<=7 && c<=7)
			return true;
		return false;
	}
	
	public void HighlightAllOff() {
		for(int r = 0; r<squares.length; r++) {
			for(int c = 0; c<squares[0].length; c++) {
					squares[r][c].setHighlighted(false);
			}
		}
	}

}
