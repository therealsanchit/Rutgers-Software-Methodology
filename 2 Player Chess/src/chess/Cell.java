package chess;

import unit.Piece;

public class Cell {
	public boolean hasPiece;
	/* position is the 'name of the cell' regards to its location in the board
	 * ex) a4 / f6
	 */
	public String position;
	public Piece piece;
	private String name;
	
	
	/* the cell name refers to ## or '  ', and in the case of a piece, 
	 * the cell name would print the name of the piece instead.
	 */
	
	public Cell(String name, String pos){
		hasPiece = false;
		this.name = name;
		this.position = pos;
	}
	
	public void setName(String name){this.name = name;}
	
	/* getPiece
	 * grabs the piece that this Cell is currently pointing to
	 * returns null if blank
	 */
	public Piece getPiece(){
		if(hasPiece)
			return piece;
		return null;
	}
	public String getName(){
		if(hasPiece)
			return piece.getName();
		else
			return this.name;
	}
	/* Overwrites current piece, sets current piece to be dead. 
	 * Make sure you check valid move before using this method */
	public void addPiece(Piece p){
		if(hasPiece){
			this.piece.isAlive = false;
			removePiece();
		}
		this.piece = p;
		hasPiece = true;
	}
	public void removePiece(){
		hasPiece = false;
		this.piece = null;
	}

	public String toString(){return getName();}
	
	public void copyCell(Object o){
		if(o == null | !(o instanceof Cell)) {
			return;
		}
		Cell c = (Cell)o;
		this.hasPiece = c.hasPiece;
		this.position = c.position;
		this.piece = c.getPiece();
	}
}
