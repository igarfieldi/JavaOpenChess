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

import javax.swing.JButton;
import javax.swing.JDialog;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.game.IGameBuilder;
import jchess.gamelogic.game.IGameBuilderFactory;

/**
 * Class responsible for drawing the fold with local game settings
 */
public class LocalSettingsPanel extends GridBagPanel implements ActionListener
{
	private static final long serialVersionUID = -9175716765749855635L;
	
	private JDialog newGameWindow;
	
	private PlayerNumberChoicePanel playerNumberChoicePanel;
	private PlayerNameInputPanel playerNameInputPanel;
	private TimerSetterPanel timerSetterPanel;
	private JButton okButton;
	private IGameBuilderFactory builderFactory;
	
	LocalSettingsPanel(JDialog newGameWindow, IGameBuilderFactory builderFactory)
	{
		super();
		this.builderFactory = builderFactory;
		this.newGameWindow = newGameWindow;
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
			IGame game = setGameProperties();
			
			JChessApp.view.addNewGameTab(game);
			drawGameWindow(game);
		}
	}

	private IGame setGameProperties()
	{
		IGameBuilder builder = builderFactory.getBuilder();
		builder.setProperty("timeLimit", "" + timerSetterPanel.getTimeLimit());
		addPlayers(builder);
		
		IGame game = builder.create();
		return game;
	}

	private void addPlayers(IGameBuilder builder)
	{
		if(playerNumberChoicePanel.getPlayerCount() == 2)
		{
			builder.addPlayer(new Player(playerNameInputPanel.getPlayerName(0), Color.WHITE));
			builder.addPlayer(new Player(playerNameInputPanel.getPlayerName(1), Color.BLACK));
		}
		else if(playerNumberChoicePanel.getPlayerCount() == 4)
		{
			builder.addPlayer(new Player(playerNameInputPanel.getPlayerName(0), Color.WHITE));
			builder.addPlayer(new Player(playerNameInputPanel.getPlayerName(1), Color.RED));
			builder.addPlayer(new Player(playerNameInputPanel.getPlayerName(2), Color.BLACK));
			builder.addPlayer(new Player(playerNameInputPanel.getPlayerName(3), Color.GOLDEN));
		}
	}
	
	private void drawGameWindow(IGame gameWindow)
	{
		gameWindow.newGame();
		this.newGameWindow.setVisible(false);
		gameWindow.getView().render();
	}
}