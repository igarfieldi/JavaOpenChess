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
package jchess.gamelogic;

import java.io.Serializable;

/**
 * Class representings game settings available for the current player
 */
public class Settings implements Serializable
{
	private static final long serialVersionUID = -8411751126357662563L;
	
	public enum GameMode
	{
		NEW_GAME, LOAD_GAME
	}
	
	private int timeForGame;
	private boolean runningGameClock;
	private boolean timeLimitSet;
	private boolean upsideDown;
	private GameMode gameMode;
	private Player playerWhite;
	private Player playerBlack;
	private boolean renderLabels = true;
	
	public Settings()
	{
		// temporarily
		this.playerWhite = new Player("", Player.Color.WHITE);
		this.playerBlack = new Player("", Player.Color.BLACK);
		this.timeLimitSet = false;
		
		gameMode = GameMode.NEW_GAME;
	}
	
	/**
	 * Method to get game time set by player
	 * 
	 * @return timeFofGame int with how long the game will leasts
	 */
	public int getTimeForGame()
	{
		return this.timeForGame;
	}
	
	public boolean isGameClockRunning()
	{
		return runningGameClock;
	}
	
	public boolean isTimeLimitSet()
	{
		return timeLimitSet;
	}
	
	public boolean isUpsideDown()
	{
		return upsideDown;
	}
	
	public GameMode getGameMode()
	{
		return gameMode;
	}
	
	public Player getWhitePlayer()
	{
		return playerWhite;
	}
	
	public Player getBlackPlayer()
	{
		return playerBlack;
	}
	
	public boolean isLabelRenderingEnabled()
	{
		return renderLabels;
	}
	
	public void setTimeForGame(int time)
	{
		this.timeForGame = time;
	}
	
	public void setTimeLimit(boolean set)
	{
		this.timeLimitSet = set;
	}
	
	public void setUpsideDown(boolean down)
	{
		this.upsideDown = down;
	}
	
	public void setGameMode(GameMode mode)
	{
		this.gameMode = mode;
	}
}
