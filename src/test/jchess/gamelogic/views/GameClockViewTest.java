/**
 * 
 */
package jchess.gamelogic.views;

import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.models.GameClockModel;

/**
 * @author Florian Bethe
 */
public class GameClockViewTest
{
	
	/**
	 * Test method for {@link jchess.gamelogic.views.GameClockView#GameClockView(jchess.gamelogic.models.GameClockModel, jchess.gamelogic.Player[])}.
	 * Tests the construction with different player counts.
	 */
	@Test
	public void testGameClockView()
	{
		Player players[] = {
				new Player("p1", Color.WHITE),
				new Player("p2", Color.RED),
				new Player("p3", Color.BLACK),
				new Player("p4", Color.GOLDEN),
				new Player("p5", Color.BLACK),
				new Player("p6", Color.WHITE)
		};
		
		// Just see if the construction goes through or not; hard to
		// test more for GUI
		for(int i = 0; i < players.length; i++) {
			Player currPlayers[] = new Player[i+1];
			for(int j = 0; j <= i; j++) {
				currPlayers[j] = players[j];
			}
			
			GameClockView view = new GameClockView(
					new GameClockModel(i+1), currPlayers);
			// For good measure; won't have any consequence
			view.render();
		}
	}
	
}
