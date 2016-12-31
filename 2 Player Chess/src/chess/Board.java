package chess;

import java.util.ArrayList;
import java.util.Stack;

import player.Black;
import player.Player;
import player.White;
import unit.Bishop;
import unit.King;
import unit.Knight;
import unit.Pawn;
import unit.Piece;
import unit.Queen;
import unit.Rook;

public class Board {
	/* important values for game */
	public static boolean isRunning;

	/* players */
	private static White PlayerWhite;
	private static Black PlayerBlack;

	public static Record record;
	/* declare size of board */
	public static Cell[][] board;

	/*
	 * constructor initalize board with 64 squares initalize positions of all
	 * pieces black is always on bottom, white is always on top
	 */
	public Board() {
		isRunning = true;

		board = new Cell[8][8];
		record = new Record();
		/* adding names to cell - it's either '##' or ' ' */

		for (int x = 0; x < 8; x++) {
			char c = 'a';
			for (int y = 0; y < 8; y++, c++) {
				if ((x + y) % 2 == 1)
					board[x][y] = new Cell("##", "" + c + (8 - x));
				else
					board[x][y] = new Cell("  ", "" + c + (8 - x));
			}
		}

		/* TODO:(JASON) add pieces in the game (add to players) */
		PlayerWhite = new White();
		PlayerBlack = new Black();
		PlayerWhite.addEnemy(PlayerBlack);
		PlayerBlack.addEnemy(PlayerWhite);

		InitalizePieces(PlayerWhite, PlayerBlack);

	}

	private void InitalizePieces(White playerWhite2, Black playerBlack2) {

		String BOTTOM = "1";
		String TOP = "8";
		String LEFT = "a";
		String RIGHT = "h";

		for (char x = 'a'; x <= 'h'; x++) {
			String pos = x + Integer.toString(Integer.parseInt(TOP) - 1);
			Piece bp = new Pawn("black", pos, board);
			PlayerBlack.pieces.add(bp);
			grabCell(pos, board).addPiece(bp);

			pos = x + Integer.toString(Integer.parseInt(BOTTOM) + 1);
			Piece wp = new Pawn("white", pos, board);
			PlayerWhite.pieces.add(wp);
			grabCell(pos, board).addPiece(wp);
		}

		// King

		String bkPos = "e8";
		String wkPos = "e1";
		Piece bk = new King("black", "e8", board);
		Piece wk = new King("white", "e1", board);
		PlayerWhite.pieces.add(wk);
		PlayerBlack.pieces.add(bk);
		grabCell(bkPos, board).addPiece(bk);
		grabCell(wkPos, board).addPiece(wk);

		// queen
		String bqPos = "d8";
		String wqPos = "d1";
		Piece bq = new Queen("black", "d8");
		Piece wq = new Queen("white", "d1");
		PlayerWhite.pieces.add(wq);
		PlayerBlack.pieces.add(bq);
		grabCell(bqPos, board).addPiece(bq);
		grabCell(wqPos, board).addPiece(wq);

		// bishops
		String bbPos = "c8";
		String wbPos = "c1";

		String bbPos2 = "f8";
		String wbPos2 = "f1";

		Piece bb = new Bishop("black", "c8");
		Piece wb = new Bishop("white", "c1");

		Piece bb2 = new Bishop("black", "f8");
		Piece wb2 = new Bishop("white", "f1");

		PlayerWhite.pieces.add(wb);
		PlayerBlack.pieces.add(bb);
		PlayerWhite.pieces.add(wb2);
		PlayerBlack.pieces.add(bb2);

		grabCell(bbPos, board).addPiece(bb);
		grabCell(wbPos, board).addPiece(wb);

		grabCell(bbPos2, board).addPiece(bb2);
		grabCell(wbPos2, board).addPiece(wb2);

		// rook
		String brPos = "a8";
		String wrPos = "a1";

		String brPos2 = "h8";
		String wrPos2 = "h1";

		Piece br = new Rook("black", "a8");
		Piece wr = new Rook("white", "a1");

		Piece br2 = new Rook("black", "h8");
		Piece wr2 = new Rook("white", "h1");

		PlayerWhite.pieces.add(wr);
		PlayerBlack.pieces.add(br);
		PlayerWhite.pieces.add(wr2);
		PlayerBlack.pieces.add(br2);

		grabCell(brPos, board).addPiece(br);
		grabCell(wrPos, board).addPiece(wr);

		grabCell(brPos2, board).addPiece(br2);
		grabCell(wrPos2, board).addPiece(wr2);

		// knight

		String bnPos = "b8";
		String wnPos = "b1";

		String bnPos2 = "g8";
		String wnPos2 = "g1";

		Piece bn = new Knight("black", "b8");
		Piece wn = new Knight("white", "b1");

		Piece bn2 = new Knight("black", "g8");
		Piece wn2 = new Knight("white", "g1");

		PlayerWhite.pieces.add(wn);
		PlayerBlack.pieces.add(bn);
		PlayerWhite.pieces.add(wn2);
		PlayerBlack.pieces.add(bn2);

		grabCell(bnPos, board).addPiece(bn);
		grabCell(wnPos, board).addPiece(wn);

		grabCell(bnPos2, board).addPiece(bn2);
		grabCell(wnPos2, board).addPiece(wn2);

		/* calculate possible moves for all pieces */
		// for(Piece p: PlayerWhite.white){
		// p.calculatePossibleMoves(board);
		// }
		// for(Piece p: PlayerBlack.black){
		// p.calculatePossibleMoves(board);
		// }
	}

	/*
	 * grabCell calculates the index of the cell on the board given a position
	 * and returns that cell
	 */
	public static Cell grabCell(String pos, Cell[][] b) {
		int y = Character.getNumericValue(pos.charAt(1));
		int x = pos.charAt(0) - 'a';

		return b[8 - y][x];
	}

//	public static Cell grabCell(String pos) {
//		int y = Character.getNumericValue(pos.charAt(1));
//		int x = pos.charAt(0) - 'a';
//
//		return board[8 - y][x];
//	}

	public static Cell grabCell(int x, int y, Cell[][] b) {
		return b[8 - y][x];
	}

	/*
	 * Pawn Promotion This is where it got a little weird... the pawn was to be
	 * promoted but technically it wasn't the pawn transforming It was where you
	 * would pick the pawn out and place it with another piece, So I'm just
	 * following what we would've done in real life, and therefore, this
	 * function belongs here
	 */
	public static void promotion(String piece, Pawn pawn) {
		String team = pawn.getTeam();
		ArrayList<Piece> pieces;
		if (team.equals("black"))
			pieces = PlayerBlack.pieces;
		else
			pieces = PlayerWhite.pieces;

		pieces.remove(pawn);
		Piece newP;
		if (piece.equals("knight")) {
			newP = new Knight(team, pawn.getPos());
		} else if (piece.equals("rook")) {
			newP = new Rook(team, pawn.getPos());
		} else if (piece.equals("bishop")) {
			newP = new Bishop(team, pawn.getPos());
		} else {
			newP = new Queen(team, pawn.getPos());
		}

		grabCell(pawn.getPos(), board).addPiece(newP);
		newP.hasMoved = true;
	}

	/*
	 * safe Check this function will go through the enemy's possible moves and
	 * return false if the place is a danger zone
	 */
	public static boolean safeCheck(Cell[][] b, String player) {
		boolean isChecked = false;
		System.out.println("TRY");
		printBoard(b);
		if (player.equals("black")) {
			for (Piece p : PlayerWhite.pieces) {
				if (!p.isAlive)
					continue;
				p.possibleMoves.clear();
				p.calculatePossibleMoves(b, p.getPos());
				
				for (Cell c : p.possibleMoves) {
					
					if (c.hasPiece
							&& c.getPiece().getName().equals(PlayerBlack.king)) {
						return false;
					}
				}
			}
		} else {
			for (Piece p : PlayerBlack.pieces) {
				if (!p.isAlive)
					continue;
				p.possibleMoves.clear();
				p.calculatePossibleMoves(b, p.getPos());
				System.out.println(p);
				for (Cell c : p.possibleMoves) {
					System.out.println(c.position);
					if (c.hasPiece
							&& c.getPiece().getName().equals(PlayerWhite.king)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * check Check This is where I will go through all the pieces and their
	 * possible Moves to find a check is happening. If a check is happening, you
	 * can do 3 things: move a king to a safe spot, block attack with another
	 * piece, or kill the attacking piece
	 */
	public static void checkChecks(Cell[][] b) {
		boolean isChecked = false;
		//printBoard(b);

		for (Piece p : PlayerBlack.pieces) {
			if (!p.isAlive)
				continue;
			p.possibleMoves.clear();
			p.calculatePossibleMoves(b, p.getPos());
			for (Cell c : p.possibleMoves) {
				if (c.hasPiece && c.getPiece().getName().equals("wK")) {
					PlayerWhite.checked = true;
					isChecked = true;
					PlayerWhite.threat.add(p);
				}
			}
		}
		if (!isChecked)
			PlayerWhite.checked = false;

		isChecked = false;
		for (Piece p : PlayerWhite.pieces) {
			if (!p.isAlive)
				continue;
			p.possibleMoves.clear();
			p.calculatePossibleMoves(b, p.getPos());
			for (Cell c : p.possibleMoves) {
				if (c.hasPiece && c.getPiece().getName().equals("bK")) {
					PlayerBlack.checked = true;
					isChecked = true;
					PlayerBlack.threat.add(p);
				}
			}
		}
		if (!isChecked)
			PlayerBlack.checked = false;
	}

	/* MAIN GAME LOOP */
	public void start() {
		while (isRunning) {
			isRunning = true;
			
			printBoard(board);
			record.storePreviousBoard();
			
			while (!PlayerWhite.turn(board)) {
				System.out.println("Invalid Input\n");
				printBoard(board);
			}

			if (!isRunning)
				return;

			if(PlayerWhite.askDraw)
				PlayerBlack.askDraw = true;
			else
				PlayerBlack.askDraw = false;
			
			if(PlayerWhite.drawConfirmed){
				isRunning = false;
				System.out.println("Tie");
				return;
			}
			
			printBoard(board);
			record.storePreviousBoard();

			while (!PlayerBlack.turn(board)) {
				System.out.println("Invalid Input\n");
				printBoard(board);
			}

			if (!isRunning)
				return;

			if(PlayerBlack.askDraw)
				PlayerWhite.askDraw = true;
			else
				PlayerWhite.askDraw = false;
			
			if(PlayerBlack.drawConfirmed){
				isRunning = false;
				System.out.println("Tie");
				return;
			}
		}

		printBoard(board);
	}

	public static void printBoard(Cell[][] b) {
		System.out.println("");
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				System.out.print(b[x][y] + " ");
			}
			System.out.println(8 - x);
		}
		for (char x = 'a'; x <= 'h'; x++) {
			System.out.print(" " + x + " ");
		}
		System.out.println("\n");
	}
}
