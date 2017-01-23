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
package jchess.gui.secondary;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import jchess.gamelogic.Player.Color;
import jchess.gui.secondary.themechooser.ThemeImageLoader;

/**
 * Class responsible for promotion of a pawn. When a pawn reaches the end of the
 * chessboard, it can be changed into a rook, bishop, queen or knight. To what a
 * pawn is promoted decides the player.
 */
public class PawnPromotionWindow extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -1026252750919159633L;
	
	private String selectedPromotion;
	
	private JButton knightButton;
	private JButton bishopButton;
	private JButton rookButton;
	private JButton queenButton;
	
	private static final String NOTHING_SELECTED = "";
	private static final String KNIGHT = "Knight";
	private static final String BISHOP = "Bishop";
	private static final String ROOK = "Rook";
	private static final String QUEEN = "Queen";
	
	/**
	 * 
	 * @return String of the piece to which the pawn will be promoted to.
	 */
	public String getSelectedPromotion()
	{
		return selectedPromotion;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            Parent of the window.
	 * @param color
	 *            Color for the piece selection.
	 */
	public PawnPromotionWindow(Frame parent, Color color)
	{
		super(parent);
		this.selectedPromotion = NOTHING_SELECTED;
		
		setWindowProperties();
		createPieceButtons(color);
		initializePieceButtons();
	}
	
	/**
	 * Sets the properties of this dialog.
	 */
	private void setWindowProperties()
	{
		final Dimension SIZE = new Dimension(520, 130);
		final GridLayout GRID_LAYOUT = new GridLayout(1, 4);
		
		this.setTitle("Choose piece!");
		this.setMinimumSize(SIZE);
		this.setSize(SIZE);
		this.setMaximumSize(SIZE);
		this.setResizable(false);
		this.setLayout(GRID_LAYOUT);
	}
	
	/**
	 * Instatiates the buttons for each possible promotion. Each button has an image of the piece
	 * in the color of the piece to be promoted.
	 * 
	 * @param color
	 * 				Color of the promoted piece
	 */
	private void createPieceButtons(Color color)
	{
		String colorName = ThemeImageLoader.getInstance().getColorString(color);
		
		this.knightButton = createImageButton(KNIGHT, colorName);
		this.bishopButton = createImageButton(BISHOP, colorName);
		this.rookButton = createImageButton(ROOK, colorName);
		this.queenButton = createImageButton(QUEEN, colorName);
	}

	/**
	 * Creates a button with the piece and the color of the promoted pawn as an image.
	 * 
	 * @param pieceName
	 * 				The name of the piece.
	 * @param colorName
	 * 				The string of the color that the pawn has.
	 * @return button with image
	 */
	private JButton createImageButton(String pieceName, String colorName)
	{
		final Image PIECE_IMAGE = ThemeImageLoader.getInstance().loadThemeImage(pieceName + "-" + colorName + ".png");
		ImageIcon imageIcon = new ImageIcon(PIECE_IMAGE);
		
		return new JButton(imageIcon);
	}
	
	/**
	 * Helper method that adds all piece buttons to the panel.
	 */
	private void initializePieceButtons()
	{
		addPieceButtonToWindow(this.queenButton);
		addPieceButtonToWindow(this.rookButton);
		addPieceButtonToWindow(this.bishopButton);
		addPieceButtonToWindow(this.knightButton);
	}
	
	/**
	 * Adds the piece buttons to the dialog with an action listener.
	 * 
	 * @param pieceButton
	 * 				The piece button to be added.
	 */
	private void addPieceButtonToWindow(JButton pieceButton)
	{
		pieceButton.addActionListener(this);
		this.add(pieceButton);
	}
	
	/**
	 * Method which is changing a pawn into queen, rook, bishop or knight
	 * 
	 * @param button
	 *            Information about performed action
	 */
	public void actionPerformed(ActionEvent button)
	{
		chooseSelectedPiece(button);
		this.setVisible(false);
	}
	
	/**
	 * Sets the selectedPromotion string to the name of the piece corresponding to the appropriate button.
	 * 
	 * @param button
	 * 				The button that was clicked.
	 */
	private void chooseSelectedPiece(ActionEvent button)
	{
		if(button.getSource() == queenButton)
			this.selectedPromotion = QUEEN;
		else if(button.getSource() == rookButton)
			this.selectedPromotion = ROOK;
		else if(button.getSource() == bishopButton)
			this.selectedPromotion = BISHOP;
		else if(button.getSource() == knightButton)
			this.selectedPromotion = KNIGHT;
	}
}
