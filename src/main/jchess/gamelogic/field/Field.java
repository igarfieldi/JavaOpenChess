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
 * Class to represent a chessboard field.
 */
public class Field
{
	private static final String FIELD_LETTERS =
			"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private final int posX;
	private final int posY;
	
	public Field(int posX, int posY)
	{
		if(posX < 0 || posY < 0) {
			throw new IllegalArgumentException("Field components must be larger than zero!");
		}
		this.posX = posX;
		this.posY = posY;
	}
	
	public Field(Field square)
	{
		this.posX = square.posX;
		this.posY = square.posY;
	}
	
	@Override
	public String toString() {
		// TODO: flip around the y coordinate for chessboard to coincide with 
		// the designation (lower left = starting point)
		return Field.getAlphabeticalDesignation(getPosX()) + Integer.toString(getPosY() + 1);
	}
	
	/**
	 * Gets the alphabetical part of a field designation.
	 * If no more letters are available, they will be repeated, e.g. a field
	 * at (72|7) will have designation 'uu7'
	 * @param x field's x component
	 * @return alphabetical part of field designation
	 */
	private static String getAlphabeticalDesignation(int x) {
		String designation = "";
		int repetitions = 1 + x / FIELD_LETTERS.length();
		int index = x % FIELD_LETTERS.length();
		for(int i = 0; i < repetitions; i++) {
			designation += FIELD_LETTERS.charAt(index);
		}
		return designation;
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
	
	/**
	 * Parses a field from a given designation.
	 * If the designation is invalid, null is returned instead.
	 * @param designation Field designation (e.g. a8)
	 * @return Field constructed from designation or null
	 */
	public static Field getFieldFromDesignation(String designation) {
		// First we need to split the alphabetical and number part
		String[] splitDesignation = designation.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		
		if(splitDesignation.length != 2) {
			return null;
		}
		if((splitDesignation[0].length() + splitDesignation[1].length()) != designation.length()) {
			// If there are characters which are not part of either side of the
			// split (e.g. a minus sign) we have an invalid designation
			return null;
		}

		// For the alphabetical part, the number of repetitions is important
		int repetitions = splitDesignation[0].length();
		// Check if the repeated letter is the same every time
		for(int i = 1; i < repetitions; i++) {
			if(splitDesignation[0].charAt(i) != splitDesignation[0].charAt(0)) {
				return null;
			}
		}
		// Also get the index of the first letter for the non-repetitive component
		int index = FIELD_LETTERS.indexOf(splitDesignation[0].charAt(0));

		if(index < 0) {
			return null;
		}
		
		
		try {
    		return new Field((repetitions - 1) * FIELD_LETTERS.length() + index,
    				Integer.parseInt(splitDesignation[1]) - 1);
		} catch(NumberFormatException exc) {
			return null;
		}
	}
}
