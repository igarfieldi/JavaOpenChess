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

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Chessboard;
import jchess.gamelogic.field.Field;
import jchess.gui.ThemeLoader;

import java.awt.Image;

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
	
	public static short value = 3;
	protected static final Image imageWhite = ThemeLoader.loadThemeImage("Bishop-W.png");
	protected static final Image imageBlack = ThemeLoader.loadThemeImage("Bishop-B.png");
	
	public Bishop(Chessboard chessboard, Player player)
	{
		super(chessboard, player); // call initialiser of super type: Piece
		// this.setImages("Bishop-W.png", "Bishop-B.png");
		this.symbol = "B";
		this.setImage();
	}
	
	@Override
	void setImage()
	{
		if(this.player.getColor() == Player.Color.BLACK)
		{
			image = imageBlack;
		} else
		{
			image = imageWhite;
		}
		orgImage = image;
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
		
		for(int h = this.square.getPosX() - 1, i = this.square.getPosY() + 1; !isout(h, i); --h, ++i) // left-up
		{
			if(this.checkPiece(h, i))// if there isn't a piece on this square
			{
				if(this.player.getColor() == Player.Color.WHITE) // white
				{
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else // or black
				{
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
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
		
		for(int h = this.square.getPosX() - 1, i = this.square.getPosY() - 1; !isout(h, i); --h, --i) // left-down
		{
			if(this.checkPiece(h, i)) // if there isn't a piece on this square
			{
				if(this.player.getColor() == Player.Color.WHITE) // white
				{
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else // or black
				{
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
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
		
		for(int h = this.square.getPosX() + 1, i = this.square.getPosY() + 1; !isout(h, i); ++h, ++i) // right-up
		{
			if(this.checkPiece(h, i))// if there isn't a piece on this square
			{
				if(this.player.getColor() == Player.Color.WHITE) // white
				{
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else // or black
				{
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
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
		
		for(int h = this.square.getPosX() + 1, i = this.square.getPosY() - 1; !isout(h, i); ++h, --i) // right-down
		{
			if(this.checkPiece(h, i))// if there isn't a piece on this square
			{
				if(this.player.getColor() == Player.Color.WHITE) // white
				{
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.getBoard().getField(h, i)))
					{
						list.add(chessboard.getBoard().getField(h, i));
					}
				} else // or black
				{
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
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
