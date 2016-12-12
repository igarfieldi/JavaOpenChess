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
package jchess.gamelogic.pieces;

import java.awt.Image;

import jchess.gamelogic.Player;
import jchess.gui.ThemeImageLoader;
import jchess.util.Copyable;

/**
 * Class to represent a piece (any kind) - this class should be extended to
 * represent pawn, bishop etc.
 */
public class Piece implements Copyable<Piece>
{
	private Player player;
	private final String SYMBOL;
	private IPieceBehaviour behaviour;
	private boolean moved;
	
	public Piece(Player player, String symbol, IPieceBehaviour behaviour)
	{
		this.player = player;
		this.SYMBOL = symbol;
		this.behaviour = behaviour;
		this.moved = false;
	}
	
	public String getName() {
		return this.behaviour.getClass().getSimpleName();
	}
	
	public IPieceBehaviour getBehaviour()
	{
		return behaviour;
	}
	
	public String getSymbol()
	{
		return SYMBOL;
	}
	
	public boolean hasMoved()
	{
		return moved;
	}
	
	public void markAsMoved()
	{
		this.moved = true;
	}
	
	public void markAsUnmoved()
	{
		this.moved = false;
	}
	
	public Image getImage()
	{
		return ThemeImageLoader.getInstance().loadThemedPieceImage(this);
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public Piece copy()
	{
		return new Piece(player, SYMBOL, behaviour);
	}
}
