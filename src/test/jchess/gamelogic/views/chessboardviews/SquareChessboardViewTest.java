package jchess.gamelogic.views.chessboardviews;

import static org.junit.Assert.assertTrue;

import java.awt.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.controllers.IBoardActionHandler;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.factories.TwoPlayerChessboardFactory;
import jchess.gamelogic.views.factories.TwoPlayerChessboardViewFactory;

public class SquareChessboardViewTest
{
	SquareChessboardView view;
	CustomActionHandler handler;
	
	@Before
	public void setUp() throws Exception
	{
		IChessboardController controller = new TwoPlayerChessboardController(
				TwoPlayerChessboardViewFactory.getInstance(),
				TwoPlayerChessboardFactory.getInstance(), null, null);
		view = (SquareChessboardView) controller.getView();
		handler = new CustomActionHandler();
		view.initialize(controller, handler);
		view.changeSize(800, 800);
	}
	
	@Test
	public void testMouseClicked()
	{
		// Click on first field
		MouseEvent event1 = new MouseEvent(view, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 50, 50, 1, false, MouseEvent.BUTTON1);
		// Click on field c7
		MouseEvent event2 = new MouseEvent(view, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 250, 610, 1, false, MouseEvent.BUTTON1);
		// Click outside of the chessboard
		MouseEvent event3 = new MouseEvent(view, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 5000, 700, 1, false, MouseEvent.BUTTON1);
		view.mousePressed(event1);
		assertTrue(handler.getClickedField().equals(new Field(0, 0)));
		view.mousePressed(event2);
		assertTrue(handler.getClickedField().equals(new Field(2, 6)));
		view.mousePressed(event3);
		assertTrue(handler.getClickedField() == null);
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
