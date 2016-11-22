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
 * Class to represent a queen piece Queen can move almost in every way:
 * 
 	|_|_|_|X|_|_|_|X|7
    |X|_|_|X|_|_|X|_|6
    |_|X|_|X|_|X|_|_|5
    |_|_|X|X|x|_|_|_|4
    |X|X|X|Q|X|X|X|X|3
    |_|_|X|X|X|_|_|_|2
    |_|X|_|X|_|X|_|_|1
    |X|_|_|X|_|_|X|_|0
    0 1 2 3 4 5 6 7
 */
public class Queen extends Piece
{
	private static final String SYMBOL = "Q";
	private static final Direction[] NORMAL_MOVEMENT = {
			new Direction(1, 0),
			new Direction(-1, 0),
			new Direction(0, 1),
			new Direction(0, -1),
			new Direction(1, 1),
			new Direction(-1, 1),
			new Direction(1, -1),
			new Direction(-1, -1)
	};
	
	@Override
	public List<Direction> getNormalMovements() {
		return Arrays.asList(Queen.NORMAL_MOVEMENT);
	}
	
	@Override
	public List<Direction> getStrikingMovements() {
		return Arrays.asList(Queen.NORMAL_MOVEMENT);
	}
	
	@Override
	public String getSymbol() {
		return Queen.SYMBOL;
	}
	
	public static short value = 9;
	
	public Queen(ChessboardController chessboard, Player player)
	{
		super(chessboard, player);// call initialiser of super type: Piece
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
		
		// ------------- as Rook --------------
		for(int i = this.getSquare().getPosY() + 1; i <= 7; ++i)
		{// up
			
			if(this.checkPiece(this.getSquare().getPosX(), i))
			{// if there isn't a piece on this square
				
				if(this.getPlayer().getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(this.getSquare().getPosX(), i)))
					{
						list.add(chessboard.getBoard().getField(this.getSquare().getPosX(), i));
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(this.getSquare().getPosX(), i)))
					{
						list.add(chessboard.getBoard().getField(this.getSquare().getPosX(), i));
					}
				}
				
				if(this.otherOwner(this.getSquare().getPosX(), i))
				{
					break;
				}
			} else // if there is a piece on this square
			
			{
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int i = this.getSquare().getPosY() - 1; i >= 0; --i)
		{// down
			
			if(this.checkPiece(this.getSquare().getPosX(), i))
			{// if there isn't a piece on this square
				
				if(this.getPlayer().getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(this.getSquare().getPosX(), i)))
					{
						list.add(chessboard.getBoard().getField(this.getSquare().getPosX(), i));
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(this.getSquare().getPosX(), i)))
					{
						list.add(chessboard.getBoard().getField(this.getSquare().getPosX(), i));
					}
				}
				
				if(this.otherOwner(this.getSquare().getPosX(), i))
				{
					break;
				}
			} else
			{// if there is a piece on this square
				
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int i = this.getSquare().getPosX() - 1; i >= 0; --i)
		{// left
			
			if(this.checkPiece(i, this.getSquare().getPosY()))
			{// if there isn't a piece on this square
				
				if(this.getPlayer().getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(i, this.getSquare().getPosY())))
					{
						list.add(chessboard.getBoard().getField(i, this.getSquare().getPosY()));
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(i, this.getSquare().getPosY())))
					{
						list.add(chessboard.getBoard().getField(i, this.getSquare().getPosY()));
					}
				}
				
				if(this.otherOwner(i, this.getSquare().getPosY()))
				{
					break;
				}
			} else
			{// if there is a piece on this square
				
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int i = this.getSquare().getPosX() + 1; i <= 7; ++i)
		{// right
			
			if(this.checkPiece(i, this.getSquare().getPosY()))
			{// if there isn't a piece on this square
				
				if(this.getPlayer().getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(i, this.getSquare().getPosY())))
					{
						list.add(chessboard.getBoard().getField(i, this.getSquare().getPosY()));
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(i, this.getSquare().getPosY())))
					{
						list.add(chessboard.getBoard().getField(i, this.getSquare().getPosY()));
					}
				}
				
				if(this.otherOwner(i, this.getSquare().getPosY()))
				{
					break;
				}
			} else
			{// if there is a piece on this square
				
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		// ------------- as Bishop ------------------
		for(int h = this.getSquare().getPosX() - 1, i = this.getSquare().getPosY() + 1; !isout(h, i); --h, ++i)
		{// left-up
			
			if(this.checkPiece(h, i))
			{// if there isn't a piece on this square
				
				if(this.getPlayer().getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else
				{// or black
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
			{// if there is a piece on this square
				
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int h = this.getSquare().getPosX() - 1, i = this.getSquare().getPosY() - 1; !isout(h, i); --h, --i)
		{// left-down
			
			if(this.checkPiece(h, i))
			{// if there isn't a piece on this square
				
				if(this.getPlayer().getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else
				{// or black
					
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
			{// if there is a piece on this square
				
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int h = this.getSquare().getPosX() + 1, i = this.getSquare().getPosY() + 1; !isout(h, i); ++h, ++i)
		{// right-up
			
			if(this.checkPiece(h, i))
			{// if there isn't a piece on this square
				
				if(this.getPlayer().getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else
				{// or black
					
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
			{// if there is a piece on this square
				
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int h = this.getSquare().getPosX() + 1, i = this.getSquare().getPosY() - 1; !isout(h, i); ++h, --i)
		{// right-down
			
			if(this.checkPiece(h, i))
			{// if there isn't a piece on this square
				
				if(this.getPlayer().getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.getSquare(),
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else
				{// or black
					
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
			{// if there is a piece on this square
				
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		// ------------------------------------
		
		return list;
	}
}
