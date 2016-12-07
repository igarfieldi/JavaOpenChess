package jchess.gamelogic.controllers.chessboardcontrollers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.JChessApp;
import jchess.gamelogic.Player;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.field.Moves;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.Bishop;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Knight;
import jchess.gamelogic.pieces.Pawn;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.Queen;
import jchess.gamelogic.pieces.Rook;
import jchess.gamelogic.views.IChessboardView;
import jchess.util.Direction;

public abstract class RegularChessboardController implements IChessboardController
{
	private static Logger log = Logger.getLogger(RegularChessboardController.class.getName());
	
	private IChessboardModel board;
	private IChessboardView view;
	
	private List<Player> players;
	private int currPlayerIndex;
	
	private Moves movesHistory;
	
	public RegularChessboardController(IChessboardView view, IChessboardModel board,
			List<Player> players)
	{
		this.board = board;
		this.view = view;
		this.players = players;
		this.movesHistory = new Moves(this, players.get(0), players.get(1));
		
		//this.movesHistory = new Moves(this, white, black);
	}
	
	@Override
	public Moves getHistory()
	{
		return movesHistory;
	}
	
	protected Player getPlayer(int index) {
		return this.players.get(index);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.field.IChessboardController#getView()
	 */
	@Override
	public IChessboardView getView()
	{
		return view;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.field.IChessboardController#getActivePlayer()
	 */
	@Override
	public Player getActivePlayer()
	{
		return this.players.get(currPlayerIndex);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.field.IChessboardController#switchToNextPlayer()
	 */
	@Override
	public void switchToNextPlayer()
	{
		currPlayerIndex++;
		if(currPlayerIndex >= players.size()) {
			currPlayerIndex = 0;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jchess.gamelogic.field.IChessboardController#switchToPreviousPlayer()
	 */
	@Override
	public void switchToPreviousPlayer()
	{
		// Since we only have two players, this is equal to switchToNextPlayer
		this.switchToNextPlayer();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.field.IChessboardController#initialize()
	 */
	@Override
	public void initialize()
	{
		this.initializePieces();
		
		this.currPlayerIndex = 0;
	}
	
	/**
	 * Initializes the chessboard's pieces. This includes checking which player
	 * is on top etc.
	 * 
	 * @param whiteIsTop
	 *            is white playing from top-side
	 */
	protected abstract void initializePieces();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jchess.gamelogic.field.IChessboardController#getPossibleMoves(jchess.
	 * gamelogic.pieces.Piece, boolean)
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
			Field activeField = null;
			if(this.getView() != null)
			{
				activeField = this.getView().getActiveSquare();
			}
			
			for(Iterator<Field> fieldIterator = reachableFields.iterator(); fieldIterator.hasNext();)
			{
				// We need to use the iterator instead of foreach to be able to
				// use .remove()
				Field currField = fieldIterator.next();
				// Simulate the board state
				IChessboardModel tempModel = board;
				board = board.copy();
				
				try {
					this.move(tempModel.getField(piece), currField, false, false, false, false);
				} catch (IllegalMoveException exc) {
					// Should not happen
					log.log(Level.SEVERE, "Unexpected error while simulating move!", exc);
				}
				if(this.isChecked(piece.getPlayer()))
				{
					// Checked moves are not allowed, so remove it
					fieldIterator.remove();
				}
				// Undo the move again
				board = tempModel;
			}
			
			if(this.getView() != null)
			{
				this.getView().select(activeField);
			}
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
		
		reachableFields.addAll(this.getCastleMoves(piece));
		
		return reachableFields;
	}
	
	protected abstract Set<Field> getCastleMoves(Piece piece);
	
	/**
	 * Returns the list of all fields in a direction a piece might consider
	 * moving to. This does not include a check for blocking by pieces. The
	 * fields are ordered; fields closer to the piece first.
	 * 
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
		
		capturableFields.addAll(this.getEnPassantMoves(piece));
		
		return capturableFields;
	}
	
	protected abstract Set<Field> getEnPassantMoves(Piece piece);
	
	/**
	 * Returns the set of fields a given piece can move to without capturing.
	 * 
	 * @param piece
	 * @param directions
	 *            Set of directions to consider
	 * @return Set of fields piece can move to
	 */
	protected final Set<Field> getMovableFieldsInDirection(Piece piece, Set<Direction> directions)
	{
		Set<Field> movableFields = new HashSet<Field>();
		
		for(Direction dir : directions)
		{
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jchess.gamelogic.field.IChessboardController#isChecked(jchess.gamelogic.
	 * Player)
	 */
	@Override
	public boolean isChecked(Player player)
	{
		for(Piece piece : board.getPieces(player))
		{
			if(piece instanceof King)
			{
				for(Player enemy : this.players) {
					// You cannot  be an enemy of yourself (at least in chess :) )
					if(player != enemy) {
    					if(!this.isThreatenedByPlayer(board.getField(piece), enemy).isEmpty()) {
    						return true;
    					}
    				}
    			}
			}
		}
		
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.field.IChessboardController#isCheckmated(jchess.
	 * gamelogic.Player)
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
	
	/*
	 * (non-Javadoc)
	 * 
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
	protected boolean isAnyThreatenedByPlayer(Set<Field> targets, Player player)
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jchess.gamelogic.field.IChessboardController#move(jchess.gamelogic.field.
	 * Field, jchess.gamelogic.field.Field, boolean, boolean, boolean)
	 */
	@Override
	public void move(Field begin, Field end) throws IllegalMoveException
	{
		move(begin, end, true, true, true, true);
	}
	
	private void move(Field begin, Field end, boolean checkMove, boolean refresh, boolean clearForwardHistory, boolean enterIntoHistory) throws IllegalMoveException
	{
		// Standard move
		Piece movedPiece = board.getPiece(begin);
		if(movedPiece == null) {
			throw new NullPointerException("No piece on starting field!");
		}

		if(checkMove) {
			if(!this.getPossibleMoves(movedPiece, true).contains(end)) {
				throw new IllegalMoveException("Cannot move " + movedPiece + " to " + end);
			}
		}
		
		Move move = new Move(begin, end, movedPiece, board.getPiece(end), CastlingType.NONE, false, null);
		Move lastMove = this.getHistory().getLastMoveFromHistory();
		
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
			if(lastMove != null && lastMove.wasPawnTwoFieldsMove()) {
				// Check if the target field lies behind the two-square pawn
				Direction backwards = ((Pawn) lastMove.getMovedPiece()).getForwardDirection().multiply(-1);
				Field behindPawn = board.getField(lastMove.getTo().getPosX() + backwards.getX(),
						lastMove.getTo().getPosY() + backwards.getY());
				if(behindPawn.equals(end)) {
					// In that case we have an En Passant at our hands
					move = new Move(begin, end, movedPiece, lastMove.getMovedPiece(),
							CastlingType.NONE, true, null);
				}
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
						Queen queen = new Queen(board.getPiece(begin).getPlayer());
						board.setPiece(begin, queen);
					} else if(newPiece.equals("Rook")) // transform pawn to rook
					{
						Rook rook = new Rook(board.getPiece(begin).getPlayer());
						board.setPiece(begin, rook);
					} else if(newPiece.equals("Bishop")) // transform pawn to
					                                     // bishop
					{
						Bishop bishop = new Bishop(board.getPiece(begin).getPlayer());
						board.setPiece(begin, bishop);
					} else // transform pawn to knight
					{
						Knight knight = new Knight(board.getPiece(begin).getPlayer());
						board.setPiece(begin, knight);
					}
					promotedPiece = board.getPiece(begin);
				}
			}
			
			move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.NONE, false,
			        promotedPiece);
		} else
		{
			move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.NONE, false, null);
		}
		
		board.movePiece(board.getPiece(begin), end);
		board.getPiece(end).markAsMoved();
		
		if(move.getCastlingMove() != CastlingType.NONE)
		{
			// Move the rook
			if(begin.getPosX() + 2 == end.getPosX())
			{
				move(board.getField(7, begin.getPosY()), board.getField(end.getPosX() - 1, begin.getPosY()), false, false,
				        false, true);
			} else
			{
				move(board.getField(0, begin.getPosY()), board.getField(end.getPosX() + 1, begin.getPosY()), false, false,
				        false, true);
			}
		}
		
		if(refresh && view != null)
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.field.IChessboardController#getBoard()
	 */
	@Override
	public IChessboardModel getBoard()
	{
		return board;
	}
}
