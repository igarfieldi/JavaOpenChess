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
package jchess.gamelogic.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import jchess.gamelogic.Settings;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.IBoardActionHandler;
import jchess.gamelogic.field.IChessboardController;
import jchess.gamelogic.field.IChessboardModel;
import jchess.gamelogic.pieces.Piece;
import jchess.gui.ThemeImageLoader;

/**
 * Class to represent chessboard. Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class ChessboardView extends JPanel implements MouseListener, IChessboardView
{
	private static Logger log = Logger.getLogger(ChessboardView.class.getName());
	private static final long serialVersionUID = 1971410121780567341L;
	
	private static final int INITIAL_HEIGHT = 480;
	private static final String[] FIELD_LETTERS = { "a", "b", "c", "d", "e", "f", "g", "h" };
	private static final String[] FIELD_NUMBERS = { "1", "2", "3", "4", "5", "6", "7", "8" };
	
	private IChessboardController controller;
	private IChessboardModel board;
	private Settings settings;
	private IBoardActionHandler boardActionHandler;

	private final ThemeImageLoader themeLoader;
	private final int MIN_LABEL_HEIGHT = 20;
	
	private Field activeField;
	private Image upDownLabel = null;
	private Image leftRightLabel = null;
	private int squareHeight;
	private int chessboardHeight;
	private int labelHeight;
	
	/**
	 * Chessboard class constructor
	 * 
	 * @param settings
	 *            reference to Settings class object for this chessboard
	 * @param moves_history
	 *            reference to Moves class object for this chessboard
	 */
	public ChessboardView(Settings settings, IChessboardController controller)
	{
		this.controller = controller;
		this.settings = settings;
		this.activeField = null;
		this.resizeChessboard(INITIAL_HEIGHT);
		this.drawLabels();
		
		themeLoader = ThemeImageLoader.getInstance();
		
		this.setDoubleBuffered(true);
		this.addMouseListener(this);
	}

	@Override
	public void render()
	{
		this.repaint();
	}

	@Override
	public void initialize(IChessboardModel board, IBoardActionHandler handler)
	{
		this.board = board;
		this.boardActionHandler = handler;
	}
	
	/**
	 * Renders the background of the board.
	 * This includes the board itself and, if applicable, its labeling.
	 * @param g2d Graphics object
	 */
	private void renderBackground(Graphics2D g2d) {
		Point topLeftPoint = this.getChessboardLocation();
		if(this.settings.isLabelRenderingEnabled())
		{
			if(upDownLabel == null || leftRightLabel == null)
			{
				this.drawLabels();
			}
			g2d.drawImage(this.upDownLabel, 0, 0, null);
			g2d.drawImage(this.upDownLabel, 0, this.getHeight() - this.labelHeight, null);
			g2d.drawImage(this.leftRightLabel, 0, 0, null);
			g2d.drawImage(this.leftRightLabel, this.getHeight() - this.labelHeight, 0, null);
		}
		g2d.drawImage(themeLoader.loadThemeImage("chessboard.png"),
				topLeftPoint.x, topLeftPoint.y, chessboardHeight, chessboardHeight, this);
	}
	
	/**
	 * Renders the given piece to the board.
	 * @param piece Piece to render
	 * @param g2d Graphics object
	 */
	private void renderPiece(Piece piece, Graphics2D g2d) {
		if(g2d == null) {
			throw new IllegalArgumentException("Graphics object must not be null!");
		} else if(piece == null) {
			throw new IllegalArgumentException("Piece object must not be null!");
		}
		
		// Get field coordinates
		Point topLeft = this.getChessboardLocation();
		int height = this.squareHeight;
		Field field = board.getField(piece);
		int x = (field.getPosX() * height) + topLeft.x;
		int y = (field.getPosY() * height) + topLeft.y;
		
		// Render resized image (to fit current square size)
		g2d.drawImage(piece.getImage(), x, y, height, height, this);
	}
	
	/**
	 * Renders the currently selected field.
	 * @param g2d Graphics object
	 */
	private void renderSelectedField(Graphics2D g2d) {
		if(activeField != null) // if some square is active
		{
			Point topLeftPoint = this.getChessboardLocation();
			g2d.drawImage(themeLoader.loadThemeImage("sel_square.png"),
					(activeField.getPosX() * squareHeight) + topLeftPoint.x,
			        (activeField.getPosY() * squareHeight) + topLeftPoint.y, 
			        squareHeight, squareHeight, null);
			
			this.renderPossibleMoves(g2d);
		}
	}
	
	/**
	 * Renders all fields the currently selected piece can move to.
	 * @param g2d Graphics object
	 */
	private void renderPossibleMoves(Graphics2D g2d) {
		if(activeField != null) {
			Piece piece = board.getPiece(activeField);
			
			if(piece != null) {
				Point topLeftPoint = this.getChessboardLocation();
				for(Field field : controller.getPossibleMoves(board.getPiece(activeField), true)) {
					g2d.drawImage(themeLoader.loadThemeImage("able_square.png"),
							(field.getPosX() * squareHeight) + topLeftPoint.x,
					        (field.getPosY() * squareHeight) + topLeftPoint.y,
					        squareHeight, squareHeight, null);
				}
			}
		}
	}
	
	/**
	 * method to get reference to square from given x and y integeres
	 * 
	 * @param x
	 *            x position on chessboard
	 * @param y
	 *            y position on chessboard
	 * @return reference to searched square
	 */
	private Field getSquare(int x, int y)
	{
		// Test if click is outside of chessboard
		if((x > labelHeight + chessboardHeight) || (y > labelHeight + chessboardHeight))
		{
			log.log(Level.FINE, "Clicked outside of the chessboard");
			return null;
		}
		if(this.settings.isLabelRenderingEnabled())
		{
			// If labels have been rendered, we need to deduct their size
			x -= this.labelHeight;
			y -= this.labelHeight;
		}
		
		// Every squareHeight a new square starts (who knew)
		int squareX = x / squareHeight;
		int squareY = y / squareHeight;
		
		log.log(Level.FINE, "Square X: " + squareX + " | Square Y: " + squareY);
		
		try {
			return board.getField(squareX, squareY);
		} catch(ArrayIndexOutOfBoundsException exc) {
			// Realistically should only happen when something with the board is f'ed up
			log.log(Level.SEVERE, "Failed to retrieve chessboard field!", exc);
			return null;
		}
	}
	
	/**
	 * Method selecting piece in chessboard
	 * 
	 * @param sq
	 *            square to select (when clicked))
	 */
	public void select(Field field)
	{
		this.activeField = field;
		
		if(field != null) {
			log.log(Level.FINE, "Active X: " + (field.getPosX() + 1) + " | Active Y: " + (field.getPosY() + 1));
		}
		this.render();
	}
	
	/**
	 * Method set variables active_x_square & active_y_square to 0 values.
	 */
	public void unselect()
	{
		this.activeField = null;
		this.render();
	}
	
	public Point getChessboardLocation()
	{
		if(this.settings.isLabelRenderingEnabled())
		{
			return new Point(this.labelHeight, this.labelHeight);
		}
		return new Point(0, 0);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		this.renderBackground(g2d);
		
		if(board != null) {
			for(Piece piece : board.getPieces()) {
				this.renderPiece(piece, g2d);
			}
			
			this.renderSelectedField(g2d);
		}
	}
	
	public void resizeChessboard(int height)
	{
		this.chessboardHeight = height;
		this.squareHeight = height / 8;
		this.labelHeight = Math.max(MIN_LABEL_HEIGHT, squareHeight / 4);
		
		if(this.settings.isLabelRenderingEnabled())
		{
			height += 2 * labelHeight;
		}
		
		this.setSize(height, height);
		this.drawLabels();
		this.render();
	}
	
	protected final void drawLabels()
	{
		// Width for entire label and offset for letters / numbers
		int labelWidth = chessboardHeight + 2 * labelHeight;
		int addX = squareHeight / 2 + labelHeight;
		
		this.upDownLabel = new BufferedImage(labelWidth, labelHeight, BufferedImage.TYPE_3BYTE_BGR);
		this.leftRightLabel = new BufferedImage(labelHeight, labelWidth, BufferedImage.TYPE_3BYTE_BGR);
		
		// Clear the label
		Graphics2D g2d = (Graphics2D) this.upDownLabel.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, labelWidth, labelHeight);
		g2d.setColor(Color.black);
		g2d.setFont(new Font("Arial", Font.BOLD, 12));

		// Render the letter strings
		this.renderStrings(g2d, Arrays.asList(FIELD_LETTERS),
				addX, 10 + labelHeight / 3,
				squareHeight, 0,
				this.settings.isUpsideDown());
		g2d.dispose();

		// Clear the label
		g2d = (Graphics2D) this.leftRightLabel.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, labelHeight, labelWidth);
		g2d.setColor(Color.black);
		g2d.setFont(new Font("Arial", Font.BOLD, 12));

		// Render the letter number strings
		this.renderStrings(g2d, Arrays.asList(FIELD_NUMBERS),
				3 + labelHeight / 3, addX,
				0, squareHeight,
				!this.settings.isUpsideDown());
		g2d.dispose();
	}
	
	private void renderStrings(Graphics2D g2d, List<String> strings,
			int startX, int startY, int stepX, int stepY, boolean inverse) {
		if(inverse) {
			// When reversed, start at the end instead
			startX += stepX * (strings.size() - 1);
			stepX *= -1;
			startY += stepY * (strings.size() - 1);
			stepY *= -1;
		}
		
		// Render each string at the current position
		for(String str : strings) {
			g2d.drawString(str, startX, startY);
			startX += stepX;
			startY += stepY;
		}
	}
	
	public Field getActiveSquare()
	{
		return activeField;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		if(event.getButton() == MouseEvent.BUTTON3) // right button
		{
			log.log(Level.FINE, "Right button click for undo");
			this.boardActionHandler.onUndoRequested();
		} else if(event.getButton() == MouseEvent.BUTTON2 && settings.getGameType() == Settings.GameType.LOCAL)
		{
			log.log(Level.FINE, "Middle button click for redo");
			this.boardActionHandler.onRedoRequested();
		} else if(event.getButton() == MouseEvent.BUTTON1) // left button
		{
			log.log(Level.FINE, "Left button click for field selection");
			this.boardActionHandler.onFieldSelection(this.getSquare(event.getX(), event.getY()));
		}
		this.render();
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
	}
}
