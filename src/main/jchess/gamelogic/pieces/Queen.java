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

import jchess.gamelogic.Player;
import jchess.util.Direction;

/**
 * Class to represent a queen piece Queen can move almost in every way.
 */
public class Queen implements IPieceBehaviour
{
	@Override
	public Set<Direction> getNormalMovements()
	{
		Set<Direction> moves = new HashSet<Direction>();
		moves.addAll(DirectionType.getDiagonalDirections());
		moves.addAll(DirectionType.getStraightDirections());
		return moves;
	}
	
	@Override
	public Set<Direction> getCapturingMovements()
	{
		return this.getNormalMovements();
	}
	
	@Override
	public boolean canMoveMultipleSteps()
	{
		return true;
	}
	
	@Override
	public boolean canSkipOverPieces()
	{
		return false;
	}
}
