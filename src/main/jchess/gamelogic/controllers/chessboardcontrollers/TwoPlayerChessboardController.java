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

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.models.IBoardFactory;
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
