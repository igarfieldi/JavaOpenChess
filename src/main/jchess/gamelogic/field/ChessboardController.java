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

import java.util.logging.Level;
import java.util.logging.Logger;

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

/**
 * Class to represent chessboard. Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class ChessboardController
{
	private static Logger log = Logger.getLogger(ChessboardController.class.getName());
	
	private ChessboardModel board = new ChessboardModel();
	private ChessboardView view;
	
	public static final int TOP = 0;
	public static final int BOTTOM = 7;
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
	public ChessboardController(Settings settings, Moves moves_history)
	{
		this.view = new ChessboardView(settings, board);
		this.settings = settings;
		this.moves_history = moves_history;
	}
	
	public ChessboardView getView() {
		return view;
	}
	
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
			end.getPiece().setSquare(null);
		}
		
		Field tempBegin = new Field(begin);// 4 moves history
		Field tempEnd = new Field(end); // 4 moves history
		// for undo
		// ---
		
		begin.getPiece().setSquare(end);// set square of piece to ending
		end.setPiece(begin.getPiece());// for ending square set piece from
		                               // beginin square
		begin.setPiece(null);// make null piece for begining square
		
		if(end.getPiece().getName().equals("King"))
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
		} else if(end.getPiece().getName().equals("Rook"))
		{
			if(!((Rook) end.getPiece()).wasMotion)
			{
				((Rook) end.getPiece()).wasMotion = true;
			}
		} else if(end.getPiece().getName().equals("Pawn"))
		{
			if(this.twoSquareMovedPawn != null
			        && board.getField(end.getPosX(), begin.getPosY()) == this.twoSquareMovedPawn.getSquare()) // en passant
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
			
			if(end.getPiece().getSquare().getPosY() == 0 || end.getPiece().getSquare().getPosY() == 7) // promote
			                                                                                 // Pawn
			{
				if(clearForwardHistory)
				{
					String color;
					if(end.getPiece().getPlayer().getColor() == Player.Color.WHITE)
					{
						color = "W"; // promotionWindow was show with pieces in
						             // this color
					} else
					{
						color = "B";
					}
					
					String newPiece = JChessApp.view.showPawnPromotionBox(color); // return
					                                                             // name
					                                                             // of
					                                                             // new
					                                                             // piece
					
					if(newPiece.equals("Queen")) // transform pawn to queen
					{
						Queen queen = new Queen(this, end.getPiece().getPlayer());
						queen.setSquare(end.getPiece().getSquare());
						end.setPiece(queen);
					} else if(newPiece.equals("Rook")) // transform pawn to rook
					{
						Rook rook = new Rook(this, end.getPiece().getPlayer());
						rook.setSquare(end.getPiece().getSquare());
						end.setPiece(rook);
					} else if(newPiece.equals("Bishop")) // transform pawn to
					                                     // bishop
					{
						Bishop bishop = new Bishop(this, end.getPiece().getPlayer());
						bishop.setSquare(end.getPiece().getSquare());
						end.setPiece(bishop);
					} else // transform pawn to knight
					{
						Knight knight = new Knight(this, end.getPiece().getPlayer());
						knight.setSquare(end.getPiece().getSquare());
						end.setPiece(knight);
					}
					promotedPiece = end.getPiece();
				}
			}
		} else if(!end.getPiece().getName().equals("Pawn"))
		{
			this.twoSquareMovedPawn = null; // erase last saved move (for En
			                                // passant)
		}
		// }
		
		if(refresh)
		{
			view.unselect();// unselect square
			view.repaint();
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
					pawn.setSquare(null);
					
					board.getField(to.getPosX(), to.getPosY()).setPiece(first.getPromotedPiece());
					Piece promoted = board.getField(to.getPosX(), to.getPosY()).getPiece();
					promoted.setSquare(board.getField(to.getPosX(), to.getPosY()));
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
				
				moved.setSquare(board.getField(begin.getPosX(), begin.getPosY()));
				
				Piece taken = last.getTakenPiece();
				if(last.getCastlingMove() != CastlingType.NONE)
				{
					Piece rook = null;
					if(last.getCastlingMove() == CastlingType.SHORT_CASTLING)
					{
						rook = board.getField(end.getPosX() - 1, end.getPosY()).getPiece();
						board.getField(7, begin.getPosY()).setPiece(rook);
						rook.setSquare(board.getField(7, begin.getPosY()));
						board.getField(end.getPosX() - 1, end.getPosY()).setPiece(null);
					} else
					{
						rook = board.getField(end.getPosX() + 1, end.getPosY()).getPiece();
						board.getField(0, begin.getPosY()).setPiece(rook);
						rook.setSquare(board.getField(0, begin.getPosY()));
						board.getField(end.getPosX() + 1, end.getPosY()).setPiece(null);
					}
					((King) moved).wasMotion = false;
					((Rook) rook).wasMotion = false;
				} else if(moved.getName().equals("Rook"))
				{
					((Rook) moved).wasMotion = false;
				} else if(moved.getName().equals("Pawn") && last.wasEnPassant())
				{
					Pawn pawn = (Pawn) last.getTakenPiece();
					board.getField(end.getPosX(), begin.getPosY()).setPiece(pawn);
					pawn.setSquare(board.getField(end.getPosX(), begin.getPosY()));
					
				} else if(moved.getName().equals("Pawn") && last.getPromotedPiece() != null)
				{
					Piece promoted = board.getField(end.getPosX(), end.getPosY()).getPiece();
					promoted.setSquare(null);
					board.getField(end.getPosX(), end.getPosY()).setPiece(null);
				}
				
				// check one more move back for en passant
				Move oneMoveEarlier = this.moves_history.getLastMoveFromHistory();
				if(oneMoveEarlier != null && oneMoveEarlier.wasPawnTwoFieldsMove())
				{
					Piece canBeTakenEnPassant = board.getField(oneMoveEarlier.getTo().getPosX(), oneMoveEarlier.getTo().getPosY()).getPiece();
					if(canBeTakenEnPassant.getName().equals("Pawn"))
					{
						this.twoSquareMovedPawn = (Pawn) canBeTakenEnPassant;
					}
				}
				
				if(taken != null && !last.wasEnPassant())
				{
					board.getField(end.getPosX(), end.getPosY()).setPiece(taken);
					taken.setSquare(board.getField(end.getPosX(), end.getPosY()));
				} else
				{
					board.getField(end.getPosX(), end.getPosY()).setPiece(null);
				}
				
				if(refresh)
				{
					view.unselect();// unselect square
					view.repaint();
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
