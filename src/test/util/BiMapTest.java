package util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import jchess.util.BiMap;

/**
 * Test class for the BiMap.
 * @author Florian Bethe
 */
public class BiMapTest
{
	BiMap<String, String> map;
	
	@Before
	public void setUp() throws Exception
	{
		map = new BiMap<String, String>();
	}
	
	/**
	 * Tests putting a key-value pair into the map.
	 */
	@Test
	public void testPutRegular()
	{
		map.put("a", "1");
		assertTrue(map.get("a").equals("1"));
		assertTrue(map.inverse().get("1").equals("a"));
	}
	
	/**
	 * Tests putting a key-value pair into the inverse of the map.
	 */
	@Test
	public void testPutInverse()
	{
		map.inverse().put("a", "1");
		assertTrue(map.get("a").equals("1"));
		assertTrue(map.inverse().get("1").equals("a"));
	}
	
	/**
	 * Tests putting a key-value pair when one part of it already has a
	 * different mapping.
	 */
	@Test
	public void testPutOverwritePartial()
	{
		map.put("a", "1");
		map.put("b", "2");
		
		map.put("a", "3");
		assertTrue(map.get("a").equals("3"));
		assertTrue(map.inverse().get("3").equals("a"));
		assertTrue(map.get("b").equals("2"));
		assertTrue(map.inverse().get("1") == null);
		
		map.put("c", "3");
		assertTrue(map.get("c").equals("3"));
		assertTrue(map.inverse().get("3").equals("c"));
		assertTrue(map.get("b").equals("2"));
		assertTrue(map.get("a") == null);
	}
	
	/**
	 * Tests putting a key-value pair when both parts are already mapped
	 * differently.
	 */
	@Test
	public void testPutOverwriteBoth()
	{
		map.put("a", "1");
		map.put("b", "2");
		
		map.put("a", "2");
		assertTrue(map.get("a").equals("2"));
		assertTrue(map.inverse().get("2").equals("a"));
		assertTrue(map.get("b") == null);
		assertTrue(map.inverse().get("1") == null);
	}
	
	/**
	 * Tests putting a different map into the bi-map.
	 */
	@Test
	public void testPutAll()
	{
		Map<String, String> m = new HashMap<String, String>();
		m.put("a", "1");
		m.put("b", "3");
		m.put("c", "2");
		map.putAll(m);
		assertTrue(map.get("a").equals("1"));
		assertTrue(map.get("b").equals("3"));
		assertTrue(map.get("c").equals("2"));
		assertTrue(map.inverse().get("1").equals("a"));
		assertTrue(map.inverse().get("3").equals("b"));
		assertTrue(map.inverse().get("2").equals("c"));
	}
	
	/**
	 * Tests removing an existing key-value pair both inverse and regular.
	 */
	@Test
	public void testRemove()
	{
		map.put("a", "1");
		map.put("b", "2");
		map.remove("a");
		assertTrue(map.get("a") == null);
		assertTrue(map.inverse().get("1") == null);
		map.inverse().remove("2");
		assertTrue(map.get("b") == null);
		assertTrue(map.inverse().get("2") == null);
	}
	
}
