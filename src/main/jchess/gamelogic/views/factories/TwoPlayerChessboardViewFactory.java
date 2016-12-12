package jchess.gamelogic.views.factories;

import jchess.gamelogic.views.IChessboardView;
import jchess.gamelogic.views.chessboardviews.TwoPlayerChessboardView;

public class TwoPlayerChessboardViewFactory implements IChessboardViewFactory
{
	private static TwoPlayerChessboardViewFactory instance;
	
	public static TwoPlayerChessboardViewFactory getInstance() {
		if(instance == null) {
			instance = new TwoPlayerChessboardViewFactory();
		}
		
		return instance;
	}
	
	@Override
	public IChessboardView create()
	{
		return new TwoPlayerChessboardView(true, false);
	}
	
}
