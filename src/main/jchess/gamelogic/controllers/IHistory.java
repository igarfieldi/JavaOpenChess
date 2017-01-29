package jchess.gamelogic.controllers;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.views.HistoryView;

public interface IHistory
{
	public void addMove(Move move);
	public HistoryView getView();
	
	public String getMovesAsString();
	public void setMoves(String moves);

	public Move getLastMove();
	public Move getLastMove(Player player);
}
