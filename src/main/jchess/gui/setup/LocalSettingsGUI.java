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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Game;
import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;

/**
 * Class responsible for drawing the fold with local game settings
 */
public class LocalSettingsGUI extends JPanel implements ActionListener, TextListener
{
	private static final long serialVersionUID = -9175716765749855635L;
	private static Logger log = Logger.getLogger(LocalSettingsGUI.class.getName());
	
	private JDialog localSettingsWindow;
	
	private ButtonGroup opponentChoiceButtonGroup;
	private JRadioButton computerOpponentRadioButton;
	private JRadioButton humanOpponentRadioButton;
	
	private JLabel firstPlayerNameLabel;
	private JLabel secondPlayerNameLabel;
	private JTextField firstPlayerNameTextField;
	private JTextField secondPlayerNameTextField;
	
	private JComboBox<String> playerColorChoiceComboBox;
	private String playerColors[] = { Localization.getMessage("white"), Localization.getMessage("black") };
	
	private JLabel computerDifficultyLabel;
	private JSlider computerDifficultySlider;
	
	private JCheckBox isWhiteOnTopOfBoardCheckBox;
	
	private JCheckBox hasTimeLimitCheckBox;
	private JComboBox<String> timeLimitsComboBox;
	private String timeLimits[] = { "1", "3", "5", "8", "10", "15", "20", "25", "30", "60", "120" };
	
	private GridBagLayout gridBagLayout;
	private GridBagConstraints gridBagConstraints;
	private JButton okButton;
	
	LocalSettingsGUI(JDialog localSettingsWindow)
	{
		super();
		this.localSettingsWindow = localSettingsWindow;
		
		createGridBagLayout();
		
		configurePlayerTypeChoiceGUI();
		configurePlayerNameInputGUI();
		configureComputerDifficultyGUI();
		configureOtherGUI();
		
		placeGuiElementsOnWindowWithPositionConstraints();
	}

	private void createGridBagLayout()
	{
		this.gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		createGridBagConstraints();
	}

	private void createGridBagConstraints()
	{
		this.gridBagConstraints = new GridBagConstraints();
		this.gridBagConstraints.gridx = 0;
		this.gridBagConstraints.gridy = 0;
		this.gridBagConstraints.insets = new Insets(3, 3, 3, 3);
	}

	private void configurePlayerTypeChoiceGUI()
	{
		this.opponentChoiceButtonGroup = new ButtonGroup();
		this.computerOpponentRadioButton = new JRadioButton(Localization.getMessage("against_computer"), false);
		this.humanOpponentRadioButton = new JRadioButton(Localization.getMessage("against_other_human"), true);
		
		this.computerOpponentRadioButton.addActionListener(this);
		this.humanOpponentRadioButton.addActionListener(this);
		
		this.opponentChoiceButtonGroup.add(computerOpponentRadioButton);
		this.opponentChoiceButtonGroup.add(humanOpponentRadioButton);
		
		this.computerOpponentRadioButton.setEnabled(false); // for now, because not implemented!
	}

	private void configurePlayerNameInputGUI()
	{
		this.firstPlayerNameLabel = new JLabel(Localization.getMessage("first_player_name") + ": ");
		this.secondPlayerNameLabel = new JLabel(Localization.getMessage("second_player_name") + ": ");
		
		this.firstPlayerNameTextField = new JTextField("", 10);
		this.firstPlayerNameTextField.setSize(new Dimension(200, 50));
		this.secondPlayerNameTextField = new JTextField("", 10);
		this.secondPlayerNameTextField.setSize(new Dimension(200, 50));
	}

	private void configureComputerDifficultyGUI()
	{
		this.computerDifficultyLabel = new JLabel(Localization.getMessage("computer_level"));
		this.computerDifficultySlider = new JSlider();
		
		this.computerDifficultySlider.setEnabled(false);
		this.computerDifficultySlider.setMaximum(3);
		this.computerDifficultySlider.setMinimum(1);
	}

	private void configureOtherGUI()
	{
		this.playerColorChoiceComboBox = new JComboBox<String>(playerColors);
		this.isWhiteOnTopOfBoardCheckBox = new JCheckBox(Localization.getMessage("upside_down"));
		
		this.hasTimeLimitCheckBox = new JCheckBox(Localization.getMessage("time_game_min"));
		this.timeLimitsComboBox = new JComboBox<String>(timeLimits);
		
		this.okButton = new JButton(Localization.getMessage("ok"));
		this.okButton.addActionListener(this);
	}

	private void placeGuiElementsOnWindowWithPositionConstraints()
	{
		setGridBagConstraints(computerOpponentRadioButton, 1, 0);
		setGridBagConstraints(humanOpponentRadioButton, 0, 1);
		setGridBagConstraints(firstPlayerNameLabel, 0, 2);
		setGridBagConstraints(firstPlayerNameTextField, 1, 2);
		setGridBagConstraints(playerColorChoiceComboBox, 0, 3);
		setGridBagConstraints(secondPlayerNameLabel, 0, 4);
		setGridBagConstraints(secondPlayerNameTextField, 0, 5);
		
		setGridBagConstraints(computerDifficultyLabel, 0, 6);
		setGridBagConstraints(computerDifficultySlider, 0, 7);
		setGridBagConstraints(isWhiteOnTopOfBoardCheckBox, 0, 8);
		setGridBagConstraints(hasTimeLimitCheckBox, 1, 8);
		setGridBagConstraints(timeLimitsComboBox, 1, 9);
		setGridBagConstraints(okButton, 1, 9);
	}

	private void setGridBagConstraints(JComponent guiElement, int gridx, int gridy)
	{
		this.gridBagLayout.setConstraints(guiElement, gridBagConstraints);
		this.add(guiElement);
		this.gridBagConstraints.gridx = gridx;
		this.gridBagConstraints.gridy = gridy;
	}

	/**
	 * Method which is checking for errors in edit tables
	 * 
	 * @param event Object where is saving this what contents edit tables
	 */
	public void textValueChanged(TextEvent event)
	{
		Object target = event.getSource();
		if(target == this.firstPlayerNameTextField || target == this.secondPlayerNameTextField)
		{
			JTextField targetTextField = new JTextField();
			targetTextField = (JTextField) target;
			
			int targetTextLength = targetTextField.getText().length();
			if(targetTextLength > 8)
			{
				try
				{
					targetTextField.setText(targetTextField.getText(0, 7));
				}
				catch(BadLocationException exception)
				{
					log.log(Level.SEVERE, "Something wrong in edit tables: \n", exception);
				}
			}
		}
	}
	
	/**
	 * Method responsible for changing the options.
	 * @param event where is saving data of performed action
	 */
	public void actionPerformed(ActionEvent event)
	{
		Object guiElement = event.getSource();
		if(guiElement == this.computerOpponentRadioButton)
			chooseComputerOpponent(true);
		else if(guiElement == this.humanOpponentRadioButton)
			chooseComputerOpponent(false);
		else if(guiElement == this.okButton)
			startGame();
	}

	private void chooseComputerOpponent(boolean isComputer)
	{
		this.computerDifficultySlider.setEnabled(isComputer);
		this.secondPlayerNameTextField.setEnabled(!isComputer);
	}
	
	private void startGame()
	{
		if(!playerNamesEmpty())
		{
			shortenLongPlayerNames();
		
			Game gameWindow = JChessApp.view.addNewTab(this.firstPlayerNameTextField.getText() + " vs " + this.secondPlayerNameTextField.getText());
			applySettings(gameWindow);
			drawGameWindow(gameWindow);
		}
	}

	private boolean playerNamesEmpty()
	{
		if(!this.computerOpponentRadioButton.isSelected()
		        && (this.firstPlayerNameTextField.getText().length() == 0 || this.secondPlayerNameTextField.getText().length() == 0))
		{
			JOptionPane.showMessageDialog(this, Localization.getMessage("fill_names"));
			return true;
		}
		if((this.computerOpponentRadioButton.isSelected() && this.firstPlayerNameTextField.getText().length() == 0))
		{
			JOptionPane.showMessageDialog(this, Localization.getMessage("fill_name"));
			return true;
		}
		else
			return false;
	}

	private void shortenLongPlayerNames()
	{
		if(this.firstPlayerNameTextField.getText().length() > 9)
			this.firstPlayerNameTextField.setText(this.trimString(firstPlayerNameTextField, 9));
		if(this.secondPlayerNameTextField.getText().length() > 9)
			this.secondPlayerNameTextField.setText(this.trimString(secondPlayerNameTextField, 9));
	}

	/**
	 * Method responsible for triming white symbols from strings
	 * 
	 * @param input Where is capt value to equal
	 * @param length How long is the string
	 * @return result trimmed String
	 */
	public String trimString(JTextField input, int length)
	{
		String result = new String();
		try
		{
			result = input.getText(0, length);
		}
		catch(BadLocationException exception)
		{
			log.log(Level.SEVERE, "Something wrong in edit tables: \n", exception);
		}
		return result;
	}

	private void applySettings(Game gameWindow)
	{
		Settings localSettings = gameWindow.getSettings();
		Player firstPlayer = localSettings.getWhitePlayer();
		Player secondPlayer = localSettings.getBlackPlayer();
		
		localSettings.setGameMode(Settings.GameMode.NEW_GAME);
		localSettings.setGameType(Settings.GameType.LOCAL);
		
		assignPlayerNames(firstPlayer, secondPlayer);
		setPlayerTypes(firstPlayer, secondPlayer);
		
		setFigureColorPlacementOnBoard(localSettings);
		setTimeLimit(gameWindow, localSettings);
		
		logGameInfo(localSettings, firstPlayer, secondPlayer);
	}

	private void assignPlayerNames(Player firstPlayer, Player secondPlayer)
	{
		if(this.playerColorChoiceComboBox.getSelectedItem().equals("White"))
		{
			firstPlayer.setName(this.firstPlayerNameTextField.getText());
			secondPlayer.setName(this.secondPlayerNameTextField.getText());
		}
		else
		{
			secondPlayer.setName(this.firstPlayerNameTextField.getText());
			firstPlayer.setName(this.secondPlayerNameTextField.getText());
		}
	}

	private void setPlayerTypes(Player firstPlayer, Player secondPlayer)
	{
		firstPlayer.setType(Player.Type.LOCAL);
		
		if(this.computerOpponentRadioButton.isSelected())
			secondPlayer.setType(Player.Type.COMPUTER);
		else
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
		        + "\ntime 4 game: " + localSettings.getTimeForGame() + "\ntime limit set: " + localSettings.isTimeLimitSet()
		        + "\nwhite on top?: " + localSettings.isUpsideDown() + "\n****************");// 4test
	}

	private void drawGameWindow(Game gameWindow)
	{
		gameWindow.newGame();
		this.localSettingsWindow.setVisible(false);
		gameWindow.getChessboard().getView().repaint();
		gameWindow.getChessboard().getView().draw();
	}
}