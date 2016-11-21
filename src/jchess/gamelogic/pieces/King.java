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

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Chessboard;
import jchess.gamelogic.field.Square;
import jchess.gui.ThemeLoader;

import java.awt.Image;

public class King extends Piece
{
	
	public boolean wasMotion = false; // true if the King has moved once
	// public boolean checked = false;
	public static short value = 99;
	private static final Image imageWhite = ThemeLoader.loadThemeImage("King-W.png");
	private static final Image imageBlack = ThemeLoader.loadThemeImage("King-B.png");
	
	public King(Chessboard chessboard, Player player)
	{
		super(chessboard, player);
		// this.setImages("King-W.png", "King-B.png");
		this.symbol = "K";
		this.setImage();
		// this.image = imageWhite;
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
		Square sq;
		Square sq1;
		for(int i = this.square.getPosX() - 1; i <= this.square.getPosX() + 1; i++)
		{
			for(int y = this.square.getPosY() - 1; y <= this.square.getPosY() + 1; y++)
			{
				if(!this.isout(i, y))
				{// out of bounds protection
					sq = this.chessboard.squares[i][y];
					if(this.square == sq)
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
			
			if(chessboard.squares[0][this.square.getPosY()].getPiece() != null
			        && chessboard.squares[0][this.square.getPosY()].getPiece().name.equals("Rook"))
			{
				boolean canCastling = true;
				
				Rook rook = (Rook) chessboard.squares[0][this.square.getPosY()].getPiece();
				if(!rook.wasMotion)
				{
					for(int i = this.square.getPosX() - 1; i > 0; i--)
					{// go left
						if(chessboard.squares[i][this.square.getPosY()].getPiece() != null)
						{
							canCastling = false;
							break;
						}
					}
					sq = this.chessboard.squares[this.square.getPosX() - 2][this.square.getPosY()];
					sq1 = this.chessboard.squares[this.square.getPosX() - 1][this.square.getPosY()];
					if(canCastling && this.isSafe(sq) && this.isSafe(sq1))
					{ // can do castling when none of Sq,sq1 is checked
						list.add(sq);
					}
				}
			}
			if(chessboard.squares[7][this.square.getPosY()].getPiece() != null
			        && chessboard.squares[7][this.square.getPosY()].getPiece().name.equals("Rook"))
			{
				boolean canCastling = true;
				Rook rook = (Rook) chessboard.squares[7][this.square.getPosY()].getPiece();
				if(!rook.wasMotion)
				{// if king was not moved before and is not checked
					for(int i = this.square.getPosX() + 1; i < 7; i++)
					{// go right
						if(chessboard.squares[i][this.square.getPosY()].getPiece() != null)
						{// if square is not empty
							canCastling = false;// cannot castling
							break; // exit
						}
					}
					sq = this.chessboard.squares[this.square.getPosX() + 2][this.square.getPosY()];
					sq1 = this.chessboard.squares[this.square.getPosX() + 1][this.square.getPosY()];
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
		return !isSafe(this.square);
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
					if(chessboard.squares[i][j].getPiece() != null
					        && chessboard.squares[i][j].getPiece().player == this.player
					        && chessboard.squares[i][j].getPiece().possibleMoves().size() != 0)
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
	private boolean isSafe(Square s) // A bit confusing code.
	{
		// Rook & Queen
		for(int i = s.getPosY() + 1; i <= 7; ++i) // up
		{
			if(this.chessboard.squares[s.getPosX()][i].getPiece() == null
			        || this.chessboard.squares[s.getPosX()][i].getPiece() == this) // if
			                                                                       // on
			                                                                       // this
			                                                                       // square
			                                                                       // isn't
			                                                                       // piece
			{
				continue;
			} else if(this.chessboard.squares[s.getPosX()][i].getPiece().player != this.player) // if
			                                                                                    // isn't
			                                                                                    // our
			                                                                                    // piece
			{
				if(this.chessboard.squares[s.getPosX()][i].getPiece().name.equals("Rook")
				        || this.chessboard.squares[s.getPosX()][i].getPiece().name.equals("Queen"))
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
			if(this.chessboard.squares[s.getPosX()][i].getPiece() == null
			        || this.chessboard.squares[s.getPosX()][i].getPiece() == this)// if
			                                                                      // there
			                                                                      // isn't
			                                                                      // a
			                                                                      // piece
			                                                                      // on
			                                                                      // this
			                                                                      // square
			{
				continue;
			} else if(this.chessboard.squares[s.getPosX()][i].getPiece().player != this.player) // if
			                                                                                    // isn't
			                                                                                    // our
			                                                                                    // piece
			{
				if(this.chessboard.squares[s.getPosX()][i].getPiece().name.equals("Rook")
				        || this.chessboard.squares[s.getPosX()][i].getPiece().name.equals("Queen"))
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
			if(this.chessboard.squares[i][s.getPosY()].getPiece() == null
			        || this.chessboard.squares[i][s.getPosY()].getPiece() == this) // if
			                                                                       // there
			                                                                       // isn't
			                                                                       // a
			                                                                       // piece
			                                                                       // on
			                                                                       // this
			                                                                       // square
			{
				continue;
			} else if(this.chessboard.squares[i][s.getPosY()].getPiece().player != this.player) // if
			                                                                                    // isn't
			                                                                                    // our
			                                                                                    // piece
			{
				if(this.chessboard.squares[i][s.getPosY()].getPiece().name.equals("Rook")
				        || this.chessboard.squares[i][s.getPosY()].getPiece().name.equals("Queen"))
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
			if(this.chessboard.squares[i][s.getPosY()].getPiece() == null
			        || this.chessboard.squares[i][s.getPosY()].getPiece() == this) // if
			                                                                       // there
			                                                                       // isn't
			                                                                       // a
			                                                                       // piece
			                                                                       // on
			                                                                       // this
			                                                                       // square
			{
				continue;
			} else if(this.chessboard.squares[i][s.getPosY()].getPiece().player != this.player) // if
			                                                                                    // isn't
			                                                                                    // our
			                                                                                    // piece
			{
				if(this.chessboard.squares[i][s.getPosY()].getPiece().name.equals("Rook")
				        || this.chessboard.squares[i][s.getPosY()].getPiece().name.equals("Queen"))
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
			if(this.chessboard.squares[h][i].getPiece() == null || this.chessboard.squares[h][i].getPiece() == this) // if
			                                                                                                         // there
			                                                                                                         // isn't
			                                                                                                         // a
			                                                                                                         // piece
			                                                                                                         // on
			                                                                                                         // this
			                                                                                                         // square
			{
				continue;
			} else if(this.chessboard.squares[h][i].getPiece().player != this.player) // if
			                                                                          // isn't
			                                                                          // our
			                                                                          // piece
			{
				if(this.chessboard.squares[h][i].getPiece().name.equals("Bishop")
				        || this.chessboard.squares[h][i].getPiece().name.equals("Queen"))
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
			if(this.chessboard.squares[h][i].getPiece() == null || this.chessboard.squares[h][i].getPiece() == this)// if
			                                                                                                        // there
			                                                                                                        // isn't
			                                                                                                        // a
			                                                                                                        // piece
			                                                                                                        // on
			                                                                                                        // this
			                                                                                                        // square
			{
				continue;
			} else if(this.chessboard.squares[h][i].getPiece().player != this.player) // if
			                                                                          // isn't
			                                                                          // our
			                                                                          // piece
			{
				if(this.chessboard.squares[h][i].getPiece().name.equals("Bishop")
				        || this.chessboard.squares[h][i].getPiece().name.equals("Queen"))
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
			if(this.chessboard.squares[h][i].getPiece() == null || this.chessboard.squares[h][i].getPiece() == this)// if
			                                                                                                        // there
			                                                                                                        // isn't
			                                                                                                        // a
			                                                                                                        // piece
			                                                                                                        // on
			                                                                                                        // this
			                                                                                                        // square
			{
				continue;
			} else if(this.chessboard.squares[h][i].getPiece().player != this.player) // if
			                                                                          // isn't
			                                                                          // our
			                                                                          // piece
			{
				if(this.chessboard.squares[h][i].getPiece().name.equals("Bishop")
				        || this.chessboard.squares[h][i].getPiece().name.equals("Queen"))
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
			if(this.chessboard.squares[h][i].getPiece() == null || this.chessboard.squares[h][i].getPiece() == this)// if
			                                                                                                        // there
			                                                                                                        // isn't
			                                                                                                        // a
			                                                                                                        // piece
			                                                                                                        // on
			                                                                                                        // this
			                                                                                                        // square
			
			{
				continue;
			} else if(this.chessboard.squares[h][i].getPiece().player != this.player) // if
			                                                                          // isn't
			                                                                          // our
			                                                                          // piece
			{
				if(this.chessboard.squares[h][i].getPiece().name.equals("Bishop")
				        || this.chessboard.squares[h][i].getPiece().name.equals("Queen"))
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
			if(this.chessboard.squares[newX][newY].getPiece() == null)// if
			                                                          // there
			                                                          // isn't a
			                                                          // piece
			                                                          // on this
			                                                          // square
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Knight"))
			{
				return false;
			}
		}
		
		// 2
		newX = s.getPosX() - 1;
		newY = s.getPosY() + 2;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.squares[newX][newY].getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Knight"))
			{
				return false;
			}
		}
		
		// 3
		newX = s.getPosX() + 1;
		newY = s.getPosY() + 2;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.squares[newX][newY].getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Knight"))
			{
				return false;
			}
		}
		
		// 4
		newX = s.getPosX() + 2;
		newY = s.getPosY() + 1;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.squares[newX][newY].getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Knight"))
			{
				return false;
			}
		}
		
		// 5
		newX = s.getPosX() + 2;
		newY = s.getPosY() - 1;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.squares[newX][newY].getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Knight"))
			{
				return false;
			}
		}
		
		// 6
		newX = s.getPosX() + 1;
		newY = s.getPosY() - 2;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.squares[newX][newY].getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Knight"))
			{
				return false;
			}
		}
		
		// 7
		newX = s.getPosX() - 1;
		newY = s.getPosY() - 2;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.squares[newX][newY].getPiece() == null)// if
			                                                          // there
			                                                          // isn't a
			                                                          // piece
			                                                          // on this
			                                                          // square
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Knight"))
			{
				return false;
			}
		}
		
		// 8
		newX = s.getPosX() - 2;
		newY = s.getPosY() - 1;
		
		if(!isout(newX, newY))
		{
			if(this.chessboard.squares[newX][newY].getPiece() == null) // if
			                                                           // there
			                                                           // isn't
			                                                           // a
			                                                           // piece
			                                                           // on
			                                                           // this
			                                                           // square
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
			                                                                                // is
			                                                                                // our
			                                                                                // piece
			{
			} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Knight"))
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
		
		if(s.getPosX() <= otherKing.square.getPosX() + 1 && s.getPosX() >= otherKing.square.getPosX() - 1
		        && s.getPosY() <= otherKing.square.getPosY() + 1 && s.getPosY() >= otherKing.square.getPosY() - 1)
		{
			return false;
		}
		
		// Pawn
		if(this.player.isTopSide()) // check if player "go" down or up
		{// System.out.println("go down");
			newX = s.getPosX() - 1;
			newY = s.getPosY() + 1;
			if(!isout(newX, newY))
			{
				if(this.chessboard.squares[newX][newY].getPiece() == null) // if
				                                                           // on
				                                                           // this
				                                                           // sqhuare
				                                                           // isn't
				                                                           // piece
				{
				} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
				                                                                                // is
				                                                                                // our
				                                                                                // piece
				{
				} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Pawn"))
				{
					return false;
				}
			}
			newX = s.getPosX() + 1;
			if(!isout(newX, newY))
			{
				if(this.chessboard.squares[newX][newY].getPiece() == null) // if
				                                                           // on
				                                                           // this
				                                                           // sqhuare
				                                                           // isn't
				                                                           // piece
				{
				} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
				                                                                                // is
				                                                                                // our
				                                                                                // piece
				{
				} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Pawn"))
				{
					return false;
				}
			}
		} else
		{// System.out.println("go up");
			newX = s.getPosX() - 1;
			newY = s.getPosY() - 1;
			if(!isout(newX, newY))
			{
				if(this.chessboard.squares[newX][newY].getPiece() == null) // if
				                                                           // on
				                                                           // this
				                                                           // sqhuare
				                                                           // isn't
				                                                           // piece
				{
				} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
				                                                                                // is
				                                                                                // our
				                                                                                // piece
				{
				} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Pawn"))
				{
					return false;
				}
			}
			newX = s.getPosX() + 1;
			if(!isout(newX, newY))
			{
				if(this.chessboard.squares[newX][newY].getPiece() == null) // if
				                                                           // on
				                                                           // this
				                                                           // sqhuare
				                                                           // isn't
				                                                           // piece
				{
				} else if(this.chessboard.squares[newX][newY].getPiece().player == this.player) // if
				                                                                                // is
				                                                                                // our
				                                                                                // piece
				{
				} else if(this.chessboard.squares[newX][newY].getPiece().name.equals("Pawn"))
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
	public boolean willBeSafeWhenMoveOtherPiece(Square sqIsHere, Square sqWillBeThere) // long
	                                                                                   // name
	                                                                                   // ;)
	{
		Piece tmp = sqWillBeThere.getPiece();
		sqWillBeThere.setPiece(sqIsHere.getPiece()); // move without redraw
		sqIsHere.setPiece(null);
		
		boolean ret = isSafe(this.square);
		
		sqIsHere.setPiece(sqWillBeThere.getPiece());
		sqWillBeThere.setPiece(tmp);
		
		return ret;
	}
}