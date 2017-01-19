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
public class GameClockController implements Runnable
{
	private static Logger log = Logger.getLogger(GameClockController.class.getName());
	
	private GameClockModel clocks;
	private GameClockView clockView;
	private Clock runningClock;
	private Thread thread;
	private IGameStateHandler stateHandler;
	
	public GameClockController(int timeLimit, Player white, Player black, Player brown, Player gray)
	{
		clocks = new GameClockModel(4);
		this.runningClock = this.clocks.getClock(0); // running/active clock
		
		this.setTimes(timeLimit);
		this.setPlayers(white, black, brown, gray);
		
		this.thread = new Thread(this);
		
		this.clockView = new GameClockView(clocks, white, black, brown, gray);
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
		if(this.clocks.getClock(2).getPlayer() == null && this.clocks.getClock(3).getPlayer() == null)
			runningClock = (runningClock == clocks.getClock(0)) ? clocks.getClock(1) : clocks.getClock(0);
		else
		{
			if(runningClock == clocks.getClock(0))
				runningClock = clocks.getClock(2);
			else if(runningClock == clocks.getClock(2))
				runningClock = clocks.getClock(1);
			else if(runningClock == clocks.getClock(1))
				runningClock = clocks.getClock(3);
			else if(runningClock == clocks.getClock(3))
				runningClock = clocks.getClock(0);
		}
	}
	
	/**
	 * Sets the current time for both clocks.
	 * 
	 * @param t1
	 *            time for clock 1
	 * @param t2
	 *            time for clock 2
	 */
	public void setTimes(int t1, int t2, int t3, int t4)
	{
		clocks.getClock(0).resetClock(t1);
		clocks.getClock(1).resetClock(t2);
		
		clocks.getClock(2).resetClock(t3);
		clocks.getClock(3).resetClock(t4);
	}
	
	/**
	 * Sets the current time for both clocks.
	 * 
	 * @param t
	 *            time to set both clocks to
	 */
	public void setTimes(int t)
	{
		this.setTimes(t, t, t, t);
	}
	
	/**
	 * Method with is setting the clock's players.
	 * 
	 * @param p1
	 *            First player
	 * @param p2
	 *            Second player
	 */
	private void setPlayers(Player p1, Player p2, Player p3, Player p4)
	{
		clocks.getClock(0).setPlayer(p1);
		clocks.getClock(1).setPlayer(p2);
		
		if (p3 != null && p4 != null)
		{
			clocks.getClock(2).setPlayer(p3);
			clocks.getClock(3).setPlayer(p4);
		}
	}
	
	/**
	 * Method with is running the time on clock.
	 */
	public void run()
	{
		while(this.runningClock != null)
		{
			if(this.runningClock.decrement())
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
				this.timeOver();
			}
		}
	}
	
	/**
	 * Method of checking is the time of the game is not over.
	 */
	private void timeOver()
	{
		switchClocks(); // Current clock ran out of time -> other clock wins
		this.stateHandler.onTimeOver();
	}

	public void setStateHandler(IGameStateHandler handler)
	{
		this.stateHandler = handler;
	}
}
