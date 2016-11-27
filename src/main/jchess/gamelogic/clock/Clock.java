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
package jchess.gamelogic.clock;

import jchess.gamelogic.Player;

/**
 * Class to represent seperate wall-clock for one player. Full ChessClock is
 * represented by GameClock object (two clock - one for each player)
 */
public class Clock
{
	
	private int timeLeft;
	private Player player;
	
	public Clock()
	{
		this.resetClock(0);
	}
	
	public Clock(int time)
	{
		this.resetClock(time);
	}
	
	/**
	 * Method to init clock with given value
	 * 
	 * @param time
	 *            tell method with how much time init clock
	 */
	public void resetClock(int time)
	{
		this.timeLeft = time;
	}
	
	/**
	 * Method to decrement value of left time
	 * 
	 * @return bool true if time_left > 0, else returns false
	 */
	public boolean decrement()
	{
		if(this.timeLeft > 0)
		{
			this.timeLeft = this.timeLeft - 1;
			return true;
		}
		return false;
	}
	
	/**
	 * Method to get left time in seconds
	 * 
	 * @return Player int integer of seconds
	 */
	public int getSecondsLeft()
	{
		return this.timeLeft;
	}
	
	/**
	 * Method to get player (owner of this clock)
	 * 
	 * @param player
	 *            player to set as owner of clock
	 */
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	/**
	 * Method to get player (owner of this clock)
	 * 
	 * @return Reference to player class object
	 */
	public Player getPlayer()
	{
		return this.player;
	}
	
	/**
	 * Method to prepare time in nice looking String
	 * 
	 * @return String of actual left game time with ':' digits in mm:ss format
	 */
	@Override
	public String toString()
	{
		int minutes = getSecondsLeft() / 60;
		int seconds = getSecondsLeft() % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
}
