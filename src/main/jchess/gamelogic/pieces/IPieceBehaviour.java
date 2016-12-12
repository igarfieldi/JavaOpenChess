package jchess.gamelogic.pieces;

import java.util.Set;

import jchess.util.Direction;

/**
 * Determines the behaviour of a piece.
 * This counts movements as well as restriction on those.
 * @author Florian Bethe
 */
public interface IPieceBehaviour
{
	public Set<Direction> getNormalMovements();
	
	public Set<Direction> getCapturingMovements();
	
	/**
	 * Can the piece move multiple times along a single direction.
	 * @return true if it can
	 */
	public boolean canMoveMultipleSteps();
	
	/**
	 * Can a piece skip over other pieces on the board.
	 * @return true if it can
	 */
	public boolean canSkipOverPieces();
}
