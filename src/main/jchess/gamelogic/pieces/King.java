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

/**
 * Class to represent a chess pawn king. King is the most important
 * piece for the game. Loose of king is the and of game.
 * When king is in danger by the opponent then it's a Checked, and when have
 * no other escape then stay on a square "in danger" by the opponent
 * then it's a CheckedMate, and the game is over.
 *
|_|_|_|_|_|_|_|_|7
|_|_|_|_|_|_|_|_|6
|_|_|_|_|_|_|_|_|5
|_|_|X|X|X|_|_|_|4
|_|_|X|K|X|_|_|_|3
|_|_|X|X|X|_|_|_|2
|_|_|_|_|_|_|_|_|1
|_|_|_|_|_|_|_|_|0
0 1 2 3 4 5 6 7
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.ChessboardController;
import jchess.gamelogic.field.Field;
import jchess.util.Direction;

public class King extends Piece
{
	private static final String SYMBOL = "K";
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
		return Arrays.asList(King.NORMAL_MOVEMENT);
	}
	
	@Override
	public List<Direction> getStrikingMovements() {
		return Arrays.asList(King.NORMAL_MOVEMENT);
	}
	
	@Override
	public String getSymbol() {
		return King.SYMBOL;
	}
	
	public boolean wasMotion = false; // true if the King has moved once
	// public boolean checked = false;
	public static short value = 99;
	
	public King(ChessboardController chessboard, Player player)
	{
		super(chessboard, player);
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
		Field sq;
		Field sq1;
		for(int i = this.getSquare().getPosX() - 1; i <= this.getSquare().getPosX() + 1; i++)
		{
			for(int y = this.getSquare().getPosY() - 1; y <= this.getSquare().getPosY() + 1; y++)
			{
				if(!this.isout(i, y))
				{// out of bounds protection
					sq = this.chessboard.getBoard().getField(i, y);
					if(this.getSquare() == sq)
					{// if we're checking the square on which the King is
						continue;
					}
					if(this.checkPiece(i, y))
					{// if there isn't a piece on this square
						if(isSafe(sq))
						{
							list.add(sq);
						}
					}
				}
			}
		}
		
		if(!this.wasMotion && !this.isChecked())
		{// check if king was not moved before
			
			if(chessboard.getBoard().getField(0, this.getSquare().getPosY()).getPiece() != null
			        && chessboard.getBoard().getField(0, this.getSquare().getPosY()).getPiece().getName().equals("Rook"))
			{
				boolean canCastling = true;
				
				Rook rook = (Rook) chessboard.getBoard().getField(0, this.getSquare().getPosY()).getPiece();
				if(!rook.wasMotion)
				{
					for(int i = this.getSquare().getPosX() - 1; i > 0; i--)
					{// go left
						if(chessboard.getBoard().getField(i, this.getSquare().getPosY()).getPiece() != null)
						{
							canCastling = false;
							break;
						}
					}
					sq = this.chessboard.getBoard().getField(this.getSquare().getPosX() - 2, this.getSquare().getPosY());
					sq1 = this.chessboard.getBoard().getField(this.getSquare().getPosX() - 1, this.getSquare().getPosY());
					if(canCastling && this.isSafe(sq) && this.isSafe(sq1))
					{ // can do castling when none of Sq,sq1 is checked
						list.add(sq);
					}
				}
			}
			if(chessboard.getBoard().getField(7, this.getSquare().getPosY()).getPiece() != null
			        && chessboard.getBoard().getField(7, this.getSquare().getPosY()).getPiece().getName().equals("Rook"))
			{
				boolean canCastling = true;
				Rook rook = (Rook) chessboard.getBoard().getField(7, this.getSquare().getPosY()).getPiece();
				if(!rook.wasMotion)
				{// if king was not moved before and is not checked
					for(int i = this.getSquare().getPosX() + 1; i < 7; i++)
					{// go right
						if(chessboard.getBoard().getField(i, this.getSquare().getPosY()).getPiece() != null)
						{// if square is not empty
							canCastling = false;// cannot castling
							break; // exit
						}
					}
					sq = this.chessboard.getBoard().getField(this.getSquare().getPosX() + 2, this.getSquare().getPosY());
					sq1 = this.chessboard.getBoard().getField(this.getSquare().getPosX() + 1, this.getSquare().getPosY());
					if(canCastling && this.isSafe(sq) && this.isSafe(sq1))
					{// can do castling when none of Sq,sq1 is checked
						list.add(sq);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Method to check is the king is checked
	 * 
	 * @return bool true if king is not save, else returns false
	 */
	public boolean isChecked()
	{
		return !isSafe(this.getSquare());
	}
	
	/**
	 * Method to check is the king is checked or stalemated
	 * 
	 * @return int 0 if nothing, 1 if checkmate, else returns 2
	 */
	public int isCheckmatedOrStalemated()
	{
		/*
		 * returns: 0-nothing, 1-checkmate, 2-stalemate
		 */
		if(this.possibleMoves().size() == 0)
		{
			for(int i = 0; i < 8; ++i)
			{
				for(int j = 0; j < 8; ++j)
				{
					if(chessboard.getBoard().getField(i, j).getPiece() != null
					        && chessboard.getBoard().getField(i, j).getPiece().getPlayer() == this.getPlayer()
					        && chessboard.getBoard().getField(i, j).getPiece().possibleMoves().size() != 0)
					{
						return 0;
					}
				}
			}
			
			if(this.isChecked())
			{
				return 1;
			} else
			{
				return 2;
			}
		} else
		{
			return 0;
		}
	}
	
	/**
	 * Method to check is the king is checked by an opponent
	 * 
	 * @param s
	 *            Square where is a king
	 * @return bool true if king is save, else returns false
	 */
	private boolean isSafe(Field s) // A bit confusing code.
	{
		// Rook & Queen
		for(int i = s.getPosY() + 1; i <= 7; ++i) // up
		{
			if(this.chessboard.getBoard().getField(s.getPosX(), i).getPiece() == null
			        || this.chessboard.getBoard().getField(s.getPosX(), i).getPiece() == this) // if
			                                                                       // on
			                                                                       // this
			                                                                       // square
			                                                                       // isn't
			                                                                       // piece
			{
				continue;
			} else if(this.chessboard.getBoard().getField(s.getPosX(), i).getPiece().getPlayer() != this.getPlayer()) // if
			                                                                                    // isn't
			                                                                                    // our
			                                                                                    // piece
			{
				if(this.chessboard.getBoard().getField(s.getPosX(), i).getPiece().getName().equals("Rook")
				        || this.chessboard.getBoard().getField(s.getPosX(), i).getPiece().getName().equals("Queen"))
				{
					return false;
				} else
				{
					break;
				}
			} else
			{
				break;
			}
		}
		
		for(int i = s.getPosY() - 1; i >= 0; --i) // down
		{
			if(this.chessboard.getBoard().getField(s.getPosX(), i).getPiece() == null
			        || this.chessboard.getBoard().getField(s.getPosX(), i).getPiece() == this)// if
			                                                                      // there
			                                                                      // isn't
			                                                                      // a
			                                                                      // piece
			                                                                      // on
			                                                                      // this
			                                                                      // square
			{
				continue;
			} else if(this.chessboard.getBoard().getField(s.getPosX(), i).getPiece().getPlayer() != this.getPlayer()) // if
			                                                                                    // isn't
			                                                                                    // our
			                                                                                    // piece
			{
				if(this.chessboard.getBoard().getField(s.getPosX(), i).getPiece().getName().equals("Rook")
				        || this.chessboard.getBoard().getField(s.getPosX(), i).getPiece().getName().equals("Queen"))
				{
					return false;
				} else
				{
					break;
				}
			} else
			{
				break;
			}
		}
		
		for(int i = s.getPosX() - 1; i >= 0; --i) // left
		{
			if(this.chessboard.getBoard().getField(i, s.getPosY()).getPiece() == null
			        || this.chessboard.getBoard().getField(i, s.getPosY()).getPiece() == this) // if
			                                                                       // there
			                                                                       // isn't
			                                                                       // a
			                                                                       // piece
			                                                                       // on
			                                                                       // this
			                                                                       // square
			{
				continue;
			} else if(this.chessboard.getBoard().getField(i, s.getPosY()).getPiece().getPlayer() != this.getPlayer()) // if
			                                                                                    // isn't
			                                                                                    // our
			                                                                                    // piece
			{
				if(this.chessboard.getBoard().getField(i, s.getPosY()).getPiece().getName().equals("Rook")
				        || this.chessboard.getBoard().getField(i, s.getPosY()).getPiece().getName().equals("Queen"))
				{
					return false;
				} else
				{
					break;
				}
			} else
			{
				break;
			}
		}
		
		for(int i = s.getPosX() + 1; i <= 7; ++i) // right
		{
			if(this.chessboard.getBoard().getField(i, s.getPosY()).getPiece() == null
			        || this.chessboard.getBoard().getField(i, s.getPosY()).getPiece() == this) // if
			                                                                       // there
			                                                                       // isn't
			                                                                       // a
			                                                                       // piece
			                                                                       // on
			                                                                       // this
			                                                                       // square
			{
				continue;
			} else if(this.chessboard.getBoard().getField(i, s.getPosY()).getPiece().getPlayer() != this.getPlayer()) // if
			                                                                                    // isn't
			                                                                                    // our
			                                                                                    // piece
			{
				if(this.chessboard.getBoard().getField(i, s.getPosY()).getPiece().getName().equals("Rook")
				        || this.chessboard.getBoard().getField(i, s.getPosY()).getPiece().getName().equals("Queen"))
				{
					return false;
				} else
				{
					break;
				}
			} else
			{
				break;
			}
		}
		
		// Bishop & Queen
		for(int h = s.getPosX() - 1, i = s.getPosY() + 1; !isout(h, i); --h, ++i) // left-up
		{
			if(this.chessboard.getBoard().getField(h, i).getPiece() == null || this.chessboard.getBoard().getField(h, i).getPiece() == this)
			{
				// If there isn't a piece on this field
				continue;
			} else if(this.chessboard.getBoard().getField(h, i).getPiece().getPlayer() != this.getPlayer()) // if
			                                                                          // isn't
			                                                                          // our
			                                                                          // piece
			{
				if(this.chessboard.getBoard().getField(h, i).getPiece().getName().equals("Bishop")
				        || this.chessboard.getBoard().getField(h, i).getPiece().getName().equals("Queen"))
				{
					return false;
				} else
				{
					break;
				}
			} else
			{
				break;
			}
		}
		
		for(int h = s.getPosX() - 1, i = s.getPosY() - 1; !isout(h, i); --h, --i) // left-down
		{
			if(this.chessboard.getBoard().getField(h, i).getPiece() == null || this.chessboard.getBoard().getField(h, i).getPiece() == this)
			{
				// If there isn't a piece on this field
				continue;
			} else if(this.chessboard.getBoard().getField(h, i).getPiece().getPlayer() != this.getPlayer()) // if
			                                                                          // isn't
			                                                                          // our
			                                                                          // piece
			{
				if(this.chessboard.getBoard().getField(h, i).getPiece().getName().equals("Bishop")
				        || this.chessboard.getBoard().getField(h, i).getPiece().getName().equals("Queen"))
				{
					return false;
				} else
				{
					break;
				}
			} else
			{
				break;
			}
		}
		
		for(int h = s.getPosX() + 1, i = s.getPosY() + 1; !isout(h, i); ++h, ++i) // right-up
		{
			if(this.chessboard.getBoard().getField(h, i).getPiece() == null || this.chessboard.getBoard().getField(h, i).getPiece() == this)
			{
				// If there isn't a piece on this field
				continue;
			} else if(this.chessboard.getBoard().getField(h, i).getPiece().getPlayer() != this.getPlayer()) // if
			                                                                          // isn't
			                                                                          // our
			                                                                          // piece
			{
				if(this.chessboard.getBoard().getField(h, i).getPiece().getName().equals("Bishop")
				        || this.chessboard.getBoard().getField(h, i).getPiece().getName().equals("Queen"))
				{
					return false;
				} else
				{
					break;
				}
			} else
			{
				break;
			}
		}
		
		for(int h = s.getPosX() + 1, i = s.getPosY() - 1; !isout(h, i); ++h, --i) // right-down
		{
			if(this.chessboard.getBoard().getField(h, i).getPiece() == null || this.chessboard.getBoard().getField(h, i).getPiece() == this)
			{
				// If there isn't a piece on this field
				continue;
			} else if(this.chessboard.getBoard().getField(h, i).getPiece().getPlayer() != this.getPlayer()) // if
			                                                                          // isn't
			                                                                          // our
			                                                                          // piece
			{
				if(this.chessboard.getBoard().getField(h, i).getPiece().getName().equals("Bishop")
				        || this.chessboard.getBoard().getField(h, i).getPiece().getName().equals("Queen"))
				{
					return false;
				} else
				{
					break;
				}
			} else
			{
				break;
			}
		}
		
		// Knight
		int newX, newY;
		
		// 1
		newX = s.getPosX() - 2;
		newY = s.getPosY() + 1;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null)// if
			                                                          // there
			                                                          // isn't a
			                                                          // piece
			                                                          // on this
			                                                          // square
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Knight"))
			{
				return false;
			}
		}
		
		// 2
		newX = s.getPosX() - 1;
		newY = s.getPosY() + 2;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Knight"))
			{
				return false;
			}
		}
		
		// 3
		newX = s.getPosX() + 1;
		newY = s.getPosY() + 2;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Knight"))
			{
				return false;
			}
		}
		
		// 4
		newX = s.getPosX() + 2;
		newY = s.getPosY() + 1;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Knight"))
			{
				return false;
			}
		}
		
		// 5
		newX = s.getPosX() + 2;
		newY = s.getPosY() - 1;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Knight"))
			{
				return false;
			}
		}
		
		// 6
		newX = s.getPosX() + 1;
		newY = s.getPosY() - 2;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Knight"))
			{
				return false;
			}
		}
		
		// 7
		newX = s.getPosX() - 1;
		newY = s.getPosY() - 2;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null)// if
			                                                          // there
			                                                          // isn't a
			                                                          // piece
			                                                          // on this
			                                                          // square
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Knight"))
			{
				return false;
			}
		}
		
		// 8
		newX = s.getPosX() - 2;
		newY = s.getPosY() - 1;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Knight"))
			{
				return false;
			}
		}
		
		// King
		King otherKing;
		if(this == chessboard.getWhiteKing())
		{
			otherKing = chessboard.getBlackKing();
		} else
		{
			otherKing = chessboard.getWhiteKing();
		}
		
		if(s.getPosX() <= otherKing.getSquare().getPosX() + 1 && s.getPosX() >= otherKing.getSquare().getPosX() - 1
		        && s.getPosY() <= otherKing.getSquare().getPosY() + 1 && s.getPosY() >= otherKing.getSquare().getPosY() - 1)
		{
			return false;
		}
		
		// Pawn
		if(this.getPlayer().isTopSide()) // check if player "go" down or up
		{
			newX = s.getPosX() - 1;
			newY = s.getPosY() + 1;
			if(!isout(newX, newY))
			{
				if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
				                                                           // on
				                                                           // this
				                                                           // sqhuare
				                                                           // isn't
				                                                           // piece
				{
				} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
				                                                                                // is
				                                                                                // our
				                                                                                // piece
				{
				} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Pawn"))
				{
					return false;
				}
			}
			newX = s.getPosX() + 1;
			if(!isout(newX, newY))
			{
				if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
				                                                           // on
				                                                           // this
				                                                           // sqhuare
				                                                           // isn't
				                                                           // piece
				{
				} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
				                                                                                // is
				                                                                                // our
				                                                                                // piece
				{
				} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Pawn"))
				{
					return false;
				}
			}
		} else
		{
			newX = s.getPosX() - 1;
			newY = s.getPosY() - 1;
			if(!isout(newX, newY))
			{
				if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
				                                                           // on
				                                                           // this
				                                                           // sqhuare
				                                                           // isn't
				                                                           // piece
				{
				} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
				                                                                                // is
				                                                                                // our
				                                                                                // piece
				{
				} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Pawn"))
				{
					return false;
				}
			}
			newX = s.getPosX() + 1;
			if(!isout(newX, newY))
			{
				if(this.chessboard.getBoard().getField(newX, newY).getPiece() == null) // if
				                                                           // on
				                                                           // this
				                                                           // sqhuare
				                                                           // isn't
				                                                           // piece
				{
				} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getPlayer() == this.getPlayer()) // if
				                                                                                // is
				                                                                                // our
				                                                                                // piece
				{
				} else if(this.chessboard.getBoard().getField(newX, newY).getPiece().getName().equals("Pawn"))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Method to check will the king be safe when move
	 * 
	 * @return bool true if king is save, else returns false
	 */
	public boolean willBeSafeWhenMoveOtherPiece(Field sqIsHere, Field sqWillBeThere) // long
	                                                                                   // name
	                                                                                   // ;)
	{
		Piece tmp = sqWillBeThere.getPiece();
		sqWillBeThere.setPiece(sqIsHere.getPiece()); // move without redraw
		sqIsHere.setPiece(null);
		
		boolean ret = isSafe(this.getSquare());
		
		sqIsHere.setPiece(sqWillBeThere.getPiece());
		sqWillBeThere.setPiece(tmp);
		
		return ret;
	}
}