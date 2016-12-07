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
 * Class to represent a chess pawn bishop Bishop can move across the chessboard
 *
 * |_|_|_|_|_|_|_|X|7
 * |X|_|_|_|_|_|X|_|6
 * |_|X|_|_| |X|_|_|5
 * |_|_|X|_|X|_|_|_|4
 * |_|_|_|B|_|_|_|_|3
 * |_| |X|_|X|_|_|_|2
 * |_|X|_|_|_|X|_|_|1
 * |X|_|_|_|_|_|X|_|0
 *  0 1 2 3 4 5 6 7
 */
public class Bishop extends Piece
{
	private static final Direction[] NORMAL_MOVEMENT = {
			new Direction(1, 1), new Direction(-1, 1),
	        new Direction(1, -1), new Direction(-1, -1) };
	
	@Override
	public Set<Direction> getNormalMovements()
	{
		return new HashSet<Direction>(Arrays.asList(Bishop.NORMAL_MOVEMENT));
	}
	
	@Override
	public Set<Direction> getCapturingMovements()
	{
		return new HashSet<Direction>(Arrays.asList(Bishop.NORMAL_MOVEMENT));
	}
	
	@Override
	public Bishop copy()
	{
		return new Bishop(player);
	}
	
	public Bishop(Player player)
	{
		super(player, "B", PieceType.BISHOP); // call initialiser of super type: Piece
	}
}
