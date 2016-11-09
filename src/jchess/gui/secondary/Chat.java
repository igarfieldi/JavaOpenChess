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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jchess.network.Client;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

/**
 * Class representing the game chat Players are in touch and can write a
 * messages to each other
 */
public class Chat extends JPanel implements ActionListener
{
	private Client client;
	private GridBagLayout gridBagLayout;
	private GridBagConstraints gridBagConstraints;
	private JScrollPane scrollPane;
	private JTextArea textOutput;
	private JTextField textInput;
	private JButton sendButton;

	public void setClient(Client client)
	{
		this.client = client;
	}

	public Chat()
	{
		super();
		
		createGuiElements();
		createGridBagLayout();
		placeGuiElementsOnWindowWithPositionConstraints();
	}
	
	private void createGuiElements()
	{
		this.textOutput = new JTextArea();
		this.textOutput.setEditable(false);
		
		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewportView(this.textOutput);
		
		this.textInput = new JTextField();
		this.textInput.addActionListener(this);
		
		this.sendButton = new JButton("^");
		this.sendButton.addActionListener(this);
	}
	
	private void createGridBagLayout()
	{
		this.gridBagLayout = new GridBagLayout();
		this.gridBagConstraints = new GridBagConstraints();
		this.gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.setLayout(gridBagLayout);
	}
	
	private void placeGuiElementsOnWindowWithPositionConstraints()
	{
		setGridBagConstraints(0, 0, 2, 1, 0, 1.0, scrollPane);
		setGridBagConstraints(0, 1, 1, 1, 1.0, 0, textInput);
		setGridBagConstraints(1, 1, 1, 1, 0, 0, sendButton);
	}
	
	private void setGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx,
	        double weighty, JComponent guiElement)
	{
		this.gridBagConstraints.gridx = gridx;
		this.gridBagConstraints.gridy = gridy;
		this.gridBagConstraints.gridwidth = gridwidth;
		this.gridBagConstraints.gridheight = gridheight;
		this.gridBagConstraints.weightx = weightx;
		this.gridBagConstraints.weighty = weighty;
		this.gridBagLayout.setConstraints(guiElement, gridBagConstraints);
		this.add(guiElement);
	}
	
	/**
	 * Method of adding message to the list
	 */
	public void addMessage(String message) // added message to list
	{
		textOutput.append(message + "\n");
		textOutput.setCaretPosition(textOutput.getDocument().getLength());
	}
	
	/**
	 * Sending message method
	 */
	public void actionPerformed(ActionEvent arg0) // sending message
	{
		this.client.sendMassage(textInput.getText());
		textInput.setText("");
	}
}
