/**
 * 
 */
package jchess.gamelogic.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

/**
 * @author Florian Bethe
 */
public class MoveTest
{
	
	/**
	 * Test method for {@link jchess.gamelogic.field.Move#wasPawnTwoFieldsMove()}.
	 * Test the detection of pawn two-field moves.
	 */
	@Test
	public void testWasPawnTwoFieldsMove()
	{
		Piece pawns[] = {
				PieceFactory.getInstance().buildPiece(null, new Direction(1, 0), PieceType.PAWN),
				PieceFactory.getInstance().buildPiece(null, new Direction(-1, 0), PieceType.PAWN),
				PieceFactory.getInstance().buildPiece(null, new Direction(0, 1), PieceType.PAWN),
				PieceFactory.getInstance().buildPiece(null, new Direction(0, -1), PieceType.PAWN)
		};
		Piece nonPawns[] = {
				PieceFactory.getInstance().buildPiece(null, new Direction(1, 0), PieceType.BISHOP),
				PieceFactory.getInstance().buildPiece(null, new Direction(1, 0), PieceType.ROOK),
				PieceFactory.getInstance().buildPiece(null, new Direction(1, 0), PieceType.QUEEN),
				PieceFactory.getInstance().buildPiece(null, new Direction(1, 0), PieceType.KNIGHT),
				PieceFactory.getInstance().buildPiece(null, new Direction(1, 0), PieceType.KING)
		};
		// All fields are meant to be used with origin field of 3,3!
		Field origin = new Field(3, 3);
		Field fields[] = {
				new Field(5, 3),		// Potentially successful fields (depends on direction)
				new Field(1, 3),
				new Field(3, 5),
				new Field(3, 1),
				new Field(4, 3),		// Failing fields
				new Field(2, 3),
				new Field(3, 4),
				new Field(3, 2),
				new Field(4, 5),		// Off-ball field (pawns usually only move straight!)
				new Field(6, 3),		// Too far field
		};

		// First check the pawns (in each direction!)
		assertTrue(new Move(origin, fields[0], pawns[0]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[1], pawns[0]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[2], pawns[0]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[3], pawns[0]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[4], pawns[0]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[5], pawns[0]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[6], pawns[0]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[7], pawns[0]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[8], pawns[0]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[9], pawns[0]).wasPawnTwoFieldsMove());

		assertFalse(new Move(origin, fields[0], pawns[1]).wasPawnTwoFieldsMove());
		assertTrue(new Move(origin, fields[1], pawns[1]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[2], pawns[1]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[3], pawns[1]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[4], pawns[1]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[5], pawns[1]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[6], pawns[1]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[7], pawns[1]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[8], pawns[1]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[9], pawns[1]).wasPawnTwoFieldsMove());

		assertFalse(new Move(origin, fields[0], pawns[2]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[1], pawns[2]).wasPawnTwoFieldsMove());
		assertTrue(new Move(origin, fields[2], pawns[2]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[3], pawns[2]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[4], pawns[2]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[5], pawns[2]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[6], pawns[2]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[7], pawns[2]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[8], pawns[2]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[9], pawns[2]).wasPawnTwoFieldsMove());

		assertFalse(new Move(origin, fields[0], pawns[3]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[1], pawns[3]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[2], pawns[3]).wasPawnTwoFieldsMove());
		assertTrue(new Move(origin, fields[3], pawns[3]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[4], pawns[3]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[5], pawns[3]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[6], pawns[3]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[7], pawns[3]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[8], pawns[3]).wasPawnTwoFieldsMove());
		assertFalse(new Move(origin, fields[9], pawns[3]).wasPawnTwoFieldsMove());
		
		// Now test all the other pieces - none of these should pass!
		for(Piece piece : nonPawns) {
			for(Field field : fields) {
				assertFalse(new Move(origin, field, piece).wasPawnTwoFieldsMove());
			}
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.field.Move#getPromotedPiece()}.
	 */
	@Test
	public void testGetPromotedPiece()
	{
		Piece pawn = PieceFactory.getInstance().buildPiece(null, new Direction(1, 0), PieceType.PAWN);
		Piece nonPawn = PieceFactory.getInstance().buildPiece(null, new Direction(0, 0), PieceType.QUEEN);
		Piece promoted = PieceFactory.getInstance().buildPiece(null, new Direction(0, 0), PieceType.QUEEN);

		// Only pawns can be promoted!
		assertTrue(new Move(new Field(0, 0), new Field(0, 1), pawn, null,
				CastlingType.NONE, false, null).getPromotedPiece() == null);
		assertTrue(new Move(new Field(0, 0), new Field(0, 1), pawn, null,
				CastlingType.NONE, false, promoted).getPromotedPiece().equals(promoted));
		assertTrue(new Move(new Field(0, 0), new Field(0, 1), nonPawn, null,
				CastlingType.NONE, false, null).getPromotedPiece() == null);
		assertTrue(new Move(new Field(0, 0), new Field(0, 1), nonPawn, null,
				CastlingType.NONE, false, promoted).getPromotedPiece() == null);
	}
	
}
