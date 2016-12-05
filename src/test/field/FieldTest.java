package field;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.field.Field;

public class FieldTest
{
	
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void testConstruction() {
		Field normal = null;
		Field negative = null;
		
		try {
			normal = new Field(5, 3);
		} catch(Exception e) {}

		assertTrue(normal.getPosX() == 5);
		assertTrue(normal.getPosY() == 3);

		try {
			negative = new Field(-5, 2);
		} catch(Exception e) {}
		assertTrue(negative == null);
	}
	
	@Test
	public void testToString()
	{
		assertTrue(new Field(0, 0).toString().equals("a1"));
		assertTrue(new Field(7, 3).toString().equals("h4"));
		assertTrue(new Field(70, 10).toString().equals("ss11"));
	}
	
	@Test
	public void testEqualsObject()
	{
		Field f1 = new Field(5, 5);
		Field f2 = new Field(5, 5);
		Field f3 = new Field(3, 5);
		assertTrue(f1.equals(f2));
		assertFalse(f1.equals(f3));
	}
	
	@Test
	public void testGetFieldFromDesignation()
	{
		Field f1 = Field.getFieldFromDesignation("a1");
		assertTrue(f1.getPosX() == 0);
		assertTrue(f1.getPosY() == 0);
		Field f2 = Field.getFieldFromDesignation("d7");
		assertTrue(f2.getPosX() == 3);
		assertTrue(f2.getPosY() == 6);
		Field f3 = Field.getFieldFromDesignation("EE19");
		assertTrue(f3.getPosX() == 82);
		assertTrue(f3.getPosY() == 18);
		Field f4 = Field.getFieldFromDesignation("EE19f");
		assertTrue(f4 == null);
		Field f5 = Field.getFieldFromDesignation(".-1");
		assertTrue(f5 == null);
	}
	
}
