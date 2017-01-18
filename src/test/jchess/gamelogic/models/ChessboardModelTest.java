/**
 * 
 */
package jchess.gamelogic.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

/**
 * @author Florian Bethe
 */
public class ChessboardModelTest
{
	private ChessboardModel model;
	private Player[] players = {
			new Player("p1", Color.WHITE),
			new Player("p2", Color.BLACK)
	};
	private Field[] fields = {
			new Field(0, 0),
			new Field(1, 0),
			new Field(2, 0),
			new Field(3, 0),
			new Field(1, 1),
			new Field(2, 1)
	};
	private Piece[] pieces = {
			PieceFactory.getInstance().buildPiece(players[0],
					new Direction(0, -1), PieceType.PAWN),
			PieceFactory.getInstance().buildPiece(players[0],
					new Direction(0, -1), PieceType.BISHOP),
			PieceFactory.getInstance().buildPiece(players[0],
					new Direction(0, -1), PieceType.QUEEN),
			PieceFactory.getInstance().buildPiece(players[1],
					new Direction(0, 1), PieceType.KNIGHT),
			PieceFactory.getInstance().buildPiece(players[1],
					new Direction(0, 1), PieceType.ROOK)
	};
	private Direction dirs[] = {
			new Direction(1, 0),
			new Direction(-1, 0),
			new Direction(0, 1),
			new Direction(0, -1),
			new Direction(1, 1),
			new Direction(-1, 1),
			new Direction(1, -1),
			new Direction(-1, -1)
	};
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		model = new ChessboardModel();

		for(Field field : fields) {
			model.addField(field);
		}
		
		model.setPiece(fields[0], pieces[0]);
		model.setPiece(fields[1], pieces[1]);
		model.setPiece(fields[2], pieces[2]);
		model.setPiece(fields[3], pieces[3]);
		model.setPiece(fields[4], pieces[4]);
	}
	
	/**
	 * Test method for adding and getting the fields.
	 */
	@Test
	public void testAddAndGetField()
	{
		assertTrue(model.getField(0, 0).equals(fields[0]));
		assertTrue(model.getField(1, 0).equals(fields[1]));
		assertTrue(model.getField(2, 0).equals(fields[2]));
		assertTrue(model.getField(3, 0).equals(fields[3]));
		assertTrue(model.getField(1, 1).equals(fields[4]));
		assertTrue(model.getField(2, 1).equals(fields[5]));
		assertTrue(model.getField(4, 0) == null);
		assertTrue(model.getField(1, 2) == null);
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#getField(jchess.gamelogic.pieces.Piece)}.
	 */
	@Test
	public void testGetFieldPiece()
	{
		assertTrue(model.getField(pieces[0]).equals(fields[0]));
		assertTrue(model.getField(pieces[1]).equals(fields[1]));
		assertTrue(model.getField(pieces[2]).equals(fields[2]));
		assertTrue(model.getField(pieces[3]).equals(fields[3]));
		assertTrue(model.getField(pieces[4]).equals(fields[4]));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#getFields()}.
	 */
	@Test
	public void testGetFields()
	{
		assertTrue(containsExactly(model.getFields(), fields));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#getFieldInDirection(jchess.gamelogic.field.Field, jchess.util.Direction)}.
	 */
	@Test
	public void testGetFieldInDirection()
	{
		assertTrue(model.getFieldInDirection(fields[0], dirs[0]).equals(fields[1]));
		assertTrue(model.getFieldInDirection(fields[3], dirs[1]).equals(fields[2]));
		assertTrue(model.getFieldInDirection(fields[1], dirs[2]).equals(fields[4]));
		assertTrue(model.getFieldInDirection(fields[4], dirs[3]).equals(fields[1]));
		assertTrue(model.getFieldInDirection(fields[1], dirs[4]).equals(fields[5]));
		assertTrue(model.getFieldInDirection(fields[2], dirs[5]).equals(fields[4]));
		assertTrue(model.getFieldInDirection(fields[4], dirs[6]).equals(fields[2]));
		assertTrue(model.getFieldInDirection(fields[5], dirs[7]).equals(fields[1]));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#getFieldsInDirection(jchess.gamelogic.field.Field, jchess.util.Direction)}.
	 */
	@Test
	public void testGetFieldsInDirection()
	{
		assertTrue(containsExactly(model.getFieldsInDirection(fields[0], dirs[0]),
				fields[1], fields[2], fields[3]));
		assertTrue(containsExactly(model.getFieldsInDirection(fields[3], dirs[1]),
				fields[0], fields[1], fields[2]));
		assertTrue(containsExactly(model.getFieldsInDirection(fields[1], dirs[2]),
				fields[4]));
		assertTrue(containsExactly(model.getFieldsInDirection(fields[4], dirs[3]),
				fields[1]));
		assertTrue(containsExactly(model.getFieldsInDirection(fields[1], dirs[4]),
				fields[5]));
		assertTrue(containsExactly(model.getFieldsInDirection(fields[2], dirs[5]),
				fields[4]));
		assertTrue(containsExactly(model.getFieldsInDirection(fields[4], dirs[6]),
				fields[2]));
		assertTrue(containsExactly(model.getFieldsInDirection(fields[5], dirs[7]),
				fields[1]));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#getPieces()}.
	 */
	@Test
	public void testGetPieces()
	{
		assertTrue(containsExactly(model.getPieces(), pieces));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#getPieces(jchess.gamelogic.Player)}.
	 */
	@Test
	public void testGetPiecesPlayer()
	{
		assertTrue(containsExactly(model.getPieces(players[0]),
				pieces[0], pieces[1], pieces[2]));
		assertTrue(containsExactly(model.getPieces(players[1]),
				pieces[3], pieces[4]));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#getPiece(jchess.gamelogic.field.Field)}.
	 */
	@Test
	public void testGetPiece()
	{
		assertTrue(model.getPiece(fields[0]).equals(pieces[0]));
		assertTrue(model.getPiece(fields[1]).equals(pieces[1]));
		assertTrue(model.getPiece(fields[2]).equals(pieces[2]));
		assertTrue(model.getPiece(fields[3]).equals(pieces[3]));
		assertTrue(model.getPiece(fields[4]).equals(pieces[4]));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#movePiece(jchess.gamelogic.pieces.Piece, jchess.gamelogic.field.Field)}.
	 */
	@Test
	public void testMovePiece()
	{
		assertTrue(model.movePiece(pieces[0], fields[5]) == null);
		assertTrue(model.getPiece(fields[0]) == null);
		assertTrue(model.getPiece(fields[5]).equals(pieces[0]));
		assertTrue(model.getField(pieces[0]).equals(fields[5]));
		
		assertTrue(model.movePiece(pieces[0], fields[1]).equals(pieces[1]));
		assertTrue(model.getPiece(fields[5]) == null);
		assertTrue(model.getPiece(fields[1]).equals(pieces[0]));
		assertTrue(model.getField(pieces[0]).equals(fields[1]));
		assertTrue(model.getField(pieces[1]) == null);
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#removePiece(jchess.gamelogic.field.Field)}.
	 */
	@Test
	public void testRemovePiece()
	{
		assertTrue(model.removePiece(fields[0]).equals(pieces[0]));
		assertTrue(model.getField(pieces[0]) == null);
		assertTrue(model.getPiece(fields[0]) == null);
		assertTrue(model.removePiece(fields[0]) == null);
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.models.ChessboardModel#copy()}.
	 */
	@Test
	public void testCopy()
	{
		Field f1 = new Field(6, 4);
		Piece p1 = PieceFactory.getInstance().buildPiece(
				players[0], new Direction(1, 1), PieceType.KING);
		
		IChessboardModel copy = model.copy();
		copy.addField(f1);
		copy.setPiece(f1, p1);
		
		assertFalse(model.getFields().contains(f1));
		assertFalse(model.getPieces().contains(p1));
	}
	
	/**
	 * Checks whether a given collection (e.g. a set) contains exactly the elements specified.
	 * TODO: wtf is heap pollution?
	 * @param set Collection to check contents of
	 * @param args Arguments that exactly are in the collection
	 * @return true if the collections contains exactly args
	 */
	private <T> boolean containsExactly(Collection<T> set, T...args) {
		if(set.size() != args.length) {
			return false;
		}
		
		for(T t : args) {
			if(!set.contains(t)) {
				return false;
			}
		}
		
		return true;
	}
	
}
