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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jchess.util.Direction;

/**
 * Class to represent a pawn piece Pawn can move only forward and can beat only
 * across In first move pawn can move 2 squares Pawn can be upgrade to rook,
 * knight, bishop, Queen if it's in the squares nearest the side where opponent
 * is located First move of pawn.
 */
public class Pawn implements IPieceBehaviour
{
	private Direction forward;
	
	public Pawn(Direction forward)
	{
		this.forward = forward;
	}
	
	@Override
	public Set<Direction> getNormalMovements()
	{
		return new HashSet<Direction>(Arrays.asList(new Direction[]{ forward }));
	}
	
	@Override
	public Set<Direction> getCapturingMovements()
	{
		return DirectionType.getConeMovement(forward);
	}
	
	@Override
	public boolean canMoveMultipleSteps()
	{
		return false;
	}
	
	@Override
	public boolean canSkipOverPieces()
	{
		return false;
	}
}
