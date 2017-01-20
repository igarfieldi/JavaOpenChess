/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.gamelogic.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.gamelogic.Clock;
import jchess.gamelogic.Player;
import jchess.gamelogic.models.GameClockModel;
import jchess.gamelogic.views.GameClockView;

/**
 * Class to representing the full game time.
 */
public class GameClockController
{
	private static Logger log = Logger.getLogger(GameClockController.class.getName());
	
	private GameClockModel clocks;
	private GameClockView clockView;
	private Clock runningClock;
	private Thread thread;
	private IGameStateHandler stateHandler;
	
	public GameClockController(int timeLimit, Player... players) {
		clocks = new GameClockModel(players.length);
		this.runningClock = clocks.getClock(0);
		
		// Set time limit and players
		this.setTimes(timeLimit);
		for(int i = 0; i < players.length; i++) {
			clocks.getClock(i).setPlayer(players[i]);
		}
		this.clockView = new GameClockView(clocks, players);
		
		this.thread = new Thread(new ClockThread());
	}
	
	/**
	 * Returns the view of the game clock.
	 * 
	 * @return game clock view
	 */
	public GameClockView getView()
	{
		return clockView;
	}
	
	/**
	 * Starts the clock thread (and thus the running clock).
	 */
	public void start()
	{
		this.thread.start();
	}
	
	/**
	 * Method of swiching the players clocks.
	 */
	public void switchClocks()
	{
		// Change the running clock to the one not currently running
		runningClock = clocks.getNextClock(runningClock);
	}
	
	/**
	 * Sets the current time for both clocks.
	 * 
	 * @param t
	 *            time to set both clocks to
	 */
	public void setTimes(int t)
	{
		for(Clock clock : clocks.getClocks()) {
			clock.resetClock(t);
		}
	}
	
	/**
	 * Method of checking is the time of the game is not over.
	 */
	private void timeOver()
	{
		this.stateHandler.onTimeOver();
	}

	public void setStateHandler(IGameStateHandler handler)
	{
		this.stateHandler = handler;
	}
	
	/**
	 * Runs down the currently active clock.
	 * @author Florian Bethe
	 */
	private class ClockThread implements Runnable {

		@Override
		public void run()
		{
			while(GameClockController.this.runningClock != null)
			{
				if(GameClockController.this.runningClock.decrement())
				{
					clockView.render();
					try
					{
						Thread.sleep(1000);
					} catch(InterruptedException exc)
					{
						log.log(Level.SEVERE, "Error putting game clock to sleep!", exc);
					}
				} else
				{
					GameClockController.this.timeOver();
				}
			}
		}
		
	}
	
}
