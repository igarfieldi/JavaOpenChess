package jchess.gamelogic.pieces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jchess.util.Direction;

public class DirectionType
{
	public static final Set<Direction> getDiagonalDirections()
	{
		return new HashSet<Direction>(Arrays.asList(new Direction[]{ new Direction(-1, -1), new Direction(1, 1),
		        new Direction(-1, 1), new Direction(1, -1) }));
	}
	
	public static final Set<Direction> getStraightDirections()
	{
		return new HashSet<Direction>(Arrays.asList(new Direction[]{ new Direction(-1, 0), new Direction(1, 0),
		        new Direction(0, -1), new Direction(0, 1) }));
	}
	
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
