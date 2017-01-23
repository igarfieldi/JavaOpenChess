package jchess.gamelogic.controllers;

import jchess.gamelogic.field.Move;
import jchess.gamelogic.views.HistoryView;

public interface IHistory
{
	public void addMove(Move move, boolean registerInHistory);
	public HistoryView getView();
	
	public String getMovesAsString();
	public void setMoves(String moves);
	
	public Move getLastMoveFromHistory();
}
