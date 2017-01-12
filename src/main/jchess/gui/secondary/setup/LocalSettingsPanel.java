/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 */
package jchess.gui.secondary.setup;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import jchess.Localization;
import jchess.gui.secondary.GridBagPanel;

/**
 * Class responsible for drawing the fold with local game settings
 */
public class LocalSettingsPanel extends GridBagPanel implements ActionListener
{
	private static final long serialVersionUID = -9175716765749855635L;
	
	private SettingsAdopter settingsAdopter;
	
	private PlayerNumberChoicePanel playerNumberChoicePanel;
	private PlayerNameInputPanel playerNameInputPanel;
	private TimerSetterPanel timerSetterPanel;
	private JButton okButton;
	
	LocalSettingsPanel()
	{
		super();
		this.settingsAdopter = new SettingsAdopter();
	}
	
	protected void initializeGuiElements()
	{
		playerNameInputPanel = new PlayerNameInputPanel();
		playerNumberChoicePanel = new PlayerNumberChoicePanel(playerNameInputPanel);
		timerSetterPanel = new TimerSetterPanel();
		
		this.okButton = new JButton(Localization.getMessage("ok"));
		this.okButton.addActionListener(this);
	}
	
	protected void placeGuiElements()
	{
		setGridBagConstraints(playerNumberChoicePanel, LEFT, 0);
		setGridBagConstraints(playerNameInputPanel, LEFT, 1);
		setGridBagConstraints(timerSetterPanel, LEFT, 2);
		setGridBagConstraints(okButton, LEFT, 3);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == this.okButton)
		{
			startGame();
			
			Window parentDialog = SwingUtilities.getWindowAncestor(this);
			parentDialog.setVisible(false);
		}
	}
	
	private void startGame()
	{
		if(!playerNameInputPanel.playerNamesEmpty())
		{
			playerNameInputPanel.shortenPlayerNames();
			settingsAdopter.createGameWindow(timerSetterPanel.getTimeLimit(), playerNumberChoicePanel.getPlayerCount(),
			        playerNameInputPanel.getPlayerNames());
		}
	}
}