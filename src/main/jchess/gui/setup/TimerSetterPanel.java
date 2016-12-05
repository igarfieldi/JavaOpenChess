package jchess.gui.setup;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import jchess.Localization;
import jchess.gamelogic.Game;
import jchess.gamelogic.Settings;

public class TimerSetterPanel extends GridBagPanel
{
	private static final long serialVersionUID = -4383996249396913058L;
	
	private JCheckBox hasTimeLimitCheckBox;
	private JComboBox<String> timeLimitsComboBox;
	private String timeLimits[] = { "1", "3", "5", "8", "10", "15", "20", "25", "30", "60", "120" };
	
	public TimerSetterPanel()
	{
		super();
		initializeGuiElements();
		placeGuiElements();
	}
	
	private void initializeGuiElements()
	{
		this.hasTimeLimitCheckBox = new JCheckBox(Localization.getMessage("time_game_min"));
		this.timeLimitsComboBox = new JComboBox<String>(timeLimits);
	}
	
	private void placeGuiElements()
	{
		setGridBagConstraints(hasTimeLimitCheckBox, 0, 1);
		setGridBagConstraints(timeLimitsComboBox, 1, 1);
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
