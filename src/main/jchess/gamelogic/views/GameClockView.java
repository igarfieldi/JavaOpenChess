package jchess.gamelogic.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import jchess.gamelogic.Player;
import jchess.gamelogic.models.GameClockModel;

public class GameClockView extends JPanel implements IRenderable
{
	private static final long serialVersionUID = -5110241622282357707L;
	
	private static final Font clockFont = new Font("Sarif", Font.ITALIC, 14);
	private static final int WIDTH_PER_PLAYER = 90;
	private static final int STRING_BUFFER_PER_SIDE = 10;
	private static final int PLAYER_NAME_HEIGHT = 50;
	private static final int CLOCK_TIME_HEIGHT = 80;
	private static final int BOX_UPPER_BORDER = 30;
	private static final int BOX_LOWER_BORDER = 90;
	private static final int BOX_HEIGHT = 30;
	private static final int BOX_BUFFER_PER_SIDE = 5;
	
	private BufferedImage background = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
	private GameClockModel clocks;
	private List<Player> players;
	
	public GameClockView(GameClockModel clocks, Player... players) {
		this.clocks = clocks;
		this.players = new ArrayList<Player>();
		for(Player player : players) {
			this.players.add(player);
		}
		
		this.setDoubleBuffered(true);
		preRenderBackgroundImage(2*STRING_BUFFER_PER_SIDE + players.length*WIDTH_PER_PLAYER);
	}
	
	private void preRenderBackgroundImage(int width)
	{
		Graphics2D g2d = (Graphics2D) this.background.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.drawBackground(g2d);
		this.setMinimumSize(new Dimension(width, BOX_LOWER_BORDER + STRING_BUFFER_PER_SIDE));
		this.setPreferredSize(new Dimension(width, BOX_LOWER_BORDER + STRING_BUFFER_PER_SIDE));
	}
	
	@Override
	public void render() {
		this.repaint();
	}
	
	@Override
	public void changeSize(int width, int height) {
		this.setSize(width, height);
	}
	
	/**
	 * Draws the clock background.
	 * @param g2d graphics context
	 */
	private void drawBackground(Graphics2D g2d)
	{
		for(int i = 0; i < players.size(); i++) {
			g2d.setColor(getPlayerBackgroundColor(i));
			g2d.fillRect(WIDTH_PER_PLAYER*i + BOX_BUFFER_PER_SIDE, BOX_UPPER_BORDER,
					WIDTH_PER_PLAYER, BOX_HEIGHT);
			g2d.setColor(Color.BLACK);
			g2d.drawRect(WIDTH_PER_PLAYER*i + BOX_BUFFER_PER_SIDE, BOX_UPPER_BORDER,
					WIDTH_PER_PLAYER, BOX_HEIGHT);
			g2d.drawRect(WIDTH_PER_PLAYER*i + BOX_BUFFER_PER_SIDE,
					(BOX_UPPER_BORDER + BOX_LOWER_BORDER) / 2,
					WIDTH_PER_PLAYER, BOX_HEIGHT);
		}
	}
	
	/**
	 * Draws the current time of both clocks.
	 * @param g2d graphics context
	 */
	private void drawTime(Graphics2D g2d)
	{
		g2d.setFont(clockFont);
		g2d.setColor(Color.BLACK);
		for(int i = 0; i < players.size(); i++) {
			g2d.drawString(clocks.getClock(i).toString(),
					STRING_BUFFER_PER_SIDE + i*WIDTH_PER_PLAYER,
					CLOCK_TIME_HEIGHT);
		}
	}
	
	private Color getPlayerBackgroundColor(int index) {
		switch(index) {
			case 0:
				return Color.WHITE;
			case 1:
				return Color.ORANGE;
			case 2:
				return Color.BLACK;
			case 3:
				return Color.GRAY;
			default:
				return Color.GREEN;
		}
	}
	
	private Color getPlayerForegroundColor(int index) {
		switch(index) {
			case 0:
				return Color.BLACK;
			case 1:
				return Color.GRAY;
			case 2:
				return Color.WHITE;
			case 3:
				return Color.ORANGE;
			default:
				return Color.MAGENTA;
		}
	}
	
	/**
	 * Draws the current player names.
	 * @param g2d graphics context
	 */
	private void drawPlayerNames(Graphics2D g2d)
	{
		g2d.setFont(clockFont);
		for(int i = 0; i < players.size(); i++) {
			g2d.setColor(getPlayerForegroundColor(i));
			g2d.drawString(players.get(i).getName(),
					STRING_BUFFER_PER_SIDE + i*WIDTH_PER_PLAYER, PLAYER_NAME_HEIGHT);
		}
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
