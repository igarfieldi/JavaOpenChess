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

import java.awt.*;
import java.awt.image.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import jchess.gamelogic.Game;
import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;

/**
 * Class to representing the full game time.
 * @param game the current game
 */
public class GameClock extends JPanel implements Runnable
{
	private static final long serialVersionUID = -5791210552595733310L;
	private static Logger log = Logger.getLogger(GameClock.class.getName());
	
	private Clock clock1;
	private Clock clock2;
	private Clock runningClock;
	private Settings settings;
	private Thread thread;
	private Game game;
	private Font clockFont;
	private final BufferedImage background;
	
	public GameClock(Game game)
	{
		this.clock1 = new Clock();// white player clock
		this.clock2 = new Clock();// black player clock
		this.runningClock = this.clock1;// running/active clock
		this.game = game;
		this.settings = game.getSettings();
		this.background = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		this.clockFont = new Font("Sarif", Font.ITALIC, 14);
		
		this.setTimes(settings.getTimeForGame());
		this.setPlayers(this.settings.getBlackPlayer(), this.settings.getWhitePlayer());
		
		this.thread = new Thread(this);
		if(this.settings.isTimeLimitSet())
		{
			thread.start();
		}
		
		this.setDoubleBuffered(true);
		
		// Pre-render the static background image
		Graphics2D g2d = (Graphics2D) this.background.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.drawBackground(g2d);
	}
	
	/**
	 * Starts the clock thread (and thus the running clock).
	 */
	public void start()
	{
		this.thread.start();
	}
	
	/**
	 * Draws the clock background.
	 * @param g2d graphics context
	 */
	private void drawBackground(Graphics2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.fillRect(5, 30, 80, 30);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(85, 30, 90, 30);
		g2d.drawRect(5, 30, 170, 30);
		g2d.drawRect(5, 60, 170, 30);
		g2d.drawLine(85, 30, 85, 90);
	}
	
	/**
	 * Draws the current time of both clocks.
	 * @param g2d graphics context
	 */
	private void drawTime(Graphics2D g2d)
	{
		g2d.setFont(clockFont);
		g2d.setColor(Color.BLACK);
		g2d.drawString(clock1.toString(), 10, 80);
		g2d.drawString(clock2.toString(), 90, 80);
	}
	
	/**
	 * Draws the current player names.
	 * @param g2d graphics context
	 */
	private void drawPlayerNames(Graphics2D g2d)
	{
		g2d.setFont(clockFont);
		g2d.setColor(Color.BLACK);
		g2d.drawString(settings.getWhitePlayer().getName(), 10, 50);
		g2d.setColor(Color.WHITE);
		g2d.drawString(settings.getBlackPlayer().getName(), 100, 50);
	}
	
	/**
	 * Annotation to superclass Graphics drawing the clock graphics
	 * 
	 * @param g
	 *            Graphics2D Capt object to paint
	 */
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(background, 0, 0, this);
		this.drawPlayerNames(g2d);
		this.drawTime(g2d);
	}
	
	/**
	 * Annotation to superclass Graphics updateing clock graphisc
	 * @param g graphics context
	 */
	@Override
	public void update(Graphics g)
	{
		paint(g);
	}
	
	/**
	 * Method of swiching the players clocks.
	 */
	public void switchClocks()
	{
		// Change the running clock to the one not currently running
		runningClock = (clock1 == runningClock) ? clock2 : clock1;
	}
	
	/**
	 * Sets the current time for both clocks.
	 * @param t1 time for clock 1
	 * @param t2 time for clock 2
	 */
	public void setTimes(int t1, int t2)
	{
		this.clock1.resetClock(t1);
		this.clock2.resetClock(t2);
	}
	
	/**
	 * Sets the current time for both clocks.
	 * @param t time to set both clocks to
	 */
	public void setTimes(int t)
	{
		this.setTimes(t, t);
	}
	
	/**
	 * Method with is setting the clock's players.
	 * @param p1 First player
	 * @param p2 Second player
	 */
	private void setPlayers(Player p1, Player p2)
	{
		if(p1.getColor() == Player.Color.WHITE)
		{
			this.clock1.setPlayer(p1);
			this.clock2.setPlayer(p2);
		} else
		{
			this.clock1.setPlayer(p2);
			this.clock2.setPlayer(p1);
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
				repaint();
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
		switchClocks();	// Current clock ran out of time -> other clock wins
		game.endGame("Time is up! " + runningClock.getPlayer().getColor().toString() +
				" player wins the game.");
	}
}
