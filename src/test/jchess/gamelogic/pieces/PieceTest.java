package jchess.gamelogic.pieces;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PieceTest
{
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void testIsOut()
	{
		// Test all possible combinations of out-left, inside, out-right (top/bottom for y ofc)
		assertTrue(Piece.isout(-1, -1));
		assertTrue(Piece.isout(-1, 3));
		assertTrue(Piece.isout(-1, 9));
		assertTrue(Piece.isout(4, -1));
		assertFalse(Piece.isout(4, 3));
		assertTrue(Piece.isout(4, 9));
		assertTrue(Piece.isout(9, -1));
		assertTrue(Piece.isout(9, 3));
		assertTrue(Piece.isout(9, 9));
	}
	
	@Test
	public void testCheckPiece() {
		// TODO: check the various conditions: enemy piece, our piece, no piece, king(??)
		fail("Not implemented yet!");
	}
	
	@Test
	public void testOtherPlayer() {
		// TODO: check the three possibilities
		fail("Not implemented yet!");
	}
}
