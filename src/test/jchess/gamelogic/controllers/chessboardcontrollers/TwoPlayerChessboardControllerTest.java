/**
 * 
 */
package jchess.gamelogic.controllers.chessboardcontrollers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IllegalMoveException;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.util.FileMapParser;

/**
 * @author Florian Bethe
 */
public class TwoPlayerChessboardControllerTest
{
	private Player white = new Player("p1", Color.WHITE);
	private Player black = new Player("p2", Color.BLACK);
	private TwoPlayerChessboardController controller;
	private IChessboardModel board;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		controller = new TwoPlayerChessboardController(
				null, TwoPlayerChessboardFactory.getInstance(), white, black);
		board = controller.getBoard();
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#save(jchess.util.FileMapParser)}.
	 */
	@Test
	public void testSave()
	{
		// TODO: cover more scenarios!
		try
		{
			controller.move(new Field(3, 6), new Field(3, 5));
			controller.move(new Field(6, 1), new Field(6, 2));
			controller.move(new Field(2, 7), new Field(4, 5));
			controller.move(new Field(6, 0), new Field(5, 2));
		} catch(IllegalMoveException e)
		{
			fail("Unexpected illegal move!");
		}
		FileMapParser saveFile = new FileMapParser();
		controller.save(saveFile);
		
		assertTrue(saveFile.getProperty("Moves").trim().equals("1. d7-d6 g2-g3 2. Bc8-e6 Ng1-f3"));
		assertTrue(saveFile.getProperty("WHITE").trim().equals(white.getName()));
		assertTrue(saveFile.getProperty("BLACK").trim().equals(black.getName()));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#load(jchess.util.FileMapParser)}.
	 */
	@Test
	public void testLoad()
	{
		FileMapParser gameLoader = new FileMapParser();
		String games[] = {
				"1. a7-a5 b2-b4 2. b8-c6 g2-g3"
		};
		
		gameLoader.setProperty("Moves", games[0]);
		controller.load(gameLoader);
		
		// This just tests the basic capability of loading a game; testing
		// correctness is done in the history test class
		
		//controller.getHistory().
		
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#switchToNextPlayer()}.
	 */
	@Test
	public void testSwitchToNextPlayer()
	{
		assertTrue(controller.getActivePlayer().equals(white));
		controller.switchToNextPlayer();
		assertTrue(controller.getActivePlayer().equals(black));
		controller.switchToNextPlayer();
		assertTrue(controller.getActivePlayer().equals(white));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#switchToPreviousPlayer()}.
	 */
	@Test
	public void testSwitchToPreviousPlayer()
	{
		assertTrue(controller.getActivePlayer().equals(white));
		controller.switchToPreviousPlayer();
		assertTrue(controller.getActivePlayer().equals(black));
		controller.switchToPreviousPlayer();
		assertTrue(controller.getActivePlayer().equals(white));
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#isChecked(jchess.gamelogic.Player)}.
	 */
	@Test
	public void testIsChecked()
	{
		
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#getPiecesCheckingPlayer(jchess.gamelogic.Player)}.
	 */
	@Test
	public void testGetPiecesCheckingPlayer()
	{
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#isCheckmated(jchess.gamelogic.Player)}.
	 */
	@Test
	public void testIsCheckmated()
	{
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#isStalemate()}.
	 */
	@Test
	public void testIsStalemate()
	{
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.chessboardcontrollers.RegularChessboardController#move(jchess.gamelogic.field.Field, jchess.gamelogic.field.Field)}.
	 */
	@Test
	public void testMove()
	{
		fail("Not yet implemented");
	}
	
}
