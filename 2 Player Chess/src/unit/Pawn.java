package unit;

import chess.Board;
import chess.Cell;

public class Pawn extends Piece {
	/* black or white */
	public boolean doubleSpace;
	private boolean promoteNextTurn;
	private String promotePiece;

	public Pawn(String team, String pos, Cell[][] board) {
		super(team);
		setName();
		setPosition(pos);
		hasMoved = false;
		doubleSpace = false;
	}

	public void setName() {
		if (team.equals("black"))
			name = "bp";
		else
			name = "wp";
	}

	/* overwriting */
	public void movePiece(String pos, Cell[][] board) {
		String previousPos = getPos();
		int y = Character.getNumericValue(pos.charAt(1));

		/* if there's no command to promote a certain piece, promote it to queen */
		if (y == 8 || y == 1) {
			if (!promoteNextTurn)
				promote("q");
		}

		if (canBlackEn(pos, board)) {
			blackEn(pos, board);
			return;
		}
		if (canWhiteEn(pos, board)) {
			whiteEn(pos, board);
			return;

		}

		Cell prev = Board.grabCell(previousPos, board);
		Cell target = Board.grabCell(pos, board);
		Piece dead = null;
		if (target.hasPiece)
			/* there was a casualty, so we add piece to casualty */
			dead = target.getPiece();

		prev.removePiece();
		setPosition(pos);
		target.addPiece(this);

		if (promoteNextTurn)
			Board.promotion(promotePiece, this);

		recordMove(previousPos, getPos(), dead);
	}

	@Override
	public void calculatePossibleMoves(Cell[][] board, String pos) {
		/* normal moves - move up */
		String ahead = "";
		int newY = 0;
		if (!hasMoved) {
			if (team.equals("black"))
				newY = posY - 2;
			else
				newY = posY + 2;

			ahead = "" + posX + newY;
			if (checkValidBoundary(ahead) && !checkOccupiedisFriend(ahead, board)
					&& !checkOccupiedisEnemy(ahead, board))
				possibleMoves.add(Board.grabCell(ahead, board));
		}

		if (team.equals("black"))
			newY = posY - 1;
		else
			newY = posY + 1;

		ahead = "" + posX + newY;
		if (checkValidBoundary(ahead) && !checkOccupiedisFriend(ahead, board)
				&& !checkOccupiedisEnemy(ahead, board))
			possibleMoves.add(Board.grabCell(ahead, board));

		/* check attacking */
		String attacking = "";

		char attackX = posX;
		attacking = "" + (--attackX) + newY;
		// System.out.print(attacking);
		if (checkValidBoundary(attacking) && checkOccupiedisEnemy(attacking, board))
			possibleMoves.add(Board.grabCell(attacking, board));

		attackX = posX;
		attacking = "" + (++attackX) + newY;
		// System.out.println(" " + attacking);
		if (checkValidBoundary(attacking) && checkOccupiedisEnemy(attacking, board))
			possibleMoves.add(Board.grabCell(attacking, board));

		if (canWhiteEn(pos, board))
			possibleMoves.add(Board.grabCell(pos, board));

		if (canBlackEn(pos, board))
			possibleMoves.add(Board.grabCell(pos, board));
	}

	private void whiteEn(String pos, Cell[][] board) {
		int oldx = posX - 'a';
		int oldy = posY;

		int newx = pos.charAt(0) - 'a';

		Cell c = Board.grabCell(oldx, oldy, board);
		Piece p = c.getPiece();

		Cell cell = Board.grabCell(newx, oldy, board);
		cell.removePiece();
		cell.hasPiece = false;

		Cell cell2 = Board.grabCell(pos, board);

		cell2.addPiece(p);
		cell2.hasPiece = true;
		p.setPosition(pos);


		c.removePiece();
		c.hasPiece = false;
	}

	private void blackEn(String pos, Cell[][] board) {
		int oldx = posX - 'a';
		int oldy = posY;

		int newx = pos.charAt(0) - 'a';

		Cell c = Board.grabCell(oldx, oldy, board);

		Cell cell = Board.grabCell(newx, oldy, board);
		Cell cell2 = Board.grabCell(pos, board);

		Piece p = c.getPiece();
		cell2.addPiece(p);
		cell2.hasPiece = true;
		p.setPosition(pos);

		cell.removePiece();
		cell.hasPiece = false;

		c.removePiece();
		c.hasPiece = false;
	}

	private boolean canBlackEn(String pos, Cell[][] board) {
		int oldx = posX - 'a';
		int oldy = posY;

		int newx = pos.charAt(0) - 'a';
		int newy = Character.getNumericValue(pos.charAt(1));

		int diffx = newx - oldx;

		if (Math.abs(diffx) == 1)
			if (posY == 5)
				if (newy - oldy == 1) {
					Cell cell = Board.grabCell(newx, oldy, board);
					if (cell.hasPiece) {
						Piece e = cell.getPiece();
						if (!e.getTeam().equals(team)) {
							return true;
						}
					}
				}

		return false;
	}

	private boolean canWhiteEn(String pos, Cell[][] board) {
		int oldx = posX - 'a';
		int oldy = posY;

		int newx = pos.charAt(0) - 'a';
		int newy = Character.getNumericValue(pos.charAt(1));

		int diffx = newx - oldx;

		if (Math.abs(diffx) == 1)
			if (posY == 4)
				if (newy - oldy == -1) {
					Cell cell = Board.grabCell(newx, oldy, board);
					if (cell.hasPiece) {
						Piece e = cell.getPiece();
						if (!e.getTeam().equals(team)) {
							return true;
						}
					}
				}
		return false;
	}

	/* the promotion prereq is checked through player user input. */
	public void promote(String piece) {
		promoteNextTurn = true;
		promotePiece = piece;
	}
}
