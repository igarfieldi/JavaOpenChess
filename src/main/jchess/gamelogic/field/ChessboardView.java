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
package jchess.gamelogic.field;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import jchess.gamelogic.Settings;
import jchess.gamelogic.pieces.Piece;
import jchess.gui.ThemeImageLoader;

/**
 * Class to represent chessboard. Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class ChessboardView extends JPanel
{
	private static Logger log = Logger.getLogger(ChessboardView.class.getName());
	
	private ChessboardModel board;
	private Settings settings;
	
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
	private static Image sel_square = ORG_SEL_IMAGE;// image of highlited square
	private static Image able_square = ORG_ARLE_IMAGE;// image of square where
	                                                  // piece can go
	private Field activeSquare;
	private Image upDownLabel = null;
	private Image LeftRightLabel = null;
	private Point topLeft = new Point(0, 0);
	private int active_x_square;
	private int active_y_square;
	private float square_height;// height of square
	
	/**
	 * Chessboard class constructor
	 * 
	 * @param settings
	 *            reference to Settings class object for this chessboard
	 * @param moves_history
	 *            reference to Moves class object for this chessboard
	 */
	public ChessboardView(Settings settings, ChessboardModel board)
	{
		this.board = board;
		this.settings = settings;
		this.setActiveSquare(null);
		this.square_height = IMG_HEIGHT / 8;// we need to devide to know height
		                                    // of field
		this.active_x_square = 0;
		this.active_y_square = 0;
		this.setDoubleBuffered(true);
		this.drawLabels((int) this.square_height);
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
	public Field getSquare(int x, int y)
	{
		if((x > this.get_height()) || (y > this.get_widht())) // test if click
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
		double square_x = x / square_height;// count which field in X was
		                                    // clicked
		double square_y = y / square_height;// count which field in Y was
		                                    // clicked
		
		if(square_x > (int) square_x) // if X is more than X parsed to Integer
		{
			square_x = (int) square_x + 1;// parse to integer and increment
		}
		if(square_y > (int) square_y) // if X is more than X parsed to Integer
		{
			square_y = (int) square_y + 1;// parse to integer and increment
		}
		// Square newActiveSquare =
		// board.getField((int)square_x-1, (int)square_y-1);//4test
		log.log(Level.FINE, "Square X: " + square_x + " | Square Y: " + square_y);
		try
		{
			return board.getField((int) square_x - 1, (int) square_y - 1);
		} catch(java.lang.ArrayIndexOutOfBoundsException exc)
		{
			log.log(Level.SEVERE, "Accessed square outside of field!", exc);
			return null;
		}
	}
	
	/**
	 * Method selecting piece in chessboard
	 * 
	 * @param sq
	 *            square to select (when clicked))
	 */
	public void select(Field sq)
	{
		this.setActiveSquare(sq);
		this.active_x_square = sq.getPosX() + 1;
		this.active_y_square = sq.getPosY() + 1;
		
		// this.draw();//redraw
		log.log(Level.FINE, "Active X: " + active_x_square + " | Active Y: " + active_y_square);
		repaint();
		
	}/*--endOf-select--*/
	
	/**
	 * Method set variables active_x_square & active_y_square to 0 values.
	 */
	public void unselect()
	{
		this.active_x_square = 0;
		this.active_y_square = 0;
		this.setActiveSquare(null);
		// this.draw();//redraw
		repaint();
	}/*--endOf-unselect--*/
	
	public int get_widht()
	{
		return this.get_widht(false);
	}
	
	public int get_height()
	{
		return this.get_height(false);
	}
	
	public int get_widht(boolean includeLables)
	{
		return this.getHeight();
	}/*--endOf-get_widht--*/
	
	public int get_height(boolean includeLabels)
	{
		if(this.settings.isLabelRenderingEnabled())
		{
			return ChessboardView.image.getHeight(null) + upDownLabel.getHeight(null);
		}
		return ChessboardView.image.getHeight(null);
	}/*--endOf-get_height--*/
	
	public int get_square_height()
	{
		int result = (int) this.square_height;
		return result;
	}
	
	/**
	 * Method to draw Chessboard and their elements (pieces etc.)
	 * 
	 * @deprecated
	 */
	public void draw()
	{
		this.getGraphics().drawImage(image, this.getTopLeftPoint().x, this.getTopLeftPoint().y, null);// draw
		                                                                                              // an
		                                                                                              // Image
		                                                                                              // of
		                                                                                              // chessboard
		this.drawLabels();
		this.repaint();
	}/*--endOf-draw--*/
	
	/**
	 * Annotations to superclass Game updateing and painting the crossboard
	 */
	@Override
	public void update(Graphics g)
	{
		repaint();
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
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
		g2d.drawImage(image, topLeftPoint.x, topLeftPoint.y, null);// draw an
		                                                           // Image of
		                                                           // chessboard
		for(Field field : board.getFields()) {
			Piece piece = field.getPiece();//board.getPiece(field);
			if(piece != null) {
				piece.draw(g);
			}
		}
		
		if((this.active_x_square != 0) && (this.active_y_square != 0)) // if
		                                                               // some
		                                                               // square
		                                                               // is
		                                                               // active
		{
			g2d.drawImage(sel_square, ((this.active_x_square - 1) * (int) square_height) + topLeftPoint.x,
			        ((this.active_y_square - 1) * (int) square_height) + topLeftPoint.y, null);// draw
			                                                                                   // image
			                                                                                   // of
			                                                                                   // selected
			                                                                                   // square
			Field tmpSquare = board.getField((int) (this.active_x_square - 1), (int) (this.active_y_square - 1));
			if(tmpSquare.getPiece() != null)
			{
				for(Field possibleField : board.getField((int) (this.active_x_square - 1), (int) (this.active_y_square - 1)).getPiece().possibleMoves()) {
					g2d.drawImage(able_square, (possibleField.getPosX() * (int) square_height) + topLeftPoint.x,
					        (possibleField.getPosY() * (int) square_height) + topLeftPoint.y, null);
				}
			}
		}
	}/*--endOf-paint--*/
	
	public void resizeChessboard(int height)
	{
		BufferedImage resized = new BufferedImage(height, height, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g = resized.createGraphics();
		g.drawImage(ChessboardView.ORG_IMAGE, 0, 0, height, height, null);
		g.dispose();
		ChessboardView.image = resized.getScaledInstance(height, height, 0);
		this.square_height = (float) (height / 8);
		if(this.settings.isLabelRenderingEnabled())
		{
			height += 2 * (this.upDownLabel.getHeight(null));
		}
		this.setSize(height, height);
		
		resized = new BufferedImage((int) square_height, (int) square_height, BufferedImage.TYPE_INT_ARGB_PRE);
		g = resized.createGraphics();
		g.drawImage(ChessboardView.ORG_ARLE_IMAGE, 0, 0, (int) square_height, (int) square_height, null);
		g.dispose();
		ChessboardView.able_square = resized.getScaledInstance((int) square_height, (int) square_height, 0);
		
		resized = new BufferedImage((int) square_height, (int) square_height, BufferedImage.TYPE_INT_ARGB_PRE);
		g = resized.createGraphics();
		g.drawImage(ChessboardView.ORG_SEL_IMAGE, 0, 0, (int) square_height, (int) square_height, null);
		g.dispose();
		ChessboardView.sel_square = resized.getScaledInstance((int) square_height, (int) square_height, 0);
		this.drawLabels();
	}
	
	protected void drawLabels()
	{
		this.drawLabels((int) this.square_height);
	}
	
	protected final void drawLabels(int square_height)
	{
		// BufferedImage uDL = new BufferedImage(800, 800,
		// BufferedImage.TYPE_3BYTE_BGR);
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
	
	public void componentMoved(ComponentEvent e)
	{
		// throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void componentShown(ComponentEvent e)
	{
		// throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void componentHidden(ComponentEvent e)
	{
		// throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public Field getActiveSquare()
	{
		return activeSquare;
	}
	
	public void setActiveSquare(Field activeSquare)
	{
		this.activeSquare = activeSquare;
	}
}
