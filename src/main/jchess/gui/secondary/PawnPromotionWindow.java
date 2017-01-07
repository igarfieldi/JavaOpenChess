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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private static Logger log = Logger.getLogger(PawnPromotionWindow.class.getName());
	
	private String selectedPromotion;
	
	private JButton knightButton;
	private JButton bishopButton;
	private JButton rookButton;
	private JButton queenButton;
	
	private static final String NOTHING_SELECTED = "";
	
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
	
	private void setWindowProperties()
	{
		final Dimension SIZE = new Dimension(520, 130);
		
		this.setTitle("Choose piece!");
		this.setMinimumSize(SIZE);
		this.setSize(SIZE);
		this.setMaximumSize(SIZE);
		this.setResizable(false);
		this.setLayout(new GridLayout(1, 4));
	}
	
	private void createPieceButtons(Color color)
	{
		// TODO: very rigid; shift into ThemeLoader?
		String colorName = setColorString(color);
		
		this.knightButton = new JButton(
		        new ImageIcon(ThemeImageLoader.getInstance().loadThemeImage("Knight-" + colorName + ".png")));
		this.bishopButton = new JButton(
		        new ImageIcon(ThemeImageLoader.getInstance().loadThemeImage("Bishop-" + colorName + ".png")));
		this.rookButton = new JButton(
		        new ImageIcon(ThemeImageLoader.getInstance().loadThemeImage("Rook-" + colorName + ".png")));
		this.queenButton = new JButton(
		        new ImageIcon(ThemeImageLoader.getInstance().loadThemeImage("Queen-" + colorName + ".png")));
	}

	private String setColorString(Color color)
	{
		String colorString = NOTHING_SELECTED;
		switch(color)
		{
			case WHITE:
				colorString = "W";
				break;
			case RED:
				colorString = "BR";
				break;
			case BLACK:
				colorString = "B";
				break;
			case GOLDEN:
				colorString = "G";
				break;
			default:
				log.log(Level.SEVERE, "Tried to show promotion pieces for non-existing color!");
				break;
		}
		return colorString;
	}
	
	private void initializePieceButtons()
	{
		addPieceButtonToWindow(this.queenButton);
		addPieceButtonToWindow(this.rookButton);
		addPieceButtonToWindow(this.bishopButton);
		addPieceButtonToWindow(this.knightButton);
	}
	
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
	
	private void chooseSelectedPiece(ActionEvent button)
	{
		if(button.getSource() == queenButton)
			this.selectedPromotion = "Queen";
		else if(button.getSource() == rookButton)
			this.selectedPromotion = "Rook";
		else if(button.getSource() == bishopButton)
			this.selectedPromotion = "Bishop";
		else if(button.getSource() == knightButton)
			this.selectedPromotion = "Knight";
	}
}
