package jchess.gui.setup;

import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import jchess.Localization;
import jchess.gamelogic.Player;

public class PlayerNameInputPanel extends GridBagPanel
{
	private static final long serialVersionUID = -5529158483741339210L;
	private static Logger log = Logger.getLogger(PlayerNameInputPanel.class.getName());
	
	private JLabel firstPlayerNameLabel;
	private JLabel secondPlayerNameLabel;
	
	private JTextField firstPlayerNameTextField;
	public String getFirstPlayerName()
	{
		return firstPlayerNameTextField.getText();
	}
	
	private JTextField secondPlayerNameTextField;
	public String getSecondPlayerName()
	{
		return secondPlayerNameTextField.getText();
	}
	
	public PlayerNameInputPanel()
	{
		super();
		
		initializeGuiElements();
		placeGuiElements();
	}

	private void initializeGuiElements()
	{
		this.firstPlayerNameLabel = new JLabel(Localization.getMessage("first_player_name") + ": ");
		this.secondPlayerNameLabel = new JLabel(Localization.getMessage("second_player_name") + ": ");
		
		this.firstPlayerNameTextField = new JTextField("", 10);
		this.firstPlayerNameTextField.setSize(new Dimension(200, 50));
		this.secondPlayerNameTextField = new JTextField("", 10);
		this.secondPlayerNameTextField.setSize(new Dimension(200, 50));
	}
	
	private void placeGuiElements()
	{
		setGridBagConstraints(firstPlayerNameLabel, 0, 0);
		setGridBagConstraints(firstPlayerNameTextField, 0, 1);
		
		setGridBagConstraints(secondPlayerNameLabel, 0, 2);
		setGridBagConstraints(secondPlayerNameTextField, 0, 3);
	}
	
	public boolean playerNamesEmpty()
	{
		if(this.firstPlayerNameTextField.getText().length() == 0 || this.secondPlayerNameTextField.getText().length() == 0)
		{
			JOptionPane.showMessageDialog(this, Localization.getMessage("fill_names"));
			return true;
		}
		else
			return false;
	}
	
	public void shortenPlayerNames()
	{
		trimPlayerString(firstPlayerNameTextField);
		trimPlayerString(secondPlayerNameTextField);
	}
	
	private void trimPlayerString(JTextField textField)
	{
		if(textField.getText().length() > 9)
		{
			String trimmedPlayerName = new String();
			try
			{
				trimmedPlayerName = textField.getText(0, 9);
			}
			catch(BadLocationException exception)
			{
				log.log(Level.SEVERE, "Something wrong in edit tables: \n", exception);
			}
			
			textField.setText(trimmedPlayerName);
		}
	}
	
	public void assignPlayerNames(Player firstPlayer, Player secondPlayer)
	{
		firstPlayer.setName(this.firstPlayerNameTextField.getText());
		secondPlayer.setName(this.secondPlayerNameTextField.getText());
	}
}