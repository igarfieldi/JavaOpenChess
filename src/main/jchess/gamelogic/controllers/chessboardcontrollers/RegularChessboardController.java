package jchess.gamelogic.controllers.chessboardcontrollers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.JChessApp;
import jchess.gamelogic.Player;
import jchess.gamelogic.controllers.History;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.IllegalMoveException;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.models.IBoardFactory;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Pawn;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
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
	
	public RegularChessboardController(IChessboardViewFactory viewFactory, IBoardFactory boardFactory,
	        List<Player> players)
	{
		this.board = boardFactory.createChessboard(players);
		if(viewFactory != null)
		{
			this.view = viewFactory.create();
		}
		this.players = players;
		this.movesHistory = new History(this, players);
		this.currPlayerIndex = 0;
	}
	
	@Override
	public void save(FileMapParser parser)
	{
		for(Player player : players)
		{
			parser.setProperty(player.getColor().toString(), player.getName());
		}
		
		parser.setProperty("Moves", this.getHistory().getMovesAsString());
	}
	
	@Override
	public void load(FileMapParser parser)
	{
		// TODO: load player names etc.!
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
		if(++currPlayerIndex >= players.size())
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
		if(--currPlayerIndex < 0)
		{
			currPlayerIndex = players.size() - 1;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jchess.gamelogic.field.IChessboardController#getPossibleMoves(jchess.
	 * gamelogic.pieces.Piece, boolean)
	 */
	@Override
	public Set<Move> getPossibleMoves(Piece piece, boolean careForCheck)
	{
		Set<Move> possibleMoves = new HashSet<Move>();
		
		// Capturing moves only
		possibleMoves.addAll(this.getCapturableFields(piece));
		
		// Non-capturing moves only
		possibleMoves.addAll(this.getMovableFields(piece));
		
		// Special case for pawns: add two-step move if it hasn't moved yet
		if(piece.getBehaviour() instanceof Pawn && !piece.hasMoved())
		{
			// Pawns only have one direction anyway
			Direction forward = piece.getBehaviour().getNormalMovements().iterator().next();
			Field pawnField = board.getField(piece);
			try {
    			Field inFrontOfPawn = new Field(pawnField.getPosX() + forward.getX(), pawnField.getPosY() + forward.getY());
    			
    			// Pawns cannot jump over pieces even at the start
    			if(board.getPiece(inFrontOfPawn) == null)
    			{
    				Field twoFieldMove = new Field(pawnField.getPosX() + 2 * forward.getX(),
    				        pawnField.getPosY() + 2 * forward.getY());
    				
    				// AND they cannot capture pieces with this move either
    				if(board.getPiece(twoFieldMove) == null) {
    					possibleMoves.add(new Move(pawnField, twoFieldMove, piece,
    							null, CastlingType.NONE, true, null));
    				}
    				
    			}
			} catch(IllegalArgumentException exc) {
				// The field is invalid (and thus doesn't exist)
			}
		}
		
		// If we do not want to allow moves that put us in check, remove those
		if(careForCheck)
		{
			this.removeMovesResultingInCheck(piece, possibleMoves);
		}
		
		return possibleMoves;
	}
	
	@Override
	public Set<Field> getPossibleThreats(Piece piece, boolean careForCheck)
	{
		Set<Field> threateningFields = new HashSet<Field>();
		Field target = board.getField(piece);
		
		for(Player enemy : players)
		{
			if(enemy != this.getActivePlayer())
			{
				// Iterate over all pieces of the player
				for(Piece enemyPiece : board.getPieces(enemy))
				{
					if(this.getPossibleMoves(enemyPiece, careForCheck).contains(target))
					{
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
	private void removeMovesResultingInCheck(Piece piece, Set<Move> moves)
	{
		// For each move we need to simuate the future board state and see
		// if we're in check
		
		// First store the pre-existing checks of other players
		Map<Player, Set<Piece>> preExistingChecks = new HashMap<Player, Set<Piece>>();
		for(Player player : this.players)
		{
			preExistingChecks.put(player, this.getPiecesCheckingPlayer(player));
		}
		
		// Store the part of the state that we cannot simply revert
		Field activeField = null;
		if(this.getView() != null)
		{
			activeField = this.getView().getActiveSquare();
		}
		
		for(Iterator<Move> fieldIterator = moves.iterator(); fieldIterator.hasNext();)
		{
			// We need to use the iterator instead of foreach to be able to
			// use .remove()
			Move currMove = fieldIterator.next();
			Field currField = currMove.getTo();
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
			
			// If either the currently moving player is in a check OR a check
			// between non-moving players opens up after the move, we have to
			// remove it
			if(this.isChecked(piece.getPlayer()) ||
					this.isUnfairCheckPresent(preExistingChecks, piece.getPlayer()))
			{
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
	 * Checks if an unfair check is
	 * present. An unfair check is a check between two players which are
	 * currently not moving. This can happen e.g. if the moving player moved a
	 * piece blocking the checking path of another player (only possible with
	 * more than 2 players). Since any previously existing checks cannot be
	 * considered unfair.
	 * 
	 * @param preExistingChecks
	 *            Map containing the set of pieces previously checking each
	 *            player
	 * @param movingPlayer
	 *            The player assumed to be currently moving
	 * @return True if an unfair check exists; false otherwise
	 */
	private boolean isUnfairCheckPresent(Map<Player, Set<Piece>> preExistingChecks, Player movingPlayer)
	{
		for(Player player : this.players)
		{
			// Check every player for possible checks that didn't exist before
			for(Piece checking : this.getPiecesCheckingPlayer(player))
			{
				// For every piece that threatens the player's king check if
				// it is NOT the currently moving player (because he just made
				// a move; he has to be allowed to put someone else in check)
				if(checking.getPlayer() != movingPlayer && !preExistingChecks.get(player).contains(checking))
				{
					// If the check didn't exist before the move it is unfair
					return true;
				}
			}
		}
		
		return false;
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
	private Set<Move> getMovableFields(Piece piece)
	{
		Set<Move> normalMoves = new HashSet<Move>();
		
		for(Field field : this.getMovableFieldsInDirection(piece, piece.getBehaviour().getNormalMovements())) {
			// TODO: what about promotion moves?
			normalMoves.add(new Move(board.getField(piece), field,
					piece, null, CastlingType.NONE, false, null));
		}
		
		normalMoves.addAll(this.getCastleMoves(piece));
		
		return normalMoves;
	}
	
	/**
	 * Checks whether a given rook and king can castle.
	 * @param type Type of castling
	 * @param king King to castle with
	 * @param rook Rook to castle with
	 * @return The king's new field if castling is possible, null otherwise
	 */
	private Field getCastledKingField(CastlingType type, Piece king, Piece rook) {
		if(rook == null || rook.hasMoved()) {
			// A non-existent rook or one that has moved already cannot castle
			return null;
		}
		
		Field kingField = getBoard().getField(king);
		Field rookField = getBoard().getField(rook);
		
		// Determine the 'direction' of the castling; e.g. (0, 1) or (-1, 0)
		Direction castleDir = new Direction(rookField.getPosX() - kingField.getPosX(),
				rookField.getPosY() - kingField.getPosY()).signum();
		
		// Determine the target field of the rook; this is dependent on the castling
		// type
		Field rookTarget;
		if(type == CastlingType.SHORT_CASTLING) {
			rookTarget = getBoard().getField(rookField.getPosX() - 2*castleDir.getX(),
					rookField.getPosY() - 2*castleDir.getY());
		} else {
			rookTarget = getBoard().getField(rookField.getPosX() - 3*castleDir.getX(),
					rookField.getPosY() - 3*castleDir.getY());
		}
		
		// Check if the rook can move to its target field meaning no other pieces
		// are in between rook and king
		Set<Field> reachable = this.getMovableFieldsInDirection(rook, rook.getBehaviour().getNormalMovements());
		if(reachable.contains(rookTarget))
		{
			// Get the fields the king would have to cross to get to its new
			// position + its old position
			Set<Field> involvedFields = new HashSet<Field>();
			involvedFields.add(kingField);
			involvedFields.add(getBoard().getField(kingField.getPosX() + castleDir.getX(),
					kingField.getPosY() + castleDir.getY()));
			involvedFields.add(getBoard().getField(kingField.getPosX() + 2*castleDir.getX(),
					kingField.getPosY() + 2*castleDir.getY()));
			
			// None of the fields involved must be in check so they must not
			// be threatened by an enemy
			for(Player enemy : this.getEnemies(king.getPlayer())) {
				if(!this.isAnyThreatenedByPlayer(involvedFields, enemy))
				{
					return getBoard().getField(kingField.getPosX() + 2*castleDir.getX(),
							kingField.getPosY() + 2*castleDir.getY());
				}
			}
		}
		
		// No castling possible
		return null;
	}
	
	/**
	 * Returns the set of possible castling moves for the given king. E.g. a
	 * king on h1 in 2p chess could castle to f1 or j1 (if the other castling
	 * conditions were fulfilled).
	 * 
	 * @param piece
	 *            King to get castling moves for
	 * @return Set of possible castling moves for the king
	 */
	private Set<Move> getCastleMoves(Piece piece) {
		Set<Move> castleMoves = new HashSet<Move>();
		
		// Castling is only possible for kings
		if(piece.getBehaviour() instanceof King)
		{
			// The king must not have been moved
			if(!piece.hasMoved())
			{
				// There are two possible rooks to castle with
				Piece leftRook = this.getRookMoveForCastling(piece, CastlingType.LONG_CASTLING).getMovedPiece();
				Piece rightRook = this.getRookMoveForCastling(piece, CastlingType.SHORT_CASTLING).getMovedPiece();
				
				// Check both rooks for possible castling
				if(leftRook != null) {
					Field castled = this.getCastledKingField(CastlingType.LONG_CASTLING,
							piece, leftRook);
					if(castled != null) {
						castleMoves.add(new Move(getBoard().getField(piece), castled,
								piece, null, CastlingType.LONG_CASTLING, false, null));
					}
				}
				if(rightRook != null) {
					Field castled = this.getCastledKingField(CastlingType.SHORT_CASTLING,
							piece, rightRook);
					if(castled != null) {
						castleMoves.add(new Move(getBoard().getField(piece), castled,
								piece, null, CastlingType.SHORT_CASTLING, false, null));
					}
				}
			}
		}
		
		return castleMoves;
	}
	
	/**
	 * Gets the corresponding move of the rook for a given castling king move.
	 * E.g. a short-castling king on h1 for 2p chess needs a rook move from k1
	 * to i1.
	 * 
	 * @param king
	 *            King which moves during castling
	 * @param type
	 *            Type of the castling (short/long)
	 * @return Move the rook will have to make
	 */
	protected abstract Move getRookMoveForCastling(Piece king, CastlingType type);
	
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
	private Set<Move> getCapturableFields(Piece piece)
	{
		Set<Move> captureMoves = new HashSet<Move>();
		
		captureMoves.addAll(this.getCapturableFieldsInDirection(piece,
		        		piece.getBehaviour().getCapturingMovements()));
		
		captureMoves.addAll(this.getEnPassantMoves(piece));
		
		return captureMoves;
	}
	
	protected abstract Set<Move> getEnPassantMoves(Piece piece);
	
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
	private Set<Move> getCapturableFieldsInDirection(Piece piece, Set<Direction> directions)
	{
		Set<Move> captureMoves = new HashSet<Move>();
		
		for(Field field : this.getThreatenedFieldsInDirection(piece, directions))
		{
			if(board.getPiece(field) != null)
			{
				captureMoves.add(new Move(board.getField(piece), field, piece,
						board.getPiece(field), CastlingType.NONE, false, null));
			}
		}
		
		return captureMoves;
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
		return !this.getPiecesCheckingPlayer(player).isEmpty();
	}
	
	@Override
	public Set<Piece> getPiecesCheckingPlayer(Player player)
	{
		Set<Piece> threateningPieces = new HashSet<Piece>();
		for(Piece piece : board.getPieces(player))
		{
			if(piece.getBehaviour() instanceof King)
			{
				for(Player enemy : this.getEnemies(player))
				{
					threateningPieces.addAll(this.isThreatenedByPlayer(board.getField(piece), enemy));
				}
			}
		}
		
		return threateningPieces;
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
			if(this.getThreatenedFieldsInDirection(piece, piece.getBehaviour().getCapturingMovements())
			        .contains(target))
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
	
	@Override
	public Set<Player> getEnemies(Player friendly)
	{
		Set<Player> enemies = new HashSet<Player>();
		
		for(Player player : this.players)
		{
			if(player != friendly)
			{
				enemies.add(player);
			}
		}
		
		return enemies;
	}

	@Override
	public Set<Player> getAllies(Player friendly)
	{
		Set<Player> allies = new HashSet<Player>();
		allies.add(friendly);
		return allies;
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
			// TODO: this is preliminary!
			Set<Field> possibleTargets = new HashSet<Field>();
			for(Move move : this.getPossibleMoves(movedPiece, true)) {
				possibleTargets.add(move.getTo());
			}
			if(!possibleTargets.contains(end))
			{
				throw new IllegalMoveException("Cannot move " + movedPiece + " to " + end);
			}
		}
		
		Move move = new Move(begin, end, movedPiece, board.getPiece(end), CastlingType.NONE, false, null);
		Move lastMove = this.getHistory().getLastMoveFromHistory();
		
		if(board.getPiece(begin).getBehaviour() instanceof King)
		{
			// Castling
			// TODO: Clean this up!
			if(begin.getPosX() + 2 == end.getPosX())
			{
				move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.SHORT_CASTLING,
				        false, null);
			} else if(begin.getPosX() - 2 == end.getPosX())
			{
				move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.LONG_CASTLING,
				        false, null);
			} else if(begin.getPosY() + 2 == end.getPosY())
			{
				move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.SHORT_CASTLING,
				        false, null);
			} else if(begin.getPosY() - 2 == end.getPosY())
			{
				move = new Move(begin, end, board.getPiece(begin), board.getPiece(end), CastlingType.LONG_CASTLING,
				        false, null);
			}
		} else if(board.getPiece(begin).getBehaviour() instanceof Pawn)
		{
			if(lastMove != null && lastMove.wasPawnTwoFieldsMove())
			{
				// Check if the target field lies behind the two-square pawn
				Direction backwards = ((Pawn)lastMove.getMovedPiece().getBehaviour()).getForwardDirection().multiply(-1);
				Field behindPawn = board.getField(lastMove.getTo().getPosX() + backwards.getX(),
				        lastMove.getTo().getPosY() + backwards.getY());
				if(behindPawn.equals(end))
				{
					// In that case we have an En Passant at our hands
					move = new Move(begin, end, movedPiece, lastMove.getMovedPiece(), CastlingType.NONE, true, null);
					getBoard().removePiece(lastMove.getTo());
				}
			}
			if(clearForwardHistory && (view != null) && this.checkForPromotion(movedPiece, end))
			{
				// Promotion of pawns has to be handled for the specific board
				// layout
				String newPiece = JChessApp.view.showPawnPromotionBox(movedPiece.getPlayer().getColor());
				
				Piece promoted = null;
				if(newPiece.equals("Queen")) // transform pawn to queen
				{
					promoted = PieceFactory.getInstance().buildPiece(movedPiece.getPlayer(), new Direction(0, 0),
					        PieceType.QUEEN);
				} else if(newPiece.equals("Rook")) // transform pawn to rook
				{
					promoted = PieceFactory.getInstance().buildPiece(movedPiece.getPlayer(), new Direction(0, 0),
					        PieceType.ROOK);
				} else if(newPiece.equals("Bishop")) // transform pawn to
				                                     // bishop
				{
					promoted = PieceFactory.getInstance().buildPiece(movedPiece.getPlayer(), new Direction(0, 0),
					        PieceType.BISHOP);
				} else if(newPiece.equals("Knight"))// transform pawn to knight
				{
					promoted = PieceFactory.getInstance().buildPiece(movedPiece.getPlayer(), new Direction(0, 0),
					        PieceType.KNIGHT);
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
		
		if(move.getCastlingMove() != CastlingType.NONE)
		{
			// Get the move the rook would make
			Move rookMove = this.getRookMoveForCastling(move.getMovedPiece(), move.getCastlingMove());
			board.movePiece(rookMove.getMovedPiece(), rookMove.getTo());
		}
		
		board.movePiece(board.getPiece(begin), end);
		board.getPiece(end).markAsMoved();
		
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
