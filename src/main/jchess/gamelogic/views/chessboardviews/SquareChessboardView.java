package jchess.gamelogic.views.chessboardviews;

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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jchess.Localization;
import jchess.gamelogic.controllers.IBoardActionHandler;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.views.IChessboardView;

public abstract class SquareChessboardView extends JPanel implements MouseListener, IChessboardView
{
	private static final long serialVersionUID = 1971410121780567341L;
	private static Logger log = Logger.getLogger(SquareChessboardView.class.getName());
	
	// TODO: this seems unnecessary...
	private static final int INITIAL_HEIGHT = 480;
	
	private IChessboardController chessboard;
	private IBoardActionHandler boardActionHandler;
	
	private final int MIN_LABEL_HEIGHT = 20;
	
	private Field activeField;
	private boolean renderLabels;
	private boolean invertedBoard;
	private Image upDownLabel = null;
	private Image leftRightLabel = null;
	
	private double squareHeight;
	private int chessboardHeight;
	private int labelHeight;
	
	public SquareChessboardView(boolean renderLabels, boolean invertedBoard)
	{
		this.activeField = null;
		this.renderLabels = renderLabels;
		this.invertedBoard = invertedBoard;
		this.changeSize(INITIAL_HEIGHT, INITIAL_HEIGHT);
		this.drawLabels();
		
		this.setDoubleBuffered(true);
		this.addMouseListener(this);
	}
	
	protected abstract Image getChessboardImage();
	
	protected abstract Image getSelectedFieldImage();

	protected abstract Image getPossibleFieldImage();
	
	protected abstract Image getThreateningFieldImage();
	
	protected abstract int getSquareCount();
	
	protected abstract List<String> getLabelLetters();
	
	protected abstract List<String> getLabelNumbers();
	
	@Override
	public void render()
	{
		this.repaint();
	}
	
	@Override
	public void initialize(IChessboardController chessboard, IBoardActionHandler handler)
	{
		this.chessboard = chessboard;
		this.boardActionHandler = handler;
	}
	
	/**
	 * Renders the background of the board. This includes the board itself and,
	 * if applicable, its labeling.
	 * 
	 * @param g2d
	 *            Graphics object
	 */
	private void renderBackground(Graphics2D g2d)
	{
		Point topLeftPoint = this.getChessboardLocation();
		if(renderLabels)
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
		g2d.drawImage(this.getChessboardImage(), topLeftPoint.x, topLeftPoint.y, chessboardHeight, chessboardHeight,
		        this);
	}
	
	/**
	 * Renders the given piece to the board.
	 * 
	 * @param piece
	 *            Piece to render
	 * @param g2d
	 *            Graphics object
	 */
	private void renderPiece(Piece piece, Graphics2D g2d)
	{
		if(g2d == null)
		{
			throw new IllegalArgumentException("Graphics object must not be null!");
		} else if(piece == null)
		{
			throw new IllegalArgumentException("Piece object must not be null!");
		}
		
		// Get field coordinates
		Point topLeft = this.getChessboardLocation();
		Field field = chessboard.getBoard().getField(piece);
		int x = (int) (field.getPosX() * this.squareHeight) + topLeft.x;
		int y = (int) (field.getPosY() * this.squareHeight) + topLeft.y;
		
		// Render resized image (to fit current square size)
		g2d.drawImage(piece.getImage(), x, y, (int) squareHeight, (int) squareHeight, this);
	}
	
	/**
	 * Renders the currently selected field.
	 * 
	 * @param g2d
	 *            Graphics object
	 */
	private void renderSelectedField(Graphics2D g2d)
	{
		if(activeField != null) // if some square is active
		{
			Point topLeftPoint = this.getChessboardLocation();
			g2d.drawImage(this.getSelectedFieldImage(), (int) (activeField.getPosX() * squareHeight) + topLeftPoint.x,
			        (int) (activeField.getPosY() * squareHeight) + topLeftPoint.y, (int) squareHeight,
			        (int) squareHeight, null);
			
			this.renderPossibleMoves(g2d);
			this.renderPossibleThreats(g2d);
		}
	}
	
	/**
	 * Renders all fields the currently selected piece can move to.
	 * 
	 * @param g2d
	 *            Graphics object
	 */
	private void renderPossibleMoves(Graphics2D g2d)
	{
		if(activeField != null && chessboard != null)
		{
			Piece piece = chessboard.getBoard().getPiece(activeField);
			
			if(piece != null)
			{
				Point topLeftPoint = this.getChessboardLocation();
				for(Move move : chessboard.getPossibleMoves(chessboard.getBoard().getPiece(activeField), true))
				{
					Field target = move.getTo();
					g2d.drawImage(this.getPossibleFieldImage(), (int) (target.getPosX() * squareHeight) + topLeftPoint.x,
					        (int) (target.getPosY() * squareHeight) + topLeftPoint.y, (int) squareHeight,
					        (int) squareHeight, null);
				}
			}
		}
	}
	
	private void renderPossibleThreats(Graphics2D g2d) {
		if(activeField != null && chessboard != null)
		{
			Piece piece = chessboard.getBoard().getPiece(activeField);
			
			if(piece != null)
			{
				// TODO: re-enable this
				Point topLeftPoint = this.getChessboardLocation();
				
				// Render all threats to this piece
				/*for(Field field : chessboard.getPossibleThreats(piece, true)) {
					g2d.drawImage(this.getThreateningFieldImage(), (int) (field.getPosX() * squareHeight) + topLeftPoint.x,
					        (int) (field.getPosY() * squareHeight) + topLeftPoint.y, (int) squareHeight,
					        (int) squareHeight, null);
				}*/
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
	private Field getFieldFromMousePosition(MouseEvent event)
	{
		int x = event.getX();
		int y = event.getY();
		
		if(renderLabels)
		{
			// If labels have been rendered, we need to deduct their size
			x -= this.labelHeight;
			y -= this.labelHeight;
		}
		
		// Test if click is outside of chessboard
		if((x < 0) || (y < 0) || (x > chessboardHeight) || (y > chessboardHeight))
		{
			log.log(Level.FINE, "Clicked outside of the chessboard");
			return null;
		}
		
		// Every squareHeight a new square starts (who knew)
		int squareX = (int) (x / squareHeight);
		int squareY = (int) (y / squareHeight);
		
		log.log(Level.FINE, "Square X: " + squareX + " | Square Y: " + squareY);
		
		if(chessboard == null)
		{
			return null;
		}
		
		try
		{
			return chessboard.getBoard().getField(squareX, squareY);
		} catch(ArrayIndexOutOfBoundsException exc)
		{
			// Realistically should only happen when something with the board is
			// f'ed up
			log.log(Level.SEVERE, "Failed to retrieve chessboard field!", exc);
			return null;
		}
	}
	
	/**
	 * Selects a field on the chessboard.
	 * 
	 * @param field
	 *            Field to select
	 */
	public void select(Field field)
	{
		this.activeField = field;
		
		if(field != null)
		{
			log.log(Level.FINE, "Active X: " + (field.getPosX() + 1) + " | Active Y: " + (field.getPosY() + 1));
		}
		this.render();
	}
	
	/**
	 * De-selects the current active field. This triggers a re-render.
	 */
	public void unselect()
	{
		this.activeField = null;
		this.render();
	}
	
	public Point getChessboardLocation()
	{
		if(renderLabels)
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
		
		if(chessboard != null)
		{
			for(Piece piece : chessboard.getBoard().getPieces())
			{
				this.renderPiece(piece, g2d);
			}
			
			this.renderSelectedField(g2d);
		}
	}
	
	@Override
	public void changeSize(int width, int height)
	{
		height = Math.min(width, height);
		this.chessboardHeight = height;
		if(renderLabels)
		{
			// If we need to consider labels the height has to be adapted
			this.labelHeight = Math.max(MIN_LABEL_HEIGHT, (int) (height / 32.0));
			this.chessboardHeight -= 2 * this.labelHeight;
		} else {
			this.labelHeight = 1;
		}
		this.squareHeight = this.chessboardHeight / (double) (this.getSquareCount());
		
		this.setSize(height, height);
		this.drawLabels();
		this.render();
	}
	
	protected final void drawLabels()
	{
		// Width for entire label and offset for letters / numbers
		int labelWidth = chessboardHeight + 2 * labelHeight;
		double addX = squareHeight / 2.0 + labelHeight;
		
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
		this.renderStrings(g2d, this.getLabelLetters(), addX, 10 + labelHeight / 3.0, squareHeight, 0, invertedBoard);
		g2d.dispose();
		
		// Clear the label
		g2d = (Graphics2D) this.leftRightLabel.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, labelHeight, labelWidth);
		g2d.setColor(Color.black);
		g2d.setFont(new Font("Arial", Font.BOLD, 12));
		
		// Render the letter number strings
		this.renderStrings(g2d, this.getLabelNumbers(), 3 + labelHeight / 3.0, addX, 0, squareHeight, !invertedBoard);
		g2d.dispose();
	}
	
	private void renderStrings(Graphics2D g2d, List<String> strings, double startX, double startY, double stepX,
	        double stepY, boolean inverse)
	{
		if(inverse)
		{
			// When reversed, start at the end instead
			startX += stepX * (strings.size() - 1);
			stepX *= -1;
			startY += stepY * (strings.size() - 1);
			stepY *= -1;
		}
		
		// Render each string at the current position
		for(String str : strings)
		{
			g2d.drawString(str, (int) startX, (int) startY);
			startX += stepX;
			startY += stepY;
		}
	}
	
	public Field getActiveSquare()
	{
		return activeField;
	}
	
	@Override
	public void mouseClicked(MouseEvent event)
	{
	}
	
	@Override
	public void mousePressed(MouseEvent event)
	{
		if(event.getButton() == MouseEvent.BUTTON1) // left button
		{
			log.log(Level.FINE, "Left button click for field selection");
			this.boardActionHandler.onFieldSelection(this.getFieldFromMousePosition(event));
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
	
	@Override
	public void showMessage(String key, String arg)
	{
		String message = Localization.getMessage(key);
		if(!arg.isEmpty())
		{
			message += " : " + arg;
		}
		JOptionPane.showMessageDialog(this, message);
	}
	
	@Override
	public Option showConfirmMessage(String key, String arg)
	{
		String message = Localization.getMessage(key);
		if(!arg.isEmpty())
		{
			message += " : " + arg;
		}
		int selectedOption = JOptionPane.showConfirmDialog(this, message, "", JOptionPane.YES_NO_CANCEL_OPTION);
		
		if(selectedOption == JOptionPane.YES_OPTION)
		{
			return Option.YES;
		} else if(selectedOption == JOptionPane.NO_OPTION)
		{
			return Option.NO;
		} else
		{
			return Option.CANCEL;
		}
	}
	
	@Override
	public String showInputMessage(String key, String arg, String initialValue)
	{
		String message = Localization.getMessage(key);
		if(!arg.isEmpty())
		{
			message += " : " + arg;
		}
		return JOptionPane.showInputDialog(this, message, initialValue);
	}
}
