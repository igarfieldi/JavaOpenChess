package jchess.gamelogic.views.chessboardviews;

import static org.junit.Assert.assertTrue;

import java.awt.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Player;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.controllers.IBoardActionHandler;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.FourPlayerChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.factories.FourPlayerChessboardFactory;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.views.factories.FourPlayerChessboardViewFactory;
import jchess.gamelogic.views.factories.TwoPlayerChessboardViewFactory;

public class SquareChessboardViewTest
{
	private SquareChessboardView view2p;
	private SquareChessboardView view4p;
	private CustomActionHandler handler;
	
	@Before
	public void setUp() throws Exception
	{
		handler = new CustomActionHandler();
		IChessboardController controller2p = new TwoPlayerChessboardController(
				TwoPlayerChessboardViewFactory.getInstance(),
				TwoPlayerChessboardFactory.getInstance(),
				new Player("p1", Color.WHITE),
				new Player("p2", Color.BLACK));
		IChessboardController controller4p = new FourPlayerChessboardController(
				FourPlayerChessboardViewFactory.getInstance(),
				FourPlayerChessboardFactory.getInstance(),
				new Player("p1", Color.WHITE),
				new Player("p2", Color.RED),
				new Player("p3", Color.BLACK),
				new Player("p4", Color.GOLDEN));
		view2p = (SquareChessboardView) controller2p.getView();
		view2p.initialize(controller2p, handler);
		view2p.changeSize(800, 800);
		view4p = (SquareChessboardView) controller4p.getView();
		view4p.initialize(controller4p, handler);
		view4p.changeSize(1400, 1400);
	}
	
	@Test
	public void testMouseClickedTwoPlayerBoard()
	{
		// Click on first field
		MouseEvent event1 = new MouseEvent(view2p, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 50, 50, 1, false, MouseEvent.BUTTON1);
		// Click on field c7
		MouseEvent event2 = new MouseEvent(view2p, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 250, 610, 1, false, MouseEvent.BUTTON1);
		// Click outside of the chessboard
		MouseEvent event3 = new MouseEvent(view2p, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 5000, 700, 1, false, MouseEvent.BUTTON1);
		view2p.mousePressed(event1);
		assertTrue(handler.getClickedField().equals(new Field(0, 0)));
		view2p.mousePressed(event2);
		assertTrue(handler.getClickedField().equals(new Field(2, 6)));
		view2p.mousePressed(event3);
		assertTrue(handler.getClickedField() == null);
	}
	
	@Test
	public void testMouseClickedFourPlayerBoard()
	{
		// Click on d1
		MouseEvent event1 = new MouseEvent(view4p, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 350, 50, 1, false, MouseEvent.BUTTON1);
		// Click on field a4
		MouseEvent event2 = new MouseEvent(view4p, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 50, 350, 1, false, MouseEvent.BUTTON1);
		// Click on field d4
		MouseEvent event3 = new MouseEvent(view4p, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 350, 350, 1, false, MouseEvent.BUTTON1);
		// Click outside of the chessboard
		MouseEvent event4 = new MouseEvent(view4p, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 5000, 700, 1, false, MouseEvent.BUTTON1);
		// Click into a corner of the chessboard
		MouseEvent event5 = new MouseEvent(view4p, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 150, 150, 1, false, MouseEvent.BUTTON1);
		view2p.mousePressed(event1);
		assertTrue(handler.getClickedField().equals(new Field(3, 0)));
		view2p.mousePressed(event2);
		assertTrue(handler.getClickedField().equals(new Field(0, 3)));
		view2p.mousePressed(event3);
		assertTrue(handler.getClickedField().equals(new Field(3, 3)));
		view2p.mousePressed(event4);
		assertTrue(handler.getClickedField() == null);
		view2p.mousePressed(event5);
		// Corner field should still be returned; it is not up to the view
		// to decide wether the field exists on the given square-board
		// variant or not!
		assertTrue(handler.getClickedField().equals(new Field(1, 1)));
	}
	
	private class CustomActionHandler implements IBoardActionHandler {
		Field clickedField = null;
		
		public Field getClickedField() {
			return clickedField;
		}
		
		@Override
		public void onFieldSelection(Field field)
		{
			clickedField = field;
		}

	}
}
