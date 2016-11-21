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
import jchess.gamelogic.field.Square;
import jchess.gui.ThemeImageLoader;

import java.awt.Image;

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
	
	public static short value = 9;
	protected static final Image imageWhite = ThemeImageLoader.loadThemeImage("Queen-W.png");
	protected static final Image imageBlack = ThemeImageLoader.loadThemeImage("Queen-B.png");
	
	public Queen(Chessboard chessboard, Player player)
	{
		super(chessboard, player);// call initialiser of super type: Piece
		// this.setImages("Queen-W.png", "Queen-B.png");
		this.symbol = "Q";
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
	public ArrayList<Square> possibleMoves()
	{
		ArrayList<Square> list = new ArrayList<Square>();
		
		// ------------- as Rook --------------
		for(int i = this.square.getPosY() + 1; i <= 7; ++i)
		{// up
			
			if(this.checkPiece(this.square.getPosX(), i))
			{// if there isn't a piece on this square
				
				if(this.player.getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[this.square.getPosX()][i]))
					{
						list.add(chessboard.squares[this.square.getPosX()][i]);
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[this.square.getPosX()][i]))
					{
						list.add(chessboard.squares[this.square.getPosX()][i]);
					}
				}
				
				if(this.otherOwner(this.square.getPosX(), i))
				{
					break;
				}
			} else // if there is a piece on this square
			
			{
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int i = this.square.getPosY() - 1; i >= 0; --i)
		{// down
			
			if(this.checkPiece(this.square.getPosX(), i))
			{// if there isn't a piece on this square
				
				if(this.player.getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[this.square.getPosX()][i]))
					{
						list.add(chessboard.squares[this.square.getPosX()][i]);
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[this.square.getPosX()][i]))
					{
						list.add(chessboard.squares[this.square.getPosX()][i]);
					}
				}
				
				if(this.otherOwner(this.square.getPosX(), i))
				{
					break;
				}
			} else
			{// if there is a piece on this square
				
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int i = this.square.getPosX() - 1; i >= 0; --i)
		{// left
			
			if(this.checkPiece(i, this.square.getPosY()))
			{// if there isn't a piece on this square
				
				if(this.player.getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[i][this.square.getPosY()]))
					{
						list.add(chessboard.squares[i][this.square.getPosY()]);
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[i][this.square.getPosY()]))
					{
						list.add(chessboard.squares[i][this.square.getPosY()]);
					}
				}
				
				if(this.otherOwner(i, this.square.getPosY()))
				{
					break;
				}
			} else
			{// if there is a piece on this square
				
				break;// we have to break because we cannot go over other
				      // pieces!
			}
		}
		
		for(int i = this.square.getPosX() + 1; i <= 7; ++i)
		{// right
			
			if(this.checkPiece(i, this.square.getPosY()))
			{// if there isn't a piece on this square
				
				if(this.player.getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[i][this.square.getPosY()]))
					{
						list.add(chessboard.squares[i][this.square.getPosY()]);
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[i][this.square.getPosY()]))
					{
						list.add(chessboard.squares[i][this.square.getPosY()]);
					}
				}
				
				if(this.otherOwner(i, this.square.getPosY()))
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
		for(int h = this.square.getPosX() - 1, i = this.square.getPosY() + 1; !isout(h, i); --h, ++i)
		{// left-up
			
			if(this.checkPiece(h, i))
			{// if there isn't a piece on this square
				
				if(this.player.getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[h][i]))
					{
						list.add(chessboard.squares[h][i]);
					}
				} else
				{// or black
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[h][i]))
					{
						list.add(chessboard.squares[h][i]);
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
		
		for(int h = this.square.getPosX() - 1, i = this.square.getPosY() - 1; !isout(h, i); --h, --i)
		{// left-down
			
			if(this.checkPiece(h, i))
			{// if there isn't a piece on this square
				
				if(this.player.getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[h][i]))
					{
						list.add(chessboard.squares[h][i]);
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[h][i]))
					{
						list.add(chessboard.squares[h][i]);
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
		
		for(int h = this.square.getPosX() + 1, i = this.square.getPosY() + 1; !isout(h, i); ++h, ++i)
		{// right-up
			
			if(this.checkPiece(h, i))
			{// if there isn't a piece on this square
				
				if(this.player.getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[h][i]))
					{
						list.add(chessboard.squares[h][i]);
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[h][i]))
					{
						list.add(chessboard.squares[h][i]);
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
		
		for(int h = this.square.getPosX() + 1, i = this.square.getPosY() - 1; !isout(h, i); ++h, --i)
		{// right-down
			
			if(this.checkPiece(h, i))
			{// if there isn't a piece on this square
				
				if(this.player.getColor() == Player.Color.WHITE)
				{// white
					
					if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[h][i]))
					{
						list.add(chessboard.squares[h][i]);
					}
				} else
				{// or black
					
					if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
					        chessboard.squares[h][i]))
					{
						list.add(chessboard.squares[h][i]);
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
