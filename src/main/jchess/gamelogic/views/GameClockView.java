package jchess.gamelogic.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import jchess.gamelogic.Settings;
import jchess.gamelogic.clock.GameClockModel;

public class GameClockView extends JPanel
{
	private static final long serialVersionUID = -5110241622282357707L;
	private static final Font clockFont = new Font("Sarif", Font.ITALIC, 14);
	
	private BufferedImage background = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
	private GameClockModel clocks;
	private Settings settings;
	
	public GameClockView(Settings settings, GameClockModel clocks) {
		this.clocks = clocks;
		this.settings = settings;
		
		this.setDoubleBuffered(true);
		
		// Pre-render the static background image
		Graphics2D g2d = (Graphics2D) this.background.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.drawBackground(g2d);
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
		g2d.drawString(this.clocks.getClock(0).toString(), 10, 80);
		g2d.drawString(this.clocks.getClock(1).toString(), 90, 80);
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
}
