package jchess.gui.secondary.setup;

import jchess.JChessApp;
import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.game.IGameBuilder;
import jchess.gamelogic.game.RegularGameBuilderFactory;

public class SettingsAdopter
{
	private static final int TWO_PLAYERS = 2;
	private static final int FOUR_PLAYERS = 4;
	
	public void createGameWindow(int timeLimit, int playerNumber, String[] playerNames)
	{
		IGame game = applyUiSettings(timeLimit, playerNumber, playerNames);
		String newGameTabTitle = setNewGameTabTitle(playerNames, playerNumber);
		
		JChessApp.view.addNewGameTab(newGameTabTitle, game);
		drawGameWindow(game);
	}

	private String setNewGameTabTitle(String[] playerNames, int playerNumber)
	{
		String newGameTabTitle = playerNames[0] + " vs. " + playerNames[1];
		if(playerNumber == FOUR_PLAYERS)
			newGameTabTitle += " vs. " + playerNames[2] + " vs. " + playerNames[3];
		return newGameTabTitle;
	}
	
	private IGame applyUiSettings(int timeLimit, int playerNumber, String[] playerNames)
	{
		IGameBuilder builder = RegularGameBuilderFactory.getInstance().getBuilder();
		builder.setProperty("timeLimit", "" + timeLimit);
		addPlayers(builder, playerNumber, playerNames);
		
		IGame game = builder.create();
		return game;
	}

	private void addPlayers(IGameBuilder builder, int playerNumber, String[] playerNames)
	{
		if(playerNumber == TWO_PLAYERS)
		{
			builder.addPlayer(new Player(playerNames[0], Color.WHITE));
			builder.addPlayer(new Player(playerNames[1], Color.BLACK));
		}
		else if(playerNumber == FOUR_PLAYERS)
		{
			builder.addPlayer(new Player(playerNames[0], Color.WHITE));
			builder.addPlayer(new Player(playerNames[1], Color.RED));
			builder.addPlayer(new Player(playerNames[2], Color.BLACK));
			builder.addPlayer(new Player(playerNames[3], Color.GOLDEN));
		}
	}
	
	private void drawGameWindow(IGame gameWindow)
	{
		gameWindow.newGame();
		gameWindow.getView().render();
	}
}
