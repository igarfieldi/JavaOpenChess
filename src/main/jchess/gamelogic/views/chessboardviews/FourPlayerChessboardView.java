package jchess.gamelogic.views.chessboardviews;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;

import jchess.gui.secondary.themechooser.ThemeImageLoader;

public class FourPlayerChessboardView extends SquareChessboardView
{
	private static final long serialVersionUID = 5506882497590771731L;
	
	private static final String[] FIELD_LETTERS = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
	        "n" };
	private static final String[] FIELD_NUMBERS = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
	        "14" };
	
	private final int CHESSBOARD_SQUARES = 14;
	private final ThemeImageLoader themeLoader;
	
	public FourPlayerChessboardView(boolean renderLabels, boolean invertedBoard)
	{
		super(renderLabels, invertedBoard);
		themeLoader = ThemeImageLoader.getInstance();
	}
	
	@Override
	protected Image getChessboardImage()
	{
		return themeLoader.loadThemeImage("extended_chessboard.png");
	}
	
	@Override
	protected Image getSelectedFieldImage()
	{
		return themeLoader.loadThemeImage("sel_square.png");
	}
	
	@Override
	protected Image getPossibleFieldImage()
	{
		return themeLoader.loadThemeImage("able_square.png");
	}
	
	@Override
	protected Image getThreateningFieldImage()
	{
		return themeLoader.loadThemeImage("threat_square.png");
	}
	
	@Override
	protected int getSquareCount()
	{
		return CHESSBOARD_SQUARES;
	}
	
	@Override
	protected List<String> getLabelLetters()
	{
		return Arrays.asList(FIELD_LETTERS);
	}
	
	@Override
	protected List<String> getLabelNumbers()
	{
		return Arrays.asList(FIELD_NUMBERS);
	}
}
