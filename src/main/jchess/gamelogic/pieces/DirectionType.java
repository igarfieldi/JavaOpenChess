package jchess.gamelogic.pieces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jchess.util.Direction;

/**
 * Contains various commonly used direction sets.
 * @author Florian Bethe
 */
public class DirectionType
{
	/**
	 * Returns the set of diagonal directions.
	 * @return Set of directions
	 */
	public static final Set<Direction> getDiagonalDirections()
	{
		return new HashSet<Direction>(Arrays.asList(new Direction[]{ new Direction(-1, -1), new Direction(1, 1),
		        new Direction(-1, 1), new Direction(1, -1) }));
	}

	/**
	 * Returns the set of straight directions.
	 * @return Set of directions
	 */
	public static final Set<Direction> getStraightDirections()
	{
		return new HashSet<Direction>(Arrays.asList(new Direction[]{ new Direction(-1, 0), new Direction(1, 0),
		        new Direction(0, -1), new Direction(0, 1) }));
	}

	/**
	 * Returns the set of directions which form a 'cone' around the provided direction.
	 * The directions have to be 'adjacent', i.e. its components must be in {-1, 0, 1}.
	 * Prime example for this is the pawn's capturing moves.
	 * @param cone Direction in which the cone shall point
	 * @return Set of directions
	 */
	public static final Set<Direction> getConeMovement(Direction cone)
	{
		Set<Direction> moves = new HashSet<Direction>();
		
		// Basically just a set of rules: 8 possible directions
		// => 8 possible sets of moves (and then utilize symmetry)
		if(cone.getX() != 0)
		{
			if(cone.getY() == 0) {
				moves.add(new Direction(cone.getX(), -1));
				moves.add(new Direction(cone.getX(), 1));
			} else {
				moves.add(new Direction(0, cone.getY()));
				moves.add(new Direction(cone.getX(), 0));
			}
		} else
		{
			if(cone.getX() == 0) {
				moves.add(new Direction(-1, cone.getY()));
				moves.add(new Direction(1, cone.getY()));
			} else {
				moves.add(new Direction(0, cone.getY()));
				moves.add(new Direction(cone.getX(), 0));
			}
		}
		
		return moves;
	}
}
