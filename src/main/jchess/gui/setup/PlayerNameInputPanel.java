package jchess.gui.setup;

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
import jchess.gui.GridBagPanel;

public class PlayerNameInputPanel extends GridBagPanel
{
	private static final long serialVersionUID = -5529158483741339210L;
	private static Logger log = Logger.getLogger(PlayerNameInputPanel.class.getName());
	
	private JLabel firstPlayerNameLabel;
	private JLabel secondPlayerNameLabel;
	private JLabel thirdPlayerNameLabel;
	private JLabel fourthPlayerNameLabel;
	
	private JTextField firstPlayerNameTextField;
	private JTextField secondPlayerNameTextField;
	private JTextField thirdPlayerNameTextField;
	private JTextField fourthPlayerNameTextField;
	
	private static final int EMPTY = 0;
	
	protected void initializeGuiElements()
	{
		this.firstPlayerNameLabel = new JLabel(Localization.getMessage("first_player_name") + ": ");
		this.secondPlayerNameLabel = new JLabel(Localization.getMessage("second_player_name") + ": ");
		this.thirdPlayerNameLabel = new JLabel(Localization.getMessage("third_player_name") + ": ");
		this.fourthPlayerNameLabel = new JLabel(Localization.getMessage("fourth_player_name") + ": ");
		
		this.firstPlayerNameTextField = createTextField(true);
		this.secondPlayerNameTextField = createTextField(true);
		this.thirdPlayerNameTextField = createTextField(false);
		this.fourthPlayerNameTextField = createTextField(false);
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
		setGridBagConstraints(firstPlayerNameTextField, LEFT, 1);
		
		setGridBagConstraints(secondPlayerNameLabel, LEFT, 2);
		setGridBagConstraints(secondPlayerNameTextField, LEFT, 3);
		
		this.gridBagConstraints.insets = BUFFER;
		
		setGridBagConstraints(thirdPlayerNameLabel, RIGHT, 0);
		setGridBagConstraints(thirdPlayerNameTextField, RIGHT, 1);
		
		setGridBagConstraints(fourthPlayerNameLabel, RIGHT, 2);
		setGridBagConstraints(fourthPlayerNameTextField, RIGHT, 3);
	}
	
	public boolean playerNamesEmpty()
	{
		if(this.firstPlayerNameTextField.getText().length() == EMPTY
		        || this.secondPlayerNameTextField.getText().length() == EMPTY || additionalTextFieldsActiveAndEmpty())
		{
			JOptionPane.showMessageDialog(this, Localization.getMessage("fill_names"));
			return true;
		}
		else
			return false;
	}
	
	private boolean additionalTextFieldsActiveAndEmpty()
	{
		return (this.thirdPlayerNameTextField.isEnabled() && this.fourthPlayerNameTextField.isEnabled())
		        && (this.thirdPlayerNameTextField.getText().length() == EMPTY
		                || this.fourthPlayerNameTextField.getText().length() == EMPTY);
	}
	
	public void shortenPlayerNames()
	{
		trimPlayerString(firstPlayerNameTextField);
		trimPlayerString(secondPlayerNameTextField);
		
		if(this.thirdPlayerNameTextField.isEnabled() && this.fourthPlayerNameTextField.isEnabled())
		{
			trimPlayerString(thirdPlayerNameTextField);
			trimPlayerString(fourthPlayerNameTextField);
		}
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
		firstPlayer.setName(this.firstPlayerNameTextField.getText());
		secondPlayer.setName(this.secondPlayerNameTextField.getText());
	}
	
	public List<String> getPlayerNames()
	{
		List<String> playerList = new ArrayList<String>();
		playerList.add(this.getPlayerName(0));
		playerList.add(this.getPlayerName(1));
		playerList.add(this.getPlayerName(2));
		playerList.add(this.getPlayerName(3));
		
		return playerList;
	}
	
	public String getPlayerName(int index)
	{
		switch(index)
		{
			case 0:
				return this.firstPlayerNameTextField.getText();
			case 1:
				return this.secondPlayerNameTextField.getText();
			case 2:
				return this.thirdPlayerNameTextField.getText();
			case 3:
				return this.fourthPlayerNameTextField.getText();
			default:
				return "";
		}
	}
	
	public void setAdditionalTextFieldsEnabled(boolean isEnabled)
	{
		this.thirdPlayerNameTextField.setEnabled(isEnabled);
		this.fourthPlayerNameTextField.setEnabled(isEnabled);
	}
}