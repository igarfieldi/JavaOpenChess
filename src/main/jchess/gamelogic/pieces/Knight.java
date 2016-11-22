/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.gamelogic.pieces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.ChessboardController;
import jchess.gamelogic.field.Field;
import jchess.util.Direction;

/**
 * Class to represent a chess pawn knight Knight's movements:
 * 
|_|_|_|_|_|_|_|_|7
|_|_|_|_|_|_|_|_|6
|_|_|2|_|3|_|_|_|5
|_|1|_|_|_|4|_|_|4
|_|_|_|K|_|_|_|_|3
|_|8|_|_|_|5|_|_|2
|_|_|7|_|6|_|_|_|1
|_|_|_|_|_|_|_|_|0
0 1 2 3 4 5 6 7
 */
public class Knight extends Piece
{
	private static final String SYMBOL = "N";
	private static final Direction[] NORMAL_MOVEMENT = {
			new Direction(1, 2),
			new Direction(-1, 2),
			new Direction(1, -2),
			new Direction(1, -2),
			new Direction(2, 1),
			new Direction(2, -1),
			new Direction(-2, 1),
			new Direction(-2, -1)
	};
	
	@Override
	public List<Direction> getNormalMovements() {
		return Arrays.asList(Knight.NORMAL_MOVEMENT);
	}
	
	@Override
	public List<Direction> getStrikingMovements() {
		return Arrays.asList(Knight.NORMAL_MOVEMENT);
	}
	
	@Override
	public String getSymbol() {
		return Knight.SYMBOL;
	}
	
	public static short value = 3;
	
	public Knight(ChessboardController chessboard, Player player)
	{
		super(chessboard, player);// call initialiser of super type: PieceW
	}
	
	/**
	 * Annotation to superclass Piece changing pawns location
	 * 
	 * @return ArrayList with new position of pawn
	 */
	@Override
	public ArrayList<Field> possibleMoves()
	{
		ArrayList<Field> list = new ArrayList<Field>();
		int newX, newY;
		// 1st move from the grid
		newX = this.getSquare().getPosX() - 2;
		newY = this.getSquare().getPosY() + 1;
		
		if(!isout(newX, newY) && checkPiece(newX, newY))
		{
			if(this.getPlayer().getColor() == Player.Color.WHITE) // white
			{
				if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			} else // or black
			{
				if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			}
		}
		
		// 2nd move from the grid
		newX = this.getSquare().getPosX() - 1;
		newY = this.getSquare().getPosY() + 2;
		
		if(!isout(newX, newY) && checkPiece(newX, newY))
		{
			if(this.getPlayer().getColor() == Player.Color.WHITE) // white
			{
				if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			} else // or black
			{
				if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			}
		}
		
		// 3rd move from the grid
		newX = this.getSquare().getPosX() + 1;
		newY = this.getSquare().getPosY() + 2;
		
		if(!isout(newX, newY) && checkPiece(newX, newY))
		{
			if(this.getPlayer().getColor() == Player.Color.WHITE) // white
			{
				if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			} else // or black
			{
				if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			}
		}
		
		// 4th move from the grid
		newX = this.getSquare().getPosX() + 2;
		newY = this.getSquare().getPosY() + 1;
		
		if(!isout(newX, newY) && checkPiece(newX, newY))
		{
			if(this.getPlayer().getColor() == Player.Color.WHITE) // white
			{
				if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			} else // or black
			{
				if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			}
		}
		
		// 5th move from the grid
		newX = this.getSquare().getPosX() + 2;
		newY = this.getSquare().getPosY() - 1;
		
		if(!isout(newX, newY) && checkPiece(newX, newY))
		{
			if(this.getPlayer().getColor() == Player.Color.WHITE) // white
			{
				if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			} else // or black
			{
				if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			}
		}
		
		// 6th move from the grid
		newX = this.getSquare().getPosX() + 1;
		newY = this.getSquare().getPosY() - 2;
		
		if(!isout(newX, newY) && checkPiece(newX, newY))
		{
			if(this.getPlayer().getColor() == Player.Color.WHITE) // white
			{
				if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			} else // or black
			{
				if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			}
		}
		
		// 7th move from the grid
		newX = this.getSquare().getPosX() - 1;
		newY = this.getSquare().getPosY() - 2;
		
		if(!isout(newX, newY) && checkPiece(newX, newY))
		{
			if(this.getPlayer().getColor() == Player.Color.WHITE) // white
			{
				if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			} else // or black
			{
				if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			}
		}
		
		// 8th move from the grid
		newX = this.getSquare().getPosX() - 2;
		newY = this.getSquare().getPosY() - 1;
		
		if(!isout(newX, newY) && checkPiece(newX, newY))
		{
			if(this.getPlayer().getColor() == Player.Color.WHITE) // white
			{
				if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			} else // or black
			{
				if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
				        chessboard.getBoard().getField(newX, newY)))
				{
					list.add(chessboard.getBoard().getField(newX, newY));
				}
			}
		}
		
		return list;
	}
}
