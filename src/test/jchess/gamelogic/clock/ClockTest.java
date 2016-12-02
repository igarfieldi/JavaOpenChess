package jchess.gamelogic.clock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ClockTest
{
	Clock chessClock;
	
	@Before
	public void setUp() throws Exception
	{
		chessClock = new Clock();
	}
	
	@Test
	public void testDecrementByOne()
	{
		chessClock.resetClock(100);
		assertTrue(chessClock.decrement());
		assertEquals(chessClock.getSecondsLeft(), 99);
	}
	
	@Test
	public void testDecrementWithZero()
	{
		chessClock.resetClock(0);
		assertFalse(chessClock.decrement());
		assertEquals(chessClock.getSecondsLeft(), 0);
	}
	
}
