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
import jchess.gamelogic.models.IBoardFactory;
import jchess.gamelogic.pieces.King;
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
	
	protected Set<Field> getCastleMoves(Piece piece) {
		Set<Field> castleFields = new HashSet<Field>();
		
		if(piece.getBehaviour() instanceof King)
		{
			// Castling
			if(!piece.hasMoved())
			{
				Field kingsField = getBoard().getField(piece);
				
				Piece leftRook = getBoard().getPiece(getBoard().getField(0, kingsField.getPosY()));
				Piece rightRook = getBoard().getPiece(getBoard().getField(7, kingsField.getPosY()));
				
				if(leftRook != null && !leftRook.hasMoved())
				{
					// Neither left rook nor king have moved yet
					Set<Field> reachable = this.getMovableFieldsInDirection(leftRook, leftRook.getBehaviour().getNormalMovements());
					if(reachable.contains(getBoard().getField(kingsField.getPosX() - 1, kingsField.getPosY())))
					{
						// Left rook can move right next to the king -> fields
						// in between both are free
						Set<Field> involvedFields = new HashSet<Field>();
						involvedFields.add(kingsField);
						involvedFields.add(getBoard().getField(kingsField.getPosX() - 1, kingsField.getPosY()));
						involvedFields.add(getBoard().getField(kingsField.getPosX() - 2, kingsField.getPosY()));
						// None of the fields involved must be in check
						
						Player enemy = getPlayer(0);
						if(piece.getPlayer() == getPlayer(0))
						{
							enemy = getPlayer(1);
						}
						if(!this.isAnyThreatenedByPlayer(involvedFields, enemy))
						{
							castleFields.add(getBoard().getField(kingsField.getPosX() - 2, kingsField.getPosY()));
						}
					}
				}
				if(rightRook != null && !rightRook.hasMoved())
				{
					// Neither left rook nor king have moved yet
					Set<Field> reachable = this.getMovableFieldsInDirection(rightRook, rightRook.getBehaviour().getNormalMovements());
					if(reachable.contains(getBoard().getField(kingsField.getPosX() + 1, kingsField.getPosY())))
					{
						// Right rook can move right next to the king -> fields
						// in between both are free
						Set<Field> involvedFields = new HashSet<Field>();
						involvedFields.add(kingsField);
						involvedFields.add(getBoard().getField(kingsField.getPosX() + 1, kingsField.getPosY()));
						involvedFields.add(getBoard().getField(kingsField.getPosX() + 2, kingsField.getPosY()));
						// None of the fields involved must be in check
						
						Player enemy = getPlayer(0);
						if(piece.getPlayer() == getPlayer(0))
						{
							enemy = getPlayer(1);
						}
						if(!this.isAnyThreatenedByPlayer(involvedFields, enemy))
						{
							castleFields.add(getBoard().getField(kingsField.getPosX() + 2, kingsField.getPosY()));
						}
					}
				}
			}
		}
		
		return castleFields;
	}
	
	@Override
	protected Set<Field> getEnPassantMoves(Piece piece)
	{
		Set<Field> enPassantMoves = new HashSet<Field>();
		
		if(piece.getBehaviour() instanceof Pawn)
		{
			Move lastMove = getHistory().getLastMoveFromHistory();
			// En passant
			if(lastMove != null && lastMove.wasPawnTwoFieldsMove())
			{
				Piece twoSquareMovedPawn = lastMove.getMovedPiece();
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
