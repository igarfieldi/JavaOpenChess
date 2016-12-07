package jchess.gamelogic.views.chessboardviews;

import static org.junit.Assert.assertTrue;

import java.awt.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.controllers.IBoardActionHandler;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.controllers.chessboardcontrollers.TwoPlayerChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.models.chessboardmodels.TwoPlayerChessboardModel;

public class SquareChessboardViewTest
{
	SquareChessboardView view;
	CustomActionHandler handler;
	
	@Before
	public void setUp() throws Exception
	{
		IChessboardModel model = new TwoPlayerChessboardModel();
		model.initialize();
		view = new TwoPlayerChessboardView(false, false);
		IChessboardController controller = new TwoPlayerChessboardController(view, model, null, null);
		handler = new CustomActionHandler();
		view.initialize(controller, handler);
		view.resizeChessboard(800);
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
