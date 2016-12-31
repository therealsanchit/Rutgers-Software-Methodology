package player;

import java.util.ArrayList;

import chess.Cell;
import unit.Piece;

/* Player
 * turn / move - 
 */
public interface Player {
	public boolean askDraw=false;
	public boolean draw=false;
	public String king = null;
	public Player enemy = null;
	public ArrayList<Piece> pieces = null;
	public ArrayList<Piece> threat = null;
	public boolean turn(Cell[][] c);
	public void addEnemy(Player p);
	public boolean move(String moveTo, Cell[][] c);
	public void resign();
	public void draw();
	public boolean promote(String[] split, Cell[][] c);
	public boolean checkValidMove(String line, Cell[][] c);
	public boolean checkValidCell(String[] split);
	public boolean parseFurther(String[] split, Cell[][] c);
	public boolean checkForMate();
}
