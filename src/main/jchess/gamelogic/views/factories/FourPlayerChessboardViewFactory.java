package jchess.gamelogic.views.factories;

import jchess.gamelogic.views.IChessboardView;
import jchess.gamelogic.views.chessboardviews.FourPlayerChessboardView;

public class FourPlayerChessboardViewFactory implements IChessboardViewFactory
{
	private static FourPlayerChessboardViewFactory instance;
	
	public static FourPlayerChessboardViewFactory getInstance() {
		if(instance == null) {
			instance = new FourPlayerChessboardViewFactory();
		}
		
		return instance;
	}
	
	@Override
	public IChessboardView create()
	{
		return new FourPlayerChessboardView(true, false);
	}
	
}
