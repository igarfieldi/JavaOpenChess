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
 * Author: Mateusz Sławomir Lach ( matlak, msl )
 */
package jchess.gamelogic.field;

import jchess.gamelogic.field.Moves.CastlingType;
import jchess.gamelogic.pieces.Piece;

public class Move
{
	private Field from = null;
	private Field to = null;
	private Piece movedPiece = null;
	private Piece takenPiece = null;
	private Piece promotedTo = null;
	private boolean wasEnPassant = false;
	private CastlingType castlingMove = CastlingType.NONE;
	private boolean wasPawnTwoFieldsMove = false;
	
	Move(Field from, Field to, Piece movedPiece, Piece takenPiece, CastlingType castlingMove, boolean wasEnPassant,
	        Piece promotedPiece)
	{
		this.from = from;
		this.to = to;
		
		this.movedPiece = movedPiece;
		this.takenPiece = takenPiece;
		
		this.castlingMove = castlingMove;
		this.wasEnPassant = wasEnPassant;
		
		if(movedPiece.getName().equals("Pawn") && Math.abs(to.getPosY() - from.getPosY()) == 2)
		{
			this.wasPawnTwoFieldsMove = true;
		} else if(movedPiece.getName().equals("Pawn") && to.getPosY() == ChessboardController.BOTTOM
		        || to.getPosY() == ChessboardController.TOP && promotedPiece != null)
		{
			// TODO: simplify condition (shouldn't promotedPiece always be null expect when promoted?)
			this.promotedTo = promotedPiece;
		}
	}
	
	public Field getFrom()
	{
		return this.from;
	}
	
	public Field getTo()
	{
		return this.to;
	}
	
	public Piece getMovedPiece()
	{
		return this.movedPiece;
	}
	
	public Piece getTakenPiece()
	{
		return this.takenPiece;
	}
	
	public boolean wasEnPassant()
	{
		return this.wasEnPassant;
	}
	
	public boolean wasPawnTwoFieldsMove()
	{
		return this.wasPawnTwoFieldsMove;
	}
	
	public CastlingType getCastlingMove()
	{
		return this.castlingMove;
	}
	
	public Piece getPromotedPiece()
	{
		return this.promotedTo;
	}
}
