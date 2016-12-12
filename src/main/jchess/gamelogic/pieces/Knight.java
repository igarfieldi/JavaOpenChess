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
 * Class to represent a chess pawn knight Knight's movements.
 */
public class Knight implements IPieceBehaviour
{
	@Override
	public Set<Direction> getNormalMovements()
	{
		return new HashSet<Direction>(Arrays.asList(new Direction[]{
				new Direction(-2, -1), new Direction(-2, 1),
		        new Direction(-1, -2), new Direction(-1, 2),
		        new Direction(1, -2), new Direction(1, 2),
		        new Direction(2, -1), new Direction(2, 1), }));
	}
	
	@Override
	public Set<Direction> getCapturingMovements()
	{
		return this.getNormalMovements();
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
