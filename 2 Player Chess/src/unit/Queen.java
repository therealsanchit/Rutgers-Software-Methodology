package unit;

import chess.Board;
import chess.Cell;

public class Queen extends Piece {

	public Queen(String team, String pos) {
		super(team);
		setName();
		setPosition(pos);
	}

	public void setName() {
		if (team.equals("black"))
			name = "bQ";
		else
			name = "wQ";
	}

	@Override
	public void calculatePossibleMoves(Cell[][] board, String pos) {
		checkHorizontalMvt(board);
		checkVerticalMvt(board);
		checkDiagonalMvt(board);
	}

}
