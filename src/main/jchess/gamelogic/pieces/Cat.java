package jchess.gamelogic.pieces;

import java.util.HashSet;
import java.util.Set;

import jchess.util.Direction;

public class Cat implements IPieceBehaviour
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
		return false;
	}
	
	@Override
	public boolean canSkipOverPieces()
	{
		return false;
	}
	
}
