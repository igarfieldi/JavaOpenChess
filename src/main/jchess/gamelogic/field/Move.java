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

import java.util.Objects;

import jchess.gamelogic.pieces.Pawn;
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
	
	public enum CastlingType
	{
		NONE, SHORT_CASTLING, LONG_CASTLING
	}
	
	public Move(Field from, Field to, Piece movedPiece, Piece takenPiece, CastlingType castlingMove, boolean wasEnPassant,
	        Piece promotedPiece)
	{
		this.from = from;
		this.to = to;
		
		this.movedPiece = movedPiece;
		this.takenPiece = takenPiece;
		
		this.castlingMove = castlingMove;
		this.wasEnPassant = wasEnPassant;
		
		// TODO: adapt to 4p chess
		if(movedPiece.getBehaviour() instanceof Pawn && (Math.abs(to.getPosY() - from.getPosY()) == 2||
				Math.abs(to.getPosX() - from.getPosX()) == 2))
		{
			this.wasPawnTwoFieldsMove = true;
		} else if(movedPiece.getBehaviour() instanceof Pawn && promotedPiece != null)
		{
			this.promotedTo = promotedPiece;
		}
	}
	
	@Override
	public int hashCode() {
		// TODO: is this good enough?
		return Objects.hash(from, to, movedPiece, takenPiece);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		} else if((obj == null) || !(obj instanceof Move)) {
			return false;
		}
		
		Move move = (Move) obj;
		return from.equals(move.from) && to.equals(move.to) &&
				movedPiece.equals(move.movedPiece) &&
				takenPiece.equals(move.takenPiece) &&
				promotedTo.equals(move.promotedTo) &&
				(wasEnPassant == move.wasEnPassant) &&
				(castlingMove == move.castlingMove) &&
				(wasPawnTwoFieldsMove == move.wasPawnTwoFieldsMove);
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
