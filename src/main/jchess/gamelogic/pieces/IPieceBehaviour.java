package jchess.gamelogic.pieces;

import java.util.Set;

import jchess.util.Direction;

public interface IPieceBehaviour
{
	public Set<Direction> getNormalMovements();
	
	public Set<Direction> getCapturingMovements();
	
	public boolean canMoveMultipleSteps();
	
	public boolean canSkipOverPieces();
}
