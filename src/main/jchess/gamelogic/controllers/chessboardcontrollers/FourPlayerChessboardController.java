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
package jchess.gamelogic.controllers.chessboardcontrollers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.models.IBoardFactory;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.views.factories.IChessboardViewFactory;
import jchess.util.Direction;

/**
 * Class to represent chessgetBoard(). Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class FourPlayerChessboardController extends RegularChessboardController
{
	private static int WHITE_BASE_LINE = 13;
	private static int RED_BASE_LINE = 0;
	private static int BLACK_BASE_LINE = 0;
	private static int GOLDEN_BASE_LINE = 13;
	
	public FourPlayerChessboardController(IChessboardViewFactory viewFactory,
			IBoardFactory boardFactory, Player white, Player red, Player black, Player golden)
	{
		super(viewFactory, boardFactory,
				Arrays.asList(new Player[]{white, red, black, golden}));
	}
	
	protected Move getRookMoveForCastling(Piece piece, CastlingType type) {
		Field field = getBoard().getField(piece);
		Field rookField = null;
		Field rookTarget = null;
		
		// Which rook gets to castle and where it should move depends on
		// the castling type and which player (ie. position on the board)
		// is castling
		if(field.getPosY() == BLACK_BASE_LINE || field.getPosY() == WHITE_BASE_LINE) {
			if(type == CastlingType.SHORT_CASTLING) {
				rookField = getBoard().getField(field.getPosX() + 3, field.getPosY());
				rookTarget = getBoard().getField(field.getPosX() + 1, field.getPosY());
			} else {
				rookField = getBoard().getField(field.getPosX() - 4, field.getPosY());
				rookTarget = getBoard().getField(field.getPosX() - 1, field.getPosY());
			}
		} else {
			if(type == CastlingType.SHORT_CASTLING) {
				rookField = getBoard().getField(field.getPosX(), field.getPosY() + 3);
				rookTarget = getBoard().getField(field.getPosX(), field.getPosY() + 1);
			} else {
				rookField = getBoard().getField(field.getPosX(), field.getPosY() - 4);
				rookTarget = getBoard().getField(field.getPosX(), field.getPosY() - 1);
			}
		}
		
		return new Move(rookField, rookTarget, getBoard().getPiece(rookField), null,
				type, false, null);
	}
	
	protected Set<Field> getCastleMoves(Piece piece) {
		Set<Field> castleFields = new HashSet<Field>();
		
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
						castleFields.add(castled);
					}
				}
				if(rightRook != null) {
					Field castled = this.getCastledKingField(CastlingType.SHORT_CASTLING,
							piece, rightRook);
					if(castled != null) {
						castleFields.add(castled);
					}
				}
			}
		}
		
		return castleFields;
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
	
	@Override
	protected Set<Field> getEnPassantMoves(Piece piece)
	{
		Set<Field> enPassantMoves = new HashSet<Field>();
		
		// TODO: proper En Passant implementation
		/*if(piece instanceof Pawn)
		{
			Move lastMove = getHistory().getLastMoveFromHistory();
			// En passant
			if(lastMove != null && lastMove.wasPawnTwoFieldsMove())
			{
				Pawn twoSquareMovedPawn = (Pawn) lastMove.getMovedPiece();
				if(getBoard().getField(piece).getPosY() == getBoard().getField(twoSquareMovedPawn).getPosY())
				{
					// Our pawn is in the same row
					if(Math.abs(getBoard().getField(piece).getPosX() - getBoard().getField(twoSquareMovedPawn).getPosX()) == 1)
					{
						// Our pawn is right next to the pawn which moved two
						// squares. Now we need to check the direction in which
						// the pawn has to go
						if(piece.getPlayer() == getPlayer(1))
						{
							enPassantMoves.add(getBoard().getField(getBoard().getField(twoSquareMovedPawn).getPosX(),
							        getBoard().getField(piece).getPosY() + 1));
						} else
						{
							enPassantMoves.add(getBoard().getField(getBoard().getField(twoSquareMovedPawn).getPosX(),
							        getBoard().getField(piece).getPosY() - 1));
						}
					}
				}
			}
		}*/
		
		return enPassantMoves;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jchess.gamelogic.field.chessboardcontrollers.RegularChessboardController#checkForPromotion(Pawn, Field)
	 */
	@Override
	protected boolean checkForPromotion(Piece pawn, Field target) {
		Direction forward = pawn.getBehaviour().getNormalMovements().iterator().next();
		
		// In 4p chess there are 4 different "base lines" where promotion
		// can happen
		if(forward.getX() == 0) {
			if(forward.getY() > 0) {
				return target.getPosY() == WHITE_BASE_LINE;
			} else {
				return target.getPosY() == BLACK_BASE_LINE;
			}
		} else {
			if(forward.getX() > 0) {
				return target.getPosX() == GOLDEN_BASE_LINE;
			} else {
				return target.getPosX() == RED_BASE_LINE;
			}
		}
	}
}
