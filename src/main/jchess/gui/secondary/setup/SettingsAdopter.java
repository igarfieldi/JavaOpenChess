package jchess.gui.secondary.setup;

import jchess.JChessApp;
import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.Player.Type;
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.game.IGameBuilder;
import jchess.gamelogic.game.RegularGameBuilderFactory;

/**
 * This class feeds the properties set in the UI of the NewGameWindow to the IGame interface which
 * then creates a new game with those settings.
 */
public class SettingsAdopter
{
	private static final int TWO_PLAYERS = 2;
	private static final int FOUR_PLAYERS = 4;
	private static final int AI_FOUR_PLAYERS = 5;
	
	/**
	 * Creates the window for the new game with the specified settings.
	 * 
	 * @param timeLimit
	 * 				The time limit of the game
	 * @param playerNumber
	 * 				The amount of opponents playing against each other.
	 * @param playerNames
	 * 				The array with all player names.
	 */
	public void createGameWindow(int timeLimit, int playerNumber, String[] playerNames)
	{
		IGame game = createGameWithSettings(timeLimit, playerNumber, playerNames);
		String newGameTabTitle = setNewGameTabTitle(playerNames, playerNumber);
		
		JChessApp.view.addNewGameTab(newGameTabTitle, game);
		drawGameWindow(game);
	}

	/**
	 * Writes the names of the opponents playing against each other into the title of the new game tab.
	 * 
	 * @param playerNames
	 * 				The array of all player names.
	 * @param playerNumber
	 * 				The amount of player names to be written on the tab title.
	 * @return
	 */
	private String setNewGameTabTitle(String[] playerNames, int playerNumber)
	{
		String newGameTabTitle = playerNames[0] + " vs. " + playerNames[1];
		if(playerNumber == FOUR_PLAYERS)
			newGameTabTitle += " vs. " + playerNames[2] + " vs. " + playerNames[3];
		if (playerNumber == AI_FOUR_PLAYERS){
			newGameTabTitle += " vs. Cat";
		}
		return newGameTabTitle;
	}
	
	/**
	 * Creates a new game with the settings given by the UI of the NewGameWindow.
	 * 
	 * @param timeLimit
	 * 				The time limit of the game.
	 * @param playerNumber
	 * 				The amount of opponents playing against each other.
	 * @param playerNames
	 * 				The array with all player names.
	 * @return new game with the specified settings.
	 */
	private IGame createGameWithSettings(int timeLimit, int playerNumber, String[] playerNames)
	{
		IGameBuilder builder = RegularGameBuilderFactory.getInstance().getBuilder();
		builder.setProperty("timeLimit", "" + timeLimit);
		addPlayers(builder, playerNumber, playerNames);
		
		IGame game = builder.create();
		return game;
	}

	/**
	 * Gives the IGameBuilder the amount of players that will play against each other which are
	 * then created.
	 * 
	 * @param builder
	 * 				The IGameBuilder interface.
	 * @param playerNumber
	 * 				The amount of opponents playing against each other.
	 * @param playerNames
	 * 				The array with all player names.
	 */
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
		else if(playerNumber == AI_FOUR_PLAYERS){
			builder.addPlayer(new Player(playerNames[0], Color.WHITE));
			builder.addPlayer(new Player(playerNames[1], Color.RED));
			builder.addPlayer(new Player(playerNames[2], Color.BLACK));
			builder.addPlayer(new Player(playerNames[3], Color.GOLDEN));
			builder.addPlayer(new Player("Cat", Color.SPECIAL, Type.COMPUTER));
		}
	}
	
	/**
	 * Renders the game window.
	 * 
	 * @param gameWindow
	 * 				The game window to be rendered.
	 */
	private void drawGameWindow(IGame gameWindow)
	{
		gameWindow.newGame();
		gameWindow.getView().render();
	}
}
