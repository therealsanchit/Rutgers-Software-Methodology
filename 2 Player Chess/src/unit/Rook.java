package unit;

import chess.Board;
import chess.Cell;

public class Rook extends Piece {
	
	public Rook(String team, String pos) {
		super(team);
		setName();
		setPosition(pos);
		hasMoved = false;
	}

	public void setName() {
		if (team.equals("black"))
			name = "bR";
		else
			name = "wR";
	}

	@Override
	public void calculatePossibleMoves(Cell[][] board, String pos) {
		checkVerticalMvt(board);
		checkHorizontalMvt(board);
//		for(Cell c: possibleMoves){
//			System.out.println(c.position);
//		}
	}

}
