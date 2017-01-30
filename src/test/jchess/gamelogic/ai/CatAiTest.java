/**
 * 
 */
package jchess.gamelogic.ai;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.AiFourPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.AiFourPlayerChessboardFactory;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory;
import jchess.gamelogic.pieces.PieceFactory.PieceType;

/**
 * @author Florian Bethe
 */
public class CatAiTest
{
	private PieceFactory factory;
	private Player players[] = {
			new Player("p1", Color.WHITE),
			new Player("p2", Color.RED),
			new Player("p3", Color.BLACK),
			new Player("p4", Color.GOLDEN)
	};
	private Player catPlayer;
	private IChessboardController controller;
	private IChessboardModel board;
	private CatAi cat;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		factory = PieceFactory.getInstance();
		catPlayer = new Player("Cat", Color.SPECIAL);
		controller = new AiFourPlayerChessboardController(null,
				AiFourPlayerChessboardFactory.getInstance(),
				players[0], players[1], players[2], players[3],
				catPlayer);
		board = controller.getBoard();
		cat = new CatAi(controller, catPlayer);
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.ai.CatAi#isAlive()}.
	 */
	@Test
	public void testIsAlive()
	{
		assertTrue(cat.isAlive());
		
		// 'Kill' the cat
		for(Piece piece : board.getPieces(catPlayer)) {
			board.removePiece(board.getField(piece));
		}
		
		assertFalse(cat.isAlive());
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.ai.CatAi#updateRespawnTimer()}.
	 */
	@Test
	public void testUpdateRespawnTimer()
	{
		// 'Kill' the cat
		for(Piece piece : board.getPieces(catPlayer)) {
			board.removePiece(board.getField(piece));
		}

		// Cat should respawn after two turns
		cat.updateRespawnTimer();
		cat.updateRespawnTimer();
		cat.updateRespawnTimer();
		
		assertTrue(cat.isAlive());
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.ai.CatAi#updateSleepTimer()}.
	 */
	@Test
	public void testUpdateSleepTimer()
	{
		assertFalse(cat.isSleeping());

		// Put the cat to sleep
		cat.sleepCat();
		assertTrue(cat.isSleeping());

		cat.updateSleepTimer();
		cat.updateSleepTimer();
		cat.updateSleepTimer();

		assertFalse(cat.isSleeping());
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.ai.CatAi#canMove()}.
	 */
	@Test
	public void testCanMove()
	{
		assertTrue(cat.canMove());
		
		// 'Kill' the cat
		for(Piece piece : board.getPieces(catPlayer)) {
			board.removePiece(board.getField(piece));
		}
		assertFalse(cat.canMove());

		// Respawn cat
		cat.updateRespawnTimer();
		cat.updateRespawnTimer();
		cat.updateRespawnTimer();
		
		// Put the cat to sleep
		cat.sleepCat();
		assertFalse(cat.canMove());
		

		// 'Kill' the cat again
		for(Piece piece : board.getPieces(catPlayer)) {
			board.removePiece(board.getField(piece));
		}

		assertFalse(cat.canMove());
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.ai.CatAi#canCapture()}.
	 */
	@Test
	public void testCanCapture()
	{
		// Move the cat piece to the middle
		Piece catPiece = null;
		for(Piece piece : board.getPieces(catPlayer)) {
			catPiece = piece;
		}
		board.movePiece(catPiece, new Field(7, 7));
		
		assertFalse(cat.canCapture());
		
		// Place a single piece next to it
		Piece other = factory.buildPiece(players[0], null, PieceType.BISHOP);
		board.setPiece(new Field(4, 7), other);
		assertFalse(cat.canCapture());
		
		board.setPiece(new Field(6, 7), other);
		assertTrue(cat.canCapture());
		
		board.removePiece(new Field(6, 7));
		Piece king = factory.buildPiece(players[0], null, PieceType.KING);
		board.setPiece(new Field(6, 7), king);
		// Cat must not be able to capture kings!
		assertFalse(cat.canCapture());
		
	}
	
}
