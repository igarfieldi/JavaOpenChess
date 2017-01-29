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
import jchess.gamelogic.pieces.Pawn;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.views.factories.IChessboardViewFactory;
import jchess.util.Direction;

/**
 * Class to represent chessgetBoard(). Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class TwoPlayerChessboardController extends RegularChessboardController
{
	private static int WHITE_BASE_LINE = 7;
	private static int BLACK_BASE_LINE = 0;
	
	public TwoPlayerChessboardController(IChessboardViewFactory viewFactory, IBoardFactory boardFactory,
			Player white, Player black)
	{
		super(viewFactory, boardFactory,
				Arrays.asList(new Player[]{white, black}));
	}
	
	@Override
	protected Move getRookMoveForCastling(Piece piece, CastlingType type) {
		Field field = getBoard().getField(piece);
		Field rookField = null;
		Field rookTarget = null;
		
		if(type == CastlingType.SHORT_CASTLING) {
			rookField = getBoard().getField(field.getPosX() + 3, field.getPosY());
			rookTarget = getBoard().getField(field.getPosX() + 1, field.getPosY());
		} else {
			rookField = getBoard().getField(field.getPosX() - 4, field.getPosY());
			rookTarget = getBoard().getField(field.getPosX() - 1, field.getPosY());
		}
		
		return new Move(rookField, rookTarget, getBoard().getPiece(rookField), null,
				type, false, null);
	}
	
	@Override
	protected Set<Move> getEnPassantMoves(Piece piece)
	{
		// TODO: for the 4p case we cannot use the last move!
		Set<Move> enPassantMoves = new HashSet<Move>();
		
		// Check if we have a pawn at our hands (precondition for en passant)
		if(piece.getBehaviour() instanceof Pawn)
		{
			Move lastMove = getHistory().getLastMoveFromHistory();
			
			// The last move has had to be a two field move from a pawn
			if(lastMove != null && lastMove.wasPawnTwoFieldsMove())
			{
				Piece twoSquareMovedPawn = lastMove.getMovedPiece();
				Field pieceField = getBoard().getField(piece);
				Field movedPawnField = getBoard().getField(twoSquareMovedPawn);
				
				// Get the forward direction of the pawn which moved two fields
				Direction movedPawnForward = ((Pawn) twoSquareMovedPawn.getBehaviour()).getForwardDirection();

				// By rotating the forward direction by 90 degrees we get the fields to its left and right
				Set<Field> candidates = new HashSet<Field>();
				// First possible field
				try {
					candidates.add(new Field(
							movedPawnField.getPosX() - movedPawnForward.rotate90Deg().getX(),
							movedPawnField.getPosY() - movedPawnForward.rotate90Deg().getY()));
				} catch(IllegalArgumentException exc) {
					// Field doesn't exist
				}
				
				// Other possible field
				try {
					candidates.add(new Field(
							movedPawnField.getPosX() + movedPawnForward.rotate90Deg().getX(),
							movedPawnField.getPosY() + movedPawnForward.rotate90Deg().getY()));
				} catch(IllegalArgumentException exc) {
					// Field doesn't exist
				}
				
				for(Field candidate : candidates) {
					if(candidate.equals(pieceField)) {
						try {
							Field targetField = getBoard().getField(
									movedPawnField.getPosX() - movedPawnForward.getX(),
									movedPawnField.getPosY() - movedPawnForward.getY());
							
							// Check if its behaviour allows such a move
							Direction captureDir = new Direction(
									targetField.getPosX() - pieceField.getPosX(),
									targetField.getPosY() - pieceField.getPosY());
							if(piece.getBehaviour().getCapturingMovements().contains(captureDir)) {
								enPassantMoves.add(new Move(pieceField, targetField, piece,
										twoSquareMovedPawn, CastlingType.NONE, true, null));
							}
						} catch(IllegalArgumentException exc) {
							// Field doesn't exist -> no en passant here
						}
					}
				}
			}
		}
		
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
		
		// Only two possibilities: pawn moves up or down; then check if it
		// is at the board end
		if(forward.getY() > 0) {
			return target.getPosY() == WHITE_BASE_LINE;
		} else {
			return target.getPosY() == BLACK_BASE_LINE;
		}
	}
}
