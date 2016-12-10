package jchess.gamelogic.controllers;

public interface IGameStateHandler
{
	public void onCheckmate();
	public void onStalemate();
	public void onTimeOver();
}
