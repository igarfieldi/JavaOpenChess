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

/**
 * Class to represent a chessboard square
 */
public class Field
{
	
	private int posX;
	private int posY;
	
	public Field(int posX, int posY)
	{
		this.posX = posX;
		this.posY = posY;
	}
	
	public Field(Field square)
	{
		this.posX = square.posX;
		this.posY = square.posY;
	}
	
	public Field clone()
	{
		return new Field(posX, posY);
	}
	
	public int getPosX()
	{
		return posX;
	}
	
	public int getPosY()
	{
		return posY;
	}
	
	@Override
	public int hashCode()
	{
		return 10000 * posY + posX;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
		{
			return true;
		} else if(obj == null || !(obj instanceof Field))
		{
			return false;
		} else
		{
			return this.posX == ((Field) obj).posX && this.posY == ((Field) obj).posY;
		}
	}
	
	@Override
	public String toString()
	{
		return "(" + posX + "|" + posY + ")";
	}
}
