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
 * Class to represent a chess pawn bishop Bishop can move across the chessboard
 *
|_|_|_|_|_|_|_|X|7
|X|_|_|_|_|_|X|_|6
|_|X|_|_| |X|_|_|5
|_|_|X|_|X|_|_|_|4
|_|_|_|B|_|_|_|_|3
|_| |X|_|X|_|_|_|2
|_|X|_|_|_|X|_|_|1
|X|_|_|_|_|_|X|_|0
0 1 2 3 4 5 6 7
 */
public class Bishop extends Piece
{
	private static final Direction[] NORMAL_MOVEMENT = {
			new Direction(1, 1),
			new Direction(-1, 1),
			new Direction(1, -1),
			new Direction(-1, -1)
	};
	
	@Override
	public List<Direction> getNormalMovements() {
		return Arrays.asList(Bishop.NORMAL_MOVEMENT);
	}
	
	@Override
	public List<Direction> getCapturingMovements() {
		return Arrays.asList(Bishop.NORMAL_MOVEMENT);
	}
	
	public static short value = 3;
	
	public Bishop(ChessboardController chessboard, Player player)
	{
		super(chessboard, player, "B"); // call initialiser of super type: Piece
	}
	
	/**
	 * Annotation to superclass Piece changing pawns location
	 * 
	 * @return ArrayList with new position of piece
	 */
	@Override
	public ArrayList<Field> possibleMoves()
	{
		ArrayList<Field> list = new ArrayList<Field>();
		
		for(int h = this.getSquare().getPosX() - 1, i = this.getSquare().getPosY() + 1; !isout(h, i); --h, ++i) // left-up
		{
			if(this.checkPiece(h, i))// if there isn't a piece on this square
			{
				if(this.getPlayer().getColor() == Player.Color.WHITE) // white
				{
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else // or black
				{
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				}
				
				if(this.otherOwner(h, i))
				{
					break;
				}
			} else
			{
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int h = this.getSquare().getPosX() - 1, i = this.getSquare().getPosY() - 1; !isout(h, i); --h, --i) // left-down
		{
			if(this.checkPiece(h, i)) // if there isn't a piece on this square
			{
				if(this.getPlayer().getColor() == Player.Color.WHITE) // white
				{
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else // or black
				{
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				}
				
				if(this.otherOwner(h, i))
				{
					break;
				}
			} else
			{
				break; // we have to break because we cannot go over other
				       // pieces!
			}
		}
		
		for(int h = this.getSquare().getPosX() + 1, i = this.getSquare().getPosY() + 1; !isout(h, i); ++h, ++i) // right-up
		{
			if(this.checkPiece(h, i))// if there isn't a piece on this square
			{
				if(this.getPlayer().getColor() == Player.Color.WHITE) // white
				{
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else // or black
				{
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				}
				
				if(this.otherOwner(h, i))
				{
					break;
				}
			} else
			{
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int h = this.getSquare().getPosX() + 1, i = this.getSquare().getPosY() - 1; !isout(h, i); ++h, --i) // right-down
		{
			if(this.checkPiece(h, i))// if there isn't a piece on this square
			{
				if(this.getPlayer().getColor() == Player.Color.WHITE) // white
				{
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else // or black
				{
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				}
				
				if(this.otherOwner(h, i))
				{
					break;
				}
			} else
			{
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		return list;
	}
}
