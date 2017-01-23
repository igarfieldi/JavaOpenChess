/**
 * 
 */
package jchess.gamelogic.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;

/**
 * Tests the GameClockController for differing player counts.
 * @author Florian Bethe
 */
public class GameClockControllerTest
{
	private Player lotsOfPlayers[] = {
			new Player("p1", Color.WHITE),
			new Player("p2", Color.RED),
			new Player("p3", Color.BLACK),
			new Player("p4", Color.GOLDEN),
			new Player("p5", Color.BLACK),
			new Player("p6", Color.RED),
			new Player("p7", Color.WHITE)
	};
	
	private List<GameClockController> controllers;
	private List<GameStateHandler> handlers;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		controllers = new ArrayList<>();
		handlers = new ArrayList<>();
		
		for(int i = 0; i < lotsOfPlayers.length; i++) {
			Player currentPlayerPool[] = new Player[i+1];
			for(int j = 0; j <= i; j++) {
				currentPlayerPool[j] = lotsOfPlayers[j];
			}
			
			GameClockController controller = new GameClockController(60, currentPlayerPool);
			// Set the state handler for checking
			GameStateHandler handler = new GameStateHandler();
			controller.setStateHandler(handler);
			controller.start();
			
			// Add both controller and handler to the list
			controllers.add(controller);
			handlers.add(handler);
			
			// No asserts needed; no exception is enough for construction
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.GameClockController#switchClocks()}.
	 * Takes at least 7 seconds to complete due to necessary Thread.sleep.
	 * 
	 */
	@Test
	public void testSwitchClockAndOnTimeOut() throws InterruptedException
	{
		for(int i = 0; i < controllers.size(); i++) {
			GameClockController controller = controllers.get(i);
			GameStateHandler handler = handlers.get(i);
			handler.reset();
			controller.setTimes(60);
			
			// No timeover should occur since it doesn't run for long enough
			for(int j = 0; j <= i; j++) {
				assertFalse(handler.triggeredTimeOver());
				controller.switchClocks();
			}

			handler.reset();
			controller.setTimes(0);

			Thread.sleep(1050);
			
			// Now the timeover should happen for every clock
			for(int j = 0; j <= i; j++) {
				// Slight delay to give the clock thread a chance to trigger
				Thread.sleep(10);
				assertTrue(handler.triggeredTimeOver());
				controller.switchClocks();
			}
		}
	}
	
	/**
	 * Test method for {@link jchess.gamelogic.controllers.GameClockController#setTimes(int)}.
	 */
	@Test
	public void testSetTimes() throws InterruptedException
	{
		for(GameClockController controller : controllers) {
			controller.setTimes(60);
			controller.setTimes(1);
			controller.setTimes(-10);
			controller.setTimes(0);
		}
	}
	
	/**
	 * Handler for game state events fired by the clock controllers.
	 * Simply raises a flag when timeOver is triggered.
	 * @author Florian Bethe
	 */
	private class GameStateHandler implements IGameStateHandler {

		private boolean timeOver = false;
		
		@Override
		public void onCheckmate()
		{
		}

		@Override
		public void onStalemate()
		{
		}

		@Override
		public void onTimeOver()
		{
			timeOver = true;
		}
		
		public void reset() {
			timeOver = false;
		}
		
		public boolean triggeredTimeOver() {
			return timeOver;
		}
	}
	
}
