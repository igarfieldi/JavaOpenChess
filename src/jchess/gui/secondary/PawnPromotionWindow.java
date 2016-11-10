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

import java.awt.*;
import javax.swing.*;

import jchess.gui.GUI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Class responsible for promotion of a pawn. When a pawn reaches the end of the
 * chessboard, it can be changed into a rook, bishop, queen or knight. To what a pawn
 * is promoted decides the player.
 * 
 * @param parent Information about the current piece
 * @param color The player color
 */
public class PawnPromotionWindow extends JDialog implements ActionListener
{
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
		initializePieceButtons(color);
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

	private void initializePieceButtons(String color)
	{
		createPieceButton(queenButton, "Queen-", color);
		createPieceButton(rookButton, "Rook-", color);
		createPieceButton(bishopButton, "Bishop-", color);
		createPieceButton(knightButton, "Knight-", color);
	}

	private void createPieceButton(JButton pieceButton, String pieceResourceName, String color)
	{
		pieceButton = new JButton(new ImageIcon(GUI.loadThemeImage(pieceResourceName + color + ".png")));
		pieceButton.addActionListener(this);
		this.add(pieceButton);
	}

	/**
	 * Method for setting the color of promoted pawn
	 * 
	 * @param color The players color
	 */
	public void setColor(String color)
	{
		this.knightButton.setIcon(new ImageIcon(GUI.loadThemeImage("Knight-" + color + ".png")));
		this.bishopButton.setIcon(new ImageIcon(GUI.loadThemeImage("Bishop-" + color + ".png")));
		this.rookButton.setIcon(new ImageIcon(GUI.loadThemeImage("Rook-" + color + ".png")));
		this.queenButton.setIcon(new ImageIcon(GUI.loadThemeImage("Queen-" + color + ".png")));
	}
	
	/**
	 * Method which is changing a pawn into queen, rook, bishop or knight
	 * 
	 * @param button Information about performed action
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
