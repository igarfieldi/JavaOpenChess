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
import java.util.logging.Logger;

import javax.swing.*;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Game;
import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;

/**
 * Class responsible for drawing the fold with local game settings
 */
public class LocalSettingsPanel extends GridBagPanel implements ActionListener
{
	private static final long serialVersionUID = -9175716765749855635L;
	private static Logger log = Logger.getLogger(LocalSettingsPanel.class.getName());
	
	private JDialog localSettingsWindow;
	
	/*private ButtonGroup opponentChoiceButtonGroup;
	private JRadioButton computerOpponentRadioButton;
	private JRadioButton humanOpponentRadioButton;*/
	
	PlayerNameInputPanel playerNameInputPanel;
	
	private JCheckBox isWhiteOnTopOfBoardCheckBox;
	
	private JCheckBox hasTimeLimitCheckBox;
	private JComboBox<String> timeLimitsComboBox;
	private String timeLimits[] = { "1", "3", "5", "8", "10", "15", "20", "25", "30", "60", "120" };
	
	private JButton okButton;
	
	LocalSettingsPanel(JDialog localSettingsWindow)
	{
		super();
		this.localSettingsWindow = localSettingsWindow;
		
		playerNameInputPanel = new PlayerNameInputPanel();
		
		//configurePlayerTypeChoiceGUI();
		initializeGuiElements();
		placeGuiElements();
	}
	
	/*private void configurePlayerTypeChoiceGUI()
	{
		this.opponentChoiceButtonGroup = new ButtonGroup();
		this.computerOpponentRadioButton = new JRadioButton(Localization.getMessage("against_computer"), false);
		this.humanOpponentRadioButton = new JRadioButton(Localization.getMessage("against_other_human"), true);
		
		this.computerOpponentRadioButton.addActionListener(this);
		this.humanOpponentRadioButton.addActionListener(this);
		
		this.opponentChoiceButtonGroup.add(computerOpponentRadioButton);
		this.opponentChoiceButtonGroup.add(humanOpponentRadioButton);
		
		this.computerOpponentRadioButton.setEnabled(false); // for now, because
		                                                    // not implemented!
	}*/
	
	private void initializeGuiElements()
	{
		this.isWhiteOnTopOfBoardCheckBox = new JCheckBox(Localization.getMessage("upside_down"));
		
		this.hasTimeLimitCheckBox = new JCheckBox(Localization.getMessage("time_game_min"));
		this.timeLimitsComboBox = new JComboBox<String>(timeLimits);
		
		this.okButton = new JButton(Localization.getMessage("ok"));
		this.okButton.addActionListener(this);
	}
	
	private void placeGuiElements()
	{
		//setGridBagConstraints(computerOpponentRadioButton, 0, 0);
		//setGridBagConstraints(humanOpponentRadioButton, 1, 0);
		setGridBagConstraints(playerNameInputPanel, 0, 1);
		
		setGridBagConstraints(isWhiteOnTopOfBoardCheckBox, 0, 2);
		setGridBagConstraints(hasTimeLimitCheckBox, 0, 3);
		setGridBagConstraints(timeLimitsComboBox, 1, 3);
		setGridBagConstraints(okButton, 1, 4);
	}
	
	/**
	 * Method responsible for changing the options.
	 * 
	 * @param event
	 *            where is saving data of performed action
	 */
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
			
			Game gameWindow = JChessApp.view.addNewTab(
			        playerNameInputPanel.getFirstPlayerName() + " vs " + playerNameInputPanel.getSecondPlayerName());
			applySettings(gameWindow);
			drawGameWindow(gameWindow);
		}
	}
	
	private void applySettings(Game gameWindow)
	{
		Settings localSettings = gameWindow.getSettings();
		Player firstPlayer = localSettings.getWhitePlayer();
		Player secondPlayer = localSettings.getBlackPlayer();
		
		localSettings.setGameMode(Settings.GameMode.NEW_GAME);
		localSettings.setGameType(Settings.GameType.LOCAL);
		
		playerNameInputPanel.assignPlayerNames(firstPlayer, secondPlayer);
		setPlayerTypes(firstPlayer, secondPlayer);
		
		setFigureColorPlacementOnBoard(localSettings);
		setTimeLimit(gameWindow, localSettings);
		
		logGameInfo(localSettings, firstPlayer, secondPlayer);
	}
	
	private void setPlayerTypes(Player firstPlayer, Player secondPlayer)
	{
		firstPlayer.setType(Player.Type.LOCAL);
		secondPlayer.setType(Player.Type.LOCAL);
	}
	
	private void setFigureColorPlacementOnBoard(Settings localSettings)
	{
		if(this.isWhiteOnTopOfBoardCheckBox.isSelected())
			localSettings.setUpsideDown(true);
		else
			localSettings.setUpsideDown(false);
	}
	
	private void setTimeLimit(Game gameWindow, Settings localSettings)
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
	
	private void logGameInfo(Settings localSettings, Player firstPlayer, Player secondPlayer)
	{
		log.info(this.timeLimitsComboBox.getActionCommand());
		log.info("****************\nStarting new game: " + firstPlayer.getName() + " vs. " + secondPlayer.getName()
		        + "\ntime 4 game: " + localSettings.getTimeForGame() + "\ntime limit set: "
		        + localSettings.isTimeLimitSet() + "\nwhite on top?: " + localSettings.isUpsideDown()
		        + "\n****************");
	}
	
	private void drawGameWindow(Game gameWindow)
	{
		gameWindow.newGame();
		this.localSettingsWindow.setVisible(false);
		gameWindow.getChessboard().getView().repaint();
	}
}