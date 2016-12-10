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
 * Class to represent a pawn piece Pawn can move only forward and can beat only
 * across In first move pawn can move 2 squares Pawn can be upgrade to rook,
 * knight, bishop, Queen if it's in the squares nearest the side where opponent
 * is located First move of pawn:
 *
|_|_|_|_|_|_|_|_|7
|_|_|_|_|_|_|_|_|6
|_|_|_|X|_|_|_|_|5
|_|_|_|X|_|_|_|_|4
|_|_|_|P|_|_|_|_|3
|_|_|_|_|_|_|_|_|2
|_|_|_|_|_|_|_|_|1
|_|_|_|_|_|_|_|_|0
0 1 2 3 4 5 6 7
 *
 * Movement of a pawn:
 *       
|_|_|_|_|_|_|_|_|7
|_|_|_|_|_|_|_|_|6
|_|_|_|_|_|_|_|_|5
|_|_|_|X|_|_|_|_|4
|_|_|_|P|_|_|_|_|3
|_|_|_|_|_|_|_|_|2
|_|_|_|_|_|_|_|_|1
|_|_|_|_|_|_|_|_|0
0 1 2 3 4 5 6 7
 *
 * Beats with can take pawn:
 *
|_|_|_|_|_|_|_|_|7
|_|_|_|_|_|_|_|_|6
|_|_|_|_|_|_|_|_|5
|_|_|X|_|X|_|_|_|4
|_|_|_|P|_|_|_|_|3
|_|_|_|_|_|_|_|_|2
|_|_|_|_|_|_|_|_|1
|_|_|_|_|_|_|_|_|0
0 1 2 3 4 5 6 7
 */
public class Pawn extends Piece
{
	private Direction normalMovement;
	private Direction twoStepMovement;
	private Direction[] capturingMovement;
	
	@Override
	public Set<Direction> getNormalMovements() {
		Set<Direction> movements = new HashSet<Direction>();
		movements.add(normalMovement);
		
		// If the pawn has not moved yet it may move two fields at once
		if(!this.hasMoved()) {
			movements.add(twoStepMovement);
		}
		return movements;
	}
	
	public Direction getForwardDirection() {
		return normalMovement;
	}
	
	@Override
	public Set<Direction> getCapturingMovements() {
		return new HashSet<Direction>(Arrays.asList(this.capturingMovement));
	}
	
	public Pawn(Player player, Direction forward)
	{
		// Apparently pawns as the basic figure do not get a symbol...
		super(player, "", false, PieceType.PAWN);
		this.normalMovement = forward;
		this.twoStepMovement = forward.multiply(2);
		this.capturingMovement = new Direction[2];
		
		// TODO: find a more independent way to determine this!
		if(forward.getX() != 0) {
			capturingMovement[0] = new Direction(forward.getX(), -1);
			capturingMovement[1] = new Direction(forward.getX(), 1);
		} else {
			capturingMovement[0] = new Direction(-1, forward.getY());
			capturingMovement[1] = new Direction(1, forward.getY());
		}
	}
	
	@Override
	public Pawn copy() {
		return new Pawn(player, normalMovement);
	}
	
	void promote(Piece newPiece)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
