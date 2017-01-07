package jchess.gui.secondary.setup;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import jchess.Localization;
import jchess.gui.secondary.GridBagPanel;

public class TimerSetterPanel extends GridBagPanel
{
	private static final long serialVersionUID = -4383996249396913058L;
	
	private JCheckBox hasTimeLimitCheckBox;
	private JComboBox<String> timeLimitsComboBox;
	private String timeLimits[];
	
	private static final String AVAILABLE_TIMELIMITS[] = new String[]{ "1", "3", "5", "8", "10", "15", "20", "25", "30",
	        "60", "120" };
	
	protected void initializeGuiElements()
	{
		this.hasTimeLimitCheckBox = new JCheckBox(Localization.getMessage("time_game_min"));
		
		this.timeLimits = AVAILABLE_TIMELIMITS;
		this.timeLimitsComboBox = new JComboBox<String>(timeLimits);
	}
	
	protected void placeGuiElements()
	{
		setGridBagConstraints(hasTimeLimitCheckBox, LEFT, 0);
		setGridBagConstraints(timeLimitsComboBox, RIGHT, 0);
	}
	
	public int getTimeLimit()
	{
		final int MINUTES = 60;
		final int NO_TIME_LIMIT = 0;
		
		if(this.hasTimeLimitCheckBox.isSelected())
		{
			String selectedTimeLimit = this.timeLimits[this.timeLimitsComboBox.getSelectedIndex()];
			return ((int) Integer.parseInt(selectedTimeLimit) * MINUTES);
		}
		else
			return NO_TIME_LIMIT;
	}
	
	public String getTimeLimitComboBoxChange()
	{
		return this.timeLimitsComboBox.getActionCommand();
	}
}
