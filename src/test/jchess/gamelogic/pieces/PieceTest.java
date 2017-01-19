/**
 * 
 */
package jchess.gamelogic.pieces;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

/**
 * @author Florian Bethe
 */
public class PieceTest
{
	Player players[] = {
			new Player("p1", Color.WHITE),
			new Player("p2", Color.BLACK),
			new Player("p3", Color.RED),
			new Player("p4", Color.GOLDEN),
	};
	List<Piece> pieces = new ArrayList<Piece>();
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		pieces.add(PieceFactory.getInstance().buildPiece(
				players[0], new Direction(0, -1), PieceType.PAWN));
		pieces.add(PieceFactory.getInstance().buildPiece(
				players[0], new Direction(0, -1), PieceType.BISHOP));
		pieces.add(PieceFactory.getInstance().buildPiece(
				players[1], new Direction(0, -1), PieceType.ROOK));
		pieces.add(PieceFactory.getInstance().buildPiece(
				players[2], new Direction(0, -1), PieceType.KNIGHT));
		pieces.add(PieceFactory.getInstance().buildPiece(
				players[2], new Direction(0, -1), PieceType.KING));
		pieces.add(PieceFactory.getInstance().buildPiece(
				players[3], new Direction(0, -1), PieceType.QUEEN));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.pieces.Piece#getName()}.
	 */
	@Test
	public void testGetName()
	{
		assertTrue(pieces.get(0).getName().equals("Pawn"));
		assertTrue(pieces.get(1).getName().equals("Bishop"));
		assertTrue(pieces.get(2).getName().equals("Rook"));
		assertTrue(pieces.get(3).getName().equals("Knight"));
		assertTrue(pieces.get(4).getName().equals("King"));
		assertTrue(pieces.get(5).getName().equals("Queen"));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.pieces.Piece#getBehaviour()}.
	 */
	@Test
	public void testGetBehaviour()
	{
		assertTrue(pieces.get(0).getBehaviour() instanceof Pawn);
		assertTrue(pieces.get(1).getBehaviour() instanceof Bishop);
		assertTrue(pieces.get(2).getBehaviour() instanceof Rook);
		assertTrue(pieces.get(3).getBehaviour() instanceof Knight);
		assertTrue(pieces.get(4).getBehaviour() instanceof King);
		assertTrue(pieces.get(5).getBehaviour() instanceof Queen);
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.pieces.Piece#getSymbol()}.
	 */
	@Test
	public void testGetSymbol()
	{
		assertTrue(pieces.get(0).getSymbol().equals(""));
		assertTrue(pieces.get(1).getSymbol().equals("B"));
		assertTrue(pieces.get(2).getSymbol().equals("R"));
		assertTrue(pieces.get(3).getSymbol().equals("N"));
		assertTrue(pieces.get(4).getSymbol().equals("K"));
		assertTrue(pieces.get(5).getSymbol().equals("Q"));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.pieces.Piece#hasMoved()}.
	 */
	@Test
	public void testHasMoved()
	{
		assertFalse(pieces.get(0).hasMoved());
		pieces.get(0).markAsMoved();
		assertTrue(pieces.get(0).hasMoved());
		pieces.get(0).markAsUnmoved();
		assertFalse(pieces.get(0).hasMoved());
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.pieces.Piece#getPlayer()}.
	 */
	@Test
	public void testGetPlayer()
	{
		assertTrue(pieces.get(0).getPlayer().equals(players[0]));
		assertTrue(pieces.get(1).getPlayer().equals(players[0]));
		assertTrue(pieces.get(2).getPlayer().equals(players[1]));
		assertTrue(pieces.get(3).getPlayer().equals(players[2]));
		assertTrue(pieces.get(4).getPlayer().equals(players[2]));
		assertTrue(pieces.get(5).getPlayer().equals(players[3]));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.pieces.Piece#copy()}.
	 */
	@Test
	public void testCopy()
	{
		Piece copy = pieces.get(0).copy();
		assertFalse(copy == pieces.get(0));
		assertTrue(copy.equals(pieces.get(0)));
	}
	
}
