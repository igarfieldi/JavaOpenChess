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
	 * Prime example for this is the pawn's capturing moves.
	 * @param cone Direction in which the cone shall point
	 * @return Set of directions
	 */
	public static final Set<Direction> getConeMovement(Direction cone)
	{
		Set<Direction> moves = new HashSet<Direction>();
		
		if(cone.getX() != 0)
		{
			moves.add(new Direction(cone.getX(), -1));
			moves.add(new Direction(cone.getX(), 1));
		} else
		{
			moves.add(new Direction(-1, cone.getY()));
			moves.add(new Direction(1, cone.getY()));
		}
		
		return moves;
	}
}
