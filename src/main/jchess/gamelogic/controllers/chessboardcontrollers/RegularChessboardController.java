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
import jchess.gamelogic.controllers.History;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.models.IBoardFactory;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.Bishop;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Knight;
import jchess.gamelogic.pieces.Pawn;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.gamelogic.pieces.Queen;
import jchess.gamelogic.pieces.Rook;
import jchess.gamelogic.views.IChessboardView;
import jchess.gamelogic.views.factories.IChessboardViewFactory;
import jchess.util.Direction;
import jchess.util.FileMapParser;

public abstract class RegularChessboardController implements IChessboardController
{
	private static Logger log = Logger.getLogger(RegularChessboardController.class.getName());
	
	private IChessboardModel board;
	private IChessboardView view;
	
	private List<Player> players;
	private int currPlayerIndex;
	
	private History movesHistory;
	
	public RegularChessboardController(IChessboardViewFactory viewFactory,
			IBoardFactory boardFactory, List<Player> players)
	{
		this.board = boardFactory.createChessboard(players);
		if(viewFactory != null) {
			this.view = viewFactory.create();
		}
		this.players = players;
		this.movesHistory = new History(this, players.get(0), players.get(1));
		this.currPlayerIndex = 0;
		
		// this.movesHistory = new Moves(this, white, black);
	}
	
	@Override
	public void save(FileMapParser parser)
	{
		for(Player player : players)
		{
			parser.setProperty(player.getColor().toString(), player.getName());
		}
		
		parser.setProperty("Moves", this.getHistory().getMovesInString());
	}
	
	@Override
	public void load(FileMapParser parser)
	{
		this.movesHistory.setMoves(parser.getProperty("Moves"));
	}
	
	@Override
	public History getHistory()
	{
		return movesHistory;
	}
	
	protected Player getPlayer(int index)
	{
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
		if(currPlayerIndex >= players.size())
		{
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
		
		// Special case for pawns: add two-step move if it hasn't moved yet
		if(piece.getBehaviour() instanceof Pawn && !piece.hasMoved())
		{
			// Pawns only have one direction anyway
			Direction forward = piece.getBehaviour().getNormalMovements().iterator().next();
			Field pawnField = board.getField(piece);
			Field inFrontOfPawn = new Field(pawnField.getPosX() + forward.getX(), pawnField.getPosY() + forward.getY());
			
			if(board.getPiece(inFrontOfPawn) == null)
			{
				// We need to remove the two-field move
				Field twoFieldMove = new Field(pawnField.getPosX() + 2 * forward.getX(),
				        pawnField.getPosY() + 2 * forward.getY());
				reachableFields.add(twoFieldMove);
			}
		}
		
		if(careForCheck)
		{
			this.removeMovesResultingInCheck(piece, reachableFields);
		}
		
		return reachableFields;
	}
	
	@Override
	public Set<Field> getPossibleThreats(Piece piece, boolean careForCheck) {
		Set<Field> threateningFields = new HashSet<Field>();
		Field target = board.getField(piece);
		
		for(Player enemy : players) {
			if(enemy != this.getActivePlayer()) {
				// Iterate over all pieces of the player
				for(Piece enemyPiece : board.getPieces(enemy)) {
					if(this.getPossibleMoves(enemyPiece, careForCheck).contains(target)) {
						threateningFields.add(board.getField(enemyPiece));
					}
				}
			}
		}
		
		return threateningFields;
	}
	
	/**
	 * Removes all fields from the set that would leave the player in a check.
	 * 
	 * @param piece
	 *            Piece to be moved
	 * @param moves
	 *            Set of (so far) possible moves
	 */
	private void removeMovesResultingInCheck(Piece piece, Set<Field> moves)
	{
		// For each move we need to simuate the future board state and see
		// if we're in check
		
		// Store the part of the state that we cannot simply revert
		Field activeField = null;
		if(this.getView() != null)
		{
			activeField = this.getView().getActiveSquare();
		}
		
		for(Iterator<Field> fieldIterator = moves.iterator(); fieldIterator.hasNext();)
		{
			// We need to use the iterator instead of foreach to be able to
			// use .remove()
			Field currField = fieldIterator.next();
			// Simulate the board state
			IChessboardModel tempModel = board;
			board = board.copy();
			
			try
			{
				this.move(tempModel.getField(piece), currField, false, false, false, false);
			} catch(IllegalMoveException exc)
			{
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
		reachableFields.addAll(this.getMovableFieldsInDirection(piece, piece.getBehaviour().getNormalMovements()));
		
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
		if(piece.getBehaviour().canMoveMultipleSteps())
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
		
		capturableFields.addAll(this.getCapturableFieldsInDirection(piece, piece.getBehaviour().getCapturingMovements()));
		
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
			if(piece.getBehaviour() instanceof King)
			{
				for(Player enemy : this.players)
				{
					// You cannot be an enemy of yourself (at least in chess :)
					// )
					if(player != enemy)
					{
						if(!this.isThreatenedByPlayer(board.getField(piece), enemy).isEmpty())
						{
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
			if(this.getThreatenedFieldsInDirection(piece, piece.getBehaviour().getCapturingMovements()).contains(target))
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
	public boolean move(Field begin, Field end) throws IllegalMoveException
	{
		return move(begin, end, true, true, true, true);
	}
	
	/**
	 * Checks whether a given pawn is eligible for promotion. With regular chess
	 * rules this occurs when a pawn reaches the end of the board. Since
	 * different boards have different directions, this method has to be
	 * overridden.
	 * 
	 * @param pawn
	 *            Pawn to check promotion for
	 * @param target
	 *            Field where the pawn will be moved to
	 * @return True if the pawn can be promoted
	 */
	protected abstract boolean checkForPromotion(Piece piece, Field target);
	
	private boolean move(Field begin, Field end, boolean checkMove, boolean refresh, boolean clearForwardHistory,
	        boolean enterIntoHistory) throws IllegalMoveException
	{
		// Standard move
		Piece movedPiece = board.getPiece(begin);
		if(movedPiece == null)
		{
			throw new NullPointerException("No piece on starting field!");
		}
		
		if(checkMove)
		{
			if(!this.getPossibleMoves(movedPiece, true).contains(end))
			{
				throw new IllegalMoveException("Cannot move " + movedPiece + " to " + end);
			}
		}
		
		Move move = new Move(begin, end, movedPiece, board.getPiece(end), CastlingType.NONE, false, null);
		Move lastMove = this.getHistory().getLastMoveFromHistory();
		
		if(board.getPiece(begin).getBehaviour() instanceof King)
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
		} else if(board.getPiece(begin).getBehaviour() instanceof Pawn)
		{
			if(lastMove != null && lastMove.wasPawnTwoFieldsMove())
			{
				// Check if the target field lies behind the two-square pawn
				Direction backwards = lastMove.getMovedPiece().getBehaviour().getNormalMovements().iterator().next().multiply(-1);
				Field behindPawn = board.getField(lastMove.getTo().getPosX() + backwards.getX(),
				        lastMove.getTo().getPosY() + backwards.getY());
				if(behindPawn.equals(end))
				{
					// In that case we have an En Passant at our hands
					move = new Move(begin, end, movedPiece, lastMove.getMovedPiece(), CastlingType.NONE, true, null);
				}
			}
			if(clearForwardHistory && this.checkForPromotion(movedPiece, end))
			{
				// Promotion of pawns has to be handled for the specific board
				// layout
				String newPiece = JChessApp.view.showPawnPromotionBox(movedPiece.getPlayer().getColor());
				
				Piece promoted = null;
				if(newPiece.equals("Queen")) // transform pawn to queen
				{
					promoted = PieceFactory.getInstance().buildPiece(movedPiece.getPlayer(), new Direction(0, 0), PieceType.QUEEN);
				} else if(newPiece.equals("Rook")) // transform pawn to rook
				{
					promoted = PieceFactory.getInstance().buildPiece(movedPiece.getPlayer(), new Direction(0, 0), PieceType.ROOK);
				} else if(newPiece.equals("Bishop")) // transform pawn to
				                                     // bishop
				{
					promoted = PieceFactory.getInstance().buildPiece(movedPiece.getPlayer(), new Direction(0, 0), PieceType.BISHOP);
				} else if(newPiece.equals("Knight"))// transform pawn to knight
				{
					promoted = PieceFactory.getInstance().buildPiece(movedPiece.getPlayer(), new Direction(0, 0), PieceType.KNIGHT);
				} else
				{
					// If promotion was cancelled don't execute the move!
					return false;
				}
				
				board.setPiece(begin, promoted);
				move = new Move(begin, end, movedPiece, board.getPiece(end), CastlingType.NONE, false, promoted);
			}
		} else
		{
			move = new Move(begin, end, movedPiece, board.getPiece(end), CastlingType.NONE, false, null);
		}
		
		board.movePiece(board.getPiece(begin), end);
		board.getPiece(end).markAsMoved();
		
		if(move.getCastlingMove() != CastlingType.NONE)
		{
			// Move the rook
			if(begin.getPosX() + 2 == end.getPosX())
			{
				move(board.getField(7, begin.getPosY()), board.getField(end.getPosX() - 1, begin.getPosY()), false,
				        false, false, true);
			} else
			{
				move(board.getField(0, begin.getPosY()), board.getField(end.getPosX() + 1, begin.getPosY()), false,
				        false, false, true);
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
		
		return true;
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
