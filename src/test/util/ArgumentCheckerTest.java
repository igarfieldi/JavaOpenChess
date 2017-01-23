package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jchess.util.ArgumentChecker;

/**
 * Test class for ArgumentChecker.
 * @author Florian Bethe
 */
public class ArgumentCheckerTest
{
	
	@Before
	public void setUp() throws Exception
	{
	}
	
	/**
	 * Test if checkForNull correctly checks for null arguments.
	 */
	@Test
	public void testCheckForNull()
	{
		Object o1 = null;
		Object o2 = new Object();

		// Neither order nor count of arguments should matter; one null 
		// object has to be enough (and if none is null it shouldn't throw)
		assertTrue(throwsIllegalArgException(o1));
		assertTrue(throwsIllegalArgException(o1, o2));
		assertTrue(throwsIllegalArgException(o2, o1));
		assertTrue(throwsIllegalArgException(o2, o2, o1));
		assertFalse(throwsIllegalArgException(o2));
		assertFalse(throwsIllegalArgException(o2, o2, o2, o2));
	}
	
	private boolean throwsIllegalArgException(Object... objs) {
		try {
			ArgumentChecker.checkForNull(objs);
		} catch(IllegalArgumentException exc) {
			return true;
		}
		
		return false;
	}
}
