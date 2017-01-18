/**
 * 
 */
package jchess.gamelogic.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian Bethe
 */
public class GameClockModelTest
{
	
	/**
	 * Test method for {@link jchess.gamelogic.models.GameClockModel#getClock(int)}.
	 */
	@Test
	public void testGetClock()
	{
		GameClockModel c1 = new GameClockModel(2);
		assertTrue(c1.getClock(0) != null);
		assertTrue(c1.getClock(1) != null);
		assertFalse(c1.getClock(0).equals(c1.getClock(1)));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.GameClockModel#getClocks()}.
	 */
	@Test
	public void testGetClocks()
	{
		GameClockModel c1 = new GameClockModel();
		GameClockModel c2 = new GameClockModel(4);
		assertTrue(c1.getClocks().size() == 0);
		assertTrue(c2.getClocks().size() == 4);
	}
	
}
