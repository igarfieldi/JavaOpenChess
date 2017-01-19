package jchess.gui.secondary.setup;

import static org.junit.Assert.*;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

public class NewGameWindowTest
{
	NewGameWindow newGameWindow;
	
	@Before
	public void setUp() throws Exception
	{
		JFrame frame = new JFrame();
		newGameWindow = new NewGameWindow(frame);
		newGameWindow.show();
	}
	
	@Test
	public void testPopUpOnTextFieldNotFilled()
	{
		//newGameWindow.
	}
	
}
