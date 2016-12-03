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
	
	private IChessboardController controller;
	private IChessboardModel board;
	private Settings settings;
	private IBoardActionHandler boardActionHandler;
	
	private static final long serialVersionUID = 1971410121780567341L;
	public static final int TOP = 0;
	public static final int BOTTOM = 7;
	public static final int IMG_X = 5;// image x position (used in JChessView
	                                  // class!)
	public static final int IMG_Y = IMG_X;// image y position (used in
	                                      // JChessView class!)
	public static final int IMG_WIDTH = 480;// image width
	public static final int IMG_HEIGHT = IMG_WIDTH;// image height
	private static final Image ORG_IMAGE = ThemeImageLoader.loadThemeImage("chessboard.png");
	private static final Image ORG_SEL_IMAGE = ThemeImageLoader.loadThemeImage("sel_square.png");
	private static final Image ORG_ARLE_IMAGE = ThemeImageLoader.loadThemeImage("able_square.png");
	
	// TODO: replace the final static images with the construction-time call to
	// the GUI class?
	private static Image image = ChessboardView.ORG_IMAGE;// image of chessboard
	private static Image selectedSquare = ORG_SEL_IMAGE;// image of highlited square
	private static Image ableSquare = ORG_ARLE_IMAGE;// image of square where
	                                                  // piece can go
	private Field activeSquare;
	private Image upDownLabel = null;
	private Image LeftRightLabel = null;
	private Point topLeft = new Point(0, 0);
	private float squareHeight;// height of square
	
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
		this.activeSquare = null;
		this.squareHeight = IMG_HEIGHT / 8;// we need to devide to know height
		                                    // of field
		this.setDoubleBuffered(true);
		this.drawLabels((int) this.squareHeight);
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
		Point topLeftPoint = this.getTopLeftPoint();
		if(this.settings.isLabelRenderingEnabled())
		{
			if(topLeftPoint.x <= 0 && topLeftPoint.y <= 0) // if renderLabels
			                                               // and (0,0), than
			                                               // draw it! (for
			                                               // first run)
			{
				this.drawLabels();
			}
			g2d.drawImage(this.upDownLabel, 0, 0, null);
			g2d.drawImage(this.upDownLabel, 0, ChessboardView.image.getHeight(null) + topLeftPoint.y, null);
			g2d.drawImage(this.LeftRightLabel, 0, 0, null);
			g2d.drawImage(this.LeftRightLabel, ChessboardView.image.getHeight(null) + topLeftPoint.x, 0, null);
		}
		g2d.drawImage(image, topLeftPoint.x, topLeftPoint.y, null);
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
		Point topLeft = this.getTopLeftPoint();
		int height = this.getSquareHeight();
		Field field = board.getField(piece);
		int x = (field.getPosX() * height) + topLeft.x;
		int y = (field.getPosY() * height) + topLeft.y;
		
		// Render resized image (to fit current square size)
		g2d.drawImage(piece.getImage(), x, y, height, height, null);
	}
	
	/**
	 * Renders the currently selected field.
	 * @param g2d Graphics object
	 */
	private void renderSelectedField(Graphics2D g2d) {
		if(activeSquare != null) // if some square is active
		{
			Point topLeftPoint = this.getTopLeftPoint();
			g2d.drawImage(selectedSquare, (activeSquare.getPosX() * (int) squareHeight) + topLeftPoint.x,
			        (activeSquare.getPosY() * (int) squareHeight) + topLeftPoint.y, null);
			
			this.renderPossibleMoves(g2d);
		}
	}
	
	/**
	 * Renders all fields the currently selected piece can move to.
	 * @param g2d Graphics object
	 */
	private void renderPossibleMoves(Graphics2D g2d) {
		if(activeSquare != null) {
			Piece piece = board.getPiece(activeSquare);
			
			if(piece != null) {
				Point topLeftPoint = this.getTopLeftPoint();
				for(Field field : controller.getPossibleMoves(board.getPiece(activeSquare), true)) {
					g2d.drawImage(ableSquare, (field.getPosX() * (int) squareHeight) + topLeftPoint.x,
					        (field.getPosY() * (int) squareHeight) + topLeftPoint.y, null);
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
		if((x > this.getHeight()) || (y > this.getWidth())) // test if click
		                                                      // is out of
		                                                      // chessboard
		{
			log.log(Level.FINE, "Clicked outside of the chessboard");
			return null;
		}
		if(this.settings.isLabelRenderingEnabled())
		{
			x -= this.upDownLabel.getHeight(null);
			y -= this.upDownLabel.getHeight(null);
		}
		double square_x = x / squareHeight;// count which field in X was
		                                    // clicked
		double square_y = y / squareHeight;// count which field in Y was
		                                    // clicked
		
		if(square_x > (int) square_x) // if X is more than X parsed to Integer
		{
			square_x = (int) square_x + 1;// parse to integer and increment
		}
		if(square_y > (int) square_y) // if X is more than X parsed to Integer
		{
			square_y = (int) square_y + 1;// parse to integer and increment
		}
		
		log.log(Level.FINE, "Square X: " + square_x + " | Square Y: " + square_y);
		try
		{
			return board.getField((int) square_x - 1, (int) square_y - 1);
		} catch(java.lang.ArrayIndexOutOfBoundsException exc)
		{
			log.log(Level.FINER, "Clicked field outside of chessboard: (" + x + "|" + y + ")");
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
		this.activeSquare = field;
		
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
		this.activeSquare = null;
		this.render();
	}
	
	public int getWidth()
	{
		return this.getWidth(false);
	}
	
	public int getHeight()
	{
		return this.getHeight(false);
	}
	
	public int getWidth(boolean includeLables)
	{
		return this.getHeight();
	}/*--endOf-get_widht--*/
	
	public int getHeight(boolean includeLabels)
	{
		if(this.settings.isLabelRenderingEnabled())
		{
			return ChessboardView.image.getHeight(null) + upDownLabel.getHeight(null);
		}
		return ChessboardView.image.getHeight(null);
	}
	
	public int getSquareHeight()
	{
		int result = (int) this.squareHeight;
		return result;
	}
	
	public Point getTopLeftPoint()
	{
		if(this.settings.isLabelRenderingEnabled())
		{
			return new Point(this.topLeft.x + this.upDownLabel.getHeight(null),
			        this.topLeft.y + this.upDownLabel.getHeight(null));
		}
		return this.topLeft;
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
		BufferedImage resized = new BufferedImage(height, height, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g = resized.createGraphics();
		g.drawImage(ChessboardView.ORG_IMAGE, 0, 0, height, height, null);
		g.dispose();
		ChessboardView.image = resized.getScaledInstance(height, height, 0);
		this.squareHeight = (float) (height / 8);
		if(this.settings.isLabelRenderingEnabled())
		{
			height += 2 * (this.upDownLabel.getHeight(null));
		}
		this.setSize(height, height);
		
		resized = new BufferedImage((int) squareHeight, (int) squareHeight, BufferedImage.TYPE_INT_ARGB_PRE);
		g = resized.createGraphics();
		g.drawImage(ChessboardView.ORG_ARLE_IMAGE, 0, 0, (int) squareHeight, (int) squareHeight, null);
		g.dispose();
		ChessboardView.ableSquare = resized.getScaledInstance((int) squareHeight, (int) squareHeight, 0);
		
		resized = new BufferedImage((int) squareHeight, (int) squareHeight, BufferedImage.TYPE_INT_ARGB_PRE);
		g = resized.createGraphics();
		g.drawImage(ChessboardView.ORG_SEL_IMAGE, 0, 0, (int) squareHeight, (int) squareHeight, null);
		g.dispose();
		ChessboardView.selectedSquare = resized.getScaledInstance((int) squareHeight, (int) squareHeight, 0);
		this.drawLabels();
	}
	
	protected void drawLabels()
	{
		this.drawLabels((int) this.squareHeight);
	}
	
	protected final void drawLabels(int square_height)
	{
		// TODO: clean up
		int min_label_height = 20;
		int labelHeight = (int) Math.ceil(square_height / 4);
		labelHeight = (labelHeight < min_label_height) ? min_label_height : labelHeight;
		int labelWidth = (int) Math.ceil(square_height * 8 + (2 * labelHeight));
		BufferedImage uDL = new BufferedImage(labelWidth + min_label_height, labelHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D uDL2D = (Graphics2D) uDL.createGraphics();
		uDL2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		uDL2D.setColor(Color.white);
		
		uDL2D.fillRect(0, 0, labelWidth + min_label_height, labelHeight);
		uDL2D.setColor(Color.black);
		uDL2D.setFont(new Font("Arial", Font.BOLD, 12));
		int addX = (square_height / 2);
		if(this.settings.isLabelRenderingEnabled())
		{
			addX += labelHeight;
		}
		
		String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h" };
		if(!this.settings.isUpsideDown())
		{
			for(int i = 1; i <= letters.length; i++)
			{
				uDL2D.drawString(letters[i - 1], (square_height * (i - 1)) + addX, 10 + (labelHeight / 3));
			}
		} else
		{
			int j = 1;
			for(int i = letters.length; i > 0; i--, j++)
			{
				uDL2D.drawString(letters[i - 1], (square_height * (j - 1)) + addX, 10 + (labelHeight / 3));
			}
		}
		uDL2D.dispose();
		this.upDownLabel = uDL;
		
		uDL = new BufferedImage(labelHeight, labelWidth + min_label_height, BufferedImage.TYPE_3BYTE_BGR);
		uDL2D = (Graphics2D) uDL.createGraphics();
		uDL2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		uDL2D.setColor(Color.white);
		// uDL2D.fillRect(0, 0, 800, 800);
		uDL2D.fillRect(0, 0, labelHeight, labelWidth + min_label_height);
		uDL2D.setColor(Color.black);
		uDL2D.setFont(new Font("Arial", Font.BOLD, 12));
		
		if(this.settings.isUpsideDown())
		{
			for(int i = 1; i <= 8; i++)
			{
				uDL2D.drawString(new Integer(i).toString(), 3 + (labelHeight / 3), (square_height * (i - 1)) + addX);
			}
		} else
		{
			int j = 1;
			for(int i = 8; i > 0; i--, j++)
			{
				uDL2D.drawString(new Integer(i).toString(), 3 + (labelHeight / 3), (square_height * (j - 1)) + addX);
			}
		}
		uDL2D.dispose();
		this.LeftRightLabel = uDL;
	}
	
	public Field getActiveSquare()
	{
		return activeSquare;
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
