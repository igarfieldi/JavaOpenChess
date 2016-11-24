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

import jchess.gamelogic.pieces.Piece;

/**
 * Class to represent a chessboard square
 */
public class Field
{
	
	private int posX; // 0-7, becouse 8 squares for row/column
	private int posY; // 0-7, becouse 8 squares for row/column
	private Piece piece = null;// object Piece on square (and extending Piecie)
	
	Field(int pozX, int pozY, Piece piece)
	{
		this.posX = pozX;
		this.posY = pozY;
		this.piece = piece;
	}/*--endOf-Square--*/
	
	Field(Field square)
	{
		this.posX = square.posX;
		this.posY = square.posY;
		this.piece = square.piece;
	}
	
	public Field clone(Field square)
	{
		return new Field(square);
	}
	
	public int getPosX()
	{
		return posX;
	}
	
	public int getPosY()
	{
		return posY;
	}
	
	public Piece getPiece()
	{
		return piece;
	}
	
	public void setPiece(Piece piece)
	{
		this.piece = piece;
		if(this.piece != null)
			this.piece.setSquare(this);
	}
	
	@Override
	public String toString() {
		return "(" + posX + "|" + posY + ":" + piece + ")";
	}
}