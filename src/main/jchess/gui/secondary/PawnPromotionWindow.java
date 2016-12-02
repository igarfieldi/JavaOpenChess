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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import jchess.gui.ThemeImageLoader;

/**
 * Class responsible for promotion of a pawn. When a pawn reaches the end of the
 * chessboard, it can be changed into a rook, bishop, queen or knight. To what a
 * pawn is promoted decides the player.
 * 
 * @param parent
 *            Information about the current piece
 * @param color
 *            The player color
 */
public class PawnPromotionWindow extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -1026252750919159633L;

	private String selectedPromotion;
	
	private JButton knightButton;
	private JButton bishopButton;
	private JButton rookButton;
	private JButton queenButton;
	
	public String getSelectedPromotion()
	{
		return selectedPromotion;
	}
	
	public PawnPromotionWindow(Frame parent, String color)
	{
		super(parent);
		this.selectedPromotion = "";
		
		initializeWindow();
        createPieceButtons(color);
        initializePieceButtons();
	}

	private void initializeWindow()
	{
		this.setTitle("Choose piece");
		this.setMinimumSize(new Dimension(520, 130));
		this.setSize(new Dimension(520, 130));
		this.setMaximumSize(new Dimension(520, 130));
		this.setResizable(false);
		this.setLayout(new GridLayout(1, 4));
	}

	private void createPieceButtons(String color)
	{
		this.knightButton = new JButton(new ImageIcon(ThemeImageLoader.loadThemeImage("Knight-" + color + ".png")));
        this.bishopButton = new JButton(new ImageIcon(ThemeImageLoader.loadThemeImage("Bishop-" + color + ".png")));
        this.rookButton = new JButton(new ImageIcon(ThemeImageLoader.loadThemeImage("Rook-" + color + ".png")));
        this.queenButton = new JButton(new ImageIcon(ThemeImageLoader.loadThemeImage("Queen-" + color + ".png")));
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
	 * Method for setting the color of promoted pawn
	 * 
	 * @param color
	 *            The players color
	 */
	public void setColor(String color)
	{
		this.knightButton.setIcon(new ImageIcon(ThemeImageLoader.loadThemeImage("Knight-" + color + ".png")));
		this.bishopButton.setIcon(new ImageIcon(ThemeImageLoader.loadThemeImage("Bishop-" + color + ".png")));
		this.rookButton.setIcon(new ImageIcon(ThemeImageLoader.loadThemeImage("Rook-" + color + ".png")));
		this.queenButton.setIcon(new ImageIcon(ThemeImageLoader.loadThemeImage("Queen-" + color + ".png")));
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
		else
			this.selectedPromotion = "Knight";
	}
}
