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
import jchess.gui.GUI;

import java.awt.Image;

/**
 * Class to represent a pawn piece Pawn can move only forward and can beat only
 * across In first move pawn can move 2 squares Pawn can be upgrade to rook,
 * knight, bishop, Queen if it's in the squares nearest the side where opponent
 * is located First move of pawn:
 *
|_|_|_|_|_|_|_|_|7
|_|_|_|_|_|_|_|_|6
|_|_|_|X|_|_|_|_|5
|_|_|_|X|_|_|_|_|4
|_|_|_|P|_|_|_|_|3
|_|_|_|_|_|_|_|_|2
|_|_|_|_|_|_|_|_|1
|_|_|_|_|_|_|_|_|0
0 1 2 3 4 5 6 7
 *
 * Movement of a pawn:
 *       
|_|_|_|_|_|_|_|_|7
|_|_|_|_|_|_|_|_|6
|_|_|_|_|_|_|_|_|5
|_|_|_|X|_|_|_|_|4
|_|_|_|P|_|_|_|_|3
|_|_|_|_|_|_|_|_|2
|_|_|_|_|_|_|_|_|1
|_|_|_|_|_|_|_|_|0
0 1 2 3 4 5 6 7
 *
 * Beats with can take pawn:
 *
|_|_|_|_|_|_|_|_|7
|_|_|_|_|_|_|_|_|6
|_|_|_|_|_|_|_|_|5
|_|_|X|_|X|_|_|_|4
|_|_|_|P|_|_|_|_|3
|_|_|_|_|_|_|_|_|2
|_|_|_|_|_|_|_|_|1
|_|_|_|_|_|_|_|_|0
0 1 2 3 4 5 6 7
 */
public class Pawn extends Piece
{
	
	boolean down;
	protected static final Image imageWhite = GUI.loadThemeImage("Pawn-W.png");
	protected static final Image imageBlack = GUI.loadThemeImage("Pawn-B.png");
	public static short value = 1;
	
	public Pawn(Chessboard chessboard, Player player)
	{
		super(chessboard, player);
		// this.setImages("Pawn-W.png", "Pawn-B.png");
		this.symbol = "";
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
		// System.out.println(this.player.goDown);//4test
		ArrayList<Field> list = new ArrayList<Field>();
		Field sq;
		Field sq1;
		int first;
		int second;
		if(this.player.isTopSide())
		{// check if player "go" down or up
			first = this.square.getPosY() + 1;// where to move, if the player is
			                                  // on the top side
			second = this.square.getPosY() + 2;// where to move, if the player
			                                   // is on the top side for the
			                                   // first move
		} else
		{
			first = this.square.getPosY() - 1;// same, for bottom side
			second = this.square.getPosY() - 2;// same, for bottom side
		}
		if(this.isout(first, first))
		{// out of bounds protection
			return list;// return empty list
		}
		sq = chessboard.getBoard().getField(this.square.getPosX(), first);
		if(sq.getPiece() == null)
		{// if next is free
		 // list.add(sq);//add
			if(this.player.getColor() == Player.Color.WHITE)
			{// white
				
				if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
				        chessboard.getBoard().getField(this.square.getPosX(), first)))
				{
					list.add(chessboard.getBoard().getField(this.square.getPosX(), first));
				}
			} else
			{// or black
				
				if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
				        chessboard.getBoard().getField(this.square.getPosX(), first)))
				{
					list.add(chessboard.getBoard().getField(this.square.getPosX(), first));
				}
			}
			
			if((player.isTopSide() && this.square.getPosY() == 1)
			        || (!player.isTopSide() && this.square.getPosY() == 6))
			{
				sq1 = chessboard.getBoard().getField(this.square.getPosX(), second);
				if(sq1.getPiece() == null)
				{
					// list.add(sq1);//only in first move
					if(this.player.getColor() == Player.Color.WHITE)
					{// white
						
						if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX(), second)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX(), second));
						}
					} else
					{// or black
						
						if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX(), second)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX(), second));
						}
					}
				}
			}
		}
		if(!this.isout(this.square.getPosX() - 1, this.square.getPosY())) // out
		                                                                  // of
		                                                                  // bounds
		                                                                  // protection
		{
			// capture
			sq = chessboard.getBoard().getField(this.square.getPosX() - 1, first);
			if(sq.getPiece() != null)
			{// check if can hit left
				if(this.player != sq.getPiece().player && !sq.getPiece().name.equals("King"))
				{
					// list.add(sq);
					if(this.player.getColor() == Player.Color.WHITE)
					{// white
						
						if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX() - 1, first)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX() - 1, first));
						}
					} else
					{// or black
						
						if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX() - 1, first)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX() - 1, first));
						}
					}
				}
			}
			
			// En passant
			sq = chessboard.getBoard().getField(this.square.getPosX() - 1, this.square.getPosY());
			if(sq.getPiece() != null && this.chessboard.getTwoSquareMovedPawn() != null
			        && sq == this.chessboard.getTwoSquareMovedPawn().square)
			{// check if can hit left
				if(this.player != sq.getPiece().player && !sq.getPiece().name.equals("King"))
				{// unnecessary
					
					// list.add(sq);
					if(this.player.getColor() == Player.Color.WHITE)
					{// white
						
						if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX() - 1, first)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX() - 1, first));
						}
					} else
					{// or black
						
						if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX() - 1, first)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX() - 1, first));
						}
					}
				}
			}
		}
		if(!this.isout(this.square.getPosX() + 1, this.square.getPosY()))
		{// out of bounds protection
			
			// capture
			sq = chessboard.getBoard().getField(this.square.getPosX() + 1, first);
			if(sq.getPiece() != null)
			{// check if can hit right
				if(this.player != sq.getPiece().player && !sq.getPiece().name.equals("King"))
				{
					// list.add(sq);
					if(this.player.getColor() == Player.Color.WHITE)
					{ // white
						
						if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX() + 1, first)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX() + 1, first));
						}
					} else
					{// or black
						
						if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX() + 1, first)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX() + 1, first));
						}
					}
				}
			}
			
			// En passant
			sq = chessboard.getBoard().getField(this.square.getPosX() + 1, this.square.getPosY());
			if(sq.getPiece() != null && this.chessboard.getTwoSquareMovedPawn() != null
			        && sq == this.chessboard.getTwoSquareMovedPawn().square)
			{// check if can hit left
				if(this.player != sq.getPiece().player && !sq.getPiece().name.equals("King"))
				{// unnecessary
					
					// list.add(sq);
					if(this.player.getColor() == Player.Color.WHITE)
					{// white
						
						if(this.chessboard.getWhiteKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX() + 1, first)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX() + 1, first));
						}
					} else
					{// or black
						
						if(this.chessboard.getBlackKing().willBeSafeWhenMoveOtherPiece(this.square,
						        chessboard.getBoard().getField(this.square.getPosX() + 1, first)))
						{
							list.add(chessboard.getBoard().getField(this.square.getPosX() + 1, first));
						}
					}
				}
			}
		}
		
		return list;
	}
	
	void promote(Piece newPiece)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
