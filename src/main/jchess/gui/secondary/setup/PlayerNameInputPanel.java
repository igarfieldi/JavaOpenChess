package jchess.gui.secondary.setup;

import java.awt.Dimension;
import java.awt.Insets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import jchess.Localization;
import jchess.gui.secondary.GridBagPanel;

/**
 * Class with a panel containing all labels and text fields used to name the players
 */
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
	private static final int MAX_PLAYERS = 4;
	
	protected void initializeGuiElements()
	{
		this.firstPlayerNameLabel = new JLabel(Localization.getMessage("first_player_name") + ": ");
		this.secondPlayerNameLabel = new JLabel(Localization.getMessage("second_player_name") + ": ");
		this.thirdPlayerNameLabel = new JLabel(Localization.getMessage("third_player_name") + ": ");
		this.fourthPlayerNameLabel = new JLabel(Localization.getMessage("fourth_player_name") + ": ");
		
		playerNameTextFields = new JTextField[MAX_PLAYERS];
		for(int i = 0; i < MAX_PLAYERS; i++)
		{
			if(i <= 1)
				this.playerNameTextFields[i] = createTextField(true);
			else
				this.playerNameTextFields[i] = createTextField(false);
		}
	}
	
	/**
	 * Creates a new text field object and sets its properties
	 * 
	 * @param isEnabled
	 * 				Determines whether the text field is active.
	 * @return new text field object
	 */
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
	
	/**
	 * Checks if the text fields have any text inside them.
	 * 
	 * @return
	 */
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
	
	/**
	 * Checks if the text fields for the third and fourth players are active and
	 * if they have any text inside them.
	 * 
	 * @return
	 */
	private boolean additionalTextFieldsActiveAndEmpty()
	{
		return (this.playerNameTextFields[2].isEnabled() && this.playerNameTextFields[3].isEnabled())
		        && (this.playerNameTextFields[2].getText().length() == EMPTY
		                || this.playerNameTextFields[3].getText().length() == EMPTY);
	}
	
	/**
	 * Trims the character length of all player names.
	 */
	public void shortenPlayerNames()
	{
		for(int i = 0; i < MAX_PLAYERS; i++)
			trimPlayerString(playerNameTextFields[i]);
	}
	
	/**
	 * Shortens the text inside the text field to 9 characters.
	 * 
	 * @param textField
	 * 				The text field whose text should be trimmed.
	 */
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
	
	/**
	 * 
	 * @return array with all player names.
	 */
	public String[] getPlayerNames()
	{
		String playerNames[] = new String[MAX_PLAYERS];
		for(int i = 0; i < MAX_PLAYERS; i++)
			playerNames[i] = this.playerNameTextFields[i].getText();
		
		return playerNames;
	}
	
	/**
	 * 
	 * @param index
	 * 				Index of the text field whose text content we want.
	 * @return text content of the specified text field
	 */
	public String getPlayerName(int index)
	{
		return this.playerNameTextFields[index].getText();
	}
	
	/**
	 * Enables or disables the text fields of the third and fourth player.
	 * 
	 * @param isEnabled
	 * 				Determines whether text fields are enabled or disabled.
	 */
	public void setAdditionalTextFieldsEnabled(boolean isEnabled)
	{
		this.playerNameTextFields[2].setEnabled(isEnabled);
		this.playerNameTextFields[3].setEnabled(isEnabled);
	}
}