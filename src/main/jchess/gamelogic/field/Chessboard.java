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
package jchess.gamelogic.field;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import jchess.JChessApp;
import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;
import jchess.gamelogic.field.Moves.CastlingType;
import jchess.gamelogic.pieces.Bishop;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Knight;
import jchess.gamelogic.pieces.Pawn;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.Queen;
import jchess.gamelogic.pieces.Rook;
import jchess.gui.ThemeLoader;

/**
 * Class to represent chessboard. Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class Chessboard extends JPanel
{
	private static Logger log = Logger.getLogger(Chessboard.class.getName());
	
	private ChessboardModel board = new ChessboardModel();
	
	private static final long serialVersionUID = 1971410121780567341L;
	public static final int TOP = 0;
	public static final int BOTTOM = 7;
	public static final int IMG_X = 5;// image x position (used in JChessView
	                                  // class!)
	public static final int IMG_Y = IMG_X;// image y position (used in
	                                      // JChessView class!)
	public static final int IMG_WIDTH = 480;// image width
	public static final int IMG_HEIGHT = IMG_WIDTH;// image height
	private static final Image ORG_IMAGE = ThemeLoader.loadThemeImage("chessboard.png");
	private static final Image ORG_SEL_IMAGE = ThemeLoader.loadThemeImage("sel_square.png");
	private static final Image ORG_ARLE_IMAGE = ThemeLoader.loadThemeImage("able_square.png");
	
	// TODO: replace the final static images with the construction-time call to
	// the GUI class?
	private static Image image = Chessboard.ORG_IMAGE;// image of chessboard
	private static Image sel_square = ORG_SEL_IMAGE;// image of highlited square
	private static Image able_square = ORG_ARLE_IMAGE;// image of square where
	                                                  // piece can go
	private Field activeSquare;
	private Image upDownLabel = null;
	private Image LeftRightLabel = null;
	private Point topLeft = new Point(0, 0);
	private int active_x_square;
	private int active_y_square;
	private float square_height;// height of square
	
	private ArrayList<Field> moves;
	private Settings settings;
	private King kingWhite;
	private King kingBlack;
	// -------- for undo ----------
	// ----------------------------
	// For En passant:
	// |-> Pawn whose in last turn moved two square
	private Pawn twoSquareMovedPawn = null;
	private Moves moves_history;
	
	/**
	 * Chessboard class constructor
	 * 
	 * @param settings
	 *            reference to Settings class object for this chessboard
	 * @param moves_history
	 *            reference to Moves class object for this chessboard
	 */
	public Chessboard(Settings settings, Moves moves_history)
	{
		this.settings = settings;
		this.setActiveSquare(null);
		this.square_height = IMG_HEIGHT / 8;// we need to devide to know height
		                                    // of field
		this.active_x_square = 0;
		this.active_y_square = 0;
		this.moves_history = moves_history;
		this.setDoubleBuffered(true);
		this.drawLabels((int) this.square_height);
	}/*--endOf-Chessboard--*/
	
	/**
	 * Method setPieces on begin of new game or loaded game
	 * 
	 * @param places
	 *            string with pieces to set on chessboard
	 * @param plWhite
	 *            reference to white player
	 * @param plBlack
	 *            reference to black player
	 */
	public void setPieces(String places, Player plWhite, Player plBlack)
	{
		
		if(places.equals("")) // if newGame
		{
			if(this.settings.isUpsideDown())
			{
				this.setPieces4NewGame(true, plWhite, plBlack);
			} else
			{
				this.setPieces4NewGame(false, plWhite, plBlack);
			}
			
		} else // if loadedGame
		{
			return;
		}
	}/*--endOf-setPieces--*/
	
	/**
	 *
	 */
	private void setPieces4NewGame(boolean upsideDown, Player plWhite, Player plBlack)
	{
		
		/* WHITE PIECES */
		Player player = plBlack;
		Player player1 = plWhite;
		if(upsideDown) // if white on Top
		{
			player = plWhite;
			player1 = plBlack;
		}
		this.setFigures4NewGame(0, player, upsideDown);
		this.setPawns4NewGame(1, player);
		this.setFigures4NewGame(7, player1, upsideDown);
		this.setPawns4NewGame(6, player1);
	}/*--endOf-setPieces(boolean upsideDown)--*/
	
	/**
	 * method set Figures in row (and set Queen and King to right position)
	 * 
	 * @param i
	 *            row where to set figures (Rook, Knight etc.)
	 * @param player
	 *            which is owner of pawns
	 * @param upsideDown
	 *            if true white pieces will be on top of chessboard
	 */
	private void setFigures4NewGame(int i, Player player, boolean upsideDown)
	{
		
		if(i != 0 && i != 7)
		{
			log.log(Level.SEVERE, "Error setting up Rook/Knight/Bishop/Queen/King!");
			return;
		} else if(i == 0)
		{
			player.setBoardSide(true);
		}

		board.getField(0, i).setPiece(new Rook(this, player));
		board.getField(7, i).setPiece(new Rook(this, player));
		board.getField(1, i).setPiece(new Knight(this, player));
		board.getField(6, i).setPiece(new Knight(this, player));
		board.getField(2, i).setPiece(new Bishop(this, player));
		board.getField(5, i).setPiece(new Bishop(this, player));
		if(upsideDown)
		{
			board.getField(4, i).setPiece(new Queen(this, player));
			if(player.getColor() == Player.Color.WHITE)
			{
				board.getField(3, i).setPiece(setWhiteKing(new King(this, player)));
			} else
			{
				board.getField(3, i).setPiece(setBlackKing(new King(this, player)));
			}
		} else
		{
			board.getField(3, i).setPiece(new Queen(this, player));
			if(player.getColor() == Player.Color.WHITE)
			{
				board.getField(4, i).setPiece(setWhiteKing(new King(this, player)));
			} else
			{
				board.getField(4, i).setPiece(setBlackKing(new King(this, player)));
			}
		}
	}
	
	/**
	 * method set Pawns in row
	 * 
	 * @param i
	 *            row where to set pawns
	 * @param player
	 *            player which is owner of pawns
	 */
	private void setPawns4NewGame(int i, Player player)
	{
		if(i != 1 && i != 6)
		{
			log.log(Level.SEVERE, "Error setting up pawn!");
			return;
		}
		for(int x = 0; x < 8; x++)
		{
			board.getField(x, i).setPiece(new Pawn(this, player));
		}
	}
	
	/**
	 * method to get reference to square from given x and y integeres
	 * 
	 * @param x
	 *            x position on chessboard
	 * @param y
	 *            y position on chessboard
	 * @return reference to searched square
	 */
	public Field getSquare(int x, int y)
	{
		if((x > this.get_height()) || (y > this.get_widht())) // test if click
		                                                      // is out of
		                                                      // chessboard
		{
			log.log(Level.FINE, "Clicked outside of the chessboard");
			return null;
		}
		if(this.settings.isLabelRenderingEnabled())
		{
			x -= this.upDownLabel.getHeight(null);
			y -= this.upDownLabel.getHeight(null);
		}
		double square_x = x / square_height;// count which field in X was
		                                    // clicked
		double square_y = y / square_height;// count which field in Y was
		                                    // clicked
		
		if(square_x > (int) square_x) // if X is more than X parsed to Integer
		{
			square_x = (int) square_x + 1;// parse to integer and increment
		}
		if(square_y > (int) square_y) // if X is more than X parsed to Integer
		{
			square_y = (int) square_y + 1;// parse to integer and increment
		}
		// Square newActiveSquare =
		// board.getField((int)square_x-1, (int)square_y-1);//4test
		log.log(Level.FINE, "Square X: " + square_x + " | Square Y: " + square_y);
		try
		{
			return board.getField((int) square_x - 1, (int) square_y - 1);
		} catch(java.lang.ArrayIndexOutOfBoundsException exc)
		{
			log.log(Level.SEVERE, "Accessed square outside of field!", exc);
			return null;
		}
	}
	
	/**
	 * Method selecting piece in chessboard
	 * 
	 * @param sq
	 *            square to select (when clicked))
	 */
	public void select(Field sq)
	{
		this.setActiveSquare(sq);
		this.active_x_square = sq.getPosX() + 1;
		this.active_y_square = sq.getPosY() + 1;
		
		// this.draw();//redraw
		log.log(Level.FINE, "Active X: " + active_x_square + " | Active Y: " + active_y_square);
		repaint();
		
	}/*--endOf-select--*/
	
	/**
	 * Method set variables active_x_square & active_y_square to 0 values.
	 */
	public void unselect()
	{
		this.active_x_square = 0;
		this.active_y_square = 0;
		this.setActiveSquare(null);
		// this.draw();//redraw
		repaint();
	}/*--endOf-unselect--*/
	
	public int get_widht()
	{
		return this.get_widht(false);
	}
	
	public int get_height()
	{
		return this.get_height(false);
	}
	
	public int get_widht(boolean includeLables)
	{
		return this.getHeight();
	}/*--endOf-get_widht--*/
	
	public int get_height(boolean includeLabels)
	{
		if(this.settings.isLabelRenderingEnabled())
		{
			return Chessboard.image.getHeight(null) + upDownLabel.getHeight(null);
		}
		return Chessboard.image.getHeight(null);
	}/*--endOf-get_height--*/
	
	public int get_square_height()
	{
		int result = (int) this.square_height;
		return result;
	}
	
	public void move(Field begin, Field end)
	{
		move(begin, end, true);
	}
	
	/**
	 * Method to move piece over chessboard
	 * 
	 * @param xFrom
	 *            from which x move piece
	 * @param yFrom
	 *            from which y move piece
	 * @param xTo
	 *            to which x move piece
	 * @param yTo
	 *            to which y move piece
	 */
	public void move(int xFrom, int yFrom, int xTo, int yTo)
	{
		try
		{
			this.move(board.getField(xFrom, yFrom), board.getField(xTo, yTo), true);
		} catch(java.lang.IndexOutOfBoundsException exc)
		{
			log.log(Level.SEVERE, "Tried to move piece outside of field!", exc);
			return;
		}
	}
	
	public void move(Field begin, Field end, boolean refresh)
	{
		this.move(begin, end, refresh, true);
	}
	
	/**
	 * Method move piece from square to square
	 * 
	 * @param begin
	 *            square from which move piece
	 * @param end
	 *            square where we want to move piece *
	 * @param refresh
	 *            chessboard, default: true
	 */
	public void move(Field begin, Field end, boolean refresh, boolean clearForwardHistory)
	{
		
		CastlingType wasCastling = Moves.CastlingType.NONE;
		Piece promotedPiece = null;
		boolean wasEnPassant = false;
		if(end.getPiece() != null)
		{
			end.getPiece().square = null;
		}
		
		Field tempBegin = new Field(begin);// 4 moves history
		Field tempEnd = new Field(end); // 4 moves history
		// for undo
		// ---
		
		begin.getPiece().square = end;// set square of piece to ending
		end.setPiece(begin.getPiece());// for ending square set piece from
		                               // beginin square
		begin.setPiece(null);// make null piece for begining square
		
		if(end.getPiece().name.equals("King"))
		{
			if(!((King) end.getPiece()).wasMotion)
			{
				((King) end.getPiece()).wasMotion = true;
			}
			
			// Castling
			if(begin.getPosX() + 2 == end.getPosX())
			{
				move(board.getField(7, begin.getPosY()), board.getField(end.getPosY() - 1, begin.getPosY()), false, false);
				wasCastling = Moves.CastlingType.SHORT_CASTLING;
				// this.moves_history.addMove(tempBegin, tempEnd,
				// clearForwardHistory, wasCastling, wasEnPassant);
				// return;
			} else if(begin.getPosX() - 2 == end.getPosX())
			{
				move(board.getField(0, begin.getPosY()), board.getField(end.getPosY() + 1, begin.getPosY()), false, false);
				wasCastling = Moves.CastlingType.LONG_CASTLING;
				// this.moves_history.addMove(tempBegin, tempEnd,
				// clearForwardHistory, wasCastling, wasEnPassant);
				// return;
			}
			// endOf Castling
		} else if(end.getPiece().name.equals("Rook"))
		{
			if(!((Rook) end.getPiece()).wasMotion)
			{
				((Rook) end.getPiece()).wasMotion = true;
			}
		} else if(end.getPiece().name.equals("Pawn"))
		{
			if(this.twoSquareMovedPawn != null
			        && board.getField(end.getPosX(), begin.getPosY()) == this.twoSquareMovedPawn.square) // en passant
			{
				// Ugly hack - put taken pawn in en passant plasty do end square
				tempEnd.setPiece(board.getPiece(board.getField(end.getPosX(), begin.getPosY())));
				board.getField(end.getPosX(), begin.getPosY()).setPiece(null);
				wasEnPassant = true;
			}
			
			if(begin.getPosY() - end.getPosY() == 2 || end.getPosY() - begin.getPosY() == 2) // moved
			                                                                                 // two
			                                                                                 // square
			{
				this.twoSquareMovedPawn = (Pawn) end.getPiece();
			} else
			{
				this.twoSquareMovedPawn = null; // erase last saved move (for En
				                                // passant)
			}
			
			if(end.getPiece().square.getPosY() == 0 || end.getPiece().square.getPosY() == 7) // promote
			                                                                                 // Pawn
			{
				if(clearForwardHistory)
				{
					String color;
					if(end.getPiece().player.getColor() == Player.Color.WHITE)
					{
						color = "W"; // promotionWindow was show with pieces in
						             // this color
					} else
					{
						color = "B";
					}
					
					String newPiece = JChessApp.jcv.showPawnPromotionBox(color); // return
					                                                             // name
					                                                             // of
					                                                             // new
					                                                             // piece
					
					if(newPiece.equals("Queen")) // transform pawn to queen
					{
						Queen queen = new Queen(this, end.getPiece().player);
						queen.chessboard = end.getPiece().chessboard;
						queen.player = end.getPiece().player;
						queen.square = end.getPiece().square;
						end.setPiece(queen);
					} else if(newPiece.equals("Rook")) // transform pawn to rook
					{
						Rook rook = new Rook(this, end.getPiece().player);
						rook.chessboard = end.getPiece().chessboard;
						rook.player = end.getPiece().player;
						rook.square = end.getPiece().square;
						end.setPiece(rook);
					} else if(newPiece.equals("Bishop")) // transform pawn to
					                                     // bishop
					{
						Bishop bishop = new Bishop(this, end.getPiece().player);
						bishop.chessboard = end.getPiece().chessboard;
						bishop.player = end.getPiece().player;
						bishop.square = end.getPiece().square;
						end.setPiece(bishop);
					} else // transform pawn to knight
					{
						Knight knight = new Knight(this, end.getPiece().player);
						knight.chessboard = end.getPiece().chessboard;
						knight.player = end.getPiece().player;
						knight.square = end.getPiece().square;
						end.setPiece(knight);
					}
					promotedPiece = end.getPiece();
				}
			}
		} else if(!end.getPiece().name.equals("Pawn"))
		{
			this.twoSquareMovedPawn = null; // erase last saved move (for En
			                                // passant)
		}
		// }
		
		if(refresh)
		{
			this.unselect();// unselect square
			repaint();
		}
		
		if(clearForwardHistory)
		{
			this.moves_history.clearMoveForwardStack();
			this.moves_history.addMove(tempBegin, tempEnd, true, wasCastling, wasEnPassant, promotedPiece);
		} else
		{
			this.moves_history.addMove(tempBegin, tempEnd, false, wasCastling, wasEnPassant, promotedPiece);
		}
	}/* endOf-move()- */
	
	public boolean redo()
	{
		return redo(true);
	}
	
	public boolean redo(boolean refresh)
	{
		if(this.settings.getGameType() == Settings.GameType.LOCAL) // redo only
		                                                           // for local
		                                                           // game
		{
			Move first = this.moves_history.redo();
			
			Field from = null;
			Field to = null;
			
			if(first != null)
			{
				from = first.getFrom();
				to = first.getTo();
				
				this.move(board.getField(from.getPosX(), from.getPosY()), board.getField(to.getPosX(), to.getPosY()), true, false);
				if(first.getPromotedPiece() != null)
				{
					Pawn pawn = (Pawn) board.getField(to.getPosX(), to.getPosY()).getPiece();
					pawn.square = null;
					
					board.getField(to.getPosX(), to.getPosY()).setPiece(first.getPromotedPiece());
					Piece promoted = board.getField(to.getPosX(), to.getPosY()).getPiece();
					promoted.square = board.getField(to.getPosX(), to.getPosY());
				}
				return true;
			}
			
		}
		return false;
	}
	
	public boolean undo()
	{
		return undo(true);
	}
	
	public synchronized boolean undo(boolean refresh) // undo last move
	{
		Move last = this.moves_history.undo();
		
		if(last != null && last.getFrom() != null)
		{
			Field begin = last.getFrom();
			Field end = last.getTo();
			try
			{
				Piece moved = last.getMovedPiece();
				board.getField(begin.getPosX(), begin.getPosY()).setPiece(moved);
				
				moved.square = board.getField(begin.getPosX(), begin.getPosY());
				
				Piece taken = last.getTakenPiece();
				if(last.getCastlingMove() != CastlingType.NONE)
				{
					Piece rook = null;
					if(last.getCastlingMove() == CastlingType.SHORT_CASTLING)
					{
						rook = board.getField(end.getPosX() - 1, end.getPosY()).getPiece();
						board.getField(7, begin.getPosY()).setPiece(rook);
						rook.square = board.getField(7, begin.getPosY());
						board.getField(end.getPosX() - 1, end.getPosY()).setPiece(null);
					} else
					{
						rook = board.getField(end.getPosX() + 1, end.getPosY()).getPiece();
						board.getField(0, begin.getPosY()).setPiece(rook);
						rook.square = board.getField(0, begin.getPosY());
						board.getField(end.getPosX() + 1, end.getPosY()).setPiece(null);
					}
					((King) moved).wasMotion = false;
					((Rook) rook).wasMotion = false;
				} else if(moved.name.equals("Rook"))
				{
					((Rook) moved).wasMotion = false;
				} else if(moved.name.equals("Pawn") && last.wasEnPassant())
				{
					Pawn pawn = (Pawn) last.getTakenPiece();
					board.getField(end.getPosX(), begin.getPosY()).setPiece(pawn);
					pawn.square = board.getField(end.getPosX(), begin.getPosY());
					
				} else if(moved.name.equals("Pawn") && last.getPromotedPiece() != null)
				{
					Piece promoted = board.getField(end.getPosX(), end.getPosY()).getPiece();
					promoted.square = null;
					board.getField(end.getPosX(), end.getPosY()).setPiece(null);
				}
				
				// check one more move back for en passant
				Move oneMoveEarlier = this.moves_history.getLastMoveFromHistory();
				if(oneMoveEarlier != null && oneMoveEarlier.wasPawnTwoFieldsMove())
				{
					Piece canBeTakenEnPassant = board.getField(oneMoveEarlier.getTo().getPosX(), oneMoveEarlier.getTo().getPosY()).getPiece();
					if(canBeTakenEnPassant.name.equals("Pawn"))
					{
						this.twoSquareMovedPawn = (Pawn) canBeTakenEnPassant;
					}
				}
				
				if(taken != null && !last.wasEnPassant())
				{
					board.getField(end.getPosX(), end.getPosY()).setPiece(taken);
					taken.square = board.getField(end.getPosX(), end.getPosY());
				} else
				{
					board.getField(end.getPosX(), end.getPosY()).setPiece(null);
				}
				
				if(refresh)
				{
					this.unselect();// unselect square
					repaint();
				}
				
			} catch(java.lang.ArrayIndexOutOfBoundsException exc)
			{
				return false;
			} catch(java.lang.NullPointerException exc)
			{
				return false;
			}
			
			return true;
		} else
		{
			return false;
		}
	}
	
	public ChessboardModel getBoard() {
		return board;
	}
	
	/**
	 * Method to draw Chessboard and their elements (pieces etc.)
	 * 
	 * @deprecated
	 */
	public void draw()
	{
		this.getGraphics().drawImage(image, this.getTopLeftPoint().x, this.getTopLeftPoint().y, null);// draw
		                                                                                              // an
		                                                                                              // Image
		                                                                                              // of
		                                                                                              // chessboard
		this.drawLabels();
		this.repaint();
	}/*--endOf-draw--*/
	
	/**
	 * Annotations to superclass Game updateing and painting the crossboard
	 */
	@Override
	public void update(Graphics g)
	{
		repaint();
	}
	
	public Point getTopLeftPoint()
	{
		if(this.settings.isLabelRenderingEnabled())
		{
			return new Point(this.topLeft.x + this.upDownLabel.getHeight(null),
			        this.topLeft.y + this.upDownLabel.getHeight(null));
		}
		return this.topLeft;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Point topLeftPoint = this.getTopLeftPoint();
		if(this.settings.isLabelRenderingEnabled())
		{
			if(topLeftPoint.x <= 0 && topLeftPoint.y <= 0) // if renderLabels
			                                               // and (0,0), than
			                                               // draw it! (for
			                                               // first run)
			{
				this.drawLabels();
			}
			g2d.drawImage(this.upDownLabel, 0, 0, null);
			g2d.drawImage(this.upDownLabel, 0, Chessboard.image.getHeight(null) + topLeftPoint.y, null);
			g2d.drawImage(this.LeftRightLabel, 0, 0, null);
			g2d.drawImage(this.LeftRightLabel, Chessboard.image.getHeight(null) + topLeftPoint.x, 0, null);
		}
		g2d.drawImage(image, topLeftPoint.x, topLeftPoint.y, null);// draw an
		                                                           // Image of
		                                                           // chessboard
		for(int i = 0; i < 8; i++) // drawPiecesOnSquares
		{
			for(int y = 0; y < 8; y++)
			{
				if(board.getField(i, y).getPiece() != null)
				{
					board.getField(i, y).getPiece().draw(g);// draw image of Piece
				}
			}
		} // --endOf--drawPiecesOnSquares
		if((this.active_x_square != 0) && (this.active_y_square != 0)) // if
		                                                               // some
		                                                               // square
		                                                               // is
		                                                               // active
		{
			g2d.drawImage(sel_square, ((this.active_x_square - 1) * (int) square_height) + topLeftPoint.x,
			        ((this.active_y_square - 1) * (int) square_height) + topLeftPoint.y, null);// draw
			                                                                                   // image
			                                                                                   // of
			                                                                                   // selected
			                                                                                   // square
			Field tmpSquare = board.getField((int) (this.active_x_square - 1), (int) (this.active_y_square - 1));
			if(tmpSquare.getPiece() != null)
			{
				this.moves = board.getField((int) (this.active_x_square - 1), (int) (this.active_y_square - 1)).getPiece()
				        .possibleMoves();
			}
			
			for(Iterator<Field> it = moves.iterator(); moves != null && it.hasNext();)
			{
				Field sq = (Field) it.next();
				g2d.drawImage(able_square, (sq.getPosX() * (int) square_height) + topLeftPoint.x,
				        (sq.getPosY() * (int) square_height) + topLeftPoint.y, null);
			}
		}
	}/*--endOf-paint--*/
	
	public void resizeChessboard(int height)
	{
		BufferedImage resized = new BufferedImage(height, height, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g = resized.createGraphics();
		g.drawImage(Chessboard.ORG_IMAGE, 0, 0, height, height, null);
		g.dispose();
		Chessboard.image = resized.getScaledInstance(height, height, 0);
		this.square_height = (float) (height / 8);
		if(this.settings.isLabelRenderingEnabled())
		{
			height += 2 * (this.upDownLabel.getHeight(null));
		}
		this.setSize(height, height);
		
		resized = new BufferedImage((int) square_height, (int) square_height, BufferedImage.TYPE_INT_ARGB_PRE);
		g = resized.createGraphics();
		g.drawImage(Chessboard.ORG_ARLE_IMAGE, 0, 0, (int) square_height, (int) square_height, null);
		g.dispose();
		Chessboard.able_square = resized.getScaledInstance((int) square_height, (int) square_height, 0);
		
		resized = new BufferedImage((int) square_height, (int) square_height, BufferedImage.TYPE_INT_ARGB_PRE);
		g = resized.createGraphics();
		g.drawImage(Chessboard.ORG_SEL_IMAGE, 0, 0, (int) square_height, (int) square_height, null);
		g.dispose();
		Chessboard.sel_square = resized.getScaledInstance((int) square_height, (int) square_height, 0);
		this.drawLabels();
	}
	
	protected void drawLabels()
	{
		this.drawLabels((int) this.square_height);
	}
	
	protected final void drawLabels(int square_height)
	{
		// BufferedImage uDL = new BufferedImage(800, 800,
		// BufferedImage.TYPE_3BYTE_BGR);
		int min_label_height = 20;
		int labelHeight = (int) Math.ceil(square_height / 4);
		labelHeight = (labelHeight < min_label_height) ? min_label_height : labelHeight;
		int labelWidth = (int) Math.ceil(square_height * 8 + (2 * labelHeight));
		BufferedImage uDL = new BufferedImage(labelWidth + min_label_height, labelHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D uDL2D = (Graphics2D) uDL.createGraphics();
		uDL2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		uDL2D.setColor(Color.white);
		
		uDL2D.fillRect(0, 0, labelWidth + min_label_height, labelHeight);
		uDL2D.setColor(Color.black);
		uDL2D.setFont(new Font("Arial", Font.BOLD, 12));
		int addX = (square_height / 2);
		if(this.settings.isLabelRenderingEnabled())
		{
			addX += labelHeight;
		}
		
		String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h" };
		if(!this.settings.isUpsideDown())
		{
			for(int i = 1; i <= letters.length; i++)
			{
				uDL2D.drawString(letters[i - 1], (square_height * (i - 1)) + addX, 10 + (labelHeight / 3));
			}
		} else
		{
			int j = 1;
			for(int i = letters.length; i > 0; i--, j++)
			{
				uDL2D.drawString(letters[i - 1], (square_height * (j - 1)) + addX, 10 + (labelHeight / 3));
			}
		}
		uDL2D.dispose();
		this.upDownLabel = uDL;
		
		uDL = new BufferedImage(labelHeight, labelWidth + min_label_height, BufferedImage.TYPE_3BYTE_BGR);
		uDL2D = (Graphics2D) uDL.createGraphics();
		uDL2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		uDL2D.setColor(Color.white);
		// uDL2D.fillRect(0, 0, 800, 800);
		uDL2D.fillRect(0, 0, labelHeight, labelWidth + min_label_height);
		uDL2D.setColor(Color.black);
		uDL2D.setFont(new Font("Arial", Font.BOLD, 12));
		
		if(this.settings.isUpsideDown())
		{
			for(int i = 1; i <= 8; i++)
			{
				uDL2D.drawString(new Integer(i).toString(), 3 + (labelHeight / 3), (square_height * (i - 1)) + addX);
			}
		} else
		{
			int j = 1;
			for(int i = 8; i > 0; i--, j++)
			{
				uDL2D.drawString(new Integer(i).toString(), 3 + (labelHeight / 3), (square_height * (j - 1)) + addX);
			}
		}
		uDL2D.dispose();
		this.LeftRightLabel = uDL;
	}
	
	public void componentMoved(ComponentEvent e)
	{
		// throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void componentShown(ComponentEvent e)
	{
		// throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void componentHidden(ComponentEvent e)
	{
		// throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public Field getActiveSquare()
	{
		return activeSquare;
	}
	
	public void setActiveSquare(Field activeSquare)
	{
		this.activeSquare = activeSquare;
	}
	
	public King getWhiteKing()
	{
		return kingWhite;
	}
	
	public King setWhiteKing(King kingWhite)
	{
		this.kingWhite = kingWhite;
		return kingWhite;
	}
	
	public King getBlackKing()
	{
		return kingBlack;
	}
	
	public King setBlackKing(King kingBlack)
	{
		this.kingBlack = kingBlack;
		return kingBlack;
	}
	
	public Pawn getTwoSquareMovedPawn()
	{
		return twoSquareMovedPawn;
	}
}
