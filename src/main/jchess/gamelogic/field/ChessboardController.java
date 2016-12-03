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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import jchess.gamelogic.views.IChessboardView;
import jchess.util.ArgumentChecker;
import jchess.util.Direction;

/**
 * Class to represent chessboard. Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class ChessboardController implements IChessboardController
{
	private static final int WIDTH = 8;
	private static final int HEIGHT = 8;
	private static final String[] FIELD_LETTERS = { "a", "b", "c", "d", "e", "f", "g", "h" };
	private static final String[] FIELD_NUMBERS = { "1", "2", "3", "4", "5", "6", "7", "8" };
	
	private IChessboardModel board;
	private IChessboardView view;
	
	private Player whitePlayer;
	private Player blackPlayer;
	private Player activePlayer;
	
	private Settings settings;
	// For En passant:
	// |-> Pawn whose in last turn moved two square
	private Pawn twoSquareMovedPawn = null;
	private Moves movesHistory;
	
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
		this.settings = settings;
		this.movesHistory = moves_history;
	}
	
	public void setView(IChessboardView view) {
		this.view = view;
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#getView()
	 */
	@Override
	public IChessboardView getView()
	{
		return view;
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#getActivePlayer()
	 */
	@Override
	public Player getActivePlayer()
	{
		return this.activePlayer;
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#switchToNextPlayer()
	 */
	@Override
	public void switchToNextPlayer()
	{
		if(this.activePlayer == this.whitePlayer)
		{
			this.activePlayer = this.blackPlayer;
		} else
		{
			this.activePlayer = this.whitePlayer;
		}
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#switchToPreviousPlayer()
	 */
	@Override
	public void switchToPreviousPlayer()
	{
		// Since we only have two players, this is equal to switchToNextPlayer
		this.switchToNextPlayer();
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#getFieldDesignation(jchess.gamelogic.field.Field)
	 */
	@Override
	public String getFieldDesignation(Field field)
	{
		ArgumentChecker.checkForNull(field);
		
		if(settings.isUpsideDown())
		{
			// In upside-down case, field (0|0) is equivalent to h1
			return FIELD_LETTERS[field.getPosX()] + FIELD_NUMBERS[HEIGHT - field.getPosY() - 1];
		} else
		{
			// If not upside down, field (0|0) is equivalent to a8
			return FIELD_LETTERS[WIDTH - field.getPosX() - 1] + FIELD_NUMBERS[field.getPosY()];
		}
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#getFieldFromDesignation(java.lang.String)
	 */
	@Override
	public Field getFieldFromDesignation(String designation)
	{
		ArgumentChecker.checkForNull(designation);
		
		if(designation.length() != 2)
		{
			return null;
		}
		
		// Check the arrays of letters and numbers for the given strings
		int x = Arrays.asList(FIELD_LETTERS).indexOf("" + designation.charAt(0));
		int y = Arrays.asList(FIELD_NUMBERS).indexOf("" + designation.charAt(1));
		
		// Account for the oddities of the chosen field coordinate system (0|0 =
		// a8 or h1)
		if(settings.isUpsideDown())
		{
			y = HEIGHT - y - 1;
		} else
		{
			x = WIDTH - x - 1;
		}
		
		return board.getField(x, y);
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#initialize()
	 */
	@Override
	public void initialize()
	{
		this.initializePlayers();
		this.initializePieces(this.whitePlayer.isTopSide());
		
		this.activePlayer = whitePlayer;
	}
	
	/**
	 * Initializes the participating chess players. Currently, we simply grab
	 * the players stored in the settings.
	 */
	private void initializePlayers()
	{
		this.whitePlayer = settings.getWhitePlayer();
		this.blackPlayer = settings.getBlackPlayer();
		
		if(settings.isUpsideDown())
		{
			this.whitePlayer.setTopSide(true);
		} else
		{
			this.blackPlayer.setTopSide(true);
		}
	}
	
	/**
	 * Initializes the chessboard's pieces. This includes checking which player
	 * is on top etc.
	 * 
	 * @param whiteIsTop
	 *            is white playing from top-side
	 */
	private void initializePieces(boolean whiteIsTop)
	{
		Player topSide = this.blackPlayer;
		Player bottomSide = this.whitePlayer;
		if(whiteIsTop)
		{
			topSide = this.whitePlayer;
			bottomSide = this.blackPlayer;
		}
		
		board.initialize();
		// Set rooks, bishops, knights
		board.setPiece(board.getField(0, 7), new Rook(this, bottomSide));
		board.setPiece(board.getField(7, 7), new Rook(this, bottomSide));
		board.setPiece(board.getField(1, 7), new Knight(this, bottomSide));
		board.setPiece(board.getField(6, 7), new Knight(this, bottomSide));
		board.setPiece(board.getField(2, 7), new Bishop(this, bottomSide));
		board.setPiece(board.getField(5, 7), new Bishop(this, bottomSide));
		board.setPiece(board.getField(0, 0), new Rook(this, topSide));
		board.setPiece(board.getField(7, 0), new Rook(this, topSide));
		board.setPiece(board.getField(1, 0), new Knight(this, topSide));
		board.setPiece(board.getField(6, 0), new Knight(this, topSide));
		board.setPiece(board.getField(2, 0), new Bishop(this, topSide));
		board.setPiece(board.getField(5, 0), new Bishop(this, topSide));
		
		// Since the queen is always placed on the field of her own color, we
		// need
		// to check on which side white is playing
		if(whiteIsTop)
		{
			board.setPiece(board.getField(4, 7), new Queen(this, bottomSide));
			board.setPiece(board.getField(3, 7), new King(this, bottomSide));
			board.setPiece(board.getField(4, 0), new Queen(this, topSide));
			board.setPiece(board.getField(3, 0), new King(this, topSide));
		} else
		{
			board.setPiece(board.getField(3, 7), new Queen(this, bottomSide));
			board.setPiece(board.getField(4, 7), new King(this, bottomSide));
			board.setPiece(board.getField(3, 0), new Queen(this, topSide));
			board.setPiece(board.getField(4, 0), new King(this, topSide));
		}
		
		// Initialize pawns: no special distinctions necessary
		for(int x = 0; x < WIDTH; x++)
		{
			board.setPiece(board.getField(x, 6), new Pawn(this, bottomSide));
			board.setPiece(board.getField(x, 1), new Pawn(this, topSide));
		}
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#getPossibleMoves(jchess.gamelogic.pieces.Piece, boolean)
	 */
	@Override
	public Set<Field> getPossibleMoves(Piece piece, boolean careForCheck)
	{
		
		Set<Field> reachableFields = new HashSet<Field>();
		
		// Capturing moves only
		reachableFields.addAll(this.getCapturableFields(piece));
		
		// Non-capturing moves only
		reachableFields.addAll(this.getMovableFields(piece));
		
		if(careForCheck)
		{
			// For each move we need to simuate the future board state and see
			// if
			// we're in check.
			
			// Store the part of the state that undo will not restore
			Field activeField = this.getView().getActiveSquare();
			Pawn twoMovedPawn = this.twoSquareMovedPawn;
			
			for(Iterator<Field> fieldIterator = reachableFields.iterator(); fieldIterator.hasNext();)
			{
				// We need to use the iterator instead of foreach to be able to
				// use .remove()
				Field currField = fieldIterator.next();
				// Simulate the board state
				IChessboardModel tempModel = board;
				board = board.copy();
				
				this.move(tempModel.getField(piece), currField, false, false, false);
				if(this.isChecked(piece.getPlayer()))
				{
					// Checked moves are not allowed, so remove it
					fieldIterator.remove();
				}
				// Undo the move again
				board = tempModel;
				this.twoSquareMovedPawn = twoMovedPawn;
			}
			
			this.getView().select(activeField);
		}
		
		return reachableFields;
	}
	
	/**
	 * Returns the set of fields where a piece can move without capturing.
	 * Pieces may not skip fields in a direction. Possibly occuring check
	 * situations are not accounted for; if need be they have to be filtered
	 * out.
	 * 
	 * @param piece
	 *            Piece to get movable fields for
	 * @return Set of fields the piece can move to without capturing
	 */
	private Set<Field> getMovableFields(Piece piece)
	{
		Set<Field> reachableFields = new HashSet<Field>();
		reachableFields.addAll(this.getMovableFieldsInDirection(piece, piece.getNormalMovements()));
		
		// Special case handling
		if(piece instanceof King)
		{
			// Castling
			if(!piece.hasMoved())
			{
				Field kingsField = board.getField(piece);
				
				Piece leftRook = board.getPiece(board.getField(0, kingsField.getPosY()));
				Piece rightRook = board.getPiece(board.getField(7, kingsField.getPosY()));
				
				if(leftRook != null && !leftRook.hasMoved())
				{
					// Neither left rook nor king have moved yet
					Set<Field> reachable = this.getMovableFieldsInDirection(leftRook, leftRook.getNormalMovements());
					if(reachable.contains(board.getField(kingsField.getPosX() - 1, kingsField.getPosY())))
					{
						// Left rook can move right next to the king -> fields
						// in between both are free
						Set<Field> involvedFields = new HashSet<Field>();
						involvedFields.add(kingsField);
						involvedFields.add(board.getField(kingsField.getPosX() - 1, kingsField.getPosY()));
						involvedFields.add(board.getField(kingsField.getPosX() - 2, kingsField.getPosY()));
						// None of the fields involved must be in check
						
						Player enemy = whitePlayer;
						if(piece.getPlayer() == whitePlayer)
						{
							enemy = blackPlayer;
						}
						if(!this.isAnyThreatenedByPlayer(involvedFields, enemy))
						{
							reachableFields.add(board.getField(kingsField.getPosX() - 2, kingsField.getPosY()));
						}
					}
				}
				if(rightRook != null && !rightRook.hasMoved())
				{
					// Neither left rook nor king have moved yet
					Set<Field> reachable = this.getMovableFieldsInDirection(rightRook, rightRook.getNormalMovements());
					if(reachable.contains(board.getField(kingsField.getPosX() + 1, kingsField.getPosY())))
					{
						// Right rook can move right next to the king -> fields
						// in between both are free
						Set<Field> involvedFields = new HashSet<Field>();
						involvedFields.add(kingsField);
						involvedFields.add(board.getField(kingsField.getPosX() + 1, kingsField.getPosY()));
						involvedFields.add(board.getField(kingsField.getPosX() + 2, kingsField.getPosY()));
						// None of the fields involved must be in check
						
						Player enemy = whitePlayer;
						if(piece.getPlayer() == whitePlayer)
						{
							enemy = blackPlayer;
						}
						if(!this.isAnyThreatenedByPlayer(involvedFields, enemy))
						{
							reachableFields.add(board.getField(kingsField.getPosX() + 2, kingsField.getPosY()));
						}
					}
				}
			}
		}
		
		return reachableFields;
	}
	
	/**
	 * Returns the list of all fields in a direction a piece might consider moving to.
	 * This does not include a check for blocking by pieces. The fields are
	 * ordered; fields closer to the piece first.
	 * @param piece
	 * @param dir
	 * @return List of fields in direction
	 */
	private List<Field> getFieldsInDirection(Piece piece, Direction dir)
	{
		List<Field> fieldsInDir = new ArrayList<Field>();
		if(piece.canMoveMultipleSteps())
		{
			// Need to add all fields in direction
			fieldsInDir.addAll(board.getFieldsInDirection(board.getField(piece), dir));
		} else
		{
			// Only a single step possible
			Field fieldInDir = board.getFieldInDirection(board.getField(piece), dir);
			if(fieldInDir != null)
			{
				fieldsInDir.add(fieldInDir);
			}
		}
		return fieldsInDir;
	}
	
	/**
	 * Returns the set of fields capturable by a piece. This only includes
	 * currently occupied fields; those empty but theoretically reachable with a
	 * capturing move are not included. This method ignores possibly invoked
	 * check positions; if need be, they need to be filtered out.
	 * 
	 * @param piece
	 *            Piece to get capturable fields for
	 * @return Set of capturable fields
	 */
	private Set<Field> getCapturableFields(Piece piece)
	{
		Set<Field> capturableFields = new HashSet<Field>();
		
		capturableFields.addAll(this.getCapturableFieldsInDirection(piece, piece.getCapturingMovements()));
		
		// Special case handling
		if(piece instanceof Pawn)
		{
			// En passant
			if(this.twoSquareMovedPawn != null)
			{
				if(board.getField(piece).getPosY() == board.getField(twoSquareMovedPawn).getPosY())
				{
					// Our pawn is in the same row
					if(Math.abs(board.getField(piece).getPosX() - board.getField(twoSquareMovedPawn).getPosX()) == 1)
					{
						// Our pawn is right next to the pawn which moved two
						// squares. Now we need to check the direction in which
						// the pawn has to go
						if(piece.getPlayer().isTopSide())
						{
							capturableFields.add(board.getField(board.getField(twoSquareMovedPawn).getPosX(),
							        board.getField(piece).getPosY() + 1));
						} else
						{
							capturableFields.add(board.getField(board.getField(twoSquareMovedPawn).getPosX(),
							        board.getField(piece).getPosY() - 1));
						}
					}
				}
			}
		}
		
		return capturableFields;
	}
	
	/**
	 * Returns the set of fields a given piece can move to without capturing.
	 * @param piece
	 * @param directions Set of directions to consider
	 * @return Set of fields piece can move to
	 */
	private Set<Field> getMovableFieldsInDirection(Piece piece, Set<Direction> directions)
	{
		Set<Field> movableFields = new HashSet<Field>();
		
		for(Direction dir : directions)
		{
			if(!piece.getPlayer().isTopSide())
			{
				dir = dir.multiply(-1);
			}
			
			for(Field field : this.getFieldsInDirection(piece, dir))
			{
				Piece currPiece = board.getPiece(field);
				if(currPiece != null)
				{
					// If we encountered a piece, we have to stop since we
					// cannot jump over them
					break;
				} else
				{
					// Otherwise we can move to this field
					movableFields.add(field);
				}
			}
		}
		
		return movableFields;
	}
	
	/**
	 * Returns the set of fields which are under threat by the given piece. This
	 * includes any capturable fields.
	 * 
	 * @param piece
	 * @param directions
	 *            Set of directions to check
	 * @return Set of threatened fields
	 */
	private Set<Field> getThreatenedFieldsInDirection(Piece piece, Set<Direction> directions)
	{
		Set<Field> threatenedFields = new HashSet<Field>();
		
		for(Direction dir : directions)
		{
			if(!piece.getPlayer().isTopSide())
			{
				dir = dir.multiply(-1);
			}
			
			for(Field field : this.getFieldsInDirection(piece, dir))
			{
				Piece currPiece = board.getPiece(field);
				
				if(currPiece != null)
				{
					if(currPiece.getPlayer() != piece.getPlayer())
					{
						threatenedFields.add(field);
					}
					break;
				} else
				{
					threatenedFields.add(field);
				}
			}
		}
		
		return threatenedFields;
	}
	
	/**
	 * Returns the set of capturable fields for a given piece.
	 * 
	 * @param piece
	 * @param directions
	 *            Set of directions to check for
	 * @return Set of capturable fields
	 */
	private Set<Field> getCapturableFieldsInDirection(Piece piece, Set<Direction> directions)
	{
		Set<Field> capturableFields = new HashSet<Field>();
		
		for(Field field : this.getThreatenedFieldsInDirection(piece, directions))
		{
			if(board.getPiece(field) != null)
			{
				capturableFields.add(field);
			}
		}
		
		return capturableFields;
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#isChecked(jchess.gamelogic.Player)
	 */
	@Override
	public boolean isChecked(Player player)
	{
		Player enemy = whitePlayer;
		if(whitePlayer == player)
		{
			enemy = blackPlayer;
		}
		
		for(Piece piece : board.getPieces(player))
		{
			if(piece instanceof King)
			{
				return !this.isThreatenedByPlayer(board.getField(piece), enemy).isEmpty();
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#isCheckmated(jchess.gamelogic.Player)
	 */
	@Override
	public boolean isCheckmated(Player player)
	{
		for(Piece piece : board.getPieces(player))
		{
			if(!this.getPossibleMoves(piece, true).isEmpty())
			{
				return false;
			}
		}
		
		return this.isChecked(player);
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#isStalemate()
	 */
	@Override
	public boolean isStalemate()
	{
		for(Piece piece : board.getPieces(this.getActivePlayer()))
		{
			if(!this.getPossibleMoves(piece, true).isEmpty())
			{
				return false;
			}
		}
		
		return !this.isChecked(this.getActivePlayer());
	}
	
	/**
	 * Checks if the given field is currently under threat from the given
	 * player.
	 * 
	 * @param target
	 *            Target field
	 * @param player
	 *            Possibly threatening player
	 * @return Set of pieces currently threatening the field
	 */
	private Set<Piece> isThreatenedByPlayer(Field target, Player player)
	{
		Set<Piece> threateningPieces = new HashSet<Piece>();
		
		for(Piece piece : board.getPieces(player))
		{
			if(this.getThreatenedFieldsInDirection(piece, piece.getCapturingMovements()).contains(target))
			{
				threateningPieces.add(piece);
			}
		}
		
		return threateningPieces;
	}
	
	/**
	 * Checks if any of the given fields are currently under threat from the
	 * given player.
	 * 
	 * @param targets
	 *            Set of fields to check
	 * @param player
	 *            Player who might be threatening
	 * @return true if any of the fields is threatened
	 */
	private boolean isAnyThreatenedByPlayer(Set<Field> targets, Player player)
	{
		for(Field target : targets)
		{
			if(!this.isThreatenedByPlayer(target, player).isEmpty())
			{
				return true;
			}
		}
		return false;
	}
	
	public void move(Field begin, Field end)
	{
		move(begin, end, true);
	}
	
	public void move(Field begin, Field end, boolean refresh)
	{
		this.move(begin, end, refresh, true);
	}
	
	public void move(Field begin, Field end, boolean refresh, boolean clearForwardHistory)
	{
		this.move(begin, end, refresh, clearForwardHistory, true);
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#move(jchess.gamelogic.field.Field, jchess.gamelogic.field.Field, boolean, boolean, boolean)
	 */
	@Override
	public void move(Field begin, Field end, boolean refresh, boolean clearForwardHistory, boolean enterIntoHistory)
	{
		// Standard move
		Move move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.NONE, false, null);
		
		if(board.getPiece(begin) instanceof King)
		{
			// Castling
			if(begin.getPosX() + 2 == end.getPosX())
			{
				move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.SHORT_CASTLING,
				        false, null);
			} else if(begin.getPosX() - 2 == end.getPosX())
			{
				move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.LONG_CASTLING,
				        false, null);
			}
		} else if(board.getPiece(begin) instanceof Pawn)
		{
			// En passant
			if(this.twoSquareMovedPawn != null
			        && board.getField(end.getPosX(), begin.getPosY()).equals(board.getField(this.twoSquareMovedPawn)))
			{
				move = new Move(begin, end, board.getPiece(begin),
				        board.getPiece(board.getField(end.getPosX(), begin.getPosY())), CastlingType.NONE, true, null);
			}
			
			if(begin.getPosY() - end.getPosY() == 2 || end.getPosY() - begin.getPosY() == 2)
			{
				this.twoSquareMovedPawn = (Pawn) board.getPiece(begin);
			} else
			{
				this.twoSquareMovedPawn = null; // erase last saved move
			}
			
			Piece promotedPiece = null;
			if(end.getPosY() == 0 || end.getPosY() == 7) // promote
			// Pawn
			{
				if(clearForwardHistory)
				{
					String color;
					if(board.getPiece(begin).getPlayer().getColor() == Player.Color.WHITE)
					{
						color = "W"; // promotionWindow was show with pieces in
						             // this color
					} else
					{
						color = "B";
					}
					
					// return name of new piece
					String newPiece = JChessApp.view.showPawnPromotionBox(color);
					
					if(newPiece.equals("Queen")) // transform pawn to queen
					{
						Queen queen = new Queen(this, board.getPiece(begin).getPlayer());
						board.setPiece(begin, queen);
					} else if(newPiece.equals("Rook")) // transform pawn to rook
					{
						Rook rook = new Rook(this, board.getPiece(begin).getPlayer());
						board.setPiece(begin, rook);
					} else if(newPiece.equals("Bishop")) // transform pawn to
					                                     // bishop
					{
						Bishop bishop = new Bishop(this, board.getPiece(begin).getPlayer());
						board.setPiece(begin, bishop);
					} else // transform pawn to knight
					{
						Knight knight = new Knight(this, board.getPiece(begin).getPlayer());
						board.setPiece(begin, knight);
					}
					promotedPiece = board.getPiece(begin);
				}
			}
			
			move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.NONE, false,
			        promotedPiece);
		} else
		{
			this.twoSquareMovedPawn = null; // erase last saved move
			move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.NONE, false, null);
		}
		
		board.movePiece(board.getPiece(begin), end);
		board.getPiece(end).markAsMoved();
		
		if(move.getCastlingMove() != CastlingType.NONE)
		{
			// Move the rook
			if(begin.getPosX() + 2 == end.getPosX())
			{
				move(board.getField(7, begin.getPosY()), board.getField(end.getPosX() - 1, begin.getPosY()), false,
				        false);
			} else
			{
				move(board.getField(0, begin.getPosY()), board.getField(end.getPosX() + 1, begin.getPosY()), false,
				        false);
			}
		}
		
		if(refresh)
		{
			view.unselect();// unselect square
			view.render();
		}
		
		if(clearForwardHistory)
		{
			this.movesHistory.clearMoveForwardStack();
		}
		
		if(enterIntoHistory)
		{
			this.movesHistory.addMove(move, clearForwardHistory);
		}
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#redo(boolean)
	 */
	@Override
	public boolean redo()
	{
		// redo only for local game
		if(this.settings.getGameType() == Settings.GameType.LOCAL)
		{
			Move first = this.movesHistory.redo();
			
			Field from = null;
			Field to = null;
			
			if(first != null)
			{
				from = first.getFrom();
				to = first.getTo();
				
				this.move(board.getField(from.getPosX(), from.getPosY()), board.getField(to.getPosX(), to.getPosY()),
				        true, false, true);
				if(first.getPromotedPiece() != null)
				{
					board.setPiece(to, first.getPromotedPiece());
				}
				return true;
			}
			
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#undo(boolean)
	 */
	@Override
	public synchronized boolean undo() // undo last move
	{
		Move last = this.movesHistory.undo();
		
		if(last != null && last.getFrom() != null)
		{
			Field begin = last.getFrom();
			Field end = last.getTo();
			try
			{
				Piece moved = last.getMovedPiece();
				board.setPiece(begin, moved);
				
				Piece taken = last.getTakenPiece();
				if(last.getCastlingMove() != CastlingType.NONE)
				{
					Piece rook = null;
					if(last.getCastlingMove() == CastlingType.SHORT_CASTLING)
					{
						rook = board.getPiece(board.getField(end.getPosX() - 1, end.getPosY()));
						board.setPiece(board.getField(7, begin.getPosY()), rook);
						board.setPiece(board.getField(end.getPosX() - 1, end.getPosY()), null);
					} else
					{
						rook = board.getPiece(board.getField(end.getPosX() + 1, end.getPosY()));
						board.setPiece(board.getField(0, begin.getPosY()), rook);
						board.setPiece(board.getField(end.getPosX() + 1, end.getPosY()), null);
					}
					moved.markAsUnmoved();
					rook.markAsUnmoved();
				} else if(moved.getName().equals("Rook"))
				{
					moved.markAsUnmoved();
				} else if(moved.getName().equals("Pawn") && last.wasEnPassant())
				{
					Pawn pawn = (Pawn) last.getTakenPiece();
					board.setPiece(board.getField(end.getPosX(), begin.getPosY()), pawn);
					
				} else if(moved.getName().equals("Pawn") && last.getPromotedPiece() != null)
				{
					// TODO: wtf does this accomplish?
					board.removePiece(end);
				}
				
				// check one more move back for en passant
				Move oneMoveEarlier = this.movesHistory.getLastMoveFromHistory();
				if(oneMoveEarlier != null && oneMoveEarlier.wasPawnTwoFieldsMove())
				{
					Piece canBeTakenEnPassant = board.getPiece(oneMoveEarlier.getTo());
					if(canBeTakenEnPassant.getName().equals("Pawn"))
					{
						this.twoSquareMovedPawn = (Pawn) canBeTakenEnPassant;
					}
				}
				
				if(taken != null && !last.wasEnPassant())
				{
					board.setPiece(board.getField(end.getPosX(), end.getPosY()), taken);
				} else
				{
					board.setPiece(board.getField(end.getPosX(), end.getPosY()), null);
				}
				
				
				view.unselect();// unselect square
				view.render();
				
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
	
	/* (non-Javadoc)
	 * @see jchess.gamelogic.field.IChessboardController#getBoard()
	 */
	@Override
	public IChessboardModel getBoard()
	{
		return board;
	}
}
