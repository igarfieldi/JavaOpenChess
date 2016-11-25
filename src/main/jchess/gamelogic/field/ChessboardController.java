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

import java.util.Arrays;
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
import jchess.util.ArgumentChecker;

/**
 * Class to represent chessboard. Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class ChessboardController
{
	private static Logger log = Logger.getLogger(ChessboardController.class.getName());
	private static final int WIDTH = 8;
	private static final int HEIGHT = 8;
	private static final String[] FIELD_LETTERS = {"a", "b", "c", "d", "e", "f", "g", "h"};
	private static final String[] FIELD_NUMBERS = {"1", "2", "3", "4", "5", "6", "7", "8"};
	
	private ChessboardModel board;
	private ChessboardView view;
	
	private Player white;
	private Player black;
	private Player activePlayer;
	
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
		this.board = new ChessboardModel(WIDTH, HEIGHT);
		this.view = new ChessboardView(settings, board);
		this.settings = settings;
		this.moves_history = moves_history;
	}
	
	public ChessboardView getView() {
		return view;
	}
	
	public Player getActivePlayer() {
		return this.activePlayer;
	}
	
	public void switchToNextPlayer() {
		if(this.activePlayer == this.white) {
			this.activePlayer = this.black;
		} else {
			this.activePlayer = this.white;
		}
	}
	
	public void switchToPreviousPlayer() {
		// Since we only have two players, this is equal to switchToNextPlayer
		this.switchToNextPlayer();
	}
	
	public boolean isOnAPlayersBaseline(Field field) {
		ArgumentChecker.checkForNull(field);
		
		if(field.getPosY() == 0) {
			// Lower player's baseline
			return true;
		} else if(field.getPosY() + 1 == HEIGHT) {
			// Upper player's baseline
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the string representation of the given field.
	 * @param field
	 * @return
	 */
	public String getFieldDesignation(Field field) {
		ArgumentChecker.checkForNull(field);
		
		if(settings.isUpsideDown()) {
			// In upside-down case, field (0|0) is equivalent to h1
			return FIELD_LETTERS[field.getPosX()] + FIELD_NUMBERS[HEIGHT - field.getPosY() - 1];
		} else {
			// If not upside down, field (0|0) is equivalent to a8
			return FIELD_LETTERS[WIDTH - field.getPosX() - 1] + FIELD_NUMBERS[field.getPosY()];
		}
	}
	
	/**
	 * Returns the field specified by the given string, if applicable.
	 * @param designation Designation of desired field
	 * @return field denoted by given string (or null if non-existent)
	 */
	public Field getFieldFromDesignation(String designation) {
		ArgumentChecker.checkForNull(designation);
		
		if(designation.length() != 2) {
			return null;
		}
		
		// Check the arrays of letters and numbers for the given strings
		int x = Arrays.asList(FIELD_LETTERS).indexOf(""+designation.charAt(0));
		int y = Arrays.asList(FIELD_NUMBERS).indexOf(""+designation.charAt(1));
		
		// Account for the oddities of the chosen field coordinate system (0|0 = a8 or h1)
		if(settings.isUpsideDown()) {
			y = HEIGHT - y - 1;
		} else {
			x = WIDTH - x - 1;
		}
		
		return board.getField(x, y);
	}
	
	/**
	 * Brings the controller (and the associated board) into a clean state.
	 */
	public void initialize() {
		this.initializePlayers();
		this.initializePieces(this.white.isTopSide());
		
		this.activePlayer = white;
	}
	
	/**
	 * Initializes the participating chess players.
	 * Currently, we simply grab the players stored in the settings.
	 */
	private void initializePlayers() {
		this.white = settings.getWhitePlayer();
		this.black = settings.getBlackPlayer();
		
		if(settings.isUpsideDown()) {
			this.white.setBoardSide(true);
		} else {
			this.black.setBoardSide(true);
		}
	}
	
	/**
	 * Initializes the chessboard's pieces.
	 * This includes checking which player is on top etc.
	 * @param whiteIsTop is white playing from top-side
	 */
	private void initializePieces(boolean whiteIsTop) {
		Player topSide = this.black;
		Player bottomSide = this.white;
		if(whiteIsTop) {
			topSide = this.white;
			bottomSide = this.black;
		}
		
		System.out.println(topSide);
		
		board.initialize();
		// Set rooks, bishops, knights
		board.getField(0, 7).setPiece(new Rook(this, bottomSide));
		board.getField(7, 7).setPiece(new Rook(this, bottomSide));
		board.getField(1, 7).setPiece(new Bishop(this, bottomSide));
		board.getField(6, 7).setPiece(new Bishop(this, bottomSide));
		board.getField(2, 7).setPiece(new Knight(this, bottomSide));
		board.getField(5, 7).setPiece(new Knight(this, bottomSide));
		board.getField(0, 0).setPiece(new Rook(this, topSide));
		board.getField(7, 0).setPiece(new Rook(this, topSide));
		board.getField(1, 0).setPiece(new Bishop(this, topSide));
		board.getField(6, 0).setPiece(new Bishop(this, topSide));
		board.getField(2, 0).setPiece(new Knight(this, topSide));
		board.getField(5, 0).setPiece(new Knight(this, topSide));
		
		// Since the queen is always placed on the field of her own color, we need
		// to check on which side white is playing
		if(whiteIsTop) {
			board.getField(4, 7).setPiece(new Queen(this, bottomSide));
			board.getField(3, 7).setPiece(setBlackKing(new King(this, bottomSide)));
			board.getField(4, 0).setPiece(new Queen(this, topSide));
			board.getField(3, 0).setPiece(setWhiteKing(new King(this, topSide)));
		} else {
			board.getField(3, 7).setPiece(new Queen(this, bottomSide));
			board.getField(4, 7).setPiece(setWhiteKing(new King(this, bottomSide)));
			board.getField(3, 0).setPiece(new Queen(this, topSide));
			board.getField(4, 0).setPiece(setBlackKing(new King(this, topSide)));
		}
		
		// Initialize pawns: no special distinctions necessary
		for(int x = 0; x < WIDTH; x++) {
			board.getField(x, 6).setPiece(new Pawn(this, bottomSide));
			board.getField(x, 1).setPiece(new Pawn(this, topSide));
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
