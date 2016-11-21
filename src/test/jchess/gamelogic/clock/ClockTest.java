package jchess.gamelogic.clock;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClockTest
{
	Clock chessClock;
	
	@Before
	public void setUp() throws Exception
	{
		chessClock = new Clock();
		chessClock.resetClock(100);
	}
	
	@Test
	public void testDecrement()
	{
		chessClock.decrement();
		assertEquals(chessClock.getSecondsLeft(), 99);
	}
	
}
