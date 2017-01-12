package jchess.gui.secondary.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import jchess.Localization;
import jchess.gui.secondary.GridBagPanel;

public class PlayerNumberChoicePanel extends GridBagPanel implements ActionListener
{
	private static final long serialVersionUID = -2395769048925349725L;
	
	private ButtonGroup gameTypeChoiceButtonGroup;
	private JRadioButton twoPlayersRadioButton;
	private JRadioButton fourPlayersRadioButton;
	
	private PlayerNameInputPanel playerNameInputPanel;
	
	public PlayerNumberChoicePanel(PlayerNameInputPanel playerNameInputPanel)
	{
		super();
		this.playerNameInputPanel = playerNameInputPanel;
	}
	
	public int getPlayerCount()
	{
		if(twoPlayersRadioButton.isSelected())
			return 2;
		else
			return 4;
	}
	
	protected void initializeGuiElements()
	{
		this.gameTypeChoiceButtonGroup = new ButtonGroup();
		
		this.twoPlayersRadioButton = createRadioButton("2_players", true);
		this.fourPlayersRadioButton = createRadioButton("4_players", false);
	}
	
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
		setGridBagConstraints(fourPlayersRadioButton, RIGHT, 0);
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == twoPlayersRadioButton)
			playerNameInputPanel.setAdditionalTextFieldsEnabled(false);
		if(event.getSource() == fourPlayersRadioButton)
			playerNameInputPanel.setAdditionalTextFieldsEnabled(true);
	}
}