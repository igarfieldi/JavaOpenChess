package jchess.gui.secondary.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import jchess.Localization;
import jchess.gui.secondary.GridBagPanel;

/**
 * Class with a panel that contains radio buttons to determine the amount of players.
 */
public class PlayerNumberChoicePanel extends GridBagPanel implements ActionListener
{
	private static final long serialVersionUID = -2395769048925349725L;
	
	private ButtonGroup gameTypeChoiceButtonGroup;
	private JRadioButton twoPlayersRadioButton;
	private JRadioButton fourPlayersRadioButton;
	private JRadioButton aiFourPlayersRadioButton;
	
	private PlayerNameInputPanel playerNameInputPanel;
	
	public PlayerNumberChoicePanel(PlayerNameInputPanel playerNameInputPanel)
	{
		super();
		this.playerNameInputPanel = playerNameInputPanel;
	}
	
	/**
	 * 
	 * @return amount of players
	 */
	public int getPlayerCount()
	{
		if(twoPlayersRadioButton.isSelected())
			return 2;
		else if (fourPlayersRadioButton.isSelected())
			return 4;
		else
			return 5;
	}
	
	protected void initializeGuiElements()
	{
		this.gameTypeChoiceButtonGroup = new ButtonGroup();
		
		this.twoPlayersRadioButton = createRadioButton("2_players", true);
		this.fourPlayersRadioButton = createRadioButton("4_players", false);
		this.aiFourPlayersRadioButton = createRadioButton("Ai_4_players", false);
	}
	
	/**
	 * Creates a new radio button object and sets its properties
	 * 
	 * @param label
	 * 				The text content of the radio button
	 * @param isSelected
	 * 				Determines if the button is selected by default.
	 * @return new radio button object
	 */
	private JRadioButton createRadioButton(String label, boolean isSelected)
	{
		JRadioButton newRadioButton;
		newRadioButton = new JRadioButton(Localization.getMessage(label), isSelected);
		newRadioButton.addActionListener(this);
		this.gameTypeChoiceButtonGroup.add(newRadioButton);
		
		return newRadioButton;
	}
	
	protected void placeGuiElements()
	{
		setGridBagConstraints(twoPlayersRadioButton, LEFT, 0);
		setGridBagConstraints(fourPlayersRadioButton, MIDDLE, 0);
		setGridBagConstraints(aiFourPlayersRadioButton, RIGHT, 0);
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == twoPlayersRadioButton)
			playerNameInputPanel.setAdditionalTextFieldsEnabled(false);
		if(event.getSource() == fourPlayersRadioButton)
			playerNameInputPanel.setAdditionalTextFieldsEnabled(true);
		if(event.getSource() == aiFourPlayersRadioButton)
			playerNameInputPanel.setAdditionalTextFieldsEnabled(true);
	}
}
