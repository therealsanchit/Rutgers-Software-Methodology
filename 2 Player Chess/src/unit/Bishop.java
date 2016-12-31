package unit;

import chess.Board;
import chess.Cell;

public class Bishop extends Piece {

	public Bishop(String team, String pos) {
		super(team);
		setName();
		setPosition(pos);
	}

	public void setName() {
		if (team.equals("black"))
			name = "bB";
		else
			name = "wB";
	}
	
	@Override
	public void calculatePossibleMoves(Cell[][] board, String pos) {
		checkDiagonalMvt(board);
	}

}
