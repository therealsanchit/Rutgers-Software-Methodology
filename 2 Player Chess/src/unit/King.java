package unit;

import chess.Board;
import chess.Cell;

public class King extends Piece {

	public String[] castling;
	public boolean isChecked;
	
	public King(String team, String pos, Cell[][] board) {
		super(team);
		setName();
		setPosition(pos);
		hasMoved = false;
		isChecked = false;
		
		castling = new String[2];
		if(this.team.equals("black")){
			castling[0] = "c8";
			castling[1] = "g8";
		}else{
			castling[0] = "c1";
			castling[1] = "g1";
		}
	}

	public void setName() {
		if (team.equals("black"))
			name = "bK";
		else
			name = "wK";
	}

	public void movePiece(String pos, Cell[][] board) {
		/* it already checks if pos is a possibleMove or not, but since we don't want it comparing
		 * strings EVERYTIME the king has to move, we only check if king hasMoved.
		 */
		String previousPos = getPos();
		if(!hasMoved){
			if(pos.equals(castling[0]) || pos.equals(castling[1])){
				castle(pos, board);
			}
		}
		
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

	@Override
	public void calculatePossibleMoves(Cell[][] board, String pos) {
		int newY;
		char newX;
			/*castling*/
		if(!hasMoved){
			for(int i=0; i<2; i++){
				if(canCastle(castling[i], board)){
					possibleMoves.add(Board.grabCell(castling[i], board));
				}
			}
		}
			/*regular move*/
		for(int countY=-1; countY<2; countY++){
			newY = posY + countY;
			for(int countX=-1; countX<2; countX++){
				// do not count the square that this king is in 
				if(countY == 0 && countX == 0)
					continue;
				
				newX = (char)((int)posX + countX);
				String checkSpot = "" + newX + newY;
				
				if(checkValidBoundary(checkSpot) && !checkOccupiedisFriend(checkSpot, board)){
					possibleMoves.add(Board.grabCell(checkSpot, board));
				}
			}
		}
//		for(Cell c: possibleMoves){
//			System.out.println(c.position);
//		}
	}
	
	
	//Castling may only be done if the king has never moved, the rook involved has never moved, the squares 
	//between the king and the rook involved are unoccupied, the king is not in check, and the king does not 
	//cross over or end on a square in which it would be in check
	/* All we need to do for castle is to move the rook. The rest of 'movePiece' function will take care of the king */
	private void castle(String pos, Cell[][] board){ 	//e1 to h1 = King goes to g1 rook goes to f1
		if((int)pos.charAt(0) < (int)posX){
			Cell c = Board.grabCell("" + 'a' + posY, board);
			c.getPiece().movePiece("" + 'd' + posY, board);
		}else{
			Cell c = Board.grabCell("" + 'h' + posY, board);
			c.getPiece().movePiece("" + 'f' + posY, board);
		}
	}
	
	private boolean canCastle(String pos, Cell[][] board){
		if(!hasMoved){ //e1
			if((int)pos.charAt(0) < (int)posX){
				for (char x=(char)((int)posX - 1); x>'a'; x--)  // e to a
					if (Board.grabCell("" + x + posY, board).hasPiece)
						return false;

				Cell cell = Board.grabCell("" + 'a' + posY, board);
				if(cell.hasPiece && !cell.getPiece().hasMoved)
					return true;
			}else{

				for (char x=(char)((int)posX + 1); x<'h'; x++){ // e to a
					if (Board.grabCell("" + x + posY, board).hasPiece)
						return false;
				}

				Cell cell = Board.grabCell("" + 'h' + posY, board);
				if(cell.hasPiece && !cell.getPiece().hasMoved)
					return true;
			}
		}
		return false;
	}
}
