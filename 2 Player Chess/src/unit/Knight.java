package unit;

import chess.Board;
import chess.Cell;

//moves from b1 to c3 or a3
//from c3 to (a4),(b5),(b1),(d5),(e4)  2x 1y       1x 2y
public class Knight extends Piece {

	public Knight(String team, String pos) {
		super(team);
		setName();
		setPosition(pos);
	}

	public void setName() {
		if (team.equals("black"))
			name = "bN";
		else
			name = "wN";
	}

	@Override
	public void calculatePossibleMoves(Cell[][] board, String pos) {
		int newY;
		char newX;
		int[] changeOne = {1, -1};
		int[] changeTwo = {2, -2};
		
		/* (up 1, left/right 2) (down 1, left/right 2) */
		for(int index1=0; index1<2; index1++){
			newY = posY + changeOne[index1];
			for(int index2=0; index2<2; index2++){
				newX = (char)((int)posX + changeTwo[index2]);
				String checkSpot = "" + newX + (newY%10);
				if(checkValidBoundary(checkSpot) && !checkOccupiedisFriend(checkSpot, board))
					possibleMoves.add(Board.grabCell(checkSpot, board));
			}
		}

		/* (right 1, up/down 2) (left 1, up/down 2) */
		for(int index1=0; index1<2; index1++){
			newX = (char)((int)posX + changeOne[index1]);
			for(int index2=0; index2<2; index2++){
				newY = posY + changeTwo[index2];
				String checkSpot = "" + newX + (newY%10);
				if(checkValidBoundary(checkSpot) && !checkOccupiedisFriend(checkSpot, board))
					possibleMoves.add(Board.grabCell(checkSpot, board));
			}
		}

	}

}
