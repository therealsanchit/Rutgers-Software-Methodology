package chess;

import java.util.Stack;

import player.Player;
import unit.Piece;

/* STACKS
 * moves and casualty have the same number of stacks. If the casualty of the previous move is 0, null should be there.
 */
public class Record {
	private Stack moves;
	private Stack casualty;
	public static Cell[][] previousBoard;
	public static Cell[][] tryBoard;

	public Record() {
		moves = new Stack();
		casualty = new Stack();
		previousBoard = new Cell[8][8];
		tryBoard = new Cell[8][8];
		for (int x = 0; x < 8; x++) {
			char c = 'a';
			for (int y = 0; y < 8; y++, c++) {
				if ((x + y) % 2 == 1) {
					previousBoard[x][y] = new Cell("##", "" + c + (8 - x));
					tryBoard[x][y] = new Cell("##", "" + c + (8 - x));
				} else {
					previousBoard[x][y] = new Cell("  ", "" + c + (8 - x));
					tryBoard[x][y] = new Cell("  ", "" + c + (8 - x));
				}
			}
		}
	}

	public void push(String from, String to, String ask) {
		moves.push(from + "," + to + "," + ask);
		casualty.push(null);
	}

	public void push(String from, String to) {
		moves.push(from + "," + to);
		casualty.push(null);
	}

	public void push(String from, String to, Piece p) {
		moves.push(from + "," + to);
		casualty.push(p);
	}

	public String[] movePop() {
		return ((String) moves.pop()).split(",");
	}

	public Piece casualtyPop() {
		return (Piece) casualty.pop();
	}

	public void undo() {
		// System.out.println("Previous Board:");
		// Board.printBoard(previousBoard);

		String[] lastmove = movePop();
		if (lastmove == null)
			return;

		Piece dead = casualtyPop();
		String from = lastmove[1];
		String to = lastmove[0];

		int oldy = Character.getNumericValue(from.charAt(1));
		int oldx = from.charAt(0) - 'a';
		int newx = to.charAt(0) - 'a';
		int newy = Character.getNumericValue(to.charAt(1));
		int diff = newx - oldx;

		Cell c = Board.grabCell(from, Board.board);
		Piece p = c.getPiece();
		
		
		if (p.getName().equals("wK") || p.getName().equals("bK") && !p.hasMoved) {
			if (Math.abs(diff) == 2) { // if 2 then it castles
				if (diff < 0) {
					Cell rook = Board.grabCell(newx + 1, oldy, Board.board);
					Piece r = rook.getPiece();

					Cell newrook = Board.grabCell(oldx + 1, oldy, Board.board);
					newrook.addPiece(r);
					newrook.hasPiece = true;
					r.setPosition("h" + oldy);
					r.hasMoved = false;

					rook.removePiece();
					rook.hasPiece = false;

					Cell newking = Board.grabCell(newx, oldy, Board.board);
					newking.addPiece(p);
					p.setPosition(to);
					p.hasMoved = false;
					c.removePiece();
					p.numOfMoves--;
					if (p.numOfMoves == 0)
						p.hasMoved = false;
					restorePreviousBoard();
					return;
				} else {
					Cell rook = Board.grabCell(newx - 1, oldy, Board.board);
					Piece r = rook.getPiece();
					Cell newrook = Board.grabCell(oldx - 2, oldy, Board.board);
					newrook.addPiece(r);
					newrook.hasPiece = true;
					r.setPosition("a" + oldy);
					r.hasMoved = false;

					rook.removePiece();
					rook.hasPiece = false;

					Cell newking = Board.grabCell(newx, oldy, Board.board);
					newking.addPiece(p);
					p.setPosition(to);
					c.removePiece();
					p.numOfMoves--;
					if (p.numOfMoves == 0)
						p.hasMoved = false;
					restorePreviousBoard();
					return;
				}
			}
		}
		// c.removePiece();
		p.setPosition(to);
		// Board.grabCell(to).addPiece(p);
		p.numOfMoves--;
		if (p.numOfMoves == 0)
			p.hasMoved = false;
		if (dead != null)
			restore(from, dead);
		restorePreviousBoard();
		// System.out.println("Board:");
		// Board.printBoard(Board.board);
	}

	/* It tries to move the piece, and see if it's safe */
	public boolean tryMove(Piece p, String moveTo, String player) {
		storeTryBoard(tryBoard);
		String moveFrom = p.getPos();
		Cell from = Board.grabCell(moveFrom, tryBoard);
		Cell to = Board.grabCell(moveTo, tryBoard);

		from.hasPiece = false;
		
		int oldx = moveFrom.charAt(0) - 'a';
		int oldy = Character.getNumericValue(moveFrom.charAt(1));

		int newx = moveTo.charAt(0) - 'a';
		int newy = Character.getNumericValue(moveTo.charAt(1));

		int diffx = newx - oldx;
		int diffy = newy - oldy;

		if (p.getName().equals("bK") || p.getName().equals("wK") && !p.hasMoved) {
			if (Math.abs(diffx) == 2 && diffy == 0) {

				if (diffx < 0) { // e1 to c1
					Cell newrook = Board.grabCell(oldx - 1, oldy, tryBoard);

					Cell oldrook = Board.grabCell(newx - 2, oldy, tryBoard);
					Piece r = oldrook.getPiece();

					newrook.addPiece(r);
					newrook.hasPiece = true;
					r.setPosition("d" + oldy);

					oldrook.removePiece();
					oldrook.hasPiece = false;

					Cell newking = Board.grabCell(newx, oldy, tryBoard);
					newking.addPiece(p);
					p.setPosition(moveTo);
					p.hasMoved = true;
					to.piece = p;
					to.hasPiece = true;
					p.numOfMoves++;
					if (Board.safeCheck(tryBoard, player)) {
						undoTry(p, moveFrom, moveTo);
						return true;
					}
					undoTry(p, moveFrom, moveTo);
					return false;

				} else {
					Cell newrook = Board.grabCell(oldx + 1, oldy, tryBoard);

					Cell oldrook = Board.grabCell(newx + 1, oldy, tryBoard);
					Piece r = oldrook.getPiece();

					newrook.addPiece(r);
					newrook.hasPiece = true;
					r.setPosition("h" + oldy);


					oldrook.removePiece();
					oldrook.hasPiece = false;

					Cell newking = Board.grabCell(newx, oldy, tryBoard);
					newking.addPiece(p);
					p.setPosition(moveTo);

					to.piece = p;
					to.hasPiece = true;
					p.numOfMoves++;
					if (Board.safeCheck(tryBoard, player)) {
						undoTry(p, moveFrom, moveTo);
						return true;
					}
					undoTry(p, moveFrom, moveTo);
					return false;
				}
			}
		}

		p.setPosition(moveTo);
		to.piece = p;
		to.hasPiece = true;
		p.numOfMoves++;
		if (Board.safeCheck(tryBoard, player)) {
			undoTry(p, moveFrom, moveTo);
			return true;
		}
		undoTry(p, moveFrom, moveTo);
		return false;

	}

	private void undoTry(Piece p, String to, String from) {

		int oldx = from.charAt(0) - 'a';
		int oldy = Character.getNumericValue(from.charAt(1));
		int newx = to.charAt(0) - 'a';
		int newy = Character.getNumericValue(to.charAt(1));
		int diff = newx - newy;

		if ((p.getName().equals("wK") || p.getName().equals("bK"))&& !p.hasMoved) {
			if (Math.abs(diff) == 2) { // if 2 then it castles
				if (diff < 0) {
					Cell rook = Board.grabCell(newx + 1, oldy, tryBoard);
					Piece r = rook.getPiece();

					Cell newrook = Board.grabCell(oldx + 1, oldy, tryBoard);
					newrook.addPiece(r);
					newrook.hasPiece = true;
					r.setPosition("h" + oldy);
					r.hasMoved = false;

					rook.removePiece();
					rook.hasPiece = false;

					Cell newking = Board.grabCell(newx, oldy, tryBoard);
					newking.addPiece(p);
					p.setPosition(to);
					p.numOfMoves--;
					if (p.numOfMoves == 0)
						p.hasMoved = false;
					return;
				} else {
					Cell rook = Board.grabCell(newx - 1, oldy, tryBoard);
					Piece r = rook.getPiece();
					Cell newrook = Board.grabCell(oldx - 2, oldy, tryBoard);
					newrook.addPiece(r);
					newrook.hasPiece = true;
					r.setPosition("a" + oldy);
					r.hasMoved = false;

					rook.removePiece();
					rook.hasPiece = false;

					Cell newking = Board.grabCell(newx, oldy, tryBoard);
					newking.addPiece(p);
					p.setPosition(to);
					p.numOfMoves--;
					if (p.numOfMoves == 0)
						p.hasMoved = false;
					return;
				}
			}
		}

		p.setPosition(to);

		p.numOfMoves--;
		if (p.numOfMoves == 0)
			p.hasMoved = false;

	}

	/*
	 * we dont need to restore anything from try board except change the Piece
	 * attributes
	 */
	private void restore(String from, Piece dead) {
		Cell c = Board.grabCell(from, Board.board);
		c.addPiece(dead);
		dead.isAlive = true;
	}

	public void storePreviousBoard() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				previousBoard[x][y].copyCell(Board.board[x][y]);
			}
		}
	}

	public void storeTryBoard(Cell[][] board) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[x][y].copyCell(Board.board[x][y]);
			}
		}
	}

	private void restorePreviousBoard() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Board.board[x][y].copyCell(previousBoard[x][y]);
			}
		}
	}

}
