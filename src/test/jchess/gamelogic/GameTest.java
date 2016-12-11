package jchess.gamelogic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.GameClockController;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.chessboardfactories.TwoPlayerChessboardFactory;
import jchess.gamelogic.views.IChessboardView;
import jchess.gamelogic.views.chessboardviews.TwoPlayerChessboardView;
import jchess.util.FileMapParser;

public class GameTest
{
	IGame game;
	IChessboardController controller;
	
	@Before
	public void setUp() throws Exception
	{
		Player white = new Player("p1", Color.WHITE);
		Player black = new Player("p1", Color.BLACK);
		
		Settings settings = new Settings();
		
		GameClockController clock = new GameClockController(settings, white, black);
		IChessboardView view = new TwoPlayerChessboardView(true, false);
		controller = new TwoPlayerChessboardController(view, TwoPlayerChessboardFactory.getInstance(),
		        new Player("p1", Color.WHITE), new Player("p1", Color.BLACK));
		
		game = new Game(settings, controller, view, clock);
		game.newGame();
	}
	
	@Test
	public void testOnFieldSelection()
	{
		// Normal selection and de-selection
		game.onFieldSelection(new Field(0, 6));
		assertTrue(game.getView().getChessboardView().getActiveSquare().equals(new Field(0, 6)));
		game.onFieldSelection(new Field(0, 6));
		assertTrue(game.getView().getChessboardView().getActiveSquare() == null);
		
		// Moving piece as white
		game.onFieldSelection(new Field(0, 6));
		game.onFieldSelection(new Field(0, 5));
		assertTrue(game.getView().getChessboardView().getActiveSquare() == null);
		assertTrue(controller.getBoard().getPiece(new Field(0, 5)) != null);
		assertTrue(controller.getBoard().getPiece(new Field(0, 6)) == null);
		
		// Moving piece as black
		game.onFieldSelection(new Field(0, 1));
		game.onFieldSelection(new Field(0, 3));
		assertTrue(game.getView().getChessboardView().getActiveSquare() == null);
		assertTrue(controller.getBoard().getPiece(new Field(0, 1)) == null);
		assertTrue(controller.getBoard().getPiece(new Field(0, 3)) != null);
		
		// Selecting empty field without preselection
		game.onFieldSelection(new Field(0, 3));
		assertTrue(game.getView().getChessboardView().getActiveSquare() == null);
		
		// Selecting empty field with preselection
		game.onFieldSelection(new Field(1, 6));
		game.onFieldSelection(new Field(0, 3));
		assertTrue(game.getView().getChessboardView().getActiveSquare().equals(new Field(1, 6)));
		
		// Selecting piece of other player
		game.onFieldSelection(new Field(1, 6)); // Deselection first
		game.onFieldSelection(new Field(3, 0)); // Wrong color selected
		assertTrue(game.getView().getChessboardView().getActiveSquare() == null);
	}
	
	@Test
	public void testLoadGame()
	{
		// Create virtual game save file
		FileMapParser parser = new FileMapParser();
		parser.setProperty("Date", "2016.12.11");
		parser.setProperty("Event", "Game");
		parser.setProperty("WHITE", "a");
		parser.setProperty("BLACK", "b");
		parser.setProperty("Moves", "1. a7-a5 b2-b4 2. b8-c6 g2-g3 ");
		
		// Check if the pieces moved to the proper position
		game.loadGame(parser.getProperty("Moves"));
		assertTrue(controller.getBoard().getPiece(new Field(0, 4)) != null);
		assertTrue(controller.getBoard().getPiece(new Field(1, 3)) != null);
		assertTrue(controller.getBoard().getPiece(new Field(2, 5)) != null);
		assertTrue(controller.getBoard().getPiece(new Field(6, 2)) != null);
		assertTrue(controller.getBoard().getPiece(new Field(0, 2)) == null);
		assertTrue(controller.getBoard().getPiece(new Field(1, 1)) == null);
		assertTrue(controller.getBoard().getPiece(new Field(1, 7)) == null);
		assertTrue(controller.getBoard().getPiece(new Field(6, 1)) == null);
		
		// TODO: check for moves with incomplete movement cycle (ie. black
		// player should have turn)
	}
	
}
