package jchess.gamelogic.models;

import java.util.List;

import jchess.gamelogic.Player;

public interface IBoardFactory
{
	public IChessboardModel createChessboard(List<Player> players);
}
