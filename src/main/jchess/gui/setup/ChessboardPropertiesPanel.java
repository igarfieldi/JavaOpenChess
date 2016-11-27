package jchess.gui.setup;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import jchess.Localization;
import jchess.gamelogic.Game;
import jchess.gamelogic.Settings;

public class ChessboardPropertiesPanel extends GridBagPanel
{
	private static final long serialVersionUID = -4383996249396913058L;
	
	private JCheckBox isWhiteOnTopOfBoardCheckBox;
	
	private JCheckBox hasTimeLimitCheckBox;
	private JComboBox<String> timeLimitsComboBox;
	private String timeLimits[] = { "1", "3", "5", "8", "10", "15", "20", "25", "30", "60", "120" };
	
	public ChessboardPropertiesPanel()
	{
		super();
		initializeGuiElements();
		placeGuiElements();
	}
	
	private void initializeGuiElements()
	{
		this.isWhiteOnTopOfBoardCheckBox = new JCheckBox(Localization.getMessage("upside_down"));
		
		this.hasTimeLimitCheckBox = new JCheckBox(Localization.getMessage("time_game_min"));
		this.timeLimitsComboBox = new JComboBox<String>(timeLimits);
	}
	
	private void placeGuiElements()
	{
		setGridBagConstraints(isWhiteOnTopOfBoardCheckBox, 0, 0);
		setGridBagConstraints(hasTimeLimitCheckBox, 0, 1);
		setGridBagConstraints(timeLimitsComboBox, 1, 1);
	}
	
	public void setFigureColorPlacementOnBoard(Settings localSettings)
	{
		if(this.isWhiteOnTopOfBoardCheckBox.isSelected())
			localSettings.setUpsideDown(true);
		else
			localSettings.setUpsideDown(false);
	}
	
	public void setTimeLimit(Game gameWindow, Settings localSettings)
	{
		if(this.hasTimeLimitCheckBox.isSelected())
		{
			String selectedTimeLimit = this.timeLimits[this.timeLimitsComboBox.getSelectedIndex()];
			Integer timeLimitValue = new Integer(selectedTimeLimit);
			
			localSettings.setTimeLimit(true);
			localSettings.setTimeForGame((int) timeLimitValue * 60);
			
			gameWindow.getGameClock().setTimes(localSettings.getTimeForGame(), localSettings.getTimeForGame());
			gameWindow.getGameClock().start();
		}
	}
	
	public String getTimeLimitComboBoxChange()
	{
		return this.timeLimitsComboBox.getActionCommand();
	}
}
