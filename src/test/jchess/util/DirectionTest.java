/**
 * 
 */
package jchess.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for util.Direction.
 * @author Florian Bethe
 */
public class DirectionTest
{
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}
	
	/**
	 * Test method for {@link jchess.util.Direction#hashCode()}.
	 */
	@Test
	public void testHashCode()
	{
		Direction d1 = new Direction(0, 0);
		Direction d2 = new Direction(2, 0);
		Direction d3 = new Direction(0, 7);
		Direction d4 = new Direction(-2, 3);
		Direction d5 = new Direction(2, -5);

		assertTrue(d1.hashCode() == 0);
		assertTrue(d2.hashCode() == 2);
		assertTrue(d3.hashCode() == 70000);
		assertTrue(d4.hashCode() == 29998);
		assertTrue(d5.hashCode() == -49998);
	}
	
	/**
	 * Test method for {@link jchess.util.Direction#Direction(int, int)}.
	 */
	@Test
	public void testConstruction()
	{
		Direction d1 = new Direction(0, 0);
		Direction d2 = new Direction(2, 5);
		Direction d3 = new Direction(-3, -4);
		Direction d4 = new Direction(d3);

		assertTrue(d1.getX() == 0 && d1.getY() == 0);
		assertTrue(d2.getX() == 2 && d2.getY() == 5);
		assertTrue(d3.getX() == -3 && d3.getY() == -4);
		assertTrue(d4.getX() == d3.getX() && d4.getY() == d3.getY());
	}
	
	/**
	 * Test method for {@link jchess.util.Direction#signum()}.
	 */
	@Test
	public void testSignum()
	{
		Direction d1 = new Direction(0, 0);
		Direction d2 = new Direction(1, 0);
		Direction d3 = new Direction(0, 3);
		Direction d4 = new Direction(-4, 0);
		Direction d5 = new Direction(3, -4);

		assertTrue(d1.signum().equals(new Direction(0, 0)));
		assertTrue(d2.signum().equals(new Direction(1, 0)));
		assertTrue(d3.signum().equals(new Direction(0, 1)));
		assertTrue(d4.signum().equals(new Direction(-1, 0)));
		assertTrue(d5.signum().equals(new Direction(1, -1)));
	}
	
	/**
	 * Test method for {@link jchess.util.Direction#add(jchess.util.Direction)}.
	 */
	@Test
	public void testAdd()
	{
		Direction d1 = new Direction(3, 2);
		Direction d2 = new Direction(1, 6);
		Direction d3 = new Direction(-6, -1);

		assertTrue(d1.add(d2).equals(new Direction(4, 8)));
		assertTrue(d1.add(d3).equals(new Direction(-3, 1)));
	}
	
	/**
	 * Test method for {@link jchess.util.Direction#subtract(jchess.util.Direction)}.
	 */
	@Test
	public void testSubtract()
	{
		Direction d1 = new Direction(3, 2);
		Direction d2 = new Direction(1, 6);
		Direction d3 = new Direction(-6, -1);

		assertTrue(d1.subtract(d2).equals(new Direction(2, -4)));
		assertTrue(d1.subtract(d3).equals(new Direction(9, 3)));
	}
	
	/**
	 * Test method for {@link jchess.util.Direction#multiply(int)}.
	 */
	@Test
	public void testMultiply()
	{
		Direction d1 = new Direction(3, 2);
		Direction d2 = new Direction(0, 1);
		Direction d3 = new Direction(-6, -1);

		assertTrue(d1.multiply(3).equals(new Direction(9, 6)));
		assertTrue(d1.multiply(-2).equals(new Direction(-6, -4)));
		assertTrue(d1.multiply(0).equals(new Direction(0, 0)));
		assertTrue(d2.multiply(3).equals(new Direction(0, 3)));
		assertTrue(d2.multiply(-2).equals(new Direction(0, -2)));
		assertTrue(d2.multiply(0).equals(new Direction(0, 0)));
		assertTrue(d3.multiply(3).equals(new Direction(-18, -3)));
		assertTrue(d3.multiply(-2).equals(new Direction(12, 2)));
		assertTrue(d3.multiply(0).equals(new Direction(0, 0)));
	}
	
	/**
	 * Test method for {@link jchess.util.Direction#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject()
	{
		Direction d1 = new Direction(2, 1);
		Direction d2 = new Direction(2, 1);
		Direction d3 = new Direction(3, 1);
		Direction d4 = new Direction(2, 0);
		Direction d5 = new Direction(-2, -1);
		Direction d6 = new Direction(-2, -1);

		assertTrue(d1.equals(d2));
		assertFalse(d1.equals(d3));
		assertFalse(d1.equals(d4));
		assertFalse(d1.equals(d5));
		assertTrue(d5.equals(d6));
	}
	
}
