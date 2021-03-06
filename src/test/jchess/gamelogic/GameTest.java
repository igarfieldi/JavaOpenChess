package jchess.gamelogic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.IllegalMoveException;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.game.IGame;
import jchess.gamelogic.game.IGameBuilder;
import jchess.gamelogic.game.UntimedGame;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.views.factories.TwoPlayerChessboardViewFactory;
import jchess.util.FileMapParser;

public class GameTest
{
	IGame game;
	IChessboardController controller;
	IGameBuilder builder;
	
	@Before
	public void setUp() throws Exception
	{
		Player white = new Player("p1", Color.WHITE);
		Player black = new Player("p2", Color.BLACK);

		this.controller = new TwoPlayerChessboardController(
				TwoPlayerChessboardViewFactory.getInstance(),
				TwoPlayerChessboardFactory.getInstance(),
				black, white);
		
		this.game = new UntimedGame(controller);
		
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
		parser.setProperty("Event", "Game2p");
		parser.setProperty("WHITE", "a");
		parser.setProperty("BLACK", "b");
		parser.setProperty("Moves", "1. a7-a5 b2-b4 2. b8-c6 g2-g3 ");
		
		// Check if the pieces moved to the proper position
		game.load(parser);
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
	
	@Test
	public void testSaveGame() throws IllegalMoveException {
		// Do some moves to save first
		controller.move(new Field(1, 6), new Field(1, 4));
		controller.move(new Field(3, 1), new Field(3, 2));
		controller.move(new Field(1, 7), new Field(2, 5));
		controller.move(new Field(6, 1), new Field(6, 3));
		controller.move(new Field(0, 7), new Field(1, 7));
		controller.move(new Field(5, 0), new Field(7, 2));
		
		FileMapParser parser = new FileMapParser();
		game.save(parser);

		assertTrue(parser.getProperty("Event").equals("Game2p"));
		assertTrue(parser.getProperty("WHITE").equals("p1"));
		assertTrue(parser.getProperty("BLACK").equals("p2"));
		System.out.println(parser.getProperty("Moves"));
		assertTrue(parser.getProperty("Moves").trim().equals("1. b7-b5 d2-d3 2. Nb8-c6 g2-g4 3. Ra8-b8 Bf1-h3"));
	}
}
