package player;

import java.util.ArrayList;

import chess.Board;
import chess.Cell;
import chess.Record;
import unit.Pawn;
import unit.Piece;

public class White implements Player{
	public String king;
	public Player enemy;
	public ArrayList<Piece> pieces;
	public ArrayList<Piece> threat;
	public String name = "white";
	public boolean checked;
	public boolean askDraw;
	public boolean drawConfirmed;
	
	public White(){
		checked = false;
		pieces = new ArrayList<Piece>();
		threat = new ArrayList<Piece>();
		king = "wK";
		askDraw = false;
	}
	public void addEnemy(Player enemy){this.enemy = enemy;}
	@Override
	public boolean turn(Cell[][] board) {
		if(checked){
			System.out.println("check!");
			if(checkForMate()){
				System.out.println("checkMATE!");
				System.out.println("Black wins!");
				Board.isRunning = false;
				return true;
				// THE GAME ENDS HERE
			}
		}
		System.out.print("White's Move: ");
		
		String command = ChessIO.readMove();
		if(!checkValidMove(command, board))
			return false;

		if(!move(command, board))
			return false;
		
		return true;
	}

	@Override
	public boolean move(String line, Cell[][] board) {
		String[] split = line.split(" ");
		if(split[0].equals("resign"))
			return true;
		if(split[0].equals("draw"))
			return true;
		
		Piece p = Board.grabCell(split[0], board).getPiece();

		if(!p.checkValidMove(split[1], board))
			return false;
		
		p.movePiece(split[1], board);

		Board.checkChecks(board);

		if(checked){
			Board.record.undo();
			System.out.println("white king checked");
			checked = false;
			return false;
		}
		
		threat.clear();
		p.hasMoved = true;
		p.numOfMoves++;
		return true;
	}

	@Override
	public void resign() {
		Board.isRunning = false;
		System.out.println("Black Player wins");
		return;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean promote(String[] split, Cell[][] board) {
	
		Piece p = Board.grabCell(split[0], board).getPiece();
		if(!p.getName().equals("wp"))
			return false;
		else if(!p.checkValidMove(split[1], board))
			return false;
		
		Pawn pawn = (Pawn) p;
		if(split[2].equalsIgnoreCase("n"))
			pawn.promote("knight");
		else if(split[2].equalsIgnoreCase("b"))
			pawn.promote("bishop");
		else if(split[2].equalsIgnoreCase("r"))
			pawn.promote("rook");
		else if(split[2].equalsIgnoreCase("q"))
			pawn.promote("queen");
		else
			return false;
		
		return true;
			
	}
	
	@Override
	public boolean checkValidMove(String line, Cell[][] board) {
		String[] split = line.split(" ");
		
		if(split.length == 1){
			/* resignation, receive draw */
			if(split[0].equals("resign")){
				resign();
				return true;
			}else if(split[0].equals("draw") && askDraw){
				drawConfirmed = true;
				return true;
			}else
				return false;
		}else if(split.length == 3)
			/* draw? , promotion of pawn */
			return parseFurther(split, board);
		else if(split.length != 2)
			return false;
		else if(!checkValidCell(split))
			return false;
		
		
		Cell c = Board.grabCell(split[0], board);
		if(!c.hasPiece)
			return false;
		else if(!c.getPiece().getTeam().equals(name))
			return false;
		
		askDraw = false;
		return true;
	}
	
	@Override
	public boolean checkValidCell(String[] split) {
		if(split[0].length() != 2 || split[1].length() != 2)
			return false;
		else if(split[0].charAt(0) < 0x61 || split[0].charAt(0) > 0x68 ||
				split[0].charAt(1) < 0x31 || split[0].charAt(1) > 0x38)
			return false;
		else if(split[1].charAt(0) < 0x61 || split[1].charAt(0) > 0x68 ||
				split[1].charAt(1) < 0x31 || split[1].charAt(1) > 0x38)
			return false;
		return true;
	}
	
	@Override
	public boolean parseFurther(String[] split, Cell[][] board) {
		if(!checkValidCell(split))
			return false;
		
		String special = split[2];
		if(special.equals("draw?")){
			askDraw = true; return true;}
		else if(!promote(split, board))
			return false;
		return true;
	}
	
	@Override
	public boolean checkForMate() {
		Piece king = null;
		for(Piece p: pieces){
			if(p.getName().equals(this.king)){
				king = p;
				break;
			}
		}
		king.possibleMoves.clear();
		king.calculatePossibleMoves(Board.board, null);
		/* if at least one move is safe from all danger zones, it's not mate */
		for(Cell c: king.possibleMoves){
			if(Board.record.tryMove(king, c.position, this.name)){
				return false;
			}
		}
		
		/* king has no more good moves to make at this point */
		for(Piece p : pieces){
			if(!p.isAlive)
				continue;
			System.out.println(p);
			p.possibleMoves.clear();
			p.calculatePossibleMoves(Board.board, p.getPos());
			for(Cell c: p.possibleMoves){
				System.out.println(c.position);
				if(Board.record.tryMove(p, c.position, this.name)){
					return false;
				}
			}
		}
		/* no one has good moves at this point */
		return true;
	}
}
