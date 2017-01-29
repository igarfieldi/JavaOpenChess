package jchess.gamelogic.pieces;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.IllegalMoveException;
import jchess.gamelogic.controllers.chessboardcontrollers.FourPlayerChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.FourPlayerChessboardFactory;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
import jchess.util.Direction;

/**
 * Unit test for the pawn behaviour.
 * Only tests its en passant move. The rest is covered in
 * PieceMovementTest.
 * @author Florian Bethe
 */
@RunWith(Parameterized.class)
public class PawnEnPassantTest
{
	private PieceFactory factory;

	private IChessboardController controller;
	private IChessboardModel board;
	private Player players[];
	
	private Direction direction;
	
	public PawnEnPassantTest(IChessboardController controller,
			Player players[], Direction direction)
	{
		this.controller = controller;
		this.players = players;
		
		this.direction = direction;
	}
	
	/**
	 * Specifies the parameter set with which the test should be carried out.
	 * @return Collection of parameter sets
	 */
	@Parameterized.Parameters
	public static Collection<Object[]> testCombinations() {
		Player players2p[] = {
				new Player("p1", Color.WHITE),
				new Player("p2", Color.BLACK)
		};
		Player players4p[] = {
				new Player("p1", Color.WHITE),
				new Player("p2", Color.RED),
				new Player("p3", Color.BLACK),
				new Player("p4", Color.GOLDEN)
		};
		IChessboardController controller2p = new TwoPlayerChessboardController(
				null, TwoPlayerChessboardFactory.getInstance(),
				players2p[0], players2p[1]);
		IChessboardController controller4p = new FourPlayerChessboardController(
				null, FourPlayerChessboardFactory.getInstance(),
				players4p[0], players4p[1], players4p[2], players4p[3]);
		
		return Arrays.asList(new Object[][] {
			{controller2p, players2p, new Direction(0, 1)},
			{controller2p, players2p, new Direction(0, -1)},
			//{controller4p, players4p, new Direction(1, 0)},
			//{controller4p, players4p, new Direction(-1, 0)},
			//{controller4p, players4p, new Direction(0, 1)},
			//{controller4p, players4p, new Direction(0, -1)},
			// Potentially enable these if 4p gets en passant
		});
	}
	
	@Before
	public void setUp() throws Exception
	{
		this.factory = PieceFactory.getInstance();
		this.board = controller.getBoard();
	}
	
	/**
	 * Tests the pawn's capability of performing en passant moves.
	 * Checks all possible combinations of fields for the two pawns.
	 */
	@Test
	public void testEnPassantWithPawn() {
		Set<Field> testFields = board.getFields();
		
		// Test for all players
		for(Player player : players) {
			Piece pawn = factory.buildPiece(player, direction, PieceType.PAWN);
			
			// Test for all fields of the board
			for(Field pawnField : testFields) {
				board.setPiece(pawnField, pawn);
				
				// Place the enemy pawn on fields not yet occupied
				for(Field testField : board.getFields()) {
					if(testField.equals(pawnField)) {
						continue;
					}
					
					// Also vary the player for the enemy pawn
					for(Player enemy : controller.getEnemies(player)) {
						Piece twoMoved = factory.buildPiece(enemy,
								direction.invert(), PieceType.PAWN);
						board.setPiece(testField, twoMoved);
						
						// Attempt to move the pawn by two fields to
						// create the en passant scenario
						try {
							Field twoMovedField = new Field(
									testField.getPosX() - 2*direction.getX(),
									testField.getPosY() - 2*direction.getY());
							// Where 'our' pawn should be able to move after
							Field enPassantField = new Field(
									testField.getPosX() - direction.getX(),
									testField.getPosY() - direction.getY());
							
							// Clear the history to avoid pollution by any previous moves
							controller.getHistory().clearHistory();
							// Execute the move
							controller.move(testField, twoMovedField);
							
							// If the two field moved pawn is right next
							// to our pawn (and perpendicular to its direction)
							// it should be able to be taken en passant
							if(this.isNextToAndPerpendicular(
									pawnField, direction, twoMovedField)) {
								assertTrue(PieceTestSupport.canMakeMove(
										controller, pawn, enPassantField));
							}
							
							board.removePiece(twoMovedField);
						} catch(IllegalMoveException|IllegalArgumentException exc) {
							// Cannot be moved by two fields (e.g. end of board
							// or field is already occupied)
						}
						
						board.removePiece(testField);
					}
				}
				
				board.removePiece(pawnField);
			}
		}
	}
	
	/**
	 * Checks if the target field is next to the attacker field.
	 * Also makes sure it is perpendicular to the given direction (90 deg).
	 * @param attacker Field of the attacker
	 * @param dir Direction to use as reference for perpendicularity
	 * @param target Target field
	 * @return True if check is met
	 */
	private boolean isNextToAndPerpendicular(Field attacker, Direction dir,
			Field target) {
		Direction perpendicular = dir.rotate90Deg();
		
		try {
			Field nextToField = new Field(
					attacker.getPosX() + perpendicular.getX(),
					attacker.getPosY() + perpendicular.getY());
			if(nextToField.equals(target)) {
				return true;
			}
		} catch(IllegalArgumentException exc) {
		}
		
		try {
			Field nextToField = new Field(
					attacker.getPosX() - perpendicular.getX(),
					attacker.getPosY() - perpendicular.getY());
			if(nextToField.equals(target)) {
				return true;
			}
		} catch(IllegalArgumentException exc) {
			// Field doesn't exist
		}
		
		return false;
	}
}
