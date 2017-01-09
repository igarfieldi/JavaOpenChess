package jchess.gui.secondary.setup;

import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import jchess.Localization;
import jchess.gamelogic.Player;
import jchess.gui.secondary.GridBagPanel;

public class PlayerNameInputPanel extends GridBagPanel
{
	private static final long serialVersionUID = -5529158483741339210L;
	private static Logger log = Logger.getLogger(PlayerNameInputPanel.class.getName());
	
	private JLabel firstPlayerNameLabel;
	private JLabel secondPlayerNameLabel;
	private JLabel thirdPlayerNameLabel;
	private JLabel fourthPlayerNameLabel;
	
	private JTextField playerNameTextFields[];
	
	private static final int EMPTY = 0;
	
	protected void initializeGuiElements()
	{
		this.firstPlayerNameLabel = new JLabel(Localization.getMessage("first_player_name") + ": ");
		this.secondPlayerNameLabel = new JLabel(Localization.getMessage("second_player_name") + ": ");
		this.thirdPlayerNameLabel = new JLabel(Localization.getMessage("third_player_name") + ": ");
		this.fourthPlayerNameLabel = new JLabel(Localization.getMessage("fourth_player_name") + ": ");
		
		playerNameTextFields = new JTextField[4];
		for(int i = 0; i < 4; i++)
		{
			if(i <= 1)
				this.playerNameTextFields[i] = createTextField(true);
			else
				this.playerNameTextFields[i] = createTextField(false);
		}
	}
	
	private JTextField createTextField(boolean isEnabled)
	{
		JTextField newTextField;
		newTextField = new JTextField("", 10);
		newTextField.setSize(new Dimension(200, 50));
		newTextField.setEnabled(isEnabled);
		
		return newTextField;
	}
	
	protected void placeGuiElements()
	{
		final Insets BUFFER = new Insets(3, 35, 3, 3);
		
		setGridBagConstraints(firstPlayerNameLabel, LEFT, 0);
		setGridBagConstraints(playerNameTextFields[0], LEFT, 1);
		
		setGridBagConstraints(secondPlayerNameLabel, LEFT, 2);
		setGridBagConstraints(playerNameTextFields[1], LEFT, 3);
		
		this.gridBagConstraints.insets = BUFFER;
		
		setGridBagConstraints(thirdPlayerNameLabel, RIGHT, 0);
		setGridBagConstraints(playerNameTextFields[2], RIGHT, 1);
		
		setGridBagConstraints(fourthPlayerNameLabel, RIGHT, 2);
		setGridBagConstraints(playerNameTextFields[3], RIGHT, 3);
	}
	
	public boolean playerNamesEmpty()
	{
		if(this.playerNameTextFields[0].getText().length() == EMPTY
		        || this.playerNameTextFields[1].getText().length() == EMPTY || additionalTextFieldsActiveAndEmpty())
		{
			JOptionPane.showMessageDialog(this, Localization.getMessage("fill_names"));
			return true;
		}
		else
			return false;
	}
	
	private boolean additionalTextFieldsActiveAndEmpty()
	{
		return (this.playerNameTextFields[2].isEnabled() && this.playerNameTextFields[3].isEnabled())
		        && (this.playerNameTextFields[2].getText().length() == EMPTY
		                || this.playerNameTextFields[3].getText().length() == EMPTY);
	}
	
	public void shortenPlayerNames()
	{
		for(int i = 0; i < 4; i++)
			trimPlayerString(playerNameTextFields[i]);
	}
	
	private void trimPlayerString(JTextField textField)
	{
		final int MAX_NAME_LENGTH = 9;
		
		if(textField.getText().length() > MAX_NAME_LENGTH)
		{
			String trimmedPlayerName = new String();
			try
			{
				trimmedPlayerName = textField.getText(0, MAX_NAME_LENGTH);
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
		firstPlayer.setName(this.playerNameTextFields[0].getText());
		secondPlayer.setName(this.playerNameTextFields[1].getText());
	}
	
	public List<String> getPlayerNames()
	{
		List<String> playerList = new ArrayList<String>();
		for(int i = 0; i < 4; i++)
			playerList.add(this.getPlayerName(i));
		
		return playerList;
	}
	
	public String getPlayerName(int index)
	{
		return this.playerNameTextFields[index].getText();
	}
	
	public void setAdditionalTextFieldsEnabled(boolean isEnabled)
	{
		this.playerNameTextFields[2].setEnabled(isEnabled);
		this.playerNameTextFields[3].setEnabled(isEnabled);
	}
}