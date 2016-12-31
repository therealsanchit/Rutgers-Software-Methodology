package unit;

import java.util.ArrayList;

import chess.Board;
import chess.Cell;
import chess.Record;

public abstract class Piece {
	
	public boolean isAlive;
	public boolean isAttacked;
	public boolean hasMoved;
	public int numOfMoves;
	public String team;
	public String name;
	/* Position
	 * ex) 	a4
	 * 		posX = 'a', posY = 4
	 */
	protected int posY;
	protected char posX;
	
	/* possibleMoves
	 * keeps track of every possible Move that this piece can make
	 * with calculatePossibleMoves()
	 * Needs to be dumped + recalculated every relocation of a piece
	 */
	public ArrayList<Cell> possibleMoves;
	/*
	 * initialization of start position must be overwritten
	 */
	public Piece(String team){
		this.isAlive = true;
		this.isAttacked = false;
		this.team = team;
		possibleMoves = new ArrayList<Cell>();
	}
	/* setPosition
	 * Sets the position of the piece.
	 * Argument should be checked for valid input before calling this function.
	 */
	public boolean setPosition(String pos){
		if(pos == null)
			return false;
		else if(pos.length() != 2)
			return false;
		
		this.posY = Character.getNumericValue(pos.charAt(1));
		this.posX = pos.charAt(0);
		return true;
	}
	/* movePiece
	 * Sets Position of the current piece to pos and moves it on the board
	 * Always check for valid input / valid move before using this method.
	 */
	public void movePiece(String pos, Cell[][] board){
		String previousPos = getPos();
		
		Cell prev = Board.grabCell(previousPos, board);
		Cell target = Board.grabCell(pos, board);
		
		Piece dead = null;
		if(target.hasPiece)
			/* there was a casualty, so we add piece to casualty */
			dead = target.getPiece();

		prev.removePiece();
		
		setPosition(pos);
		target.addPiece(this);
		
		recordMove(previousPos, getPos(), dead);
	}
	
	public String getName(){return name;}
	public String getTeam(){return team;}
	
	/* ABSTACT */
	public abstract void calculatePossibleMoves(Cell[][] board, String moveTo);
	
	public boolean checkValidMove(String moveTo, Cell[][] board) {
		possibleMoves.clear();
		calculatePossibleMoves(board, moveTo);
		
		for(Cell c: possibleMoves){
			if(c.position.equals(moveTo))
				return true;
		}
		return false;
	}

	protected boolean checkOccupied(String moveTo, Cell[][] b){
		Cell c = Board.grabCell(moveTo, b);
		if(c.hasPiece)
			return true;
		return false;
	}
	/* returns false on empty or friend cell */
	protected boolean checkOccupiedisEnemy(String moveTo, Cell[][] b){
		Cell c = Board.grabCell(moveTo, b);
		if(c.hasPiece)
			if(!c.getPiece().getTeam().equals(team))
				return true;
		return false;
	}
	/* returns false on empty or enemy cell */
	protected boolean checkOccupiedisFriend(String moveTo, Cell[][] b){
		Cell c = Board.grabCell(moveTo, b);
		if(c.hasPiece)
			if(c.getPiece().getTeam().equals(team))
				return true;
		return false;
	}
	public boolean checkValidBoundary(String pos){
		int y = Character.getNumericValue(pos.charAt(1));
		char x = pos.charAt(0);
		if(y < 1 || y > 8)
			return false;
		else if(x < 0x61 || x > 0x68)
			return false;
		return true;
	}
	protected void checkDiagonalMvt(Cell[][] b){
		/* check quadrants... ++ , -+, --. +- */
		int[] changeX = {1, -1, -1, 1};
		int[] changeY = {1, 1, -1, -1};
		
		for(int i=0; i<4; i++){
			String checkSpot = null;
			char newX = posX;
			int newY = posY;
			do{
				if(checkSpot != null)
					if(!checkSpotinBoard(checkSpot, b))
						break;
					
				newX = (char)((int)newX + changeX[i]);
				newY = newY + changeY[i];
				
				checkSpot = "" + newX + newY;
			}while(checkValidBoundary(checkSpot) && !checkOccupiedisFriend(checkSpot, b));
		}
	}
	protected void checkHorizontalMvt(Cell[][] b){
		/* to the right of position */
		for(char i=(char)((int)posX + 1); i<'i'; i++){
			String checkSpot = "" + i + posY;
			if(!checkSpotinBoard(checkSpot, b))
				break;
		}
		/* to the left of position */
		for(char i=(char)((int)posX - 1); i>='a'; i--){
			String checkSpot = "" + i + posY;
			if(!checkSpotinBoard(checkSpot, b))
				break;
		}
	}
	protected void checkVerticalMvt(Cell[][] b){
		/* to top or bottom */
		for(int i=posY+1; i<9; i++){
			String checkSpot = "" + posX + i;
			if(!checkSpotinBoard(checkSpot, b))
				break;
		}
		for(int i=posY-1; i>0; i--){
			String checkSpot = "" + posX + i;
			if(!checkSpotinBoard(checkSpot, b))
				break;
		}
	}
	
	private boolean checkSpotinBoard(String checkSpot, Cell[][] b){
		if(checkValidBoundary(checkSpot)){
			Cell c = Board.grabCell(checkSpot, b);
			//System.out.println("CHECKSPOT: " + c.position);
			if(c.hasPiece){
				if(!c.getPiece().getTeam().equals(team))
					possibleMoves.add(c);
				return false;
			}else
				possibleMoves.add(c);
			return true;
		}
		return false;
	}
	
	public String getPos(){return "" + posX + posY;}
	
	public void recordMove(String from, String to, Piece dead){
		if(dead == null)
			Board.record.push(from, to);
		else
			Board.record.push(from, to, dead);
	}
	
	public boolean equals(Object o){
		if(o == null | !(o instanceof Piece)) {
			return false;
		}
		
		Piece other = (Piece)o;
		
		return (other.posX == this.posX &&
				other.posY == this.posY &&
				other.team.equals(this.team) &&
				other.getName().equals(this.name));
	}
	
	public String toString(){
		return this.team + " " + this.name + " " + this.posX + this.posY;
	}
}
