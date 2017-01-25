package jchess.gui.secondary.setup;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import jchess.Localization;
import jchess.gui.secondary.GridBagPanel;

/**
 * Class with a panel that contains a checkbox and combobox to set a game's time limit.
 */
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
		setGridBagConstraints(timeLimitsComboBox, MIDDLE, 0);
	}
	
	/**
	 * 
	 * @return time limit that was selected in the combo box in seconds per minute.
	 */
	public int getTimeLimit()
	{
		final int SECONDS_PER_MINUTE = 60;
		final int NO_TIME_LIMIT = 0;
		
		if(this.hasTimeLimitCheckBox.isSelected())
		{
			String selectedTimeLimit = this.timeLimits[this.timeLimitsComboBox.getSelectedIndex()];
			return ((int) Integer.parseInt(selectedTimeLimit) * SECONDS_PER_MINUTE);
		}
		else
			return NO_TIME_LIMIT;
	}
	
	/**
	 * 
	 * @return action command for the change of the combobox' content.
	 */
	public String getTimeLimitComboBoxChange()
	{
		return this.timeLimitsComboBox.getActionCommand();
	}
}
