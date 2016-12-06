package jchess.gamelogic.views.chessboardviews;

import static org.junit.Assert.*;

import java.awt.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import jchess.gamelogic.Settings;
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
		IChessboardController controller = new TwoPlayerChessboardController(new Settings(), view, model);
		handler = new CustomActionHandler();
		view.initialize(controller, handler);
		view.resizeChessboard(800);
	}
	
	@Test
	public void testMouseClicked()
	{
		// Click on first field
		MouseEvent event1 = new MouseEvent(view, MouseEvent.MOUSE_CLICKED,
				System.currentTimeMillis(), 0, 50, 50, 1, false, MouseEvent.BUTTON1);
		// Click on field c7
		MouseEvent event2 = new MouseEvent(view, MouseEvent.MOUSE_CLICKED,
				System.currentTimeMillis(), 0, 250, 610, 1, false, MouseEvent.BUTTON1);
		// Click outside of the chessboard
		MouseEvent event3 = new MouseEvent(view, MouseEvent.MOUSE_CLICKED,
				System.currentTimeMillis(), 0, 5000, 700, 1, false, MouseEvent.BUTTON1);
		// Middle mouse button click -> redo
		MouseEvent event4 = new MouseEvent(view, MouseEvent.MOUSE_CLICKED,
				System.currentTimeMillis(), 0, 50, 50, 1, false, MouseEvent.BUTTON2);
		// Right mouse button click -> undo
		MouseEvent event5 = new MouseEvent(view, MouseEvent.MOUSE_CLICKED,
				System.currentTimeMillis(), 0, 50, 50, 1, false, MouseEvent.BUTTON3);
		view.mouseClicked(event1);
		assertTrue(handler.getClickedField().equals(new Field(0, 0)));
		view.mouseClicked(event2);
		assertTrue(handler.getClickedField().equals(new Field(2, 6)));
		view.mouseClicked(event3);
		assertTrue(handler.getClickedField() == null);
		view.mouseClicked(event4);
		assertTrue(handler.wasRedo());
		view.mouseClicked(event5);
		assertTrue(handler.wasUndo());
	}
	
	
	
	private class CustomActionHandler implements IBoardActionHandler {
		Field clickedField = null;
		boolean undo = false;
		boolean redo = false;
		
		public Field getClickedField() {
			return clickedField;
		}
		
		public boolean wasUndo() {
			return undo;
		}
		
		public boolean wasRedo() {
			return redo;
		}
		
		@Override
		public void onFieldSelection(Field field)
		{
			clickedField = field;
			undo = false;
			redo = false;
		}

		@Override
		public void onRedoRequested()
		{
			clickedField = null;
			undo = false;
			redo = true;
		}

		@Override
		public void onUndoRequested()
		{
			clickedField = null;
			undo = true;
			redo = false;
		}
	}
}
