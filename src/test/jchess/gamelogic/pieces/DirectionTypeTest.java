/**
 * 
 */
package jchess.gamelogic.pieces;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import jchess.util.Direction;

/**
 * @author Florian Bethe
 */
public class DirectionTypeTest
{
	
	/**
	 * Test method for {@link jchess.gamelogic.pieces.DirectionType#getConeMovement(jchess.util.Direction)}.
	 */
	@Test
	public void testGetConeMovement()
	{
		Direction dirs[] = {
				new Direction(1, 0),
				new Direction(-1, 0),
				new Direction(0, 1),
				new Direction(0, -1),
				new Direction(1, 1),
				new Direction(-1, 1),
				new Direction(1, -1),
				new Direction(-1, -1)
		};

		// Test all the cones (for 'adjacent' moves!)
		assertTrue(containsExactly(DirectionType.getConeMovement(dirs[0]),
				dirs[4], dirs[6]));
		assertTrue(containsExactly(DirectionType.getConeMovement(dirs[1]),
				dirs[5], dirs[7]));
		assertTrue(containsExactly(DirectionType.getConeMovement(dirs[2]),
				dirs[4], dirs[5]));
		assertTrue(containsExactly(DirectionType.getConeMovement(dirs[3]),
				dirs[6], dirs[7]));
		assertTrue(containsExactly(DirectionType.getConeMovement(dirs[4]),
				dirs[0], dirs[2]));
		assertTrue(containsExactly(DirectionType.getConeMovement(dirs[5]),
				dirs[1], dirs[2]));
		assertTrue(containsExactly(DirectionType.getConeMovement(dirs[6]),
				dirs[0], dirs[3]));
		assertTrue(containsExactly(DirectionType.getConeMovement(dirs[7]),
				dirs[1], dirs[3]));
	}
	
	/**
	 * Checks whether the given set of direction contains exactly the two directions provided.
	 * @param dirs Set of directions to check
	 * @param d1 First direction to contain
	 * @param d2 Second direction to contain
	 * @return true if the set contains exactly d1 and d2
	 */
	private boolean containsExactly(Set<Direction> dirs, Direction d1, Direction d2) {
		if(dirs.size() != 2) {
			return false;
		}
		
		return dirs.contains(d1) && dirs.contains(d2);
	}
	
}
