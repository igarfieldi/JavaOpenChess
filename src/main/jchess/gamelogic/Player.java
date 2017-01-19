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
package jchess.gamelogic;

import java.io.Serializable;

/**
 * Class representing the player in the game
 */
public class Player implements Serializable
{
	private static final long serialVersionUID = 8138895278754660665L;
	
	public enum Color
	{
		WHITE, RED, BLACK, GOLDEN
	}
	
	public enum Type
	{
		LOCAL, COMPUTER
	}
	
	private String name;
	private Color color;
	private Type playerType;
	
	public Player(String name, Color color) {
		this(name, color, Type.LOCAL);
	}
	
	public Player(String name, Color color, Type type) {
		this.name = name;
		this.color = color;
		this.playerType = type;
	}
	
	/**
	 * Method getting the players name
	 * 
	 * @return name of player
	 */
	public String getName()
	{
		return this.name;
	}
	
	public Color getColor()
	{
		return this.color;
	}
	
	public Type getType()
	{
		return this.playerType;
	}
}
