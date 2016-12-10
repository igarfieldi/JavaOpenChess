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
package jchess.gui.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Game;
import jchess.gamelogic.Settings;

/**
 * Class responsible for drawing the fold with local game settings
 */
public class LocalSettingsPanel extends GridBagPanel implements ActionListener
{
	private static final long serialVersionUID = -9175716765749855635L;
	private static Logger log = Logger.getLogger(LocalSettingsPanel.class.getName());
	
	private JDialog newGameWindow;
	
	private PlayerNumberChoicePanel playerNumberChoicePanel;
	private PlayerNameInputPanel playerNameInputPanel;
	private TimerSetterPanel timerSetterPanel;
	private JButton okButton;
	
	LocalSettingsPanel(JDialog newGameWindow)
	{
		super();
		this.newGameWindow = newGameWindow;
		
		initializeGuiElements();
		placeGuiElements();
	}
	
	private void initializeGuiElements()
	{
		playerNameInputPanel = new PlayerNameInputPanel();
		playerNumberChoicePanel = new PlayerNumberChoicePanel(playerNameInputPanel);
		timerSetterPanel = new TimerSetterPanel();
		
		this.okButton = new JButton(Localization.getMessage("ok"));
		this.okButton.addActionListener(this);
	}
	
	private void placeGuiElements()
	{
		setGridBagConstraints(playerNumberChoicePanel, 0, 0);
		setGridBagConstraints(playerNameInputPanel, 0, 1);
		setGridBagConstraints(timerSetterPanel, 0, 2);
		setGridBagConstraints(okButton, 0, 3);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == this.okButton)
			startGame();
	}
	
	private void startGame()
	{
		if(!playerNameInputPanel.playerNamesEmpty())
		{
			playerNameInputPanel.shortenPlayerNames();
			Game gameWindow = null;
			
			if(playerNumberChoicePanel.getPlayerCount() == 2) {
				gameWindow = JChessApp.view.addNewTwoPlayerTab(
				        playerNameInputPanel.getPlayerName(0),
				        playerNameInputPanel.getPlayerName(1));
			} else if(playerNumberChoicePanel.getPlayerCount() == 4) {
				gameWindow = JChessApp.view.addNewFourPlayerTab(
				        playerNameInputPanel.getPlayerName(0),
				        playerNameInputPanel.getPlayerName(1),
				        playerNameInputPanel.getPlayerName(2),
				        playerNameInputPanel.getPlayerName(3));
			} else {
				log.log(Level.SEVERE, "Could not start game because the number of players is not supported!");
				return ;
			}
			applySettings(gameWindow);
			drawGameWindow(gameWindow);
		}
	}
	
	private void applySettings(Game gameWindow)
	{
		Settings localSettings = gameWindow.getSettings();
		
		localSettings.setGameMode(Settings.GameMode.NEW_GAME);
		timerSetterPanel.setTimeLimit(gameWindow, localSettings);
	}
	
	private void drawGameWindow(Game gameWindow)
	{
		gameWindow.newGame();
		this.newGameWindow.setVisible(false);
		gameWindow.getChessboard().getView().render();
	}
}