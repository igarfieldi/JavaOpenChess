package jchess.gui.secondary.setup;

import jchess.JChessApp;
import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.game.IGameBuilder;
import jchess.gamelogic.game.RegularGameBuilderFactory;

public class GameWindowCreator
{
	public void createGameWindow(int timeLimit, int playerNumber, String[] playerNames)
	{
		IGame game = setGameProperties(timeLimit, playerNumber, playerNames);
		
		JChessApp.view.addNewGameTab(game);
		drawGameWindow(game);
	}
	
	private IGame setGameProperties(int timeLimit, int playerNumber, String[] playerNames)
	{
		IGameBuilder builder = RegularGameBuilderFactory.getInstance().getBuilder();
		builder.setProperty("timeLimit", "" + timeLimit);
		addPlayers(builder, playerNumber, playerNames);
		
		IGame game = builder.create();
		return game;
	}

	private void addPlayers(IGameBuilder builder, int playerNumber, String[] playerNames)
	{
		final int TWO_PLAYERS = 2;
		final int FOUR_PLAYERS = 4;
		
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
