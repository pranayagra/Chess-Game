import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JOptionPane;

public abstract class ChessPiece {	
	private Image img;
	//variable for what team/color
	private boolean teamBlack;
	private String myColor;
	//variable for what Square
	private Square mySquare;
	private boolean neverMoved = true;

	public ChessPiece(String im, boolean teamBlack, Square mySquare){
		loadImage(im);
		this.mySquare = mySquare;
		this.teamBlack = teamBlack;
		if(teamBlack)
			myColor = "Black";
		else
			myColor = "White";
	}
	
	
	
	public Square getMySquare() {
		return mySquare;
	}

	public void setMySquare(Square mySquare) {
		this.mySquare = mySquare;
	}
	
	public int dRow(Square dest) {
		return Math.abs(mySquare.getRow() - dest.getRow()); //absolute delta Row
	}
	
	public int dCol(Square dest) {
		return Math.abs(mySquare.getCol() - dest.getCol()); //absolute delta Col
	}


	public Image getImg() {
		return img;
	}



	public void setImg(Image img) {
		this.img = img;
	}



	public boolean isTeamBlack() {
		return teamBlack;
	}



	public void setTeamBlack(boolean teamBlack) {
		this.teamBlack = teamBlack;
	}
	
	

	public String getMyColor() {
		return myColor;
	}



	public void setMyColor(String myColor) {
		this.myColor = myColor;
	}
	
	public boolean isNeverMoved() {return neverMoved;}
	public void setNeverMoved(boolean x) {neverMoved = x;}


	//helper function to load image
	private void loadImage( String im ){
		img = Toolkit.getDefaultToolkit().getImage( getClass().getResource(im) );
	    
		MediaTracker tracker = new MediaTracker (new Component () {});
		tracker.addImage(img, 0);
		//block while reading image
		try { tracker.waitForID (0); }
	        catch (InterruptedException e) {
	        	JOptionPane.showMessageDialog(null, "Error reading file");
	        }
	}

	public void draw(Graphics g){
		g.drawImage(img,0,0,90,90,null,null);
	}
	
	public abstract boolean isMoveLegal(Square dest);
	
	public void move(Square dest) {
			mySquare.setPiece(null); 
			setMySquare(dest);
			mySquare.setPiece(this);
			mySquare.repaint();
			dest.repaint();
		return;
	}
}
