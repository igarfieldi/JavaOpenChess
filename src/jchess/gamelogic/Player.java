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
        BLACK,
        WHITE
    }

    public enum Type
    {
        LOCAL,
        NETWORK,
        COMPUTER
    }
    
    private String name;
    private Color color;
    private Type playerType;
    private boolean topSide;

    public Player()
    {
    	// TODO: default values?
    }

    public Player(String name, String color)
    {
        this.name = name;
        this.color = Player.Color.valueOf(color);
        this.topSide = false;
    }

    /** Method getting the players name
     *  @return name of player
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
    
    public boolean isTopSide()
    {
    	return topSide;
    }

    /** Method setting the players name
     *  @param name name of player
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Method setting the players type
     *  @param type type of player - enumerate
     */
    public void setType(Type type)
    {
        this.playerType = type;
    }
    
    public void setBoardSide(boolean top)
    {
    	this.topSide = top;
    }
}
